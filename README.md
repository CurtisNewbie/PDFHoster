# PDFHoster

Host PDF files on one device and browse them on other devices using web browser

This WebApp is powered by:
- Quarkus
- Angular
- and <a href="https://github.com/intbot/ng2-pdfjs-viewer">ng2-pdfjs-viewer</a> (which internally uses PDF.js)

## Usage

This app doesn't detect whether the files hosted are actually PDF files, it's your responsibility to put right files into the folder. All PDF files should be placed inside a folder called **`pdfs`**, if this folder doesn't exist, the app will create it on startup. The files in this folder are hosted and watched for changes, so you can add or remove PDFs at any time you want, the change will be detected.

The folder **`pdfs`** should be at the same level as the app (executable):
 
For example:

    ../
      |
      |_ pdfhoster-1.0-bundled.jar
      |
      |_ pdfs/
            |
            |_ 'core-java.pdf'
            |_ 'effective-java.pdf'
            |_ 'microservices-and-patterns.pdf'
