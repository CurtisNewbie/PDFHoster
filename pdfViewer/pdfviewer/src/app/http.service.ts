import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASEURL = `http://${location.hostname}:8080`;

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(private http: HttpClient) { }

  getFileNames(): Observable<string[]> {
    return this.http.get<string[]>(`${BASEURL}/file/names`, { responseType: "json" });
  }

  getFile(filename: string): Observable<any> {
    let headers: HttpHeaders = new HttpHeaders({ 'filename': filename });
    return this.http.get(`${BASEURL}/file`, { headers: headers, responseType: 'blob', observe: "body" });
  }

  getUploadUrl(): string {
    return `${BASEURL}/file`;
  }
}
