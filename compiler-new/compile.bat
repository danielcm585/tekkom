java JLex.Main Scanner.lex
java java_cup.Main Parser.cup
ren Scanner.lex.java Yylex.java
javac -Xlint:-deprecation *.java