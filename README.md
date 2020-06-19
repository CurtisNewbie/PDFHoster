# PDFHoster

Host PDF files on one device (as the server) and browse them on other devices (as the clients) using web browser. This app also supports uploading files on the client-side devices. Customer configuration is supported using both the `application.properties` and `CLI`, read the last section for more information.

This WebApp is powered by:

- Quarkus
- Angular
- and <a href="https://github.com/intbot/ng2-pdfjs-viewer">ng2-pdfjs-viewer</a> (which internally uses PDF.js)

## Usage

This app doesn't detect whether the files hosted are actually PDF files, it's your responsibility to put right files into the folder. By default, all PDF files should be placed inside a folder called **`pdfs`**, if this folder doesn't exist, the app will create it on startup (You can configure this, read the last section). The files in this folder are hosted and watched for changes, so you can add or remove PDFs at any time you want, the change will be detected.

By default, the folder **`pdfs`** should be at the same level as the app (executable):

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

Once the app is up and running, you can access the app via:

    http://localhost:8080/

Notice that you will need to change the **localhost** to the IP address of the hoster (e.g., 192.168.1.1).

## Extra Configuration

There are two ways to configure the app, one is through the `application.properties`, but you will have to build the whole app on your own. Another one is through the `CLI`. Both have their pros and cons, select one based on your need. Understand this chapter is important for you to use, because you might not want to use the default `/pdfs` directory.

**application.properties:**

e.g.,

    // this configures the absolute/relative path to your directory, which is by default "pdfs/"
    io.config.scan.dir=

    // this configures the default filename to be used for files uploaded when none provided
    io.config.def.filename=

**CLI Arguments:**

e.g.,

    // this can be used to disable file uploading functionality, it's enabled by default
    -DUploadingEnabled=

    for example:

        // this will disable the file uploading functionality, since we set it to false
        java -jar pdfhoster-1.0-bundled.jar -DUploadingEnabled=false

    // this allows you to overwrite the absolute/relative path to your directory, it's optional
    -DDirectory=

    for example:

        // this overwrites the previously configuration and uses the "/home/zhuangyongj/Documents/mypdfs"
        java -jar pdfhoster-1.0-bundled.jar -DDirectory=/home/zhuangyongj/Documents/mypdfs

Again, this is another typical example that I actually used:

    java -jar target/pdfhoster-1.0-SNAPSHOT-runner.jar -DUploadingEnabled=false -DDirectory=/home/zhuangyongj/Documents/pdfs

## Demo
<img src="https://user-images.githubusercontent.com/45169791/85094935-919f7480-b222-11ea-92fd-f3b78b483e9a.gif" width="700">
