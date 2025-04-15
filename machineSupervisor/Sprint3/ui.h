#ifndef UI_H
#define UI_H

#define MAX_BUFFER_SIZE 256

#include "mach_manager.h"
#include "operations.h" // Inclusão do cabeçalho das operações
#include "instructions.h" // Inclusão do cabeçalho das instruções

// Funções
void print_menu();
void handle_user_input(MachManager* manager);
void free_manager_resources(MachManager* manager);
int read_serial_data(float* temperature, float* humidity);
void trim(char* str);

#endif // UI_H
