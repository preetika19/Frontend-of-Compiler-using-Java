JAVA=java
JAVAC=javac
JFLEX=$(JAVA) -jar jflex-full-1.8.2.jar
CUPJAR=./java-cup-11b.jar
CUP=$(JAVA) -jar $(CUPJAR)
CP=.:$(CUPJAR)

default: run

.SUFFIXES: $(SUFFIXES) .class .java

.java.class:
		$(JAVAC) -cp $(CP) $*.java

FILE=    Lexer.java      parser.java    sym.java \
	LexerTest.java ScannerTest.java\
	Program.java Memberdecls.java Methoddecl.java Fielddecl.java Expr.java Stmt.java \
	Argdecl.java Name.java Token.java

run: example_input.txt Phase2_empty.txt Phase2_fields.txt Phase2_full.txt Phase2_methods.txt

example_input.txt: all
		$(JAVA) -cp $(CP) ScannerTest example_input.txt > example_output.txt
		cat example_input.txt
		cat -n example_output.txt

Phase2_empty.txt: all
		$(JAVA) -cp $(CP) ScannerTest Phase2_empty.txt > Phase2_empty_output.txt
		cat Phase2_empty.txt
		cat -n Phase2_empty_output.txt

Phase2_fields.txt: all
		$(JAVA) -cp $(CP) ScannerTest Phase2_fields.txt > Phase2_fields_output.txt
		cat Phase2_fields.txt
		cat -n Phase2_fields_output.txt

Phase2_full.txt: all
		$(JAVA) -cp $(CP) ScannerTest Phase2_full.txt > Phase2_full_output.txt
		cat Phase2_full.txt
		cat -n Phase2_full_output.txt

Phase2_methods.txt: all
		$(JAVA) -cp $(CP) ScannerTest Phase2_methods.txt > Phase2_methods_output.txt
		cat Phase2_methods.txt
		cat -n Phase2_methods_output.txt


all: Lexer.java parser.java $(FILE:java=class)

clean:
		rm -f *.class *~ *.bak Lexer.java parser.java sym.java

Lexer.java: tokens.jflex
		$(JFLEX) tokens.jflex

parser.java: grammar.cup
		$(CUP) -interface < grammar.cup

parserD.java: grammar.cup
		$(CUP) -interface -dump < grammar.cup
