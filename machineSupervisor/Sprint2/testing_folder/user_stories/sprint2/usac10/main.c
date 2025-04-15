#include <stdio.h>
#include "asm.h"

int vec[] = {21, 55, 24, 100, 456, 51};
int length = sizeof(vec) / sizeof(vec[0]);
int me = 0;
int result = -1;

int main() {
	printf("array inicial: ");
    for (int i = 0; i < length; i++) {
        printf("%d ", vec[i]);
    }
    printf("\n");
	
	result = median(vec, length, &me);
	
	if (result == 1)
	{
		printf("array final: ");
    for (int i = 0; i < length; i++) {
        printf("%d ", vec[i]);
    }
    printf("\n");
    
    printf("Mediana : %d\n", me);
	}else
	{
		printf("Array nao pode ter tamanho nulo/negativo!");
	}
	
	
	
    return 0;
}
