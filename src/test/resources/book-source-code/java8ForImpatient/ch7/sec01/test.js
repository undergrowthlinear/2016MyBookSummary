// Run as jjs < test.js

'Hello, World'
'Hello, World'.length
function factorial(n) { return n <= 1 ? 1 : n * factorial(n - 1) }
factorial(10)
var input = new java.util.Scanner(new java.net.URL('http://horstmann.com').openStream())
input.useDelimiter('$')
var contents = input.next()
contents.substring(0, 1000)
