#include "ui.h"
#include "mach_manager.h"
#include "machine_config.h"
#include "operations.h"
#include "instructions.h"
#include "ac01/extract_data.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <ctype.h>

#define MAX_INSTRUCTIONS 100
#define BUFFER_SIZE 256

void print_menu() {
    printf("\n----- Menu -----\n");
    printf("1. Adicionar máquinas\n");
    printf("2. Carregar operações\n");
    printf("3. Instruir máquinas\n");
    printf("4. Consultar estado das máquinas\n");
    printf("5. Remover máquina\n");
    printf("6. Atualizar máquina\n");
    printf("7. Mostrar status das máquinas\n");
    printf("8. Atribuir operação a uma máquina\n");
    printf("9. Verificar alertas\n");
    printf("10. Sair\n");
    printf("Escolha uma opção: ");
}

void clearInputBuffer() {
    int c;
    while ((c = getchar()) != '\n' && c != EOF);
}

void free_manager_resources(MachManager* manager) {
    for (int i = 0; i < manager->machine_count; i++) {
        free(manager->machines[i]->config); // Libera configurações
        free(manager->machines[i]);        // Libera estrutura Machine
    }
    free(manager); // Libera o gerenciador
}

int read_serial_data(float* temperature, float* humidity) {
    FILE* serial = fopen("/dev/ttyS0", "r");  // Abre a porta serial (ajuste conforme necessário)
    if (!serial) {
        perror("Erro ao abrir a porta serial");
        return 0;  // Retorna 0 em caso de erro
    }

    // Aguarda a resposta do Arduino
    char buffer[BUFFER_SIZE];
    if (!fgets(buffer, sizeof(buffer), serial)) {
        printf("Erro ao ler da porta serial ou dados ausentes.\n");
        fclose(serial);
        return 0;  // Retorna 0 se não houver dados
    }

    fclose(serial);  // Fecha a porta serial, pois já temos os dados na variável `buffer`

    // Debug: exibir os dados recebidos
    printf("Dados recebidos: %s\n", buffer);
    
    trim(buffer);

    // Variáveis para extração
    char unit[20];
    int value;
    
    // Extraímos os dados para temperatura (TEMP)
    if (extract_data(buffer, "TEMP", unit, &value)) {
        *temperature = (float)value;
    } else {
        printf("Falha ao extrair dados de temperatura.\n");
        return 0;  // Retorna 0 se não conseguir extrair
    }
    
    printf("Temperatura: %.2f graus %s\n", *temperature, unit);
    
     // Extraímos os dados para umidade (HUM)
    if (extract_data(buffer, "HUM", unit, &value)) {
        *humidity = (float)value;
    } else {
        printf("Falha ao extrair dados de humidade.\n");
        return 0;  // Retorna 0 se não conseguir extrair
    }

    printf("Humidade: %d %%\n",value);
    return 1;  // Retorna 1 em caso de sucesso
}

// Função para remover espaços em branco e quebras de linha no início e no final da string
void trim(char* str) {
    // Remover espaços em branco do início
    while (isspace((unsigned char)*str)) {
        str++;
    }

    // Remover espaços em branco do final
    if (*str == 0)  // Caso a string esteja vazia após o trim inicial
        return;

    char* end = str + strlen(str) - 1;
    while (end > str && isspace((unsigned char)*end)) {
        end--;
    }

    // Coloca o caractere nulo no final da string "trimada"
    *(end + 1) = 0;
}

