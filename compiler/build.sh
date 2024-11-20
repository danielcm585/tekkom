javac JLex/Main.java
javac java_cup/Main.java
java JLex.Main Scanner.lex
java java_cup.Main Parser.cup
mv Scanner.lex.java Yylex.java
javac *.java