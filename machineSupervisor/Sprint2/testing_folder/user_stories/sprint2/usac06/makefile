# makefile

main: main.o dequeue_value.o
		gcc main.o dequeue_value.o -z noexecstack -o main

main.o: main.c dequeue_value.h
		gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o

dequeue_value.o: dequeue_value.s
		gcc -g -Wall -Wextra -fanalyzer -c dequeue_value.s -o dequeue_value.o
		
run: main
		./main		

clean:
		rm *.o main
