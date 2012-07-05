#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define MAX_ID_LEN 32
typedef enum { false, true } bool ;

struct stack {
	int top;
	void *data[100];
};

void stack_push(struct stack *stack, void *data) {
	stack->data[stack->top++] = data;
}

void *stack_pop(struct stack *stack) {
	return stack->data[--stack->top];
}

struct parse_state {
	char *ptr;	 // pointer to current read position
	int  number;
	char word[MAX_ID_LEN];

	struct stack stack;
};


bool token(struct parse_state *state, const char* tok) {
	char *p = state->ptr;
	while (*p && isspace(*p)) p++;
	
	strcpy(state->word, tok);
	while (*p && *tok && *p == *tok) {
		p++;
		tok++;
	}

	if (*tok) {
		return false;
	}

	state->ptr = p;
	return true;
}

// identifier = [a-zA-Z][a-zA-Z0-9_]*
bool identifier(struct parse_state *state) {
	char *p = state->ptr;
	int i=0;
	while (*p && isspace(*p)) p++;

	if (!isalpha(*p)) {
		return false;
	}
	state->word[i++] = *p++;

	while (isalpha(*p) || isdigit(*p) || *p == '_') {
		state->word[i++] = *p++;
	}

	state->ptr = p;
	state->word[i] = '\0';
	return true;
}

void push_state(struct parse_state *state) {
	stack_push(&state->stack, state->ptr);
}

bool parse_accept(struct parse_state *state) {
	stack_pop(&state->stack);
	return true;
}

bool parse_reject(struct parse_state *state) {
	state->ptr = stack_pop(&state->stack);
	return false;
}

// number = [0-9]+ 
bool number(struct parse_state *state) {
	int value = 0;
	char *p = state->ptr;
	while (*p && isspace(*p)) p++;

	if (!isdigit(*p)) {
		return false;
	}
	value = *p++ - '0';
	while (isdigit(*p)) {
		value = value * 10 + *p++ - '0';
	}
		
	state->ptr = p;
	state->number = value;
	return true;
}

/////////////////////////////
struct decltype_t {
	char   kind[MAX_ID_LEN];     
	int    value;    // size for array
	struct decltype_t *next;
};

struct decltype_t* make_decltype(char typename[], int value) {
	struct decltype_t* d;
	d = malloc(sizeof(*d));
	strcpy(d->kind, typename);
	d->value = value;
	d->next = NULL;
	return d;
}

struct decltype_t* make_function_type() {
	return make_decltype("function returning", 0);
}

struct decltype_t* make_array_type(int size) {
	struct decltype_t* d;
	d = malloc(sizeof(*d));
	sprintf(d->kind, "array of %d", size);
	d->value = size;
	d->next = NULL;
	return d;
}

struct decl_t {
	char name[MAX_ID_LEN];
	struct decltype_t *type;
};


void add_type(struct decl_t *decl, struct decltype_t *type) {
	type->next = decl->type;
	decl->type = type;
}

void decl_print(struct decl_t *decl) {
	struct decltype_t *type = decl->type;
	printf("decl: %s is", decl->name);
	while (type) {
		printf(" %s", type->kind);
		type = type->next;
	}
	printf("\n");
}

bool declaration(struct parse_state *state, struct decl_t *decl);
bool type_specifier(struct parse_state *state);
bool declarator(struct parse_state *state, struct decl_t *decl);
bool pointer(struct parse_state *state, struct decl_t *decl);
bool direct_declarator(struct parse_state *state, struct decl_t *decl);
bool suffix(struct parse_state *state, struct decl_t *decl);

// declaration = type_specifier declarator
bool declaration(struct parse_state *state, struct decl_t *decl) {
	push_state(state);
	if (type_specifier(state)) {
		printf("%s\n", state->word);
		decl->type = make_decltype(state->word, 0);
		if ( declarator(state, decl)) {
			return parse_accept(state);
		}
	}
	return parse_reject(state);
}

// type_specifier = char | int
bool type_specifier(struct parse_state *state) {
	push_state(state);
	if (token(state, "char") || token(state, "int")) {
		printf("%s\n", state->word);
		return parse_accept(state);
	}
	return parse_reject(state);
}

// declarator = pointer? direct_declarator
bool declarator(struct parse_state *state, struct decl_t *decl) {
	push_state(state);
	pointer(state, decl);
	if (direct_declarator(state, decl)) {
		return parse_accept(state);
	}
	return parse_reject(state);
}

// pointer = '*' +
bool pointer(struct parse_state *state, struct decl_t *decl) {
	push_state(state);
	if (token(state, "*")) {
		add_type(decl, make_decltype("pointer to", 0));
		while (token(state, "*"))
			add_type(decl, make_decltype("pointer to", 0));
		return parse_accept(state);
	}
	return parse_reject(state);
}

// direct_declarator = ( id | '(' declarator ')' ) suffix*
bool direct_declarator(struct parse_state *state, struct decl_t *decl) {
	push_state(state);
	struct decl_t decl2;
	if (identifier(state)) {
		strcpy(decl->name, state->word);
		while (suffix(state, decl))
			;
		return parse_accept(state);
	}
	if (token(state, "(") && declarator(state, &decl2) && token(state, ")")) {
		while (suffix(state, decl))
			;
		todo decl2 -> decl

		return parse_accept(state);
	}
	return parse_reject(state);
}

// suffix = ('[' const ']' | '(' param-list ')')
bool suffix(struct parse_state *state, struct decl_t *decl) {
	push_state(state);
	if (token(state, "[") && number(state) && token(state, "]")) {
		add_type(decl, make_array_type(state->number));
		return parse_accept(state);
	} 
	if (token(state, "(") && token(state, ")")) {
		add_type(decl, make_function_type());
		return parse_accept(state);
	}
	return parse_reject(state);
}
int main() {
	char input[] = "int (*a[10])()";
	struct parse_state state = { .ptr = input, };
	struct decl_t decl;
	
	if (declaration(&state, &decl)) {
		printf("remainder: %s\n", state.ptr);
		decl_print(&decl);
	}

	
}
