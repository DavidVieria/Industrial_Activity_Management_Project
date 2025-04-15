// machine_config.c
#include "machine_config.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BUFFER_SIZE 256
#define MAX_MACHINES 10

static void cleanup_configs(MachineConfig** configs, int count) {
    if (configs) {
        for (int i = 0; i < count; i++) {
            if (configs[i]) {
                free(configs[i]);
            }
        }
        free(configs);
    }
}

MachineConfig** load_machine_configs(const char* filename, int* count) {
    FILE* file = fopen(filename, "r");
    if (!file) {
        printf("Erro ao abrir o arquivo de configuração: %s\n", filename);
        return NULL;
    }

    char buffer[BUFFER_SIZE];
    MachineConfig** configs = malloc(sizeof(MachineConfig*) * MAX_MACHINES);
    if (!configs) {
        printf("Erro: Falha na alocação de memória para as configurações.\n");
        fclose(file);
        return NULL;
    }

    // Inicializar todos os ponteiros como NULL
    for (int i = 0; i < MAX_MACHINES; i++) {
        configs[i] = NULL;
    }
    *count = 0;

    // Pular a primeira linha (cabeçalho)
    if (!fgets(buffer, sizeof(buffer), file)) {
        printf("Erro: Arquivo vazio ou corrompido.\n");
        free(configs);
        fclose(file);
        return NULL;
    }

    // Ler cada linha de configuração
    while (fgets(buffer, sizeof(buffer), file) && *count < MAX_MACHINES) {
        configs[*count] = (MachineConfig*)malloc(sizeof(MachineConfig));
        if (!configs[*count]) {
            printf("Erro: Falha na alocação de memória para a máquina %d.\n", *count);
            cleanup_configs(configs, *count);
            fclose(file);
            return NULL;
        }
        
        if (sscanf(buffer, "%d,%49[^,],%f,%f,%f,%f,%d,%d",
                   &configs[*count]->id,
                   configs[*count]->name,
                   &configs[*count]->min_temp,
                   &configs[*count]->max_temp,
                   &configs[*count]->min_hum,
                   &configs[*count]->max_hum,
                   &configs[*count]->circular_buffer_length,
                   &configs[*count]->moving_median_window) != 8) {
            
            printf("Erro: Formato inválido na linha %d do arquivo de configuração.\n", *count + 2);
            cleanup_configs(configs, *count + 1);
            fclose(file);
            return NULL;
        }
        (*count)++;
    }

    fclose(file);

    if (*count == 0) {
        free(configs);
        return NULL;
    }

    return configs;
}

void free_machine_configs(MachineConfig** configs, int count) {
    cleanup_configs(configs, count);
}

void print_machine_config(MachineConfig* config) {
    if (!config) {
        printf("Configuração não disponível.\n");
        return;
    }
    printf("\nConfiguração da Máquina:\n");
    printf("ID: %d\n", config->id);
    printf("Nome: %s\n", config->name);
    printf("Temperatura mínima: %.2f°C, máxima: %.2f°C\n", config->min_temp, config->max_temp);
    printf("Humidade mínima: %.2f%%, máxima: %.2f%%\n", config->min_hum, config->max_hum);
    printf("Tamanho do Buffer Circular: %d\n", config->circular_buffer_length);
    printf("Tamanho da Janela da Mediana: %d\n", config->moving_median_window);
}
