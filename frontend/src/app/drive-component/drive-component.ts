import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Drive} from '../interfaces/drive';
import {DatePipe} from '@angular/common';

@Component({
  selector: '[app-drive-component]',
  imports: [
    DatePipe
  ],
  templateUrl: './drive-component.html',
  styleUrl: './drive-component.css',
})
export class DriveComponent {
  @Input() drive!: Drive;
  @Input() showEndRental: boolean = true;
  @Input() showTotalAmount: boolean = true;
  @Input() showEndButton: boolean = true;
  @Input() showCancelButton: boolean = true;
  @Input() showDeleteButton: boolean = true;
  @Input() showPaymentButton: boolean = true;
  @Input() showAdminInfo: boolean = true;
  @Output() cancel = new EventEmitter<number>
  @Output() delete = new EventEmitter<number>
  @Output() payment = new EventEmitter<Drive>
  @Output() complete = new EventEmitter<Drive>
  @Output() userInfo = new EventEmitter<number>


  protected onCancel(){
    this.cancel.emit(this.drive.driveId);
  }

  protected onDelete() {
    this.delete.emit(this.drive.driveId);
  }

  protected onPayment() {
    this.payment.emit(this.drive);
  }

  protected onComplete() {
    this.complete.emit(this.drive);
  }

  protected onUserInfo() {
    this.userInfo.emit(this.drive.userId);
  }
}
