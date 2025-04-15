#include "mach_manager.h"
#include "operations.h"
#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <termios.h>

// Criar o gerenciador de máquinas
MachManager* create_mach_manager() {
    MachManager* manager = (MachManager*) malloc(sizeof(MachManager));
    if (manager == NULL) {  // Verifique se a alocação foi bem-sucedida
        printf("Erro na alocação de memória para MachManager.\n");
        exit(1);  // Ou retorne NULL, se preferir
    }
    manager->machine_count = 0;
    return manager;
}

// Adicionar uma máquina ao gerente
void add_machine(MachManager* manager, MachineConfig* config) {
    if (manager->machine_count < MAX_MACHINES) {
        manager->machines[manager->machine_count] = (Machine*) malloc(sizeof(Machine));
        manager->machines[manager->machine_count]->config = config;
        manager->machines[manager->machine_count]->state = OFF;
        manager->machine_count++;
    } else {
        printf("Limite de máquinas atingido (%d).\n", MAX_MACHINES);
    }
}

// Remover uma máquina do gerente
void remove_machine(MachManager* manager, int machine_id) {
    for (int i = 0; i < manager->machine_count; i++) {
        if (manager->machines[i]->config->id == machine_id) {
            if (manager->machines[i]->state == IN_OPERATION){
                printf("Erro: Não é possível remover a máquina com ID %d porque ela está em operação.\n", machine_id);
                return;
            }
            free(manager->machines[i]->config);
            free(manager->machines[i]);
            for (int j = i; j < manager->machine_count - 1; j++) {
                manager->machines[j] = manager->machines[j + 1];
            }
            manager->machine_count--;
            printf("Máquina com ID %d removida.\n", machine_id);
            return;
        }
    }
    printf("Máquina com ID %d não encontrada.\n", machine_id);
}

// Atualizar dados de uma máquina (simulação de sensores)
void update_machine_data(Machine* machine, float temperature, float humidity) {
    // Atualiza os dados da máquina com os valores de temperatura e umidade
    machine->current_temp = temperature;
    machine->current_hum = humidity;
    
    if (machine->state == OFF){
		machine->state = ON;
	}

    printf("Máquina %d atualizada com Temperatura: %.2fºC e Humidade: %.2f%%\n",
           machine->config->id, temperature, humidity);
}

// Verificar alertas de temperatura e umidade
void check_for_alerts(MachManager* manager) {
    printf("Se ambas a temperatura e humidade estiverem fora dos valores, TODOS os LEDs acendem.\n");
    printf("Se só a temperatura estiver fora dos valores, o LED BRANCO acende.\n");
    printf("Se só a humidade estiver fora dos valores, o LED AMARELO acende.\n\n");

    int alert_found = 0; // Flag para verificar se há alertas

    for (int i = 0; i < manager->machine_count; i++) {
        Machine* machine = manager->machines[i];
        const char* command = "OFF"; // Comando padrão

        int temp_out_of_bounds = machine->current_temp < machine->config->min_temp || 
                                 machine->current_temp > machine->config->max_temp;

        int hum_out_of_bounds = machine->current_hum < machine->config->min_hum || 
                                machine->current_hum > machine->config->max_hum;

        // Determinar comando com base nos limites
        if (temp_out_of_bounds && hum_out_of_bounds) {
            command = "ON 1 1 1 1 1";
            printf("Alerta: Temperatura e Humidade fora dos limites na máquina %d.\n", machine->config->id);
            printf("Temperatura: %.2f°C, Limites: %.2f°C - %.2f°C.\n",
                   machine->current_temp, machine->config->min_temp, machine->config->max_temp);
            printf("Humidade: %.2f%%, Limites: %.2f%% - %.2f%%.\n",
                   machine->current_hum, machine->config->min_hum, machine->config->max_hum);
            alert_found = 1;
        } else if (temp_out_of_bounds) {
            command = "ON 1 0 0 0 0";
            printf("Alerta: Temperatura fora dos limites na máquina %d (%.2f°C). Limites: %.2f°C - %.2f°C.\n",
                   machine->config->id, machine->current_temp, machine->config->min_temp, machine->config->max_temp);
            alert_found = 1;
        } else if (hum_out_of_bounds) {
            command = "ON 0 0 0 0 1";
            printf("Alerta: Humidade fora dos limites na máquina %d (%.2f%%). Limites: %.2f%% - %.2f%%.\n",
                   machine->config->id, machine->current_hum, machine->config->min_hum, machine->config->max_hum);
            alert_found = 1;
        }

        // Enviar comando para a máquina
        int command_sent = sendCommandToMachine(command);
        if (command_sent) {
            printf("Comando enviado para a máquina %d: %s\n\n", machine->config->id, command);
        } else {
            printf("Falha ao enviar comando para a máquina %d: %s\n", machine->config->id, command);
        }

        // Pausar por 5 segundos antes de processar a próxima máquina
        sleep(5);
    }

    // Se nenhum alerta for encontrado, mostrar mensagem de tudo dentro dos limites
    if (!alert_found) {
        printf("Todas as máquinas estão dentro dos limites de temperatura e humidade.\n");
    }
}

