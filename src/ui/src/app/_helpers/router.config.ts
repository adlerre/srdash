import { UIRouter } from "@uirouter/core";

import { routerHook } from "./router.hook";

export function routerConfigFn(router: UIRouter) {
    const transitionService = router.transitionService;

    routerHook(transitionService);
}
