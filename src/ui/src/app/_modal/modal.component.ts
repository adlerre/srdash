import { Component, ViewEncapsulation, ElementRef, Input, OnInit, OnDestroy } from "@angular/core";

import { ModalService } from "./modal.service";

@Component({
    selector: "ui-modal",
    templateUrl: "modal.component.html",
    styleUrls: ["modal.component.scss"],
    encapsulation: ViewEncapsulation.None
})
export class ModalComponent implements OnInit, OnDestroy {
    @Input() id: string;
    private element: any;

    constructor(private modalService: ModalService, private el: ElementRef) {
        this.element = el.nativeElement;
    }

    ngOnInit(): void {
        if (!this.id) {
            throw new Error("modal must have an id");
        }

        document.body.appendChild(this.element);

        this.element.addEventListener("click", el => {
            if (el.target.className === "modal") {
                this.close();
            }
        });

        this.modalService.add(this);
    }

    ngOnDestroy(): void {
        this.modalService.remove(this.id);
        this.element.remove();
    }

    open(): void {
        this.element.style.display = "block";
        document.body.classList.add("modal-open");
    }

    close(): void {
        this.element.style.display = "none";
        document.body.classList.remove("modal-open");
    }
}
