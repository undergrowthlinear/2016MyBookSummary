# apache-comnons系列之commons-net3.6 学习笔记
## 概述
### SocketClient
  - The SocketClient provides the basic operations that are required of client objects accessing sockets.
#### FTP
  - FTP provides the basic the functionality necessary to implement your own FTP client.  It extends org.apache.commons.net.SocketClient since
  - extending TelnetClient was causing unwanted behavior (like connections that did not time out properly)
##### FTPClient
  - FTPClient encapsulates all the functionality necessary to store and retrieve files from an FTP server.  This class takes care of all
  - low level details of interacting with an FTP server and provides a convenient higher level interface.
#### DiscardTCPClient
  - The DiscardTCPClient class is a TCP implementation of a client for the Discard protocol described in RFC 863.
##### EchoTCPClient
  - The EchoTCPClient class is a TCP implementation of a client for the Echo protocol described in RFC 862.
### ProtocolCommandSupport
  - ProtocolCommandSupport is a convenience class for managing a list of ProtocolCommandListeners and firing ProtocolCommandEvents.  You can
  - simply delegate ProtocolCommandEvent firing and listener registering/unregistering tasks to this class.
  - Object __source;
  - ListenerList __listeners
### ProtocolCommandEvent
  - There exists a large class of IETF protocols that work by sending an ASCII text command and arguments to a server, and then receiving an ASCII text reply.
### ProtocolCommandListener
  - EventListener
### FTPFile
  - The FTPFile class is used to represent information about files stored on an FTP server.
### FTPFileEntryParser
  - FTPFileEntryParser defines the interface for parsing a single FTP file listing and converting that information into an {@link org.apache.commons.net.ftp.FTPFile} instance.
#### UnixFTPEntryParser
  - Implementation FTPFileEntryParser and FTPFileListParser for standardUnix Systems.
### FTPFileFilters
  - Implements some simple FTPFileFilter classes.
## 测试
- org.apache.commons.net.SocketClientTest
    - protected int _timeout_----The timeout to use after opening a socket
    - protected Socket _socket_----The socket used for the connection.
    - protected InputStream _input_----The socket's InputStream.
    - protected OutputStream _output_----The socket's OutputStream.
    - protected SocketFactory _socketFactory_----The socket's SocketFactory.
    - protected ServerSocketFactory _serverSocketFactory_----The socket's ServerSocket Factory.
    - connect
      - Opens a Socket connected to a remote host at the specified port and originating from the current host at a system assigned port.
      - Before returning, {@link #_connectAction_  _connectAction_() } is called to perform connection initialization actions.
    - _connectAction_
      - _input_ = _socket_.getInputStream();
      - _output_ = _socket_.getOutputStream();
    - disconnect
      - Disconnects the socket connection.
      - closeQuietly
- FtpTest
    - protected BufferedReader _controlInput_----Wraps SocketClient._input_ to facilitate the reading of text from the FTP control connection.
    - protected BufferedWriter _controlOutput_----Wraps SocketClient._output_ to facilitate the writing of text to the FTP control connection.
    - _replyCode/_replyLines/_newReplyString/_commandSupport_
    - _connectAction_
      - _controlInput_ =new CRLFLineReader(new InputStreamReader(_input_, getControlEncoding()));
      - _controlOutput_ =new BufferedWriter(new OutputStreamWriter(_output_, getControlEncoding()));
    - sendCommand
      -  Sends an FTP command to the server, waits for a reply and returns the numerical response code
      - __buildMessage
      - __send
          - _controlOutput_.write(message);
          - _controlOutput_.flush();
          - fireCommandSent
            - If there are any listeners, send them the command details./事件监听处理发送事件
            - getCommandSupport().fireCommandSent(command, message);
          - __getReply
            - String line = _controlInput_.readLine();/等待命令返回信息
            - _replyLines.add(line);
            - fireReplyReceived
      - fireReplyReceived
        - If there are any listeners, send them the reply details./事件监听处理返回信息
        - getCommandSupport().fireReplyReceived(replyCode, reply);
- org.apache.commons.net.ftp.FTPClientTest/examples.ftp.FTPClientExample
    - private int __dataConnectionMode----决定数据传输时的初始化动作由服务器还是客户端进行初始化
    - __initDefaults
        - __dataConnectionMode = ACTIVE_LOCAL_DATA_CONNECTION_MODE;(数据传输由服务器进行初始化)
    - login
        - Login to the FTP server using the provided username and password.
    - user
        - A convenience method to send the FTP USER command to the server,receive the reply, and return the reply code.
        - FTPCmd.USER
    - pass
        - A convenience method to send the FTP PASS command to the server,receive the reply, and return the reply code
        - FTPCmd.PASS
    - storeFile
        - Stores a file on the server using the given name and taking input from the given InputStream.
        - _storeFile
            - _openDataConnection_
              - if (__dataConnectionMode == ACTIVE_LOCAL_DATA_CONNECTION_MODE)
                - port(getReportHostAddress(), server.getLocalPort())
                -  socket = server.accept();
              - else
                - socket = _socketFactory_.createSocket();
            - Util.copyStream
            - completePendingCommand()
    - retrieveFile
        - Retrieves a named file from the server and writes it to the given OutputStream.
        - _retrieveFile
            - _openDataConnection_
            - Util.copyStream
            - completePendingCommand();
    - listFiles
      - Using the default system autodetect mechanism, obtain a list of file information for the current working directory.
      - 依赖解析引擎按照指定的解析格式和过滤器选出需要的文件列表
        - FTPListParseEngine engine = initiateListParsing((String) null, pathname);
        - engine.getFiles()