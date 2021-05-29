import { Injectable } from "@angular/core";

import { Subject } from "rxjs";

import { Packet } from "../definitions";

import { WebsocketService } from "../_services/websocket.service";

const WS_CONTEXT = "/telemetry";

@Injectable()
export class DashboardService {

    public events: Subject<Packet>;

    constructor(private wsService: WebsocketService<Packet>) {
        this.events = this.wsService.connect(WebsocketService.buildWSURL(WS_CONTEXT));
    }

}
