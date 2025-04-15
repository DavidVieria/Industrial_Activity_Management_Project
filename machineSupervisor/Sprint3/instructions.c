#include "instructions.h"
#include "ac04/format_command.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <termios.h>
#include <time.h>
#include <unistd.h>

int load_instructions(const char* filename, Instruction*** instructions, int* instruction_count) {
    FILE* file = fopen(filename, "r");
    if (!file) {
        printf("Erro ao abrir o arquivo de instruções: %s\n", filename);
        return 0;
    }

    char buffer[BUFFER_SIZE];
    int count = 0;
    Instruction** inst_array = malloc(MAX_INSTRUCTIONS * sizeof(Instruction*));
    if (!inst_array) {
        printf("Erro na alocação de memória para as instruções.\n");
        fclose(file);
        return 0;
    }

    // Ignorar a primeira linha (cabeçalho)
    if (!fgets(buffer, sizeof(buffer), file)) {
        printf("Erro ao ler a linha de cabeçalho.\n");
        free(inst_array);
        fclose(file);
        return 0;
    }

    while (fgets(buffer, sizeof(buffer), file) != NULL) {
        buffer[strcspn(buffer, "\n")] = '\0'; // Remove newline character

        char* machine_id_str = strtok(buffer, ";");
        if (!machine_id_str) {
            printf("Linha inválida: %s\n", buffer);
            continue;
        }

        int machine_id = atoi(machine_id_str);

        // Inicializa a estrutura de instrução
        Instruction* inst = malloc(sizeof(Instruction));
        if (!inst) {
            printf("Erro na alocação de memória para uma instrução.\n");
            for (int i = 0; i < count; i++) {
                free(inst_array[i]);
            }
            free(inst_array);
            fclose(file);
            return 0;
        }

        inst->id = machine_id;

        // Lê os pares de estados e operações
        for (int i = 0; i < 3; i++) {
            char* state_str = strtok(NULL, ";");
            char* operation_str = strtok(NULL, ";");

            if (!state_str || !operation_str) {
                printf("Dados incompletos para a máquina %d.\n", machine_id);
                continue;
            }

            // Define o estado e a operação
            switch (i) {
                case 0:
                    inst->state1 = strcmp(state_str, "ON") == 0 ? ON : strcmp(state_str, "OFF") == 0 ? OFF : IN_OPERATION;
                    inst->operation1 = atoi(operation_str);
                    break;
                case 1:
                    inst->state2 = strcmp(state_str, "ON") == 0 ? ON : strcmp(state_str, "OFF") == 0 ? OFF : IN_OPERATION;
                    inst->operation2 = atoi(operation_str);
                    break;
                case 2:
                    inst->state3 = strcmp(state_str, "ON") == 0 ? ON : strcmp(state_str, "OFF") == 0 ? OFF : IN_OPERATION;
                    inst->operation3 = atoi(operation_str);
                    break;
                default:
                    break;
            }
        }

        inst_array[count++] = inst;
    }

    if (count == 0) {
        free(inst_array);
        fclose(file);
        return 0;
    }

    fclose(file);
    *instructions = inst_array;
    *instruction_count = count;
    return 1;
}

void send_instructions_to_machine(MachManager* manager, Instruction** instructions, int instruction_count) {
    printf("Envio das instruções: \n");

    for (int i = 0; i < instruction_count; i++) {
        Instruction* inst = instructions[i];
        Machine* machine = NULL;

        for (int j = 0; j < manager->machine_count; j++) {
            if (manager->machines[j]->config->id == inst->id) {
                machine = manager->machines[j];
                break;
            }
        }

        if (machine != NULL) {
            printf("Envio das instruções para a máquina %d:\n", machine->config->id);

            // Processa os 3 pares de estado e operação
            char cmd[100]; // Tamanho suficiente para acomodar o comando
            for (int k = 0; k < 3; k++) {
                MachineState state;
                int operation;

                // Dependendo do índice, pega o estado e operação apropriados
                switch (k) {
                    case 0:
                        state = inst->state1;
                        operation = inst->operation1;
                        break;
                    case 1:
                        state = inst->state2;
                        operation = inst->operation2;
                        break;
                    case 2:
                        state = inst->state3;
                        operation = inst->operation3;
                        break;
                    default:
                        continue;
                }

                // Chama a função format_command para criar o comando
                int res = format_command(state == ON ? "ON" : state == OFF ? "OFF" : "OP", operation, cmd);
                if (res == 1) {
                    // Substituindo as vírgulas por espaços
                    for (int i = 0; cmd[i] != '\0'; i++) {
                        if (cmd[i] == ',') {
                            cmd[i] = ' ';
                        }
                    }

                    printf("Comando gerado: %s\n", cmd);
                    // Envia o comando para a máquina
                    sendCommandToMachine(cmd);
                    
                    sleep(5);
                } else {
                    printf("Falha ao gerar o comando para a máquina %d, estado %s, operação %d\n", inst->id, 
                            state == ON ? "ON" : state == OFF ? "OFF" : "IN_OPERATION", operation);
                }
            }
        } else {
            printf("Máquina com ID %d não encontrada.\n", inst->id);
        }
    }
}

int sendCommandToMachine(const char* command) {
    // Abrir a porta serial
    int serial_fd = open("/dev/ttyS0", O_WRONLY | O_NOCTTY);
    if (serial_fd == -1) {
        perror("Erro ao abrir a porta serial");
        return 0;  // Falha ao abrir a porta serial
    }

    // Configurar a comunicação serial (velocidade, bits de dados, paridade, etc.)
    struct termios options;
    tcgetattr(serial_fd, &options);  // Obter as configurações atuais da porta
    cfsetispeed(&options, B9600);    // Definir a velocidade de entrada (9600 bauds)
    cfsetospeed(&options, B9600);    // Definir a velocidade de saída (9600 bauds)
    options.c_cflag |= (CLOCAL | CREAD);  // Habilitar leitura e ignorar controle de modem
    options.c_cflag &= ~CSIZE;            // Limpar a máscara de tamanho de caractere
    options.c_cflag |= CS8;               // 8 bits de dados
    options.c_iflag &= ~(IXON | IXOFF | IXANY); // Desabilitar controle de fluxo
    options.c_iflag &= ~ICANON;           // Modo não canônico
    options.c_oflag &= ~OPOST;            // Sem processamento de saída

    // Aplicar as configurações
    tcsetattr(serial_fd, TCSANOW, &options);

    // Enviar o comando para a máquina (ou Arduino)
    ssize_t bytes_written = write(serial_fd, command, strlen(command));
    if (bytes_written == -1) {
        perror("Erro ao enviar comando");
        close(serial_fd);
        return 0;  // Falha ao enviar o comando
    }

    // Fechar a porta serial
    close(serial_fd);
    return 1;  // Sucesso
}

void print_instructions(Instruction** instructions, int instruction_count) {
    printf("Instruções carregadas:\n");
    for (int i = 0; i < instruction_count; i++) {
        Instruction* inst = instructions[i];
        printf("- Máquina %d: estado %s, operação %d, estado %s, operação %d, estado %s, operação %d\n",
               inst->id,
               inst->state1 == ON ? "ON" : inst->state1 == OFF ? "OFF" : "IN_OPERATION",
               inst->operation1,
               inst->state2 == ON ? "ON" : inst->state2 == OFF ? "OFF" : "IN_OPERATION",
               inst->operation2,
               inst->state3 == ON ? "ON" : inst->state3 == OFF ? "OFF" : "IN_OPERATION",
               inst->operation3);
    }
}



