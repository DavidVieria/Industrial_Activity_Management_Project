UI: main.o
	gcc main.o -z noexecstack -no-pie -o UI

main.o: main.c
	gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o

clean:
	rm -f *.o UI

run: UI
	./UI
