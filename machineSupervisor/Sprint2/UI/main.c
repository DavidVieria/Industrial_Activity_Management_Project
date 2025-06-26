#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>  // Para usar chdir

// Prototipos das funções para as user stories
void user_story_1();
void user_story_2();
void user_story_3();
void user_story_4();
void user_story_5();
void user_story_6();
void user_story_7();
void user_story_8();
void user_story_9();
void user_story_10();

// Diretório inicial do projeto
const char *diretorio_inicial = "C:/Users/05dar/Documents/MyRepositories/Industrial_Activity_Management_Project/machineSupervisor";

// Função para mudar para o diretório da user story
int mudar_para_diretorio(const char *user_story) {
    char caminho[1024];
    snprintf(caminho, sizeof(caminho), "%s/%s", diretorio_inicial, user_story);
    if (chdir(caminho) != 0) {
        perror("Erro ao mudar para o diretório da user story");
        return -1;
    }
    return 0;
}

// Função para voltar ao diretório inicial
int voltar_para_diretorio_inicial() {
    if (chdir(diretorio_inicial) != 0) {
        perror("Erro ao voltar para o diretório inicial");
        return -1;
    }
    return 0;
}

void show_menu() {
    printf("====================================\n");
    printf("      Escolha uma User Story        \n");
    printf("====================================\n");
    for (int i = 1; i <= 10; i++) {
        printf("%d - User Story %d\n", i, i);
    }
    printf("0 - Sair\n");
    printf("====================================\n");
    printf("Escolha uma opcao: ");
}

int main() {
    int opcao;

    do {
        show_menu();
        if (scanf("%d", &opcao) != 1) {
            printf("Erro na entrada. Por favor insira um numero.\n");
            while (getchar() != '\n'); // Limpar buffer
            continue;
        }

        switch (opcao) {
            case 1:
                user_story_1();
                break;
            case 2:
                user_story_2();
                break;
            case 3:
                user_story_3();
                break;
            case 4:
                user_story_4();
                break;
            case 5:
                user_story_5();
                break;
            case 6:
                user_story_6();
                break;
            case 7:
                user_story_7();
                break;
            case 8:
                user_story_8();
                break;
            case 9:
                user_story_9();
                break;
            case 10:
                user_story_10();
                break;
            // Adicione casos para todas as 10 user stories
            case 0:
                printf("Saindo do programa. Obrigado!\n");
                break;
            default:
                printf("Opcao invalida. Tente novamente.\n");
                break;
        }
    } while (opcao != 0);

    return 0;
}

// Implementacao das user stories
void user_story_1() {
    printf("Executando User Story 1...\n");

    if (mudar_para_diretorio("ac01") != 0) return;

    if (system("make") != 0) {
        printf("Erro ao compilar a User Story 1.\n");
        voltar_para_diretorio_inicial();
        return;
    }

    if (system("./arqcp_USAC01") != 0) {
        printf("Erro ao executar a User Story 1.\n");
    }

    system("make clean");
    voltar_para_diretorio_inicial();
}

void user_story_2() {
    printf("Executando User Story 2...\n");

    if (mudar_para_diretorio("ac02") != 0) return;

    if (system("make") != 0) {
        printf("Erro ao compilar a User Story 2.\n");
        voltar_para_diretorio_inicial();
        return;
    }

    if (system("./arqcp_USAC02") != 0) {
        printf("Erro ao executar a User Story 2.\n");
    }

    system("make clean");
    voltar_para_diretorio_inicial();
}

void user_story_3() {
    printf("Executando User Story 3...\n");

    if (mudar_para_diretorio("ac03") != 0) return;

    if (system("make") != 0) {
        printf("Erro ao compilar a User Story 3.\n");
        voltar_para_diretorio_inicial();
        return;
    }

    if (system("./arqcp_USAC03") != 0) {
        printf("Erro ao executar a User Story 3.\n");
    }

    system("make clean");
    voltar_para_diretorio_inicial();
}

void user_story_4() {
    printf("Executando User Story 4...\n");

    if (mudar_para_diretorio("ac04") != 0) return;

    if (system("make") != 0) {
        printf("Erro ao compilar a User Story 4.\n");
        voltar_para_diretorio_inicial();
        return;
    }

    if (system("./arqcp_USAC04") != 0) {
        printf("Erro ao executar a User Story 4.\n");
    }

    system("make clean");
    voltar_para_diretorio_inicial();
}

void user_story_5() {
printf("Executando User Story 5...\n");

    if (mudar_para_diretorio("ac05") != 0) return;

    if (system("make") != 0) {
        printf("Erro ao compilar a User Story 5.\n");
        voltar_para_diretorio_inicial();
        return;
    }

    if (system("./arqcp_USAC05") != 0) {
        printf("Erro ao executar a User Story 5.\n");
    }

    system("make clean");
    voltar_para_diretorio_inicial();
}

void user_story_6() {
    printf("Executando User Story 6...\n");

    if (mudar_para_diretorio("ac06") != 0) return;

    if (system("make") != 0) {
        printf("Erro ao compilar a User Story 6.\n");
        voltar_para_diretorio_inicial();
        return;
    }

    if (system("./arqcp_USAC06") != 0) {
        printf("Erro ao executar a User Story 6.\n");
    }

    system("make clean");
    voltar_para_diretorio_inicial();
}

void user_story_7() {
    printf("Executando User Story 7...\n");

    if (mudar_para_diretorio("ac07") != 0) return;

    if (system("make") != 0) {
        printf("Erro ao compilar a User Story 7.\n");
        voltar_para_diretorio_inicial();
        return;
    }

    if (system("./arqcp_USAC07") != 0) {
        printf("Erro ao executar a User Story 7.\n");
    }

    system("make clean");
    voltar_para_diretorio_inicial();
}

void user_story_8() {
    printf("Executando User Story 8...\n");

    if (mudar_para_diretorio("ac08") != 0) return;

    if (system("make") != 0) {
        printf("Erro ao compilar a User Story 8.\n");
        voltar_para_diretorio_inicial();
        return;
    }

    if (system("./arqcp_USAC08") != 0) {
        printf("Erro ao executar a User Story 8.\n");
    }

    system("make clean");
    voltar_para_diretorio_inicial();
}

void user_story_9() {
    printf("Executando User Story 9...\n");

    if (mudar_para_diretorio("ac09") != 0) return;

    if (system("make") != 0) {
        printf("Erro ao compilar a User Story 9.\n");
        voltar_para_diretorio_inicial();
        return;
    }

    if (system("./arqcp_USAC09") != 0) {
        printf("Erro ao executar a User Story 9.\n");
    }

    system("make clean");
    voltar_para_diretorio_inicial();
}

void user_story_10() {
    printf("Executando User Story 10...\n");

    if (mudar_para_diretorio("ac10") != 0) return;

    if (system("make") != 0) {
        printf("Erro ao compilar a User Story 10.\n");
        voltar_para_diretorio_inicial();
        return;
    }

    if (system("./arqcp_USAC10") != 0) {
        printf("Erro ao executar a User Story 10.\n");
    }

    system("make clean");
    voltar_para_diretorio_inicial();
}
