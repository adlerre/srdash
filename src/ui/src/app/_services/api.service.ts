import { Injectable } from "@angular/core";
import { HttpClient, HttpEventType, HttpResponse } from "@angular/common/http";

import { environment } from "../../environments/environment";

import { Observable, Subject } from "rxjs";
import { distinctUntilChanged, last, map } from "rxjs/operators";
import { Config } from "../definitions";

export enum DetailLevel {
    SUMMARY = "summary",
    NORMAL = "normal",
    DETAILED = "detailed"
}

export interface ObjectUrl {
    url: string;
    type?: string | null;
    size?: number;
    filename?: string | null;
}

@Injectable()
export class ApiService {

    public base = environment.apiBaseUrl;

    public httpOptions = environment.apiHttpOptions;

    static getFileNameFromResponseContentDisposition(res: HttpResponse<any>) {
        const contentDisposition = res.headers.get("content-disposition") || "";
        const re = new RegExp("filename\\s*=\\s*\"?([^;\"]+)", "i");
        if (re.test(contentDisposition)) {
            const matches = re.exec(contentDisposition);
            const fileName = (matches[1] || "untitled").trim();
            return fileName;
        }

        return null;
    }

    constructor(public $http: HttpClient) {
    }

    acceptDetailsHeader(level: DetailLevel = DetailLevel.NORMAL, mimeType: string = "application/json") {
        const options = Object.assign({}, this.httpOptions);

        if (level !== DetailLevel.NORMAL) {
            if (!options["headers"]) {
                options["headers"] = {};
            }
            options["headers"]["accept"] = `${mimeType}; detail=${level}`;
        }

        return options;
    }

    forceNoCache(force: boolean, options?: Object) {
        const opts = Object.assign({}, options || this.httpOptions);

        if (force) {
            if (!opts["headers"]) {
                opts["headers"] = {};
            }
            opts["headers"]["Cache-Control"] = [
                "no-cache",
                "max-age=0",
                "must-revalidate"
            ];
        }

        return opts;
    }

    createObjectUrl(url: string, progress?: Subject<number>): Observable<ObjectUrl> {
        return this.$http.get(url, {
            headers: {accept: "*/*"},
            observe: "events",
            responseType: "blob",
            reportProgress: progress !== undefined
        }).pipe(
            distinctUntilChanged(),
            map(event => {
                if (event.type === HttpEventType.DownloadProgress && progress && event.total) {
                    const percentDone = Math.round(100 * event.loaded / event.total);
                    progress.next(percentDone);
                } else if (event.type === HttpEventType.Response) {
                    if (progress) {
                        progress.next(100);
                        progress.complete();
                    }
                    return {
                        url: window.URL.createObjectURL(event.body),
                        type: event.body.type,
                        size: event.body.size,
                        filename: ApiService.getFileNameFromResponseContentDisposition(event)
                    };
                }
            }),
            last()
        );
    }

    /**
     * Config
     */
    getConfiguration(startsWith?: string) {
        return this.$http.get<Config>(`${this.base}/config/${startsWith || ""}`);
    }

    /**
     * System
     */
    doReboot() {
        return this.$http.put(`${this.base}/system/reboot`, this.httpOptions);
    }

    doShutdown() {
        return this.$http.put(`${this.base}/system/shutdown`, this.httpOptions);
    }

    /**
     * External Assets
     */
    gitInfo() {
        return this.$http.get(`${this.base}/assets/git.json`, this.httpOptions);
    }

}
