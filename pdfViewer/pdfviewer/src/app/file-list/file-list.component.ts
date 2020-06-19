import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { HttpService } from '../http.service';

@Component({
  selector: 'app-file-list',
  templateUrl: './file-list.component.html',
  styleUrls: ['./file-list.component.css']
})
export class FileListComponent implements OnInit {

  list: string[] = [];
  fileUploadUrl: string = this.http.getUploadUrl();
  found: string[] = [];
  // searchedStr = "";

  @Output()
  selectEmitter: EventEmitter<string> = new EventEmitter();

  constructor(private http: HttpService) { }

  ngOnInit() {
    this.http.getFileNames().subscribe({
      next: (val: string[]) => {
        this.list = val;
      }, error: (e) => { console.log }
    });
  }

  onClick(name: string): void {
    this.selectEmitter.emit(name);
    console.log("Clicked", name);
  }

  search(searchedStr: any): void {
    let lowerStr: string = searchedStr.target.value.toLowerCase();
    if (lowerStr.length == 0 && this.found.length > 0) { this.found = []; }
    else {
      this.found = [];
      this.list.forEach((v, i, a) => {
        if (v.toLowerCase().indexOf(lowerStr) >= 0)
          this.found.push(v);
      })
    }
  }
}
