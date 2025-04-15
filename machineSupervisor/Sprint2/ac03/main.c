#include <stdio.h>
#include "asm.h"

#define BITS_SIZE 5

int n;
char str[] =" shdhsdh %444 sdjshd 64";
int result = 0;

int main() {
    result = get_number(str, &n);
    
    printf("====================================\n");
    
    printf("%d: %d\n", result, n);
    
    printf("====================================\n");
    return 0;
}
