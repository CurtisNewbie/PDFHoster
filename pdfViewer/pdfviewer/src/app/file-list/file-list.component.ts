import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';

@Component({
  selector: 'app-file-list',
  templateUrl: './file-list.component.html',
  styleUrls: ['./file-list.component.css']
})
export class FileListComponent implements OnInit {

  list: string[] = [];
  hidden: boolean = false;

  @Output()
  click: EventEmitter<string> = new EventEmitter();

  constructor() { }

  ngOnInit() { }

  onClick(i: number): void {
    this.click.emit(this.list[i]);
    console.log("Clicked", i);
  }

  toggleDisplay(): void {
    this.hidden = !this.hidden;
  }
}
