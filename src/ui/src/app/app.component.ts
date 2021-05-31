import { Component, ViewChild, OnInit } from "@angular/core";
import { TranslateService } from "@ngx-translate/core";

import { ApiService } from "./_services/api.service";

@Component({
    selector: "ui-root",
    templateUrl: "./app.component.html"
})
export class AppComponent implements OnInit {

    private langs = ["de", "en"];
    
    public gitInfo: any;

    constructor(private $api: ApiService, public translate: TranslateService) {
        translate.addLangs(this.langs);
        translate.setDefaultLang(this.langs[0]);

        const lang = window.localStorage.getItem("lang") || translate.getBrowserLang();
        document.querySelector("html").setAttribute("lang", lang);
        translate.use(lang);
    }

    ngOnInit() {
        this.$api.gitInfo().subscribe(gitInfo => this.gitInfo = gitInfo);
    }

}