void handle_user_input(MachManager* manager) {
    int option, machine_id;
    char filename[256];
    char operation_name[50];
    int operation_number;
    Instruction** instructions;
    int instruction_count = 0;
    MachineConfig** configs;
    int config_count;
    Operation** operations;
    int operation_count;

    while (1) {
        print_menu();
        if (scanf("%d", &option) != 1) {
            printf("Entrada inválida. Por favor insira um número.\n");
            while (getchar() != '\n');
            continue;
        }

        switch (option) {
            case 1:
                printf("Digite o nome do arquivo de configuração (e.g. machines_config.txt): ");
                scanf("%s", filename);
                configs = load_machine_configs(filename, &config_count);
                if (configs) {
                    for (int i = 0; i < config_count; i++) {
                        add_machine(manager, configs[i]);
                        printf("Máquina %s (ID: %d) adicionada com sucesso!\n",
                               configs[i]->name, configs[i]->id);
                    }
                    free(configs);
                } else {
                    printf("Falha ao carregar o arquivo de configuração.\n");
                }
                break;

            case 2:
                printf("Digite o nome do arquivo de operações (e.g. operations.txt): ");
                scanf("%s", filename);
                operations = load_operations(filename, &operation_count);
                if (operations) {
                    printf("Operações carregadas com sucesso!\n");
                    save_operations_to_file(operations, operation_count);

                    for (int i = 0; i < operation_count; i++) {
                        print_operation(operations[i]);
                    }
                    free_operations(operations, operation_count);
                } else {
                    printf("Falha ao carregar o arquivo de operações.\n");
                }
                break;

            case 3:
                if (manager->machine_count == 0) {
                    printf("Não há máquinas registadas. Registe máquinas no sistema antes de as instruir.\n");
                    break;
                }

                if (operation_count == 0) {
                    printf("Não há operações carregadas. Carregue operações no sistema antes de instruir as máquinas.\n");
                    break;
                }

                if (instruction_count > 0) {
                    printf("Instruções já carregadas. Deseja substituir as instruções existentes? (S/N): ");
                    char choice;
                    scanf(" %c", &choice);
                    if (toupper(choice) != 'S') {
                        break;
                    }
                }

                printf("Digite o nome do arquivo de instruções (e.g. instructions.txt): ");
                scanf("%255s", filename);  // Limita a entrada a 255 caracteres
                clearInputBuffer();        // Limpa o buffer após a leitura


                if (load_instructions(filename, &instructions, &instruction_count)) {
                    printf("Instruções carregadas com sucesso!\n\n");
                    print_instructions(instructions, instruction_count);  // Adiciona esta linha
                    int fd = 0; // Assuming fd is 0, you can change it to the appropriate value
                    printf("\n");
                    send_instructions_to_machine(manager, instructions, instruction_count, fd);
                    for (int i = 0; i < instruction_count; i++) {
                        free(instructions[i]);
                    }
                    free(instructions);
                } else {
                    printf("Falha ao carregar o arquivo de instruções. Verifique o nome do arquivo.\n");
                }
                break;

            case 4:
                printf("Digite o ID da máquina que pretende consultar o estado: ");
                if (scanf("%d", &machine_id) == 1) {
                    // Encontre a máquina pelo ID
                    int found = 0;
                    for (int i = 0; i < manager->machine_count; i++) {
                        if (manager->machines[i]->config->id == machine_id) {
                            // Exibe o estado da máquina
                            show_machine_state(manager->machines[i]);
                            found = 1;
                            break;
                        }
                    }
                    if (!found) {
                        printf("Máquina com ID %d não encontrada.\n", machine_id);
                    }
                } else {
                    printf("ID inválido.\n");
                    while (getchar() != '\n'); // Limpar buffer
                }
                break;

            case 5:
                printf("Digite o ID da máquina a ser removida: ");
                if (scanf("%d", &machine_id) == 1) {
                    remove_machine(manager, machine_id);
                } else {
                    printf("ID inválido.\n");
                    while (getchar() != '\n'); // Limpar buffer
                }
                break;

            case 6:
                printf("Digite o ID da máquina a ser atualizada: ");
                if (scanf("%d", &machine_id) == 1) {
                    for (int i = 0; i < manager->machine_count; i++) {
                        if (manager->machines[i]->config->id == machine_id) {
                            float temperature, humidity;
                            if (read_serial_data(&temperature, &humidity)) {
                                update_machine_data(manager->machines[i], temperature, humidity);
                                printf("Dados da máquina com ID %d atualizados.\n", machine_id);
                            } else {
                                printf("Falha ao atualizar os dados da máquina.\n");
                            }
                            break;
                        }
                    }
                } else {
                    printf("ID inválido.\n");
                    while (getchar() != '\n'); // Limpar buffer
                }
                break;

            case 7:
                display_machine_status(manager);
                break;

            case 8:{
                printf("Digite o ID da máquina para atribuir uma operação: ");
                if (scanf("%d", &machine_id) != 1) {
                    printf("ID inválido.\n");
                    while (getchar() != '\n');
                    break;
                }
                printf("Digite o nome da operação: ");
                scanf("%s", operation_name);
                printf("Digite o número da operação: ");
                if (scanf("%d", &operation_number) != 1) {
                    printf("Número da operação inválido.\n");
                    while (getchar() != '\n');
                    break;
                }

                // Criação dinâmica de uma estrutura Operation
                Operation* new_operation = (Operation*)malloc(sizeof(Operation));
                if (new_operation == NULL) {
                    printf("Erro ao alocar memória para a operação.\n");
                    break;
                }

                new_operation->machine_id = machine_id;
                strncpy(new_operation->operation, operation_name, sizeof(new_operation->operation) - 1);
                new_operation->operation[sizeof(new_operation->operation) - 1] = '\0'; // Garante terminação
                new_operation->operation_number = operation_number;

                 // Preenche timestamp com a data/hora atual
                time_t now = time(NULL);
                struct tm* time_info = localtime(&now);
                strftime(new_operation->timestamp, sizeof(new_operation->timestamp), "%Y-%m-%dT%H:%M:%S", time_info);

                // Chama a função assign_operation
                 assign_operation(manager, machine_id, new_operation);

                 // Libera a operação criada, pois já foi utilizada
                 free(new_operation);
                break;
                }

            case 9:
                check_for_alerts(manager);
                break;

            case 10:
                free_manager_resources(manager);
                printf("Saindo do programa...\n");
                exit(0);
                break;

            default:
                printf("Opção inválida. Tente novamente.\n");
                break;
        }
    }
}


