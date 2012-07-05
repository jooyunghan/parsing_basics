#include <stdio.h>

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
	char word[32];

	struct stack stack;
};


bool token(struct parse_state *state, const char* tok) {
	char *p = state->ptr;
	while (*p && isspace(*p)) p++;
	
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

bool declaration(struct parse_state *state);
bool type_specifier(struct parse_state *state);
bool declarator(struct parse_state *state);
bool pointer(struct parse_state *state);
bool direct_declarator(struct parse_state *state);
bool suffix(struct parse_state *state);

// declaration = type_specifier declarator
bool declaration(struct parse_state *state) {
	push_state(state);
	if (type_specifier(state) && declarator(state)) {
		return parse_accept(state);
	}
	return parse_reject(state);
}

// type_specifier = char | int
bool type_specifier(struct parse_state *state) {
	push_state(state);
	if (token(state, "char") || token(state, "int")) {
		return parse_accept(state);
	}
	return parse_reject(state);
}

// declarator = pointer? direct_declarator
bool declarator(struct parse_state *state) {
	push_state(state);
	pointer(state);
	if (direct_declarator(state)) {
		return parse_accept(state);
	}
	return parse_reject(state);
}

// pointer = '*' +
bool pointer(struct parse_state *state) {
	push_state(state);
	if (token(state, "*")) {
		while (token(state, "*"))
			;
		return parse_accept(state);
	}
	return parse_reject(state);
}

// direct_declarator = ( id | '(' declarator ')' ) suffix*
bool direct_declarator(struct parse_state *state) {
	push_state(state);
	if (identifier(state)
		|| (token(state, "(") && declarator(state) && token(state, ")"))) {
		while (suffix(state))
			;
		return parse_accept(state);
	}
	return parse_reject(state);
}

// suffix = ('[' const ']' | '(' param-list ')')
bool suffix(struct parse_state *state) {
	push_state(state);
	if (token(state, "[") && number(state) && token(state, "]")) {
		return parse_accept(state);
	} 
	if (token(state, "(") && token(state, ")")) {
		return parse_accept(state);
	}
	return parse_reject(state);
}

int main() {
	//char input[] = "int (*a[10])()";
	char input[] = "int *a8";
	struct parse_state state = { .ptr = input, };
	
	if (declaration(&state)) {
		printf("recognized:[%s]\n", state.word);
		printf("remainder: %s\n", state.ptr);
	}

}
