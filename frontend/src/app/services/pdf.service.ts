import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LibraryInfo } from '../models/library-info.model';

@Injectable({ providedIn: 'root' })
export class PdfService {
  private baseUrl = '/api/pdf';

  constructor(private http: HttpClient) {}

  getLibraries(): Observable<LibraryInfo[]> {
    return this.http.get<LibraryInfo[]>(`${this.baseUrl}/libraries`);
  }

  generatePdf(library: string): Observable<HttpResponse<Blob>> {
    return this.http.get(`${this.baseUrl}/${library}`, {
      responseType: 'blob',
      observe: 'response',
    });
  }
}
