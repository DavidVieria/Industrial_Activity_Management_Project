#include <stdio.h>
#include "asm.h"

#define BITS_SIZE 5

int n = 26;
char bits[BITS_SIZE];
int res = 0;

int main() {
    int res = get_number_binary ( n , bits );
	printf ( "%d: %d , %d , %d , %d , %d\n" ,res , bits [4] , bits [3] , bits [2] , bits [1] , bits [0]);
	return 0;
}
