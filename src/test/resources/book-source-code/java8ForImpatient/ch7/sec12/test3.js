#!/data/apps/jdk1.8.0/bin/jrunscript -f

// Change the path above to your Java installation, then run as
// chmod +x test3.js
// ./test3.js fred

if (!arguments[0]) {
    print("Usage: ./test3 username")
    exit(1)
}

var javaHome = $ENV.JAVA_HOME
print(javaHome)
print("Hello ${arguments[0]}, please reenter your name")
var username = readLine('Username: ')
var password = java.lang.System.console().readPassword("${username}, please enter your password: ")
print(new java.lang.String(password).replaceAll('.', '*'))


