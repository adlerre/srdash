import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpClientModule, HttpClient } from "@angular/common/http";
import { NgModule, Injectable } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { TranslateCompiler, TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { MESSAGE_FORMAT_CONFIG, TranslateMessageFormatCompiler } from "ngx-translate-messageformat-compiler";

import { UIRouterModule } from "@uirouter/angular";

import { routerConfigFn } from "./_helpers/router.config";

import { ModalModule } from "./_modal";

import { ApiService } from "./_services/api.service";
import { AppComponent } from "./app.component";

import { WebsocketService } from "./_services/websocket.service";

import { DashboardComponent, DashboardStates } from "./dashboard/dashboard.component";

export function createTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http, "./assets/i18n/", ".json");
}

// @FIXME workaround for ivy build
@Injectable({ providedIn: "root" })
export class InjectableTranslateMessageFormatCompiler extends TranslateMessageFormatCompiler { }

@NgModule({
    declarations: [
        AppComponent,
        DashboardComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        FormsModule,
        ModalModule,
        ReactiveFormsModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: (createTranslateLoader),
                deps: [HttpClient]
            },
            compiler: {
                provide: TranslateCompiler,
                // @FIXME workaround for ivy build
                // useClass: TranslateMessageFormatCompiler
                useClass: InjectableTranslateMessageFormatCompiler
            }
        }),
        UIRouterModule.forRoot({
            states: [
                DashboardStates
            ],
            useHash: false,
            config: routerConfigFn,
            otherwise: "/"
        })
    ],
    providers: [
        {
            provide: MESSAGE_FORMAT_CONFIG, useValue: {
                biDiSupport: false,
                intlSupport: false,
                strictNumberSign: false
            }
        },
        ApiService,
        WebsocketService
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
