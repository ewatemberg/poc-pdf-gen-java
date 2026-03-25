import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { PdfService } from '../../services/pdf.service';
import { LibraryInfo, GenerationResult } from '../../models/library-info.model';
import { ComparisonTableComponent } from '../comparison-table/comparison-table.component';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-pdf-comparison',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    ComparisonTableComponent,
  ],
  templateUrl: './pdf-comparison.component.html',
  styleUrl: './pdf-comparison.component.scss',
})
export class PdfComparisonComponent implements OnInit {
  libraries = signal<LibraryInfo[]>([]);
  results = signal<GenerationResult[]>([]);
  previewUrl = signal<SafeResourceUrl | null>(null);
  previewLibrary = signal<string>('');
  loadingLibrary = signal<string | null>(null);

  private currentBlobUrl: string | null = null;

  constructor(
    private pdfService: PdfService,
    private sanitizer: DomSanitizer,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.pdfService.getLibraries().subscribe({
      next: (libs) => this.libraries.set(libs),
      error: () =>
        this.snackBar.open('Error al cargar librerias. Verifique que el backend este corriendo.', 'OK', {
          duration: 5000,
        }),
    });
  }

  generatePdf(library: LibraryInfo): void {
    this.loadingLibrary.set(library.name);
    const startTime = performance.now();

    this.pdfService.generatePdf(library.name).subscribe({
      next: (response) => {
        const clientTimeMs = Math.round(performance.now() - startTime);
        const serverTimeMs = parseInt(response.headers.get('X-Generation-Time-Ms') || '0', 10);
        const blob = response.body!;
        const sizeKb = blob.size / 1024;

        // Clean up previous blob URL
        if (this.currentBlobUrl) {
          URL.revokeObjectURL(this.currentBlobUrl);
        }
        this.currentBlobUrl = URL.createObjectURL(blob);
        this.previewUrl.set(this.sanitizer.bypassSecurityTrustResourceUrl(this.currentBlobUrl));
        this.previewLibrary.set(library.name);

        // Add result
        const existing = this.results().filter((r) => r.libraryName !== library.name);
        this.results.set([
          ...existing,
          { libraryName: library.name, serverTimeMs, clientTimeMs, sizeKb, pdfUrl: this.currentBlobUrl },
        ]);

        this.loadingLibrary.set(null);
      },
      error: (err) => {
        this.loadingLibrary.set(null);
        this.snackBar.open(`Error generando PDF con ${library.name}: ${err.message}`, 'OK', {
          duration: 5000,
        });
      },
    });
  }

  downloadPdf(library: LibraryInfo): void {
    const result = this.results().find((r) => r.libraryName === library.name);
    if (result) {
      const a = document.createElement('a');
      a.href = result.pdfUrl;
      a.download = `comprobante-${library.name}.pdf`;
      a.click();
    } else {
      this.generateAndDownload(library);
    }
  }

  private generateAndDownload(library: LibraryInfo): void {
    this.pdfService.generatePdf(library.name).subscribe({
      next: (response) => {
        const blob = response.body!;
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `comprobante-${library.name}.pdf`;
        a.click();
        URL.revokeObjectURL(url);
      },
    });
  }

  generateAll(): void {
    for (const lib of this.libraries()) {
      this.generatePdf(lib);
    }
  }

  getLicenseColor(license: string): string {
    if (license.includes('Apache')) return 'primary';
    if (license.includes('AGPL')) return 'warn';
    return 'accent';
  }
}
