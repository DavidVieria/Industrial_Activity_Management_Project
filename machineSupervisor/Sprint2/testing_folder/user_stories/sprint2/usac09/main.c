#include <stdio.h>
#include "sort_array.h"

int main(void) {
    int array[] = {7,-3,0,-4,5,1,2,0,6,7};
    int length = 10;
    int order = 0;

	printf("\nOriginal Array: %d", array[0]);

	for (int i = 1; i < length; i++) {
		printf(", %d", array[i]);
    }

    int res = sort_array(array, length, order);

    printf("\nChanged Array: %d", array[0]);

	for (int i = 1; i < length; i++) {
		printf(", %d", array[i]);
    }


    if (res == 1) {
		printf("\nSuccess!\n");
	} else if (res == 0) {
		printf("\nFailure!\n");
	} else {
		printf("\nUnexpected error!\n");
	}

    return 0;
}
