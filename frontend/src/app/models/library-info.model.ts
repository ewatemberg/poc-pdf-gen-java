export interface LibraryInfo {
  name: string;
  version: string;
  license: string;
  approach: string;
  endpoint: string;
}

export interface GenerationResult {
  libraryName: string;
  serverTimeMs: number;
  clientTimeMs: number;
  sizeKb: number;
  pdfUrl: string;
}
