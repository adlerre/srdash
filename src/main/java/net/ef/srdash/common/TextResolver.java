/*
 * This program is free software; you can use it, redistribute it
 * and / or modify it under the terms of the GNU General Public License
 * (GPL) as published by the Free Software Foundation; either version 3
 * of the License or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program, in a file called gpl.txt or license.txt.
 * If not, write to the Free Software Foundation Inc.,
 * 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */
package net.ef.srdash.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>
 * This class parses and resolve strings which contains variables. To add a
 * variable call <code>addVariable</code>.
 * </p>
 * <p>
 * The algorithm is optimized that each character is touched only once.
 * </p>
 * <p>
 * To resolve a string a valid syntax is required:
 * </p>
 * <p>
 * <b>{}:</b> Use curly brackets for variables or properties. For example
 * "{var1}" or "{MCR.basedir}"
 * </p>
 * <p>
 * <b>[]:</b> Use squared brackets to define a condition. All data within
 * squared brackets is only used if the internal variables are not null and not
 * empty. For example "[hello {lastName}]" is only resolved if the value of
 * "lastName" is not null and not empty. Otherwise the whole content in the
 * squared brackets are ignored.
 * </p>
 * <p>
 * <b>\:</b> Use the escape character to use all predefined characters.
 * </p>
 * <p>
 * Sample:<br>
 * "Lastname: {lastName}[, Firstname: {firstName}]"<br>
 * </p>
 * 
 * @author Matthias Eichner
 */
public class TextResolver {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(TextResolver.class);

	/** The term container. */
	protected TermContainer termContainer;

	/**
	 * This map contains all variables that can be resolved.
	 */
	protected Map<String, String> variablesMap;

	/**
	 * Retains the text if a variable couldn't be resolved. Example if
	 * {Variable} could not be resolved: true: "Hello {Variable}" -&gt; "Hello
	 * {Variable}" false: "Hello "
	 * <p>
	 * By default retainText is true
	 * </p>
	 */
	protected boolean retainText;

	/**
	 * Defines how deep the text is resolved.
	 * <dl>
	 * <dt>Deep</dt>
	 * <dd>everything is resolved</dd>
	 * <dt>NoVariables</dt>
	 * <dd>the value of variables is not being resolved</dd>
	 * </dl>
	 */
	protected ResolveDepth resolveDepth;

	/** The tracker. */
	protected CircularDependencyTracker tracker;

