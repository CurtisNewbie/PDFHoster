import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PdfFile } from './model/file';

const BASEURL = `http://${location.hostname}:8080`;

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(private http: HttpClient) { }

  getFiles(): Observable<PdfFile[]> {
    return this.http.get<PdfFile[]>(`${BASEURL}/file/all`, { responseType: "json" });
  }

  getFile(id: number): Observable<any> {
    return this.http.get(`${BASEURL}/file/${id}`, { responseType: 'blob', observe: "body" });
  }

  getUploadUrl(): string {
    return `${BASEURL}/file`;
  }
}
