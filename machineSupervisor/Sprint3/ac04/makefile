# Makefile

arqcp_USAC04: main.o format_command.o
		gcc main.o format_command.o -z noexecstack -o arqcp_USAC04
		
main.o: main.c format_command.h
		gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o		

format_command.o: format_command.s 
		gcc -g -Wall -Wextra -fanalyzer -c format_command.s -o format_command.o
		
run: arqcp_USAC04
		./arqcp_USAC04

clean:
		rm *.o arqcp_USAC04
