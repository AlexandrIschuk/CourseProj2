import {Component} from '@angular/core';
import {Observable} from 'rxjs';
import {Drive} from '../interfaces/drive';
import {DynamicDialogRef } from 'primeng/dynamicdialog';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-confirm-component',
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './confirm-component.html',
  styleUrl: './confirm-component.css',
})
export class ConfirmComponent {

  protected errorMessage = '';

  constructor(
    public ref: DynamicDialogRef,
  ) {}

  protected onConfirm() {
    this.ref.close(true)
  }

  protected onClosed() {
    this.ref.close(false);
  }
}

