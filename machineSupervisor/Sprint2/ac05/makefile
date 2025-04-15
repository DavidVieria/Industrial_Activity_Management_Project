# makefile

arqcp_USAC05: main.o enqueue_value.o
		gcc main.o enqueue_value.o -z noexecstack -o arqcp_USAC05

main.o: main.c enqueue_value.h
		gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o

enqueue_value.o: enqueue_value.s
		gcc -g -Wall -Wextra -fanalyzer -c enqueue_value.s -o enqueue_value.o
		
run: arqcp_USAC05
		./arqcp_USAC05

clean:
		rm *.o arqcp_USAC05
