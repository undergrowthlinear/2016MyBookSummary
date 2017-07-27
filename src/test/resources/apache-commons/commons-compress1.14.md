# apache-comnons系列之commons-compress1.14 学习笔记
## 概述
- ArchiveStreamProvider
  - Creates Archive {@link ArchiveInputStream}s and {@link ArchiveOutputStream}s
- ArchiveStreamFactory
  - Factory to create Archive[In|Out]putStreams from names or the first bytes of the InputStream. In order to add other implementations, you should extend
  - ArchiveStreamFactory and override the appropriate methods (and call their implementation from super of course).
  - ArchiveStreamFactory.ZIP
- ArchiveInputStream
- ZipArchiveInputStream
  - Implements an input stream that can read Zip archives.
- ArchiveOutputStream
- ZipArchiveOutputStream
  - Reimplementation of {@link java.util.zip.ZipOutputStream java.util.zip.ZipOutputStream} that does handle the extended functionality of this package, especially internal/external file
  - attributes and extra fields with different layouts for local file data and central directory entries.
- JarArchiveOutputStream
  - Subclass that adds a special extra field to the very first entry which allows the created archive to be used as an executable jar on Solaris.
## 测试
- org.apache.commons.compress.archivers.zip.ZipArchiveInputStreamTest
    - getNextZipEntry
      - readFirstLocalFileHeader
    - IOUtils.toByteArray(in)
    - matches
      - Checks if the signature matches what is expected for a zip file.Does not currently handle self-extracting zips which may have arbitrary leading content.
- org.apache.commons.compress.archivers.ArchiveOutputStreamTest
    - testCallSequenceZip
      - putArchiveEntry
        - writeEntryHeader
     - IOUtils.copy(is, aos1)