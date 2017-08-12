# apache-comnons系列之commons-exec1.3 学习笔记
## 概述
- 参考
  - http://blog.csdn.net/fd_mas/article/details/50147701
  - http://blog.csdn.net/accountwcx/article/details/46787603
## Executor
````
The main abstraction to start an external process.
 * The interface allows to
 *  <li>set a current working directory for the subprocess</li>
 *  <li>provide a set of environment variables passed to the subprocess</li>
 *  <li>capture the subprocess output of stdout and stderr using an ExecuteStreamHandler</li>
 *  <li>kill long-running processes using an ExecuteWatchdog</li>
 *  <li>define a set of expected exit values</li>
 *  <li>terminate any started processes when the main process is terminating using a ProcessDestroyer</li>
 * The following example shows the basic usage:
 * Executor exec = new DefaultExecutor();
 * CommandLine cl = new CommandLine("ls -l");
 * int exitvalue = exec.execute(cl);
````
  - execute---->Methods for starting synchronous execution. The child process inherits all environment variables of the parent process
  - execute---->Methods for starting asynchronous execution. The child process inherits all environment variables of the parent process. Result provided to callback handler.
### DaemonExecutor---->DefaultExecutor
  - ExecuteStreamHandler streamHandler---->taking care of output and error stream
  - File workingDirectory---->the working directory of the process
  - ExecuteWatchdog watchdog---->monitoring of long running processes
  - CommandLauncher launcher---->launches the command in a new process
  - int[] exitValues---->the exit values considered to be successful
  - ProcessDestroyer processDestroyer---->optional cleanup of started processes
  - Thread executorThread---->worker thread for asynchronous execution
  - org.apache.commons.exec.DefaultExecutor.executeInternal(执行方法内部调用此方法)
    - Execute an internal process. If the executing thread is interrupted while waiting for the child process to return the child process will be killed
    - org.apache.commons.exec.DefaultExecutor.launch(利用命令创建器创建进程)
      - org.apache.commons.exec.launcher.Java13CommandLauncher.exec
        - Runtime.getRuntime().exec
    - 将进程的输出流、输入流、错误流转向流处理器(委托给org.apache.commons.exec.PumpStreamHandler)
      - streams.setProcessInputStream(process.getOutputStream())
      - streams.setProcessOutputStream(process.getInputStream())
      - streams.setProcessErrorStream(process.getErrorStream());
    - 利用看门狗监控进程的信息
      - watchdog.start(process)(委托给org.apache.commons.exec.ExecuteWatchdog.start---->org.apache.commons.exec.Watchdog.start)
    - 同步执行---->exitValue = process.waitFor();
    - 或者异步执行---->创建新线程执行进程的创建等操作---->this.executorThread = createThread(runnable, "Exec Default Executor");
## ExecuteStreamHandler
````
Used by {@code Execute} to handle input and output stream of subprocesses.
````
### PumpStreamHandler---->ExecuteStreamHandler
````
Copies standard output and error of sub-processes to standard output and error of the parent process. If output or error stream are set to null, any feedback from that stream will be lost.
````
  - Thread outputThread;/Thread errorThread;/Thread inputThread;
  - OutputStream out;/OutputStream err;/OutputStream err;
## ExecuteWatchdog---->TimeoutObserver
````
Destroys a process running for too long. For example:
When starting an asynchronous process than 'ExecuteWatchdog' is the keeper of the process handle. In some cases it is useful not to define
 a timeout (and pass 'INFINITE_TIMEOUT') and to kill the process explicitly using 'destroyProcess()'.
````
  - Process process
  - org.apache.commons.exec.ExecuteWatchdog.start(Watches the given process and terminates it, if it runs for too long. All information from the previous run are reset.)
    - this.notifyAll();
    - watchdog.start();
  - org.apache.commons.exec.ExecuteWatchdog.stop
    - watchdog.stop();
## Watchdog
  - observers = new Vector<TimeoutObserver>(1);
  - org.apache.commons.exec.Watchdog.run
  - org.apache.commons.exec.Watchdog.fireTimeoutOccured(通知超时时间的观察者)
## CommandLine
````
CommandLine objects help handling command lines specifying processes to execute. The class can be used to a command line by an application.
````
  - arguments = new Vector<Argument>()---->The arguments of the command
  - String executable---->The program to execute
  - org.apache.commons.exec.CommandLine.parse(解析命令与参数)
    - translateCommandline(line);
## DefaultExecuteResultHandler---->ExecuteResultHandler
````
The callback handlers for the result of asynchronous process execution. When a process is started asynchronously the callback provides you with the result of
 the executed process, i.e. the exit value or an exception
````
  - org.apache.commons.exec.ExecuteResultHandler.onProcessComplete(在执行器中用新线程创建新进程完成后的回调方法)
  - org.apache.commons.exec.DefaultExecuteResultHandler.waitFor
    - Causes the current thread to wait, if necessary, until the process has terminated. This method returns immediately if
    - the process has already terminated. If the process has not yet terminated, the calling thread will be blocked until the process exits
## ShutdownHookProcessDestroyer---->ProcessDestroyer
````
Destroys all registered {@link java.lang.Process} after a certain event, typically when the VM exits
````
  - processes = new Vector<Process>()---->the list of currently running processes
  - ProcessDestroyerImpl destroyProcessThread = null---->he thread registered at the JVM to execute the shutdown handler
  - org.apache.commons.exec.ShutdownHookProcessDestroyer.run(进程退出时的回调方法)
## Java13CommandLauncher---->CommandLauncherImpl---->CommandLauncher
````
Interface to shield the caller from the various platform-dependent implementations.
````
  - A command launcher for JDK/JRE 1.3 (and higher). Uses the built-in Runtime.exec() command
  - org.apache.commons.exec.launcher.Java13CommandLauncher.exec
## 测试
- org.apache.commons.exec.TutorialTest