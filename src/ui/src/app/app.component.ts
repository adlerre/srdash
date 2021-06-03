import { Component, OnInit } from "@angular/core";
import { TranslateService } from "@ngx-translate/core";

import { ModalService } from "./_modal";

import { ApiService } from "./_services";

@Component({
    selector: "ui-root",
    templateUrl: "./app.component.html"
})
export class AppComponent implements OnInit {

    private langs = ["de", "en"];

    public gitInfo: any;

    constructor(private $api: ApiService, public translate: TranslateService, private modalService: ModalService) {
        translate.addLangs(this.langs);
        translate.setDefaultLang(this.langs[0]);

        const lang = window.localStorage.getItem("lang") || translate.getBrowserLang();
        document.querySelector("html").setAttribute("lang", lang);
        translate.use(lang);
    }

    ngOnInit() {
        this.$api.gitInfo().subscribe(gitInfo => this.gitInfo = gitInfo);
    }

    public onDblClick(_event: any) {
        this.openModal("system-modal");
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

}
