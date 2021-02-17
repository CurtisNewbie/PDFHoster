rm -r ../pdfHoster/pdfhoster/src/main/resources/META-INF/resources/*
(cd ../pdfViewer/pdfviewer; ng build;)
cp -r ../pdfViewer/pdfviewer/dist/pdfviewer/* ../pdfHoster/pdfhoster/src/main/resources/META-INF/resources
(cd ../pdfHoster/pdfhoster; mvn clean package)
find .. -name *runner.jar -type f -exec cp {} . \;


