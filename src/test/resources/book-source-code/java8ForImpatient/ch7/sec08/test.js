// Run as jjs < test.js

var names = java.util.Arrays.asList('Fred', 'Wilma', 'Barney')
var first = names[0]
names[0] = 'Duke'
for each (var name in names) print(name)
var scores = new java.util.HashMap
scores['Fred'] = 10 // Calls scores.put('Fred', 10)
for each (var e in scores.entrySet()) print(e.key + "->" + e.value)
