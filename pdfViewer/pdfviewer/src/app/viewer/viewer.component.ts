import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { HttpService } from '../http.service';
import { PdfJsViewerComponent } from 'ng2-pdfjs-viewer';

@Component({
  selector: 'app-viewer',
  templateUrl: './viewer.component.html',
  styleUrls: ['./viewer.component.css']
})
export class ViewerComponent implements OnInit {

  pdfDoc: Blob;
  filename: string;
  shortname: string;

  @ViewChild('pdfViewer', { static: true })
  pdfViewer: PdfJsViewerComponent;

  @ViewChild('viewerPlaceHolder', { static: true })
  viewerPlaceHolder: ElementRef;

  constructor(private http: HttpService) {
  }

  ngOnInit() {
  }

  displayFile(filename: string) {
    this.filename = filename;
    this.fetchFile();
    this.viewerPlaceHolder.nativeElement.scrollIntoView();
  }

  private fetchFile() {
    let name = this.filename;
    if (name) {
      this.shortname = this.shorten(name);
      this.http.getFile(name).subscribe({
        next: (val) => { this.pdfViewer.pdfSrc = val; this.pdfViewer.refresh(); },
        error: (e) => { console.log },
        complete: () => {
          console.log("Fetched PDF file", name);
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
