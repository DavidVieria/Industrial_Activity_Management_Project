#include <stdio.h>
#include "asm.h" // Assumindo que você já tem o arquivo header

int main() {
    // Configuração inicial
    int buffer[10] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}; // Buffer circular
    int length = 10; // Tamanho do buffer
    int tail = 0;    // Tail inicial
    int head = 7;    // Head inicial (indica que os elementos de 1 a 7 estão no buffer)
    int array[10] = {0}; // Array para onde os elementos serão movidos
    int n; // Número de elementos a mover
	
	printf("====================================\n");

    // Teste 1: mover 5 elementos
    n = 5;
    int available_elements = (head - tail + length) % length;
	printf("Elementos disponíveis antes do teste: %d\n", available_elements);

    printf("Teste 1: Mover %d elementos\n", n);
    if (move_n_to_array(buffer, length, &tail, &head, n, array)) {
        printf("Elementos movidos: ");
        for (int i = 0; i < n; i++) {
            printf("%d ", array[i]);
        }
        printf("\nNovo valor de tail: %d\n", tail);
    } else {
        printf("Erro: Não há %d elementos disponíveis para mover.\n", n);
    }

    // Teste 2: mover mais elementos do que o disponível
    n = 10;
    printf("\nTeste 2: Mover %d elementos\n", n);
    if (move_n_to_array(buffer, length, &tail, &head, n, array)) {
        printf("Elementos movidos: ");
        for (int i = 0; i < n; i++) {
            printf("%d ", array[i]);
        }
        printf("\nNovo valor de tail: %d\n", tail);
    } else {
        printf("Erro: Não há %d elementos disponíveis para mover.\n", n);
    }

    // Teste 3: mover exatamente os elementos restantes
    n = 2;
    printf("\nTeste 3: Mover %d elementos\n", n);
    if (move_n_to_array(buffer, length, &tail, &head, n, array)) {
        printf("Elementos movidos: ");
        for (int i = 0; i < n; i++) {
            printf("%d ", array[i]);
        }
        printf("\nNovo valor de tail: %d\n", tail);
    } else {
        printf("Erro: Não há %d elementos disponíveis para mover.\n", n);
    }
    
    printf("====================================\n");

    return 0;
}
