JAVA=java
JAVAC=javac
JFLEX=$(JAVA) -jar jflex-full-1.8.2.jar
CUPJAR=./java-cup-11b.jar
CUP=$(JAVA) -jar $(CUPJAR)
CP=.:$(CUPJAR)

default: all

.SUFFIXES: $(SUFFIXES) .class .java

.java.class:
		$(JAVAC) -cp $(CP) $*.java

FILE=    Lexer.java      parser.java    sym.java \
	LexerTest.java ScannerTest.java\
	Program.java Memberdecls.java Methoddecl.java Fielddecl.java Expr.java Stmt.java \
	Argdecl.java Name.java BinaryOperator.java Token.java

all: ScannerTests

ScannerTests: build
	@rm -f Outputs.txt;
	@for file in ./p3tests/*.as; do \
		echo "File: $$file" >> Outputs.txt; \
		$(JAVA) -cp $(CP) ScannerTest $$file >> Outputs.txt; \
	done;
	@cat -n Outputs.txt


build: Lexer.java parser.java $(FILE:java=class)

dump: Lexer.java parserD.java $(FILE:java=class)

clean:
		rm -f *.class *~ *.bak Lexer.java parser.java sym.java 

Lexer.java: tokens.jflex
		$(JFLEX) tokens.jflex

parser.java: grammar.cup
		$(CUP) -interface < grammar.cup

parserD.java: grammar.cup
		$(CUP) -interface -dump < grammar.cup
