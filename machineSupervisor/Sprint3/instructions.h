#ifndef INSTRUCTIONS_H
#define INSTRUCTIONS_H

#define BUFFER_SIZE 256
#define MAX_INSTRUCTIONS 100  // Define MAX_INSTRUCTIONS

#include "mach_manager.h" // Include mach_manager.h to recognize MachManager

// Define instructions
typedef struct {
    int id;                // Machine identifier
    MachineState state1;    // Machine state (ON, OFF, IN_OPERATION)
    int operation1;
    MachineState state2;
    int operation2;
    MachineState state3;
    int operation3;
} Instruction;

int load_instructions(const char* filename, Instruction*** instructions, int* instruction_count);
void send_instructions_to_machine(MachManager* manager, Instruction** instructions, int instruction_count);
int sendCommandToMachine(const char* command);
void print_instructions(Instruction** instructions, int instruction_count);

#endif // INSTRUCTIONS_H
