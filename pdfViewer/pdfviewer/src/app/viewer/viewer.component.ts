import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { HttpService } from '../http.service';
import { PdfJsViewerComponent } from 'ng2-pdfjs-viewer';
import { PdfFile } from '../model/file';

@Component({
  selector: 'app-viewer',
  templateUrl: './viewer.component.html',
  styleUrls: ['./viewer.component.css']
})
export class ViewerComponent implements OnInit {

  pdfDoc: Blob;
  file: PdfFile;
  shortname: string;

  @ViewChild('pdfViewer', { static: true })
  pdfViewer: PdfJsViewerComponent;

  @ViewChild('viewerPlaceHolder', { static: true })
  viewerPlaceHolder: ElementRef;

  constructor(private http: HttpService) {
  }

  ngOnInit() {
  }

  displayFile(file: PdfFile) {
    this.file = file;
    this.fetchFile();
    this.viewerPlaceHolder.nativeElement.scrollIntoView();
  }

  private fetchFile() {
    let id = this.file.id;
    if (id) {
      this.shortname = this.shorten(this.file.name);
      this.http.getFile(id).subscribe({
        next: (val) => { this.pdfViewer.pdfSrc = val; this.pdfViewer.refresh(); },
        error: (e) => { console.log },
        complete: () => {
          console.log("Fetched PDF file", this.file.name);
        }
      })
    }
  }

  private shorten(name: string): string {
    let delimitor = name.lastIndexOf("/");
    if (delimitor < name.length - 1) {
      return name.substring(delimitor + 1, name.length);
    }
  }
}
