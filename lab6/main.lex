%option noyywrap bison-bridge bison-locations
%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <ctype.h>

#define TAG_NUMBER 1
#define TAG_IDENT  2

char *tag_names[] = { "END_OF_PROGRAM", "RIM_NUMBER", "IDENT" };

typedef struct Position Position;

struct Pair {
    char *value;
    int key;
};

struct Dictionary {
    struct Pair *pairs;
    int size;
};

struct Dictionary dictionary = {NULL,-1};

int find_key(char *ident) {
    for (int i = 0; i < dictionary.size; i++) {
        if (strcmp(dictionary.pairs[i].value, ident) == 0) {
            return dictionary.pairs[i].key;
        }
    }
    dictionary.size = dictionary.size + 1;
    dictionary.pairs = realloc(dictionary.pairs, (dictionary.size + 1) * sizeof(struct Pair));
    struct Pair p;
    p.value = (char*)malloc(sizeof(char) * 100);
    strcpy(p.value, ident);
    p.key = dictionary.size + 1;
    dictionary.pairs[dictionary.size] = p;
    return dictionary.size + 1;
}

struct Position {
	int line, pos, index;
};

void print_pos(Position *p) {
	printf("(%d,%d)", p->line, p->pos);
}

struct Fragment {
	Position starting, following;
};

typedef struct Fragment YYLTYPE;
typedef struct Fragment Fragment;

void print_frag(Fragment* f) {
	print_pos(&(f->starting));
	printf("-");
	print_pos(&(f->following));
}

union Token {
	int value;
};

typedef union Token YYSTYPE;

struct Position cur;
#define YY_USER_ACTION {             \
    int i;                           \
    yylloc->starting = cur;          \
    for (i = 0; i < yyleng; i++) {   \
        if (yytext[i] == '\n') {     \
            cur.line++;              \
            cur.pos = 1;             \
        }                            \
        else                         \
            cur.pos++;               \
        cur.index++;                 \
    }                                \
    yylloc->following = cur;         \
}

void init_scanner (char *program) {
    cur.line = 1;
    cur.pos = 1;
    cur.index = 0;
    yy_scan_string(program);
}

void err (char *msg) {
    printf ("Error");
    print_pos(&cur);
    printf(":%s\n",msg);
}

char *cnvtolwr(char *str)
{
    unsigned char *mystr = (unsigned char *)str;

    while (*mystr) {
        *mystr = tolower(*mystr);
        mystr++;
    }
    return str;
}

static int roman_to_integer(char c)
{
    switch(c) {
    case 'i':
        return 1;
    case 'v':
        return 5;
    case 'x':
        return 10;
    case 'l':
        return 50;
    case 'c':
        return 100;
    case 'd':
        return 500;
    case 'm':
        return 1000;
    default:
        return 0;
    }
}

int roman_to_int (char *s)
{
    int i;
    int int_num = roman_to_integer(s[0]);

    for (i = 1; s[i] != '\0'; i++) {
        int prev_num = roman_to_integer(s[i - 1]);
        int cur_num = roman_to_integer(s[i]);
        if (prev_num < cur_num) {
            int_num = int_num - prev_num + (cur_num - prev_num);
        } else {
            int_num += cur_num;
        }
    }
    return int_num;
}

%}

RIM_NUMBER [Nn][Ii][Hh][Ii][Ll]|[Mm]{0,3}([Cc][Mm]|[Cc][Dd]|[Dd]?[Cc]{0,3})?([Xx][Cc]|[Xx][Ll]|[Ll]?[Xx]{0,3})?([Ii][Xx]|[Ii][Vv]|[Vv]?[Ii]{0,3})?
IDENT [0-9]+

%%
[\n\t ]+

{RIM_NUMBER}    {
                         char *lowerText = cnvtolwr(yytext);
                         if (strcmp(lowerText, "nihil") == 0) {
                             yylval->value = 0;
                         } else {
                             yylval->value = roman_to_int(lowerText);
                         }
                         return TAG_NUMBER;
             }

{IDENT}      {
                         yylval->value = find_key(yytext);
                         return TAG_IDENT;
             }

.         err("ERROR");

<<EOF>>     return 0;

%%


int main(){
    int tag;
    YYSTYPE value;
    YYLTYPE coords;
    FILE *inputfile;
    long size_str;
    char *str;
    union Token token;
    inputfile = fopen("test.txt", "r");
    fseek(inputfile, 0, SEEK_END);
    size_str = ftell(inputfile);
    rewind(inputfile);
    str = (char*)malloc(sizeof(char)*(size_str + 1));
    size_t n = fread(str, sizeof(char), size_str, inputfile);
    str[size_str] = '\0';
    fclose (inputfile);
    init_scanner(str);
    printf("Tokens:\n");
    do {
        tag = yylex(&value, &coords);
        if (tag != 0){
            printf("%s ", tag_names[tag]);
            print_frag(&coords);
            printf(": %d\n", value.value);
        }
    }
    while (tag != 0);
    free(str);
    printf("\nDictionary:\n");
    for (int i = 0; i <= dictionary.size; i++) {
        printf("%d: %s\n", dictionary.pairs[i].key, dictionary.pairs[i].value);
        free(dictionary.pairs[i].value);
    }
    return 0;
}