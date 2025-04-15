arqcp_USAC10: asm.o  main.o
		gcc asm.o  main.o -z noexecstack -no-pie -o arqcp_USAC10

asm.o: asm.s
		gcc -g -Wall -Wextra -fanalyzer -c asm.s -o asm.o

main.o: main.c asm.h
		gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o

clean:
		rm -f *.o arqcp_USAC10

run: arqcp_USAC10
		./arqcp_USAC10
