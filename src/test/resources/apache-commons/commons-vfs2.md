# apache-comnons系列之commons-vfs2 学习笔记
## 概述
- 参考
  - http://p7engqingyang.iteye.com/blog/1702429
  - http://blog.csdn.net/ffm83/article/details/42080387
## VFS
  - The main entry point for the VFS.  Used to create {@link FileSystemManager} instances.
## FileSystemManager
  - A FileSystemManager manages a set of file systems.  This interface is used to locate a {@link FileObject} by name from one of those file systems.
  - A file system manager can recognise several types of file names
    - Absolute URI(file:/c:/somefile)
    - Absolute local file name(/home/someuser/a-file)
    - Relative path(../somefile)
### StandardFileSystemManager---->DefaultFileSystemManager---->FileSystemManager
  - A {@link org.apache.commons.vfs2.FileSystemManager} that configures itself from an XML (Default: providers.xml) configuration file.
## FileSystem
  - A file system, made up of a hierarchy of files.
### LocalFileSystem---->AbstractFileSystem---->FileSystem
  - A local file system.
  - String rootFile
  - resolveFile(委托底层文件系统创建/解析文件)
    - org.apache.commons.vfs2.provider.local.LocalFileSystem.createFile
## FileObject
  - Represents a file, and is used to access the content and structure of the file
### LocalFile---->AbstractFileObject---->FileObject
  - A file object implementation which uses direct file access.
  - String rootFile;
  - File file;
  - org.apache.commons.vfs2.provider.local.LocalFile.doAttach(核心方法,将路径与文件绑定)
  - org.apache.commons.vfs2.provider.local.LocalFile.doGetInputStream(底层获取输入流,fs委托其获取)
  - org.apache.commons.vfs2.provider.local.LocalFile.doGetOutputStream(底层获取输出流,fs委托其获取)
### HdfsFileObject---->AbstractFileObject---->FileObject
  - A VFS representation of an HDFS file.
  - FileSystem hdfs;(委托给hadoop进行底层操作)
## FileProvider
  - A file provider.  Each file provider is responsible for handling files for a particular URI scheme.
### DefaultLocalFileProvider---->LocalFileProvider
  - DefaultLocalFileProvider---->AbstractOriginatingFileProvider---->AbstractFileProvider
  - A file system provider, which uses direct file access.
## VfsComponent
  - This interface is used to manage the lifecycle of all VFS components.
  - init/close
  - This includes all implementations of the following interfaces:
    - org.apache.commons.vfs2.provider.FileProvider
    - org.apache.commons.vfs2.FileSystem
    - org.apache.commons.vfs2.provider.FileReplicator
    - org.apache.commons.vfs2.provider.TemporaryFileStore
## FileReplicator
  - Responsible for making local replicas of files.
### DefaultFileReplicator
## VfsComponentContext
  - Allows VFS components to access the services they need, such as the file replicator.  A VFS component is supplied with a context as part of its initialisation.
### DefaultVfsComponentContext
  - DefaultFileSystemManager manager
## 测试
- org.apache.commons.vfs2.VFS(入口)
  - org.apache.commons.vfs2.VFS.getManager
  - createManager("org.apache.commons.vfs2.impl.StandardFileSystemManager")
- org.apache.commons.vfs2.impl.test.DefaultFileSystemManagerTest
  - providers = new HashMap();
  - components = new ArrayList();
  - LocalFileProvider localFileProvider;
  - FileProvider defaultProvider;
  - org.apache.commons.vfs2.impl.StandardFileSystemManager.init(初始化方法从providers.xml或者META-INF/vfs-providers.xml进行provider等信息的加载与配置)
    - org.apache.commons.vfs2.impl.StandardFileSystemManager.configure(org.w3c.dom.Element)
  - org.apache.commons.vfs2.impl.DefaultFileSystemManager.resolveFile(java.lang.String)