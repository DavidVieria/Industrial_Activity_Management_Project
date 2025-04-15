#ifndef MACH_MANAGER_H
#define MACH_MANAGER_H

#define MAX_MACHINES 10
#define MAX_OPERATIONS 10  // Add this line

#include "machine_config.h"
#include "operations.h" // Inclusão das operações
#include <time.h>       // Include time.h for time functions

// Definir estados das máquinas
typedef enum {
    OFF = 0,
    ON = 1,
    IN_OPERATION = 2
} MachineState;

// Estrutura Machine
typedef struct {
    MachineConfig* config;
    MachineState state;
    float current_temp;
    float current_hum;
    int operation_count;
    int operation_ids[10];
    time_t timestamp;
} Machine;

// Estrutura MachManager
typedef struct {
    Machine* machines[MAX_MACHINES];
    int machine_count;
} MachManager;

// Funções do MachManager
MachManager* create_mach_manager();
void add_machine(MachManager* manager, MachineConfig* config);
void remove_machine(MachManager* manager, int machine_id);
void update_machine_data(Machine* machine, float temperature, float humidity);
void check_for_alerts(MachManager* manager);
void display_machine_status(MachManager* manager);
void show_machine_state(Machine* machine);
void assign_operation(MachManager* manager, int machine_id, Operation* operation);
int sendCommandToMachine2(const char* command);

#endif // MACH_MANAGER_H
