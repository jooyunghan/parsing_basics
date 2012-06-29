/* calculator example */
#include <iostream>
using namespace std;

int main() {
	int left, right;
	char op;
	cin >> left >> op >> right;

	int result;
	if (op == '+')
		result = left + right;
	else if (op == '-')
		result = left - right;
	else 
		result = 0;

	cout << result << endl;
	return 0;
}