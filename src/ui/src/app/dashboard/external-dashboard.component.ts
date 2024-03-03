import { Component, Input, ViewEncapsulation } from "@angular/core";
import { DomSanitizer } from "@angular/platform-browser";
import { ApiService } from "../_services";
import { Config } from "../definitions";
import { lastValueFrom } from "rxjs";

@Component({
    selector: "ui-external-dashboard",
    templateUrl: "./external-dashboard.component.html",
    styleUrls: ["./external-dashboard.component.scss"],
    encapsulation: ViewEncapsulation.None
})
export class ExternalDashboardComponent {

    private static CONFIG_PREFIX = "Dash.External.";

    @Input()
    private config: Config;

    constructor(private sanitizer: DomSanitizer) {
    }

    getDashbaordUrl() {
        const url = this.getConfigValue(ExternalDashboardComponent.CONFIG_PREFIX + "URL");
        return url && url.length !== 0 ?
            this.sanitizer.bypassSecurityTrustResourceUrl(url) :
            null;
    }

    getConfigValue(key: string) {
        if (this.config && this.config.entry.length !== 0) {
            return this.config.entry.find(e => e.key === key)?.value || null;

        }
        return null;
    }

}

export async function resolveFnConfig($api: ApiService) {
    return lastValueFrom($api.getConfiguration("Dash.External."));
}

export const ExternalDashboardStates = {
    name: "external-dashboard",
    url: "/external-dashboard",
    component: ExternalDashboardComponent,
    resolve: [
        {
            token: "config",
            deps: [ApiService],
            resolveFn: resolveFnConfig
        }
    ]
};
