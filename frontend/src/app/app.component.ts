import { Component } from '@angular/core';
import { PdfComparisonComponent } from './components/pdf-comparison/pdf-comparison.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [PdfComparisonComponent],
  template: '<app-pdf-comparison />',
  styles: [],
})
export class AppComponent {}
