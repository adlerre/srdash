package net.ef.srdash.telemetry.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import net.ef.srdash.telemetry.data.elements.Header;

public abstract class Packet {

	private Header header;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public String toJSON() {
		ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		String json = "";
		try {
			json = mapper.writeValueAsString(this);
		} catch (Exception e) {
			// TODO: Handle this exception
		}
		return json.replace("\\u0000", "");
	}

}
