// Run as jjs -scripting test1.js

var output = `ls -al`

print(output)

var output2 = $EXEC('grep -v java', `ls -al`)

print(output2)
