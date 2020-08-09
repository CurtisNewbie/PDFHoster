import { Component, ViewChild } from '@angular/core';
import { ViewerComponent } from './viewer/viewer.component';
import { PdfFile } from './model/file';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'PdfViewer';
  hidden: boolean = false;
  filename: string;

  @ViewChild("pdfViewer", { static: false })
  child: ViewerComponent;

  toggleDisplay(): void {
    this.hidden = !this.hidden;
  }

  onSelect(file: PdfFile) {
    this.filename = file.name;
    this.child.displayFile(file);
  }
}
