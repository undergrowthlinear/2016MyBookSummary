# apache-comnons系列之commons-fileupload1.3.3 学习笔记
## 概述
- FileUploadBase
  - High level API for processing file uploads.
- FileUpload
- ServletFileUpload
  - High level API for processing file uploads.
- FileItemFactory
  - A factory interface for creating {@link FileItem} instances. Factories can provide their own custom configuration, over and above that provided by the default file upload implementation
  - createItem
- FileItem
  - This class represents a file or form item that was received within a<code>multipart/form-data</code> POST request
- FileUploadException
  - Exception for errors encountered while processing the request
## 测试
- org.apache.commons.fileupload.ServletFileUploadTest
    - parseRequest
        - Processes an <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a> compliant <code>multipart/form-data</code> stream.
          - boundary = getBoundary(contentType);
          - multi = new MultipartStream(input, boundary, notifier);
          - Streams.copy(item.openStream(), fileItem.getOutputStream(), true);