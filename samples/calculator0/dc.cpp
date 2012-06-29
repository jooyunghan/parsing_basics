/* calculator example */
#include <iostream>
#include <vector>
using namespace std;

const char number = '8'; // Token kind : number

class Token {
public:
	char kind;		// number or op_char( +,-,*,/,(,) )
	double value;	// valid if kind is number, otherwise 0
	Token(char k) : kind(k), value(0) { }
	Token(char k, double v) : kind(k), value(v) { }
};

Token get_token();

class Token_stream {
	bool full;
	Token buffer;
public:
	Token_stream() : full(false), buffer(number) { }
	Token get() {
		if (full) {
			full = false;
			return buffer;
		}
		return get_token();
	}
	void putback(Token t) {
		if (full) {
			throw "can't putback";
		}
		full = true;
		buffer = t;
	}
};

Token_stream ts;

double expression();
double term();
double primary();

/*
 expression = term ( ('+'|'-') term )*
 */
double expression() {
	double left = term();
	while (true) {
		Token t = ts.get();
		switch (t.kind) {
		case '+': 
			left += term();
			break;
		case '-':
			left -= term();
			break;
		default:
			ts.putback(t);
			return left;
		}
	}
}

/*
 term = primary ( ('*'|'/') primary )*
 */
double term() {
	double left = primary();
	while (true) {
		Token t = ts.get();
		switch (t.kind) {
		case '*': 
			left *= primary();
			break;
		case '/':
			left /= primary();
			break;
		default:
			ts.putback(t);
			return left;
		}
	}
}

/*
 primary = 
	number
	'(' expression ')'
 */
double primary() {
	Token t = ts.get();
	switch (t.kind) {
	case number:
		return t.value;
	case '(': {
		double d = expression();
		t = ts.get();
		if (t.kind != ')') throw("')' not found");
		return d; }
	default:
		throw ("primary not found");
	}
}

int main() try {
	while (true) {
		cout << "> ";

		Token t = ts.get();
		while (t.kind == ';') {
			t = ts.get();
		}
		ts.putback(t);
		
		cout << "= " << expression() << endl;
	}
	return 0;
} catch(...) {
	cout << "Terminating." << endl;
}

bool is_op_char(char ch) {
	return ch == '+'
		|| ch == '-'
		|| ch == '*'
		|| ch == '/'
		|| ch == ';' // print
		;
}

Token get_token() {
	char ch = 0;
	cin >> ch;
	if (is_op_char(ch)) {
		return Token(ch);
	} else if (isdigit(ch) || ch == '.') {
		cin.putback(ch);
		double d;
		cin >> d;
		return Token(number, d);
	} 

	throw("invalid char read");
}