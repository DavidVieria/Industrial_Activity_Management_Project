# makefile

arqcp_USAC09: main.o sort_array.o
		gcc main.o sort_array.o -z noexecstack -o arqcp_USAC09

main.o: main.c sort_array.h
		gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o

sort_array.o: sort_array.s
		gcc -g -Wall -Wextra -fanalyzer -c sort_array.s -o sort_array.o
		
run: arqcp_USAC09
		./arqcp_USAC09

clean:
		rm *.o arqcp_USAC09
