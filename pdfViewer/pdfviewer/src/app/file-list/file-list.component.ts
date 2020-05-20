import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';

@Component({
  selector: 'app-file-list',
  templateUrl: './file-list.component.html',
  styleUrls: ['./file-list.component.css']
})
export class FileListComponent implements OnInit {

  list: string[] = ["/home/zhuangyongj/git/pdfHoster/pdfHoster/pdfhoster/target/pdfs/core-java-cheasheet.pdf", "/home/zhuangyongj/git/pdfHoster/pdfHoster/pdfhosdfasdfet/pdfsdfasjava-casdsadaheet.pdf", "/home/zhuangyongj/git/pdfHoster/pdfHoster/pasdf/pdfs/core-java-chesdfet.pdf"];

  @Output()
  selectEmitter: EventEmitter<string> = new EventEmitter();

  constructor() { }

  ngOnInit() { }

  onClick(i: number): void {
    this.selectEmitter.emit(this.list[i]);
    console.log("Clicked", i);
  }
}
