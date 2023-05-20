/*-***
 *
 * This file defines a stand-alone lexical analyzer for a subset of the Pascal
 * programming language.  This is the same lexer that will later be integrated
 * with a CUP-based parser.  Here the lexer is driven by the simple Java test
 * program in ./PascalLexerTest.java, q.v.  See 330 Lecture Notes 2 and the
 * Assignment 2 writeup for further discussion.
 *
 */


import java_cup.runtime.*;


%%
/*-*
 * LEXICAL FUNCTIONS:
 */

%cup
%line
%column
%unicode
%class Lexer

%{

/**
 * Return a new Symbol with the given token id, and with the current line and
 * column numbers.
 */
Symbol newSym(int tokenId) {
    return new Symbol(tokenId, yyline, yycolumn);
}

/**
 * Return a new Symbol with the given token id, the current line and column
 * numbers, and the given token value.  The value is used for tokens such as
 * identifiers and numbers.
 */
Symbol newSym(int tokenId, Object value) {
    return new Symbol(tokenId, yyline, yycolumn, value);
}

%}


/*-*
 * PATTERN DEFINITIONS:
 */


/**
 * Implement patterns as regex here
 */
intlit = [0-9][0-9]*
floatlit = [0-9]+[.][0-9]+
id = [a-zA-Z][a-zA-Z0-9]*
character  = [^\\\n\t\"\']|\\.
string = {character}*
charlit = \'{character}\'
strlit = \"{string}\"
whitespace = [ \n\t\r]
single_line_comment = \\.*
multi_line_comment =  \\\*(\\[^\*]|[^\\])*\*\\



%%
/**
 * LEXICAL RULES:
 */

/**
 * Implement terminals here, ORDER MATTERS!
 */

class           {return newSym(sym.CLASS, "class");}
read            {return newSym(sym.READ, "read");}
print           {return newSym(sym.PRINT, "print");}
printline       {return newSym(sym.PRINTLINE, "printline");}
return          {return newSym(sym.RETURN, "return");}
"{"             {return newSym(sym.LEFT_CURLY, "{");}
"}"             {return newSym(sym.RIGHT_CURLY, "}");}
"["             {return newSym(sym.LEFT_SQUARE, "[");}
"]"             {return newSym(sym.RIGHT_SQUARE, "]");}
"("             {return newSym(sym.LEFT_BRACKET, "(");}
")"             {return newSym(sym.RIGHT_BRACKET, ")");}
final           {return newSym(sym.FINAL, "final");}
int             {return newSym(sym.INT, "int");}
char            {return newSym(sym.CHAR, "char");}
bool            {return newSym(sym.BOOL, "bool");}
float           {return newSym(sym.FLOAT, "float");}
"="             {return newSym(sym.ASSIGNMENT, "=");}
":"             {return newSym(sym.COLON, ":");}
";"             {return newSym(sym.SEMI_COLON, ";");}
","             {return newSym(sym.COMMA, ",");}
void            {return newSym(sym.VOID, "void");}
true            {return newSym(sym.TRUE, "true");}
false           {return newSym(sym.FALSE, "false");}
"~"             {return newSym(sym.NEGATION, "~");}
"*"             {return newSym(sym.MULTIPLY, "*");}
"/"             {return newSym(sym.DIVIDE, "/");}
"+"             {return newSym(sym.ADD, "+");}
"-"             {return newSym(sym.SUBTRACT, "-");}
"<"             {return newSym(sym.LESS_THAN, "<");}
">"             {return newSym(sym.GREATER_THAN, ">");}
"<="            {return newSym(sym.LESS_THAN_EQUAL, "<=");}
">="            {return newSym(sym.GREATER_THAN_EQUAL, ">=");}
"=="            {return newSym(sym.EQUAL, "==");}
"<>"            {return newSym(sym.NOT_EQUAL, "<>");}
"||"            {return newSym(sym.OR, "||");}
"&&"            {return newSym(sym.AND, "&&");}
"++"			{return newSym(sym.INCREAMENT, "++");}
"--"			{return newSym(sym.DECREAMENT, "--");}
"?"             {return newSym(sym.QUESTION, "?");}
if              {return newSym(sym.IF, "if");}
else            {return newSym(sym.ELSE, "else");}
while           {return newSym(sym.WHILE, "while");}
{intlit}        {return newSym(sym.INTLIT, yytext());}
{floatlit}      {return newSym(sym.FLOATLIT, yytext());}
{charlit}       {return newSym(sym.CHARLIT, yytext());}
{strlit}        {return newSym(sym.STRLIT, yytext());}
{id}            {return newSym(sym.ID, yytext());}
{single_line_comment}   {}
{multi_line_comment}    {}
{whitespace}    { /* Ignore whitespace. */ }
.               {System.out.println("Illegal char, '" + yytext() +
                    "' line: " + yyline + ", column: " + yychar); } 