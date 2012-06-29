/* calculator example */
#include <iostream>
using namespace std;

int main() {
	int left, right;
	char op;
	cout << "example: 1+2+3 (including ';')" << endl << "> ";
	cin >> left;
	while (cin >> op) {
		if (op != ';') { cin >> right; }
		switch (op) {
		case '+': left += right; break;
		case '-': left -= right; break;
		case '*': left *= right; break;
		case '/': left /= right; break;
		default : 
			cout << "= " << left << endl; 
			cout << "example: 1+2+3 (including ';')" << endl << "> ";
			cin >> left;
			break;
		}
	}
	return 0;
}