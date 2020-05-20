import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'pdfviewer';
  hidden: boolean = false;


  toggleDisplay(): void {
    this.hidden = !this.hidden;
  }

}
