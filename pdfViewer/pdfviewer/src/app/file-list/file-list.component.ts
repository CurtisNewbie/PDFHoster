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

  onClick(i: number): void {
    this.selectEmitter.emit(this.list[i]);
    console.log("Clicked", i);
  }
}
