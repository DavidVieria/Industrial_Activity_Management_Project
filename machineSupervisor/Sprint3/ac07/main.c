#include <stdio.h>
#include "asm.h"

#define MAX_BUFFER_SIZE 100  // Tamanho máximo do buffer
int buffer[MAX_BUFFER_SIZE]; // Buffer circular com capacidade definida
int length = 0;             // Número real de elementos no buffer
int head = 0;               // Índice do próximo elemento a ser inserido
int tail = 0;               // Índice do próximo elemento a ser removido
int num_elements = 0;       // Número de elementos no buffer (atualizado ao chamar get_n_element)

int main() {
    printf("====================================\n");

    // Solicitar o número de elementos a ser inserido
    printf("Digite o número de elementos a ser inserido (máximo %d):\n", MAX_BUFFER_SIZE);
    if (scanf("%d", &length) != 1 || length <= 0 || length > MAX_BUFFER_SIZE) {
        printf("Erro: número de elementos inválido.\n");
        return 1;
    }

    // Solicitar os valores do buffer ao usuário
    printf("Digite %d valores inteiros para o buffer circular:\n", length);
    for (int i = 0; i < length; i++) {
        printf("Elemento %d: ", i + 1);
        if (scanf("%d", &buffer[head]) != 1) {
            printf("Erro ao ler o valor do buffer.\n");
            return 1;
        }

        // Atualiza o índice head e realiza o "trim" do buffer
        if (num_elements < MAX_BUFFER_SIZE) {
            num_elements++;
        } else {
            // Se o buffer estiver cheio, move o tail para frente e sobrescreve o valor mais antigo
            tail = (tail + 1) % MAX_BUFFER_SIZE;
        }

        // Atualiza o head para o próximo índice
        head = (head + 1) % MAX_BUFFER_SIZE;
    }

    // Solicitar o índice do próximo elemento a ser removido (tail)
    printf("Digite o índice do próximo elemento a ser removido (tail, de 0 a %d):\n", num_elements - 1);
    if (scanf("%d", &tail) != 1 || tail < 0 || tail >= num_elements) {
        printf("Erro: índice inválido para tail.\n");
        return 1;
    }

    // Solicitar o índice do próximo elemento a ser inserido (head)
    printf("Digite o índice do próximo elemento a ser inserido (head, de 0 a %d):\n", num_elements - 1);
    if (scanf("%d", &head) != 1 || head < 0 || head >= num_elements) {
        printf("Erro: índice inválido para head.\n");
        return 1;
    }

    // Chamar a função get_n_element
    num_elements = get_n_element(buffer, length, &tail, &head);

    // Exibir o resultado
    printf("Resultado: %d elementos\n", num_elements);
    printf("====================================\n");

    return 0;
}
