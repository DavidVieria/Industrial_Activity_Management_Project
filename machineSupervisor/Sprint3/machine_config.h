#ifndef MACHINE_CONFIG_H
#define MACHINE_CONFIG_H

typedef struct {
    int id;
    char name[50];
    float min_temp;
    float max_temp;
    float min_hum;
    float max_hum;
    int circular_buffer_length;
    int moving_median_window;
} MachineConfig;

// Funções para configurar a máquina
MachineConfig** load_machine_configs(const char* filename, int* count);
void print_machine_config(MachineConfig* config);
void free_machine_configs(MachineConfig** configs, int count);

#endif
