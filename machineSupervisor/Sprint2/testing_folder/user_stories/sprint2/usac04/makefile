# Makefile

main: main.o format_command.o
		gcc main.o format_command.o -z noexecstack -o main
		
main.o: main.c format_command.h
		gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o		

format_command.o: format_command.s 
		gcc -g -Wall -Wextra -fanalyzer -c format_command.s -o format_command.o
		
run: main
		./main		

clean:
		rm *.o main
