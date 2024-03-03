import { Component, OnInit } from "@angular/core";
import { TranslateService } from "@ngx-translate/core";

import { ModalService } from "./_modal";

import { ApiService } from "./_services";
import { StateService } from "@uirouter/core";

@Component({
    selector: "ui-root",
    templateUrl: "./app.component.html"
})
export class AppComponent implements OnInit {

    private static SCREENS = ["dashboard", "external-dashboard"];

    private langs = ["de", "en"];

    private screenIndex: number = 0;

    public gitInfo: any;

    constructor(private $api: ApiService, public translate: TranslateService, private modalService: ModalService,
                private state: StateService) {
        translate.addLangs(this.langs);
        translate.setDefaultLang(this.langs[0]);

        const lang = window.localStorage.getItem("lang") || translate.getBrowserLang();
        document.querySelector("html").setAttribute("lang", lang);
        translate.use(lang);
    }

    ngOnInit() {
        this.$api.gitInfo().subscribe(gitInfo => this.gitInfo = gitInfo);
    }

    public onDblClick(_event: Event) {
        this.openModal("system-modal");
    }

    public onSwipeDown(_event: Event) {
        if (this.screenIndex > 0) {
            this.screenIndex--;
        } else {
            this.screenIndex = AppComponent.SCREENS.length - 1;
        }
        this.navigateTo(AppComponent.SCREENS[this.screenIndex]);
    }

    public onSwipeUp(_event: Event) {
        if (this.screenIndex < AppComponent.SCREENS.length - 1) {
            this.screenIndex++;
        } else {
            this.screenIndex = 0;
        }
        this.navigateTo(AppComponent.SCREENS[this.screenIndex]);
    }

    public openModal(id: string) {
        this.modalService.open(id);
    }

    public closeModal(id: string) {
        this.modalService.close(id);
    }

    public doReboot(_event: any) {
        this.$api.doReboot().subscribe(() => this.closeModal("system-modal"));
    }

    public doShutdown(_event: any) {
        this.$api.doShutdown().subscribe(() => this.closeModal("system-modal"));
    }

    private navigateTo(target: string) {
        this.state.go(target, {}, {reload: true});
    }

}
