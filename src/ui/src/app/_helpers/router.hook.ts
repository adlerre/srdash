import { TransitionService, Transition } from "@uirouter/core";

export function routerHook(transitionService: TransitionService) {

    const injectReturnToCriteria = {
        to: (state) => {
            return state.name === "login" && state.params.returnTo && state.params.returnTo.value() == null;
        }
    };

    const injectReturnTo = (transition: Transition) => {
        transition.to().params.returnTo = transition.router
            .stateService.target(transition.from().name, transition.params("from"));
        return transition.to();
    };

    const allStateCriteria = {
        to: (state) => {
            return true;
        }
    };

    transitionService.onCreate(injectReturnToCriteria, injectReturnTo, { priority: 10 });

}
