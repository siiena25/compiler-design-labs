%{
#include "y.tab.h"
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

int yyerror();

int lines_number = 0;
bool comment_check = false;
bool debug = false;
bool lines_number_mode = false;
bool firstDecl = true;

int tb = 0;
int reverseStrokeFlag = 0; // флаг обратного хода
int state_after_close_flag = 0;
int close_tag_count = 0;
int open_tag_count = 0;
int text_flag = 0;
int i = 0;
int max_lines_number = 10000;
int max_string_length = 40;
int cur_string_length = 0;

void tbs(int tb, int reverseStrokeFlag) {
	if (lines_number < max_lines_number) {
	    int count = 0;
	    switch (reverseStrokeFlag) {
	        case 0:
	            count = open_tag_count;
	            break;
	        case 2:
            	count = abs(open_tag_count - close_tag_count);
            	break;
            default:
            	 count = open_tag_count - 1;
            	 break;
	    }
       	for (i = 0; i < count; i++) {
          	printf("    ");
        }
	}
	//printf(" tb = %d, flag = %d, open = %d, close = %d;", tb, reverseStrokeFlag, open_tag_count, close_tag_count);
}

%}

letter												[A-Za-z_]
digit												[0-9]
special_symbols										"&lt"|"&gt"|"&amp"
all_symbols											[\~\@\#\$\%\^\*\!\"\(\)\+\{\}\"\|\[\]\;\'\\\,\.\/]

%%

"<!--"												{
                                                        if (debug) {
                                                            printf("COMMENT_OPEN\n");
                                                        } else {
                                                            //printf("<!--");
                                                        }
                                                        comment_check = true;
                                                        printf("%s", yytext);
                                                        //return COMMENT_OPEN;
                                                    }
"-->"												{
                                                        if (debug) {
                                                            printf("COMMENT_CLOSE\n");
                                                        } else {
                                                            //printf("-->");
                                                        }
                                                        comment_check = false;
                                                        printf("%s", yytext);
                                                        //return COMMENT_CLOSE;
                                                    }
"<?"												{
                                                        if(!comment_check)
                                                        {
                                                            if (debug) {
                                                                printf("DECLARATION_OPEN\n");
                                                            } else {
                                                                lines_number++;
                                                                if (lines_number_mode) {
                                                                    printf("%d.", lines_number);
                                                                }
                                                                printf("<?");
                                                            }
                                                            return DECLARATION_OPEN;
                                                        } else {
                                                            printf("%s", yytext);
                                                            //printf("СOMMENT_DO\n");
                                                        }
                                                    }
"?>"												{
                                                        if(!comment_check)
                                                        {
                                                            if (debug) {
                                                                printf("DECLARATION_CLOSE\n");
                                                            } else {
                                                                printf("?>");
                                                            }
                                                            return DECLARATION_CLOSE;
                                                        } else {
                                                            printf("%s", yytext);
                                                            //printf("СOMMENT_DC\n");
                                                        }
                                                    }
"<!DOCTYPE"											{
                                                        if(!comment_check)
                                                        {
                                                            if (debug) {
                                                                printf("DTD_OPEN\n");
                                                            } else {
                                                                if (lines_number < max_lines_number) {
                                                                    lines_number++;
                                                                    printf("\n");
                                                                    if (lines_number_mode) {
                                                                        printf("%d.", lines_number);
                                                                    }
                                                                }
                                                                printf("<!DOCTYPE");
                                                                //lines_number++;
                                                            }
                                                            return DTD_OPEN;
                                                        } else {
                                                            printf("%s", yytext);
                                                            //printf("СOMMENT_DTD\n");
                                                        }
                                                    }
"<"													{
                                                        state_after_close_flag = 0;
                                                        //printf("!state_after_close_flag=0!");
                                                        if(!comment_check)
                                                        {
                                                            if (debug) {
                                                                printf("TAG_BEG_OPEN\n");
                                                            } else {
                                                                if (lines_number < max_lines_number) {
                                                                    lines_number++;
                                                                    printf("\n");
                                                                    if (lines_number_mode) {
                                                                        printf("%d.", lines_number);
                                                                    }
                                                                }
                                                                if (open_tag_count != 0) {
                                                                    if (close_tag_count > 1) {
                                                                        tbs(tb, 2);
                                                                    } else {
                                                                        tbs(tb, reverseStrokeFlag);
                                                                    }
                                                                }
                                                                printf("<");
                                                            }
                                                            //lines_number++;
                                                            return TAG_BEG_OPEN;
                                                        } else {
                                                            printf("%s", yytext);
                                                            //printf("СOMMENT_TBO\n");
                                                        }
                                                    }
"</"												{
                                                        state_after_close_flag = 0;
                                                        if(!comment_check)
                                                        {
                                                            if (debug) {
                                                                printf("TAG_END_OPEN\n");
                                                            } else {
                                                                if (text_flag != 1) {
                                                                    if (lines_number < max_lines_number) {
                                                                        lines_number++;
                                                                        printf("\n");
                                                                        if (lines_number_mode) {
                                                                            printf("%d.", lines_number);
                                                                        }
                                                                    }
                                                                    if (close_tag_count > 0) {
                                                                        if (close_tag_count != 2) {
                                                                            tbs(tb, 2);
                                                                        } else {
                                                                            tbs(tb, 1);
                                                                        }
                                                                    } else {
                                                                        tbs(tb, reverseStrokeFlag);
                                                                    }
                                                                }
                                                                //printf("%d-%d</", text_flag, open_tag_count);
                                                                printf("</");
                                                            }
                                                            //lines_number++;
                                                            return TAG_END_OPEN;
                                                        } else {
                                                            printf("%s", yytext);
                                                            //printf("СOMMENT_TEO\n");
                                                        }
                                                    }
">"													{
                                                        state_after_close_flag = 1;
                                                        //printf("!state_after_close_flag=1!");
                                                        if(!comment_check)
                                                        {
                                                            if (debug) {
                                                                printf("TAG_CLOSE\n");
                                                            } else {
                                                                printf(">");
                                                            }
                                                            return TAG_CLOSE;
                                                        } else {
                                                            printf("%s", yytext);
                                                            //printf("СOMMENT_TC\n");
                                                        }
                                                    }
"/>"												{
                                                        if(!comment_check) {
                                                            if (debug) {
                                                                printf("EMPTY_TAG_CLOSE\n");
                                                            } else {
                                                                printf("/>");
                                                            }
                                                            return EMPTY_TAG_CLOSE;
                                                        } else {
                                                            printf("%s", yytext);
                                                            //printf("СOMMENT_ETC\n");
                                                        }
                                                    }
({letter})({letter}|{digit})*                       {
                                                        if(!comment_check)
                                                        {
                                                            if (debug) {
                                                                printf("NAME %s\n", yytext);
                                                            } else {
                                                                printf("%s", yytext);
                                                                if (state_after_close_flag == 1) {
                                                                    text_flag = 1;
                                                                    //printf("!text_flag=1!");
                                                                }
                                                            }
                                                            return NAME;
                                                        } else {
                                                            printf("%s", yytext);
                                                            //printf("СOMMENT_N\n");
                                                        }
                                                    }
((\")({letter}|{digit}|{special_symbols}|[^">"])*(\"))	{
                                                        if(!comment_check)
                                                        {
                                                            if (debug) {
                                                                printf("ATTRIBUTE %s\n", yytext);
                                                            } else {
                                                                printf("%s", yytext);
                                                            }
                                                            return ATTRIBUTE;
                                                        } else {
                                                            printf("%s", yytext);
                                                            //printf("СOMMENT_ATTR\n");
                                                        }
                                                    }
((\')({letter}|{digit}|{special_symbols}|[^">"])*(\'))	{
                                                        if(!comment_check)
                                                        {
                                                            if (debug) {
                                                                printf("ATTRIBUTE %s\n", yytext);
                                                            } else {
                                                                printf("%s", yytext);
                                                            }
                                                            return ATTRIBUTE;
                                                        } else {
                                                            printf("%s", yytext);
                                                            //printf("СOMMENT_ATTR\n");
                                                        }
                                                    }
({all_symbols}|{special_symbols}|{digit})* 			{
                                                        if(!comment_check)
                                                        {
                                                            if (debug) {
                                                                printf("TEXT %s\n", yytext);
                                                            } else {
                                                                printf("%s", yytext);
                                                            }
                                                            //lines_number++;
                                                            return TEXT;
                                                        } else {
                                                            printf("%s", yytext);
                                                            //printf("СOMMENT_TXT\n");
                                                        }
                                                    }
\:													{
                                                        if(!comment_check)
                                                        {
                                                            if (debug) {
                                                                printf("COLON\n");
                                                            } else {
                                                                printf(":");
                                                            }
                                                            return *yytext;
                                                        } else  {
                                                            printf("%s", yytext);
                                                            //printf("СOMMENT_CLN\n");
                                                        }
                                                    }
\=													{
                                                        if(!comment_check)
                                                        {
                                                            if (debug) {
                                                                printf("ASSIGN\n");
                                                            } else {
                                                                printf("=");
                                                            }
                                                            return *yytext;
                                                        } else {
                                                            printf("%s", yytext);
                                                            //printf("СOMMENT_ASS\n");
                                                        }
                                                    }
\?													{
                                                        if(!comment_check)
                                                        {
                                                            if (debug) {
                                                                printf("QUESTION\n");
                                                            } else {
                                                                printf("?");
                                                            }
                                                            return *yytext;
                                                        } else {
                                                            printf("%s", yytext);
                                                            //printf("СOMMENT_QST\n");
                                                        }
                                                    }
\-													{
                                                        if(!comment_check)
                                                        {
                                                            if (debug) {
                                                                printf("MINUS\n");
                                                            } else {
                                                                printf("-");
                                                            }
                                                            return *yytext;
                                                        } else {
                                                            printf("%s", yytext);
                                                            //printf("СOMMENT_MNS\n");
                                                        }
                                                    }
"\n"                    							{
                                                        //lines_number++;
                                                        if (comment_check) {
                                                            printf("%s", yytext);
                                                        }
                                                    }
"\t"                  							    ;
[ ]+                                                {
                                                        if (!debug) {
                                                            if (!comment_check) {
                                                                printf(" ");
                                                            } else {
                                                                printf("%s", yytext);
                                                            }
                                                        }
                                                    }
.                                                   {
                                                        text_flag = 1;
                                                        if (!comment_check) {
                                                            printf("%s", yytext);
                                                        } else {
                                                            printf("%s", yytext);
                                                        }
                                                    }

%%


int yyerror()
{
    printf("# error on line %d!\n", ++lines_number);
    exit(1);
}

int yywrap()
{
    printf("\n\n# correct!\n\n");
    exit(1);
}