# Nome do executável
EXEC = machine_manager

# Diretórios do código (ac01 e ac04)
SRC_DIR_AC01 = ac01
SRC_DIR_AC04 = ac04

# Arquivos de objetos (incluindo a nova pasta ac04)
OBJ_FILES = main.o machine_config.o mach_manager.o ui.o operations.o instructions.o $(SRC_DIR_AC01)/extract_data.o $(SRC_DIR_AC04)/format_command.o

# Regras principais
$(EXEC): $(OBJ_FILES)
	# Compilando e criando o executável
	gcc $(OBJ_FILES) -z noexecstack -no-pie -o $(EXEC)

# Compilação dos arquivos .c
main.o: main.c
	gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o

machine_config.o: machine_config.c
	gcc -g -Wall -Wextra -fanalyzer -c machine_config.c -o machine_config.o

mach_manager.o: mach_manager.c
	gcc -g -Wall -Wextra -fanalyzer -c mach_manager.c -o mach_manager.o

ui.o: ui.c
	gcc -g -Wall -Wextra -fanalyzer -c ui.c -o ui.o

operations.o: operations.c
	gcc -g -Wall -Wextra -fanalyzer -c operations.c -o operations.o

instructions.o: instructions.c
	gcc -g -Wall -Wextra -fanalyzer -c instructions.c -o instructions.o

# Compilação do arquivo .s (assembly) da ac01
$(SRC_DIR_AC01)/extract_data.o: $(SRC_DIR_AC01)/extract_data.s
	gcc -g -Wall -Wextra -fanalyzer -c $(SRC_DIR_AC01)/extract_data.s -o $(SRC_DIR_AC01)/extract_data.o

# Compilação do arquivo .s (assembly) da ac04
$(SRC_DIR_AC04)/format_command.o: $(SRC_DIR_AC04)/format_command.s
	gcc -g -Wall -Wextra -fanalyzer -c $(SRC_DIR_AC04)/format_command.s -o $(SRC_DIR_AC04)/format_command.o

# Limpeza dos arquivos gerados
clean:
	rm -f *.o $(SRC_DIR_AC01)/*.o $(SRC_DIR_AC04)/*.o $(EXEC)

# Executar o programa
run: $(EXEC)
	./$(EXEC)