// Exibir todas as máquinas do sistema
void display_machine_status(MachManager* manager) {
    if (manager->machine_count == 0) {
        printf("Nenhuma máquina disponível.\n");
        return;
    }

    for (int i = 0; i < manager->machine_count; i++) {
        Machine* machine = manager->machines[i];
        printf("Máquina ID: %d, Estado: %s, Temp: %.2f°C, Hum: %.2f%%\n",
                machine->config->id,
                machine->state == OFF ? "OFF" : machine->state == ON ? "ON" : "IN_OPERATION",
                machine->current_temp,
                machine->current_hum);
    }
}

// Exibir o estado de uma máquina
void show_machine_state(Machine* machine) {
    const char* state_str;
    char command[50]; // Buffer para armazenar o comando
    int command_sent = 0;

    // Determinar o estado da máquina e preparar o comando correspondente
    switch (machine->state) {
        case OFF:
            state_str = "OFF";
            snprintf(command, sizeof(command), "OFF");
            command_sent = sendCommandToMachine(command);
            break;
        case ON:
            state_str = "ON";
            snprintf(command, sizeof(command), "ON 1 0 0 0 0");
            command_sent = sendCommandToMachine(command);
            break;
        case IN_OPERATION:
            state_str = "IN OPERATION";
            snprintf(command, sizeof(command), "ON 1 1 1 1 1");
            command_sent = sendCommandToMachine(command);
            break;
        default:
            state_str = "UNKNOWN";
            printf("Estado desconhecido para a máquina ID: %d\n", machine->config->id);
            return;
    }

    // Exibir informações sobre o estado da máquina
    printf("Máquina ID: %d, Estado: %s\n", machine->config->id, state_str);

    // Exibir mensagem de sucesso ou falha no envio do comando
    if (command_sent) {
        printf("Comando enviado com sucesso: %s\n", command);
    } else {
        printf("Falha ao enviar o comando: %s\n", command);
    }
}

void assign_operation(MachManager* manager, int machine_id, Operation* operation) {
    int machine_found = 0;

    // Verificar se a máquina existe no sistema
    for (int i = 0; i < manager->machine_count; i++) {
        if (manager->machines[i]->config->id == machine_id) {
            manager->machines[i]->state = IN_OPERATION;
            printf("Operação \"%s\" atribuída à máquina ID %d com sucesso.\n",
                   operation->operation, machine_id);
            machine_found = 1;
            break;
        }
    }

    if (!machine_found) {
        printf("Erro: Máquina com ID %d não encontrada.\n", machine_id);
        return;
    }

    // Abrir o arquivo original e preparar um temporário
    FILE* file = fopen("operations_log.csv", "r");
    if (!file) {
        printf("Erro ao abrir o arquivo de operações para leitura.\n");
        return;
    }

    FILE* temp_file = fopen("temp.csv", "w");
    if (!temp_file) {
        printf("Erro ao criar arquivo temporário.\n");
        fclose(file);
        return;
    }

    char buffer[1024];
    int is_updated = 0;

    while (fgets(buffer, sizeof(buffer), file)) {
        char* line = strdup(buffer); // Duplicar a linha para evitar modificação direta
        char* token = strtok(line, ",");
        int current_machine_id = atoi(token); // Extrair o ID da máquina

        if (current_machine_id == machine_id) {
            // Atualizar operações existentes da máquina encontrada
            char* operations = strtok(NULL, "\n");
            if (operations) {
                fprintf(temp_file, "%d,\"%s, %s (%d, %s)\"\n",
                        current_machine_id, operations, operation->operation, operation->operation_number, operation->timestamp);
            } else {
                fprintf(temp_file, "%d,\"%s (%d, %s)\"\n",
                        current_machine_id, operation->operation, operation->operation_number, operation->timestamp);
            }
            is_updated = 1;
        } else {
            // Copiar linha sem alterações
            fprintf(temp_file, "%s", buffer);
        }

        free(line);
    }

    // Caso não tenha atualizado, significa que é uma nova máquina no arquivo
    if (!is_updated) {
        fprintf(temp_file, "%d,\"%s (%d, %s)\"\n",
                machine_id,
                operation->operation,
                operation->operation_number,
                operation->timestamp);
    }

    fclose(file);
    fclose(temp_file);

    // Substituir o arquivo original pelo temporário
    remove("operations_log.csv");
    rename("temp.csv", "operations_log.csv");

    printf("Arquivo atualizado com sucesso.\n");
}

int sendCommandToMachine2(const char* command) {
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

