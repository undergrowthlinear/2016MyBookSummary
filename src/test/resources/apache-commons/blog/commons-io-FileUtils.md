# apache-comnons系列之commons-io-2.5 org.apache.commons.io.FileUtils学习笔记
## 概述
* Facilities are provided in the following areas:
     * writing to a file
     * reading from a file
     * converting to and from a URL
     * comparing file content
     * file last changed date
     * calculating a checksum
     * make a directory including parent directories
     * copying files and directories
     * deleting files and directories
     * listing files and directories by filter and extension
## 测试
* org.apache.commons.io.FileUtilsTestCase
* directory
    * forceMkdir
        * Makes a directory, including any necessary but nonexistent parent directories.
    * copyFile
        * This method copies the contents of the specified source file to the specified destination file. The directory holding the destination file is created if it does not exist. If the destination file exists, then this method will overwrite it
        * doCopyFile完成复制
    * deleteDirectory
        * Deletes a directory recursively
        * cleanDirectory/directory.delete
    * listFiles
        * Finds files within a given directory (and optionally its subdirectories). All files found are filtered by an IOFileFilter.
        * innerListFiles--FileFilterUtils.or--directory.listFiles((FileFilter) filter)
* file
    * getFile/toFile
        * Construct a file from the set of name elements
    * openInputStream/openOutputStream
    * toURLs
        * Converts each of an array of <code>File</code> to a <code>URL</code>.
    * isFileNewer
        * Tests if the specified <code>File</code> is newer than the reference <code>File</code>.
    * byteCountToDisplaySize
        * Returns a human-readable version of the file size, where the input represents a specific number of bytes
    * contentEquals
        * This method checks to see if the two files are different lengths or if they point to the same file, before resorting to byte-by-byte comparison of the contents.
        * IOUtils.contentEquals(input1, input2)
## FilenameUtils
* This class defines six components within a filename
 * (example C:\dev\project\file.txt):
 * the prefix - C:\
 * the path - dev\project\
 * the full path - C:\dev\project\
 * the name - file.txt
 * the base name - file
 * the extension - txt