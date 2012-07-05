/*
 simple_lex
 - scan number(int), identifier, other characters as operator
 number : [0-9]+
 identifier : [a-zA-Z][a-zA-Z0-9]*

 example:
 	$ echo int *a[10] | simple_lex
	Identifier : [int]
	Operator : *
	Identfier : [a]
	Operator : [
	Number : 10
	Operator : ]
	EOF
	$
 */
#include <stdio.h>
#include <string.h>
#include <ctype.h>

typedef enum {
	S_Eof, S_Number, S_Identifier, S_Operator 
} Symbol;

int number;
char* text;

int get_token() {
	int ch = getchar();
	while (isspace(ch)) {
		ch = getchar();
	}
	if (ch == EOF) {
		return S_Eof;
	}
	if (isdigit(ch)) {
		ungetc(ch, stdin);
		scanf("%d", &number);
		return S_Number;
	}
	if (isalpha(ch) || ch == '_') {
		char buf[100], *p = buf;
		do {
			*p++ = ch;
		} while ( (ch = getchar()) != EOF && isalnum(ch) );
		ungetc(ch, stdin);
		*p = '\0';
		if (text) { 
			free(text);
		}
		text = strdup(buf);
		return S_Identifier;
	}
	return ch;
}

int main() {
	while (1) {
		Symbol s = get_token();
		switch (s) {
			case S_Number : 
				printf("Number : %d\n", number);
				break;
			case S_Eof :
				printf("EOF\n");
				return 0;
			case S_Identifier :
				printf("Identifier : [%s]\n", text);
				break;
			default:
				printf("Operator : %c\n", s);
				break;
		}
	}
}
