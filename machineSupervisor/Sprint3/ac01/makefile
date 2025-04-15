# Makefile

arqcp_USAC01: main.o extract_data.o
		gcc main.o extract_data.o -z noexecstack -o arqcp_USAC01
		
main.o: main.c extract_data.h
		gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o		

extract_data.o: extract_data.s 
		gcc -g -Wall -Wextra -fanalyzer -c extract_data.s -o extract_data.o
		
run: arqcp_USAC01
		./arqcp_USAC01

clean:
		rm *.o arqcp_USAC01
