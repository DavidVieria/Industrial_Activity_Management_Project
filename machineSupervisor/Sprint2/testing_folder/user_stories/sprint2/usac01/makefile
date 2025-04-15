# Makefile

main: main.o extract_data.o
		gcc main.o extract_data.o -z noexecstack -o main
		
main.o: main.c extract_data.h
		gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o		

extract_data.o: extract_data.s 
		gcc -g -Wall -Wextra -fanalyzer -c extract_data.s -o extract_data.o
		
run: main
		./main		

clean:
		rm *.o main
