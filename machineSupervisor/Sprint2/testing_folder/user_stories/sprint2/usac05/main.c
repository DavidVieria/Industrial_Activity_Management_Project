#include <stdio.h>
#include "enqueue_value.h"

int main(void) {
    int buffer[] = {0,0,0,0};       // change to the desired buffer
    int length = 4;         // change to the desired length
    int tail = 0;       // change to the desired tail
    int head = 3;       // change to the desired head
    int value = 95;         // change to the desired value

	printf("\nOriginal Buffer: %d", buffer[0]);

	for (int i = 1; i < length; i++) {
		printf(", %d", buffer[i]);
    }

    int res =  enqueue_value(buffer, length, &tail, &head, value) ;

    printf("\nChanged Buffer: %d", buffer[0]);

	for (int i = 1; i < length; i++) {
		printf(", %d", buffer[i]);
    }

    printf("\nTail: %d\n", tail);
    printf("Head: %d\n", head);

    if (res == 1) {
		printf("Buffer full!\n");
	} else if (res == 0) {
		printf("Buffer not full!\n");
	} else {
		printf("Unexpected error!\n");
	}

    return 0;
}
