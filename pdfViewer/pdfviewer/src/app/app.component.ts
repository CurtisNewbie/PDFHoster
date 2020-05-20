import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'pdfviewer';
  hidden: boolean = false;
  filename: string;

  toggleDisplay(): void {
    this.hidden = !this.hidden;
  }

  onSelect(fname: string) {
    this.filename = fname;
  }
}
