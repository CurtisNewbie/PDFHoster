import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { HttpService } from '../http.service';
import { PdfFile } from '../model/file';

@Component({
  selector: 'app-file-list',
  templateUrl: './file-list.component.html',
  styleUrls: ['./file-list.component.css']
})
export class FileListComponent implements OnInit {

  list: PdfFile[] = [];
  fileUploadUrl: string = this.http.getUploadUrl();
  found: PdfFile[] = [];
  // searchedStr = "";

  @Output()
  selectEmitter: EventEmitter<PdfFile> = new EventEmitter();

  constructor(private http: HttpService) { }

  ngOnInit() {
    this.http.getFiles().subscribe({
      next: (val) => {
        this.list = val;
      }, error: (e) => { console.log }
    });
  }

  onClick(f: PdfFile): void {
    this.selectEmitter.emit(f);
    console.log("Clicked", f.name);
  }

  search(searchedStr: any): void {
    let lowerStr: string = searchedStr.target.value.toLowerCase();
    if (lowerStr.length == 0 && this.found.length > 0) { this.found = []; }
    else {
      this.found = [];
      this.list.forEach((v, i, a) => {
        if (v.name.toLowerCase().indexOf(lowerStr) >= 0)
          this.found.push(v);
      })
    }
  }
}
