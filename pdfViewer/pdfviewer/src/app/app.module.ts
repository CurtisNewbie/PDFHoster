import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FileListComponent } from './file-list/file-list.component';
import { ViewerComponent } from './viewer/viewer.component';
import { PdfJsViewerModule } from 'ng2-pdfjs-viewer';

@NgModule({
  declarations: [
    AppComponent,
    FileListComponent,
    ViewerComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    PdfJsViewerModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
