#include <stdio.h>
#include "asm.h"

int buffer[] = {10, 20, 30, 40, 50, 15};    								// Buffer circular com capacidade para 5 elementos
int length = sizeof(buffer) / sizeof(buffer[0]);        					// Tamanho do buffer
int head = 1;       														// Índice do próximo elemento a ser inserido
int tail = 3;         														// Índice do próximo elemento a ser removido
int num_elements = 0;														// Número de elementos no buffer (atualizado ao chamar get_n_element)


int main() {
    num_elements = get_n_element(buffer, length, &tail, &head);
    
    printf("====================================\n");
    
    printf("Resultado: %d elementos\n", num_elements);
    
    printf("====================================\n");
    
    return 0;
}
