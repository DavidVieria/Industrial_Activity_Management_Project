#include "operations.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BUFFER_SIZE 256
#define MAX_OPERATIONS 100

static void cleanup_operations(Operation** operations, int count) {
    if (operations) {
        for (int i = 0; i < count; i++) {
            if (operations[i]) {
                free(operations[i]);
            }
        }
        free(operations);
    }
}

Operation** load_operations(const char* filename, int* count) {
    FILE* file = fopen(filename, "r");
    if (!file) {
        printf("Erro ao abrir o arquivo de operações: %s\n", filename);
        return NULL;
    }

    char buffer[BUFFER_SIZE];
    Operation** operations = malloc(sizeof(Operation*) * MAX_OPERATIONS);
    if (!operations) {
        printf("Erro: Falha na alocação de memória para as operações.\n");
        fclose(file);
        return NULL;
    }

    // Inicializar todos os ponteiros como NULL
    for (int i = 0; i < MAX_OPERATIONS; i++) {
        operations[i] = NULL;
    }
    *count = 0;

    // Pular a primeira linha (cabeçalho)
    if (!fgets(buffer, sizeof(buffer), file)) {
        printf("Erro: Arquivo vazio ou corrompido.\n");
        free(operations);
        fclose(file);
        return NULL;
    }

    // Ler cada linha de operação
	while (fgets(buffer, sizeof(buffer), file) && *count < MAX_OPERATIONS) {
		trim_newline(buffer); // Remover a quebra de linha do buffer

		operations[*count] = (Operation*)malloc(sizeof(Operation));
		if (!operations[*count]) {
			printf("Erro: Falha na alocação de memória para a operação %d.\n", *count);
			cleanup_operations(operations, *count);
			fclose(file);
			return NULL;
		}

		// Ajuste para o formato correto do arquivo de entrada
		if (sscanf(buffer, "%d,%49[^,],%d,%[^,]",
				&operations[*count]->machine_id,
				operations[*count]->operation,
				&operations[*count]->operation_number,
				operations[*count]->timestamp) != 4) {
			printf("Erro: Formato inválido na linha %d do arquivo de operações.\n", *count + 2);
			cleanup_operations(operations, *count + 1);
			fclose(file);
			return NULL;
		}
		(*count)++;
	}

    fclose(file);

    if (*count == 0) {
        free(operations);
        return NULL;
    }

    return operations;
}

void trim_newline(char* str) {
    size_t len = strlen(str);
    if (len > 0 && (str[len - 1] == '\n' || str[len - 1] == '\r')) {
        str[len - 1] = '\0';
    }
}

void free_operations(Operation** operations, int count) {
    cleanup_operations(operations, count);
}

void print_operation(Operation* operation) {
    if (!operation) {
        printf("Operação não disponível.\n");
        return;
    }
    printf("\nOperação da Máquina ID: %d\n", operation->machine_id);
    printf("Nome da Operação: %s\n", operation->operation);
    printf("Número da Operação: %d\n", operation->operation_number);
    printf("Timestamp: %s\n", operation->timestamp);
}

void save_operations_to_file(Operation** operations, int operations_count) {
    FILE* file = fopen("operations_log.csv", "w");
    if (!file) {
        printf("Erro ao criar o arquivo de operações.\n");
        return;
    }

    fprintf(file, "Machine ID,Operations\n");

    // Tabela para armazenar as operações por ID da máquina
    char* machine_operations[MAX_OPERATIONS] = {NULL};
    int machine_ids[MAX_OPERATIONS] = {0};
    int machine_count = 0;

    for (int i = 0; i < operations_count; i++) {
    int machine_id = operations[i]->machine_id;

    // Procurar se já existe este ID
    int index = -1;
    for (int j = 0; j < machine_count; j++) {
        if (machine_ids[j] == machine_id) {
            index = j;
            break;
        }
    }

    // Criar uma nova entrada se for um novo ID
    if (index == -1) {
        index = machine_count++;
        machine_ids[index] = machine_id;

        // Remover quebras de linha das strings relevantes
        trim_newline(operations[i]->timestamp);
        trim_newline(operations[i]->operation);

        machine_operations[index] = malloc(4096);
        snprintf(machine_operations[index], 4096, "\"%s (%d, %s)\"",
                 operations[i]->operation, operations[i]->operation_number, operations[i]->timestamp);
    } else {
        // Concatenar para o ID existente
        trim_newline(operations[i]->timestamp);
        trim_newline(operations[i]->operation);

        strncat(machine_operations[index], ", ", 4096 - strlen(machine_operations[index]) - 1);
        strncat(machine_operations[index],
                operations[i]->operation, 4096 - strlen(machine_operations[index]) - 1);
        snprintf(machine_operations[index] + strlen(machine_operations[index]),
                 4096 - strlen(machine_operations[index]),
                 " (%d, %s)", operations[i]->operation_number, operations[i]->timestamp);
    }
}

// Escrever as operações no ficheiro
for (int i = 0; i < machine_count; i++) {
    trim_newline(machine_operations[i]); // Garante que não haja quebras no final
    fprintf(file, "%d,%s\n", machine_ids[i], machine_operations[i]);
    free(machine_operations[i]);
}


    fclose(file);
    printf("Operações salvas no arquivo operations_log.csv.\n");
}
