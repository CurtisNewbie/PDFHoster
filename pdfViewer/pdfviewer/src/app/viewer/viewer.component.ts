import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-viewer',
  templateUrl: './viewer.component.html',
  styleUrls: ['./viewer.component.css']
})
export class ViewerComponent implements OnInit {

  pdfDoc: Blob;

  @Input("filename")
  filename: string;

  constructor() { }

  ngOnInit() {
  }
}
