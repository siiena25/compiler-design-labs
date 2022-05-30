%{
#include <stdio.h>
extern FILE *yyin;

int yylex();

void yyerror(const char *s);

extern int tb;
extern int reverseStrokeFlag;
extern int text_flag;
extern int state_after_close_flag;
extern int open_tag_count;
extern int close_tag_count;
%}

%start statement
%token DECLARATION_OPEN DECLARATION_CLOSE
%token CDATA_OPEN CDATA_CLOSE
%token COMMENT_OPEN COMMENT_CLOSE
%token TAG_BEG_OPEN TAG_END_OPEN TAG_CLOSE EMPTY_TAG_CLOSE
%token NAME ATTRIBUTE TEXT QUESTION DTD_OPEN

%%

//возможные выражения
statement:
	declaration statement
	| comment statement
	| entry statement
	| dtd statement
	| ;

//doctype declaration
dtd:
	DTD_OPEN text TAG_CLOSE;

declaration:
	DECLARATION_OPEN name attribute DECLARATION_CLOSE;

comment:
	COMMENT_OPEN COMMENT_CLOSE
	| COMMENT_OPEN text COMMENT_CLOSE;

entry:
	tag_beg text statement tag_end
	| tag_beg statement tag_end
	| empty_tag;

text:
	TEXT text
	| NAME text
	| ':' text
	| '-' text
	| '=' text
	| '?' text
	| ATTRIBUTE text
	| ;

name:
	NAME attribute
	| NAME '-' name
	| NAME ':' name;

empty_tag: // <name/>
	TAG_BEG_OPEN name EMPTY_TAG_CLOSE
	;

attribute:
	NAME '=' ATTRIBUTE attribute
	| NAME ':' attribute
	| NAME '-' attribute
	| ;

tag_beg: // <name>
	TAG_BEG_OPEN name TAG_CLOSE {
		open_tag_count++;
		close_tag_count = 0;
		if (text_flag != 1) {
			reverseStrokeFlag = 0; ++tb;
		} else {
		        tb += open_tag_count - 1;
		}
	}
	;

tag_end: // </name>
        TAG_END_OPEN name TAG_CLOSE {
                close_tag_count++;
        	open_tag_count--;
        	tb -= open_tag_count;
        	if (reverseStrokeFlag == 2) {
        	    reverseStrokeFlag = 1;
        	}
        	if (text_flag != 1) {
        		reverseStrokeFlag = 1;
        		--tb;
        	} else {
        		text_flag = 0;
        	}
        }
	;

%%

int main(int argc, char *argv[])
{
	printf("\n# check file %s:\n\n", argv[1]);
	yyin = fopen(argv[1], "r");
	yyparse();
}