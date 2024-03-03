import { BrowserModule, HAMMER_GESTURE_CONFIG, HammerGestureConfig, HammerModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpClient, HttpClientModule } from "@angular/common/http";
import { Injectable, NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { TranslateCompiler, TranslateLoader, TranslateModule } from "@ngx-translate/core";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { MESSAGE_FORMAT_CONFIG, TranslateMessageFormatCompiler } from "ngx-translate-messageformat-compiler";
import * as Hammer from "hammerjs";

import { UIRouterModule } from "@uirouter/angular";

import { routerConfigFn } from "./_helpers/router.config";

import { ModalModule } from "./_modal";

import { ApiService } from "./_services";
import { AppComponent } from "./app.component";

import { WebsocketService } from "./_services/websocket.service";

import { DashboardComponent, DashboardStates, ExternalDashboardComponent, ExternalDashboardStates } from "./dashboard";

export function createTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http, "./assets/i18n/", ".json");
}

@Injectable({providedIn: "root"})
export class AppHammerConfig extends HammerGestureConfig {
    overrides = <any>{
        swipe: {direction: Hammer.DIRECTION_ALL},
    };
}

// @FIXME workaround for ivy build
@Injectable({providedIn: "root"})
export class InjectableTranslateMessageFormatCompiler extends TranslateMessageFormatCompiler {
}

@NgModule({
    declarations: [
        AppComponent,
        DashboardComponent,
        ExternalDashboardComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HammerModule,
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
                DashboardStates,
                ExternalDashboardStates
            ],
            useHash: false,
            config: routerConfigFn,
            otherwise: "/"
        }),
    ],
    providers: [
        {
            provide: MESSAGE_FORMAT_CONFIG,
            useValue: {
                biDiSupport: false,
                intlSupport: false,
                strictNumberSign: false
            }
        },
        {
            provide: HAMMER_GESTURE_CONFIG,
            useClass: AppHammerConfig,
        },
        ApiService,
        WebsocketService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
