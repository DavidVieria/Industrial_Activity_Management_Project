#ifndef OPERATIONS_H
#define OPERATIONS_H

typedef struct {
    int machine_id;
    char operation[50];  // Nome da operação
    int operation_number;
    char timestamp[20];  // Data e hora no formato YYYY-MM-DDTHH:MM:SS
} Operation;

Operation** load_operations(const char* filename, int* count);
void free_operations(Operation** operations, int count);
void print_operation(Operation* operation);
void save_operations_to_file(Operation** operations, int operation_count);  // Novo protótipo
void trim_newline(char* str);

#endif // OPERATIONS_H
