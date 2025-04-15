# makefile

main: main.o enqueue_value.o
		gcc main.o enqueue_value.o -z noexecstack -o main

main.o: main.c enqueue_value.h
		gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o

enqueue_value.o: enqueue_value.s
		gcc -g -Wall -Wextra -fanalyzer -c enqueue_value.s -o enqueue_value.o
		
run: main
		./main		

clean:
		rm *.o main
