# apache-comnons系列之commons-io-2.5 org.apache.commons.io.IOUtils学习笔记
## 概述
- OutputStream、InputStream、Writer、Reader学习笔记
  - http://blog.csdn.net/undergrowth/article/details/43820639
* General IO stream manipulation utilities.
 * This class provides static utility methods for input/output operations.
     * closeQuietly - these methods close a stream ignoring nulls and exceptions
     * toXxx/read - these methods read data from a stream
     * write - these methods write data to a stream
     * copy - these methods copy all the data from one stream to another
     * contentEquals - these methods compare the content of two streams
 * byte-to-char methods and char-to-byte methods
     * default encoding and encoding
 * buffered internally
     * default buffer size of 4K
## 测试
* org.apache.commons.io.IOUtilsTestCase
* closeQuietly
    * Closes an <code>OutputStream</code> unconditionally
    * Equivalent to {@link OutputStream#close()}, except any exceptions will be ignored
    * This is typically used in finally blocks
    * 借助于closeable.close()关闭源
* toByteArray
    * 依赖copy
* copy
    * 依赖copyLarge,copyLarge以4k为一组复制数据
    * Large streams (over 2GB) will return a bytes copied value of <code>-1</code> after the copy has completed since the correct number of bytes cannot be returned as an int
* readLines
    * This method buffers the input internally, so there is no need to use a <code>BufferedInputStream</code>.
* contentEquals
    * Compares the contents of two Streams to determine if they are equal or not.
    * This method buffers the input internally using <code>BufferedInputStream</code> if they are not already buffered.
    * 一个byte一个byte比较/字节和字符