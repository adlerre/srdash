import { Component, ViewChild, OnInit } from "@angular/core";
import { StateService, UIRouterGlobals } from "@uirouter/core";
import { TranslateService } from "@ngx-translate/core";

import { of } from "rxjs";

@Component({
    selector: "ui-root",
    templateUrl: "./app.component.html"
})
export class AppComponent implements OnInit {

    private langs = ["de", "en"];

    constructor(public translate: TranslateService, private $state: StateService, private globals: UIRouterGlobals) {
        translate.addLangs(this.langs);
        translate.setDefaultLang(this.langs[0]);

        const lang = window.localStorage.getItem("lang") || translate.getBrowserLang();
        document.querySelector("html").setAttribute("lang", lang);
        translate.use(lang);
    }

    ngOnInit() {
    }

}
