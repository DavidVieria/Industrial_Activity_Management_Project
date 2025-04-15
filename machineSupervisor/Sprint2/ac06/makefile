# makefile

arqcp_USAC06: main.o dequeue_value.o
		gcc main.o dequeue_value.o -z noexecstack -o arqcp_USAC06

main.o: main.c dequeue_value.h
		gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o

dequeue_value.o: dequeue_value.s
		gcc -g -Wall -Wextra -fanalyzer -c dequeue_value.s -o dequeue_value.o
		
run: arqcp_USAC06
		./arqcp_USAC06

clean:
		rm *.o arqcp_USAC06
