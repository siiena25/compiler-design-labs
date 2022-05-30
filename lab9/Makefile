all: lab9

lab9: clean
	yacc -d parser.y
	lex lexer.l
	cc lex.yy.c y.tab.c -o xml_parser -lm

clean:
	rm -rf xml_parser lex.yy.c y.tab.c y.tab.h