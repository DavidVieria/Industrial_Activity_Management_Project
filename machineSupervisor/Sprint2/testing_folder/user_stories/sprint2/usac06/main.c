#include <stdio.h>
#include "dequeue_value.h"

int main(void) {
    int buffer[] = {6,7,8,9};
    int length = 4;
    int tail = 1;
    int head = 3;
    int value;

    int res =  dequeue_value(buffer, length, &tail, &head, &value);

    printf("\nBuffer: %d", buffer[0]);

	for (int i = 1; i < length; i++) {
		printf(", %d", buffer[i]);
    }

    printf("\n\nRead Value: %d\n", value);
    printf("Tail: %d\n", tail);
    printf("Head: %d\n", head);

    if (res == 1) {
		printf("Success!\n");
	} else if (res == 0) {
		printf("Failure!\n");
	} else {
		printf("Unexpected error!\n");
	}

    return 0;
}
