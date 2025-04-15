#include <stdio.h>
#include <string.h> // Adiciona esta linha para declarar strlen corretamente
#include "format_command.h"

int value;
char str[20];
char cmd[20];

int main(void) {
    printf("====================================\n");

    // Solicitar a string 'str' ao usuário
    printf("Digite a string de comando ('oN'; 'oP'; 'oFF' para ser válido):\n");
    if (fgets(str, sizeof(str), stdin) == NULL) {
        printf("Erro ao ler a string.\n");
        return 1;
    }

    // Remover o '\n' que pode ser capturado por fgets
    size_t len = strlen(str);
    if (len > 0 && str[len - 1] == '\n') {
        str[len - 1] = '\0';
    }

    // Solicitar o valor 'value' ao usuário
    printf("Digite o valor (exemplo: 26):\n");
    if (scanf("%d", &value) != 1) {
        printf("Erro ao ler o valor.\n");
        return 1;
    }

    // Chamar a função format_command com os dados fornecidos
    int res = format_command(str, value, cmd);
    printf("%d: %s\n", res, cmd);

    printf("====================================\n");
    return 0;
}
