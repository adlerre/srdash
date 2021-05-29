import { Component, OnInit } from "@angular/core";

import { Subscription } from "rxjs";

import { Packet, CarTelemetryData, CarStatusData, LapData, SessionData, SessionType, TYRE_SPECS, CompoundInfo } from "../definitions";
import { DashboardService } from "./dashboard.service";

@Component({
    selector: "ui-dashboard",
    templateUrl: "./dashboard.component.html",
    providers: [DashboardService]
})
export class DashboardComponent implements OnInit {

    socket: Subscription;

    carTelemetryData: CarTelemetryData;

    carStatusData: CarStatusData;

    sessionData: SessionData;

    lapData: LapData;

    ersStoreEngeryMax = 4000000;

    tyresDamage = 0;

    maxFuelInTank = 0;

    constructor(private $svc: DashboardService) { }

    ngOnInit() {
        this.socket = this.$svc.events.subscribe((packet: Packet) => {
            this.handlePacket(packet);
        });
    }

    public floatToTime(float: number): string {
        const neg = float < 0;

        const value = neg ? float * -1 : float;
        const minutes = Math.floor(value / 60);
        const seconds = value % 60;

        return (neg ? "-" : "") + minutes + ":" + (Math.floor(seconds).toString().length < 2 ? "0" : "") + seconds.toFixed(3);
    }

    public calcESRUsage() {
        if (this.carStatusData) {
            this.ersStoreEngeryMax = Math.max(this.ersStoreEngeryMax, this.carStatusData.ersStoreEngery);
            const esrUsage = Math.floor(100 / this.ersStoreEngeryMax * this.carStatusData.ersStoreEngery);

            return isNaN(esrUsage) ? 100 : esrUsage;
        }

        return 0;
    }

    public calcFuelReserve(): number {
        if (this.sessionData && this.sessionData.sessionType !== SessionType.TIME_TRIAL && this.carStatusData && this.lapData) {
            this.maxFuelInTank = Math.max(this.maxFuelInTank, this.carStatusData.fuelInTank);

            if (this.maxFuelInTank !== 0) {
                const totalDistance = this.sessionData.totalLaps * this.sessionData.trackLength;
                const fpm = this.maxFuelInTank / totalDistance;
                const fpt = this.sessionData.trackLength * fpm;
                const eLaps = this.sessionData.totalLaps - this.lapData.currentLapNum;

                return this.carStatusData.fuelInTank - (fpt * eLaps);
            }
        }

        return 0;
    }

    public calcLapDifference(time: number): number {
        if (this.sessionData && this.lapData && this.lapData.lapDistance > 0 && this.lapData.currentLapTime > 0) {
            const gt = time || this.lapData.bestLapTime || 0;
            const cpm = this.lapData.currentLapTime / this.lapData.lapDistance;

            return cpm * this.sessionData.trackLength - gt;
        }

        return 0;
    }

    public getTyreCompoundInfo(tyreCompound: number): CompoundInfo {
        return TYRE_SPECS[tyreCompound] || null;
    }

    private handlePacket(packet: Packet) {
        if (packet && packet.header) {
            const playerCarIndex = packet.header.playerCarIndex;
            if (packet.carTelemetryData) {
                this.carTelemetryData = packet.carTelemetryData[playerCarIndex];
            } else if (packet.carStatuses) {
                this.carStatusData = packet.carStatuses[playerCarIndex];
                this.tyresDamage = Math.max(
                    this.carStatusData.tyresDamage.frontLeft,
                    this.carStatusData.tyresDamage.frontRight,
                    this.carStatusData.tyresDamage.rearLeft,
                    this.carStatusData.tyresDamage.rearRight
                );
            } else if (packet.lapDataList) {
                this.lapData = packet.lapDataList[playerCarIndex];
            } else if ((<SessionData>packet).weather) {
                this.sessionData = packet;
            }
        }
    }

}

export const DashboardStates = {
    name: "dashboard",
    url: "/",
    component: DashboardComponent,
};
