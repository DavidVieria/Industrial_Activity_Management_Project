#include <stdio.h>
#include "extract_data.h"

int main() {
    char str[100];
    char token[20];
    char unit[20];
    int value;

    printf("====================================\n");

    // Solicitar a string ao usuário
    printf("Digite a string de entrada (exemplo: TEMP&unit:celsius&value:20#HUM&unit:percentage&value:80):\n");
    scanf("%99[^\n]%*c", str); // Lê até 99 caracteres ou até nova linha, descartando o '\n'

    // Solicitar o token ao usuário
    printf("Digite o token a ser buscado (exemplo: TEMP ou HUM):\n");
    scanf("%19s", token);

    // Chamar a função extract_data
    int res = extract_data(str, token, unit, &value);
    printf("%d:%s,%d\n", res, unit, value); // Imprime o resultado

    printf("====================================\n");
    return 0;
}