	/**
	 * Creates the term list for the text resolver and adds the default terms.
	 *
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InstantiationException
	 *             the instantiation exception
	 */
	protected void registerDefaultTerms()
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
		registerTerm(Variable.class);
		registerTerm(Condition.class);
		registerTerm(EscapeCharacter.class);
	}

	/**
	 * Register a new term. The resolver invokes the term via reflection.
	 *
	 * @param termClass
	 *            the term class to register.
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InstantiationException
	 *             the instantiation exception
	 */
	public void registerTerm(Class<? extends Term> termClass)
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
		this.termContainer.add(termClass);
	}

	/**
	 * Unregister a term.
	 *
	 * @param termClass
	 *            this class is unregistered
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 */
	public void unregisterTerm(Class<? extends Term> termClass)
			throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		this.termContainer.remove(termClass);
	}

	/**
	 * Defines how deep the text is resolved.
	 * <dl>
	 * <dt>Deep</dt>
	 * <dd>everything is resolved</dd>
	 * <dt>NoVariables</dt>
	 * <dd>the value of variables is not being resolved</dd>
	 * </dl>
	 */
	public enum ResolveDepth {

		/** The Deep. */
		Deep,
		/** The No variables. */
		NoVariables
	}

	/**
	 * Creates a new text resolver with a map of variables.
	 */
	public TextResolver() {
		this.variablesMap = new HashMap<>();
		this.setResolveDepth(ResolveDepth.Deep);
		this.setRetainText(true);
		this.tracker = new CircularDependencyTracker(this);
		try {
			this.termContainer = new TermContainer(this);
			this.registerDefaultTerms();
		} catch (Exception exc) {
			throw new IllegalArgumentException("Unable to register default terms", exc);
		}
	}

	/**
	 * Creates a new text resolver. To add variables call
	 * <code>addVariable</code>, otherwise only MyCoRe property resolving is
	 * possible.
	 *
	 * @param variablesMap
	 *            the variables map
	 */
	public TextResolver(Map<String, String> variablesMap) {
		this();
		mixin(variablesMap);
	}

	/**
	 * Instantiates a new text resolver.
	 *
	 * @param properties
	 *            the properties
	 */
	public TextResolver(Properties properties) {
		this();
		mixin(properties);
	}

	/**
	 * Gets the term container.
	 *
	 * @return the term container
	 */
	protected TermContainer getTermContainer() {
		return this.termContainer;
	}

	/**
	 * Gets the tracker.
	 *
	 * @return the tracker
	 */
	protected CircularDependencyTracker getTracker() {
		return this.tracker;
	}

	/**
	 * Mixin.
	 *
	 * @param variables
	 *            the variables
	 */
	public void mixin(Map<String, String> variables) {
		for (Entry<String, String> entrySet : variables.entrySet()) {
			String key = entrySet.getKey();
			String value = entrySet.getValue();
			this.addVariable(key, value);
		}
	}

	/**
	 * Mixin.
	 *
	 * @param properties
	 *            the properties
	 */
	public void mixin(Properties properties) {
		for (Entry<Object, Object> entrySet : properties.entrySet()) {
			String key = entrySet.getKey().toString();
			String value = entrySet.getValue().toString();
			this.addVariable(key, value);
		}
	}

	/**
	 * Sets if the text should be retained if a variable couldn't be resolved.
	 * <p>
	 * Example:<br>
	 * true: "Hello {Variable}" -&gt; "Hello {Variable}"<br>
	 * false: "Hello "
	 * </p>
	 * <p>
	 * By default retainText is true
	 * </p>
	 *
	 * @param retainText
	 *            the new retain text
	 */
	public void setRetainText(boolean retainText) {
		this.retainText = retainText;
	}

	/**
	 * Checks if the text should be retained if a variable couldn't be resolved.
	 * <p>
	 * By default retainText is true
	 * </p>
	 *
	 * @return true, if is retain text
	 */
	public boolean isRetainText() {
		return this.retainText;
	}

	/**
	 * Adds a new variable to the resolver. This overwrites a existing variable
	 * with the same name.
	 * 
	 * @param name
	 *            name of the variable
	 * @param value
	 *            value of the variable
	 * @return the previous value of the specified name, or null if it did not
	 *         have one
	 */
	public String addVariable(String name, String value) {
		return variablesMap.put(name, value);
	}

	/**
	 * Removes a variable from the resolver. This method does nothing if no
	 * variable with the name exists.
	 *
	 * @param name
	 *            the name
	 * @return the value of the removed variable, or null if no variable with
	 *         the name exists
	 */
	public String removeVariable(String name) {
		return variablesMap.remove(name);
	}

	/**
	 * Checks if a variable with the specified name exists.
	 *
	 * @param name
	 *            the name
	 * @return true if a variable exists, otherwise false
	 */
	public boolean containsVariable(String name) {
		return variablesMap.containsKey(name);
	}

	/**
	 * Sets the resolve depth.
	 * 
	 * @param resolveDepth
	 *            defines how deep the text is resolved.
	 */
	public void setResolveDepth(ResolveDepth resolveDepth) {
		this.resolveDepth = resolveDepth;
	}

	/**
	 * Returns the current resolve depth.
	 * 
	 * @return resolve depth enumeration
	 */
	public ResolveDepth getResolveDepth() {
		return this.resolveDepth;
	}

	/**
	 * This method resolves all variables in the text. The syntax is described
	 * at the head of the class.
	 * 
	 * @param text
	 *            the string where the variables have to be resolved
	 * @return the resolved string
	 */
	public String resolve(String text) {
		this.getTracker().clear();
		Text textResolver = new Text(this);
		textResolver.resolve(text, 0);
		return textResolver.getValue();
	}

	/**
	 * Returns the value of a variable.
	 * 
	 * @param varName
	 *            the name of the variable
	 * @return the value
	 */
	public String getValue(String varName) {
		return variablesMap.get(varName);
	}

	/**
	 * Returns a <code>Map</code> of all variables.
	 * 
	 * @return a <code>Map</code> of all variables.
	 */
	public Map<String, String> getVariables() {
		return variablesMap;
	}

	/**
	 * A term is a defined part in a text. In general, a term is defined by
	 * brackets, but this is not required. Here are some example terms:
	 * <ul>
	 * <li>Variable: {term1}</li>
	 * <li>Condition: [term2]</li>
	 * <li>EscapeChar: \[</li>
	 * </ul>
	 * 
	 * You can write your own terms and add them to the text resolver. A sample
	 * is shown in the <code>MCRTextResolverTest</code> class.
	 * 
	 * @author Matthias Eichner
	 */
	protected static abstract class Term {
		/**
		 * The string buffer within the term. For example: {<b>var</b>}.
		 */
		protected StringBuffer termBuffer;

		/**
		 * If the term is successfully resolved. By default this is true.
		 */
		protected boolean resolved;

		/**
		 * The current character position in the term.
		 */
		protected int position;

		/** The text resolver. */
		protected TextResolver textResolver;

		/**
		 * Instantiates a new term.
		 *
		 * @param textResolver
		 *            the text resolver
		 */
		public Term(TextResolver textResolver) {
			this.textResolver = textResolver;
			this.termBuffer = new StringBuffer();
			this.resolved = true;
			this.position = 0;
		}

		/**
		 * Resolves the text from the startPosition to the end of the text or if
		 * a term specific end character is found.
		 * 
		 * @param text
		 *            the term to resolve
		 * @param startPosition
		 *            the current character position
		 * @return the value of the term after resolving
		 */
		public String resolve(String text, int startPosition) {
			for (position = startPosition; position < text.length(); position++) {
				Term internalTerm = getTerm(text, position);
				if (internalTerm != null) {
					position += internalTerm.getStartEnclosingString().length();
					internalTerm.resolve(text, position);
					if (!internalTerm.resolved) {
						resolved = false;
					}
					position = internalTerm.position;
					termBuffer.append(internalTerm.getValue());
				} else {
					boolean complete = resolveInternal(text, position);
					if (complete) {
						int endEnclosingSize = getEndEnclosingString().length();
						if (endEnclosingSize > 1) {
							position += endEnclosingSize - 1;
						}
						break;
					}
				}
			}
			return getValue();
		}

		/**
		 * Returns a new term in dependence of the current character (position
		 * of the text). If no term is defined null is returned.
		 *
		 * @param text
		 *            the text
		 * @param pos
		 *            the pos
		 * @return a term or null if no one found
		 */
		private Term getTerm(String text, int pos) {
			TermContainer termContainer = this.getTextResolver().getTermContainer();
			for (Entry<String, Class<? extends Term>> termEntry : termContainer.getTermSet()) {
				String startEnclosingStringOfTerm = termEntry.getKey();
				if (text.startsWith(startEnclosingStringOfTerm, pos)
						&& !startEnclosingStringOfTerm.equals(this.getEndEnclosingString())) {
					try {
						return termContainer.instantiate(termEntry.getValue());
					} catch (Exception exc) {
						LOGGER.error(exc);
					}
				}
			}
			return null;
		}

		/**
		 * Does term specific resolving for the current character.
		 *
		 * @param text
		 *            the text
		 * @param pos
		 *            the pos
		 * @return true if the end string is reached, otherwise false
		 */
		protected abstract boolean resolveInternal(String text, int pos);

		/**
		 * Returns the value of the term. Overwrite this if you don't want to
		 * get the default termBuffer content as value.
		 * 
		 * @return the value of the term
		 */
		public String getValue() {
			return termBuffer.toString();
		}

		/**
		 * Implement this to define the start enclosing string for your term.
		 * The resolver searches in the text for this string, if found, the text
		 * is processed by your term.
		 * 
		 * @return the start enclosing string
		 */
		public abstract String getStartEnclosingString();

		/**
		 * Implement this to define the end enclosing string for your term. You
		 * have to check manual in the <code>resolveInternal</code> method if
		 * the end of your term is reached.
		 * 
		 * @return the end enclosing string
		 */
		public abstract String getEndEnclosingString();

		/**
		 * Gets the text resolver.
		 *
		 * @return the text resolver
		 */
		public TextResolver getTextResolver() {
			return textResolver;
		}

	}

	/**
	 * A variable is surrounded by curly brackets. It supports recursive
	 * resolving for the content of the variable. The name of the variable is
	 * set by the termBuffer and the value is equal the content of the
	 * valueBuffer.
	 */
	protected static class Variable extends Term {

		/**
		 * A variable doesn't return the termBuffer, but this valueBuffer.
		 */
		private StringBuffer valueBuffer;

		/** The complete. */
		private boolean complete;

		/**
		 * Instantiates a new variable.
		 *
		 * @param textResolver
		 *            the text resolver
		 */
		public Variable(TextResolver textResolver) {
			super(textResolver);
			valueBuffer = new StringBuffer();
			complete = false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * de.uni_jena.rz.services.common.TextResolver.Term#resolveInternal(java
		 * .lang.String, int)
		 */
		@Override
		public boolean resolveInternal(String text, int pos) {
			if (text.startsWith(getEndEnclosingString(), pos)) {
				this.track();
				// get the value from the variables table
				String value = getTextResolver().getValue(termBuffer.toString());
				if (value == null) {
					resolved = false;
					if (getTextResolver().isRetainText()) {
						this.valueBuffer.append(getStartEnclosingString()).append(termBuffer.toString())
								.append(getEndEnclosingString());
					}
					this.untrack();
					complete = true;
					return true;
				}
				// resolve the content of the variable recursive
				// to resolve all other internal variables, condition etc.
				if (getTextResolver().getResolveDepth() != ResolveDepth.NoVariables) {
					Text recursiveResolvedText = resolveText(value);
					resolved = recursiveResolvedText.resolved;
					value = recursiveResolvedText.getValue();
				}
				// set the value of the variable
				valueBuffer.append(value);
				this.untrack();
				complete = true;
				return true;
			}
			termBuffer.append(text.charAt(pos));
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.uni_jena.rz.services.common.TextResolver.Term#getValue()
		 */
		@Override
		public String getValue() {
			if (!complete) {
				// assume that the variable is not complete
				StringBuffer buf = new StringBuffer();
				buf.append(getStartEnclosingString()).append(termBuffer.toString());
				return buf.toString();
			}
			return valueBuffer.toString();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.uni_jena.rz.services.common.TextResolver.Term#
		 * getStartEnclosingString()
		 */
		@Override
		public String getStartEnclosingString() {
			return "{";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.uni_jena.rz.services.common.TextResolver.Term#
		 * getEndEnclosingString()
		 */
		@Override
		public String getEndEnclosingString() {
			return "}";
		}

		/**
		 * Tracks the variable to check for circular dependency.
		 */
		protected void track() {
			this.getTextResolver().getTracker().track("var", getTrackID());
		}

		/**
		 * Untrack.
		 */
		protected void untrack() {
			this.getTextResolver().getTracker().untrack("var", getTrackID());
		}

		/**
		 * Gets the track ID.
		 *
		 * @return the track ID
		 */
		protected String getTrackID() {
			return new StringBuffer(getStartEnclosingString()).append(termBuffer.toString())
					.append(getEndEnclosingString()).toString();
		}

		/**
		 * This method resolves all variables in the text. The syntax is
		 * described at the head of the class.
		 * 
		 * @param text
		 *            the string where the variables have to be resolved
		 * @return the resolved string
		 */
		public Text resolveText(String text) {
			Text textResolver = new Text(getTextResolver());
			textResolver.resolve(text, 0);
			return textResolver;
		}

	}

	/**
	 * A condition is defined by squared brackets. All data which is set in
	 * these brackets is only used if the internal variables are not null and
	 * not empty. For example "[hello {lastName}]" is only resolved if the value
	 * of "lastName" is not null and not empty. Otherwise the whole content in
	 * the squared brackets are ignored.
	 */
	protected static class Condition extends Term {

		/**
		 * Instantiates a new condition.
		 *
		 * @param textResolver
		 *            the text resolver
		 */
		public Condition(TextResolver textResolver) {
			super(textResolver);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * de.uni_jena.rz.services.common.TextResolver.Term#resolveInternal(java
		 * .lang.String, int)
		 */
		@Override
		protected boolean resolveInternal(String text, int pos) {
			if (text.startsWith(getEndEnclosingString(), pos)) {
				return true;
			}
			termBuffer.append(text.charAt(pos));
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.uni_jena.rz.services.common.TextResolver.Term#getValue()
		 */
		@Override
		public String getValue() {
			if (resolved) {
				return super.getValue();
			}
			return "";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.uni_jena.rz.services.common.TextResolver.Term#
		 * getStartEnclosingString()
		 */
		@Override
		public String getStartEnclosingString() {
			return "[";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.uni_jena.rz.services.common.TextResolver.Term#
		 * getEndEnclosingString()
		 */
		@Override
		public String getEndEnclosingString() {
			return "]";
		}
	}

	/**
	 * As escape character the backslashed is used. Only the first character
	 * after the escape char is add to the term.
	 */
	protected static class EscapeCharacter extends Term {

		/**
		 * Instantiates a new escape character.
		 *
		 * @param textResolver
		 *            the text resolver
		 */
		public EscapeCharacter(TextResolver textResolver) {
			super(textResolver);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * de.uni_jena.rz.services.common.TextResolver.Term#resolveInternal(java
		 * .lang.String, int)
		 */
		@Override
		public boolean resolveInternal(String text, int pos) {
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * de.uni_jena.rz.services.common.TextResolver.Term#resolve(java.lang.
		 * String, int)
		 */
		@Override
		public String resolve(String text, int startPos) {
			position = startPos;
			char c = text.charAt(position);
			termBuffer.append(c);
			return termBuffer.toString();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.uni_jena.rz.services.common.TextResolver.Term#
		 * getStartEnclosingString()
		 */
		@Override
		public String getStartEnclosingString() {
			return "\\";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.uni_jena.rz.services.common.TextResolver.Term#
		 * getEndEnclosingString()
		 */
		@Override
		public String getEndEnclosingString() {
			return "";
		}
	}

	/**
	 * A simple text, every character is added to the term (except its a special
	 * one).
	 */
	protected static class Text extends Term {

		/**
		 * Instantiates a new text.
		 *
		 * @param textResolver
		 *            the text resolver
		 */
		public Text(TextResolver textResolver) {
			super(textResolver);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * de.uni_jena.rz.services.common.TextResolver.Term#resolveInternal(java
		 * .lang.String, int)
		 */
		@Override
		public boolean resolveInternal(String text, int pos) {
			termBuffer.append(text.charAt(pos));
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.uni_jena.rz.services.common.TextResolver.Term#
		 * getStartEnclosingString()
		 */
		@Override
		public String getStartEnclosingString() {
			return "";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.uni_jena.rz.services.common.TextResolver.Term#
		 * getEndEnclosingString()
		 */
		@Override
		public String getEndEnclosingString() {
			return "";
		}
	}

	/**
	 * Simple class to hold terms and instantiate them.
	 */
	protected static class TermContainer {

		/** The term map. */
		protected Map<String, Class<? extends Term>> termMap = new HashMap<String, Class<? extends Term>>();

		/** The text resolver. */
		protected TextResolver textResolver;

		/**
		 * Instantiates a new term container.
		 *
		 * @param textResolver
		 *            the text resolver
		 */
		public TermContainer(TextResolver textResolver) {
			this.textResolver = textResolver;
		}

		/**
		 * Instantiate.
		 *
		 * @param termClass
		 *            the term class
		 * @return the term
		 * @throws InvocationTargetException
		 *             the invocation target exception
		 * @throws NoSuchMethodException
		 *             the no such method exception
		 * @throws InstantiationException
		 *             the instantiation exception
		 * @throws IllegalAccessException
		 *             the illegal access exception
		 */
		public Term instantiate(Class<? extends Term> termClass) throws InvocationTargetException,
				NoSuchMethodException, InstantiationException, IllegalAccessException {
			Constructor<? extends Term> c = termClass.getConstructor(TextResolver.class);
			return c.newInstance(this.textResolver);
		}

		/**
		 * Adds the.
		 *
		 * @param termClass
		 *            the term class
		 * @throws InvocationTargetException
		 *             the invocation target exception
		 * @throws NoSuchMethodException
		 *             the no such method exception
		 * @throws InstantiationException
		 *             the instantiation exception
		 * @throws IllegalAccessException
		 *             the illegal access exception
		 */
		public void add(Class<? extends Term> termClass) throws InvocationTargetException, NoSuchMethodException,
				InstantiationException, IllegalAccessException {
			Term term = instantiate(termClass);
			this.termMap.put(term.getStartEnclosingString(), termClass);
		}

		/**
		 * Removes the.
		 *
		 * @param termClass
		 *            the term class
		 * @throws InvocationTargetException
		 *             the invocation target exception
		 * @throws NoSuchMethodException
		 *             the no such method exception
		 * @throws InstantiationException
		 *             the instantiation exception
		 * @throws IllegalAccessException
		 *             the illegal access exception
		 */
		public void remove(Class<? extends Term> termClass) throws InvocationTargetException, NoSuchMethodException,
				InstantiationException, IllegalAccessException {
			Term term = instantiate(termClass);
			this.termMap.remove(term.getStartEnclosingString());
		}

		/**
		 * Gets the term set.
		 *
		 * @return the term set
		 */
		public Set<Entry<String, Class<? extends Term>>> getTermSet() {
			return this.termMap.entrySet();
		}

	}

	/**
	 * The Class CircularDependencyTracker.
	 */
	protected static class CircularDependencyTracker {

		/** The text resolver. */
		protected TextResolver textResolver;

		/** The track map. */
		protected Map<String, List<String>> trackMap;

		/**
		 * Instantiates a new circular dependency tracker.
		 *
		 * @param textResolver
		 *            the text resolver
		 */
		public CircularDependencyTracker(TextResolver textResolver) {
			this.textResolver = textResolver;
			this.trackMap = new HashMap<>();
		}

		/**
		 * Track.
		 *
		 * @param type
		 *            the type
		 * @param id
		 *            the id
		 * @throws CircularDependencyExecption
		 *             the circular dependency execption
		 */
		public void track(String type, String id) throws CircularDependencyExecption {
			List<String> idList = trackMap.get(type);
			if (idList == null) {
				idList = new ArrayList<>();
				trackMap.put(type, idList);
			}
			if (idList.contains(id)) {
				throw new CircularDependencyExecption(idList, id);
			}
			idList.add(id);
		}

		/**
		 * Untrack.
		 *
		 * @param type
		 *            the type
		 * @param id
		 *            the id
		 */
		public void untrack(String type, String id) {
			List<String> idList = trackMap.get(type);
			if (idList == null) {
				LOGGER.error("text resolver circular dependency tracking error: cannot get type " + type + " of " + id);
				return;
			}
			idList.remove(id);
		}

		/**
		 * Clear.
		 */
		public void clear() {
			this.trackMap.clear();
		}

	}

	/**
	 * The Class CircularDependencyExecption.
	 */
	protected static class CircularDependencyExecption extends RuntimeException {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -2448797538275144448L;

		/** The dependency list. */
		private List<String> dependencyList;

		/** The id. */
		private String id;

		/**
		 * Instantiates a new circular dependency execption.
		 *
		 * @param dependencyList
		 *            the dependency list
		 * @param id
		 *            the id
		 */
		public CircularDependencyExecption(List<String> dependencyList, String id) {
			this.dependencyList = dependencyList;
			this.id = id;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#getMessage()
		 */
		@Override
		public String getMessage() {
			StringBuffer msg = new StringBuffer("A circular dependency exception occurred");
			msg.append("\n").append("circular path: ");
			for (String dep : dependencyList) {
				msg.append(dep).append(" > ");
			}
			msg.append(id);
			return msg.toString();
		}

		/**
		 * Gets the id.
		 *
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * Gets the dependency list.
		 *
		 * @return the dependency list
		 */
		public List<String> getDependencyList() {
			return dependencyList;
		}

	}

}