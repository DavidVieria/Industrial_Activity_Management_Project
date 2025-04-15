#include "mach_manager.h"
#include "ui.h"
#include <stdio.h>
#include <stdlib.h>

int main() {
    // Inicializar o gerenciador de máquinas
    MachManager* manager = create_mach_manager();

    // Chama a função que gerencia a interação com o usuário
    handle_user_input(manager);

    return 0;
}
