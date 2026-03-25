import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { GenerationResult } from '../../models/library-info.model';

@Component({
  selector: 'app-comparison-table',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule],
  templateUrl: './comparison-table.component.html',
  styleUrl: './comparison-table.component.scss',
})
export class ComparisonTableComponent {
  results = input.required<GenerationResult[]>();
  displayedColumns = ['libraryName', 'serverTimeMs', 'clientTimeMs', 'sizeKb'];
}
