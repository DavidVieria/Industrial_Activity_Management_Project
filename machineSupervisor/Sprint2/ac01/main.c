#include <stdio.h>
#include "extract_data.h"

int main() {
    char str[] = "TEMP&unit:celsius&value:20#HUM&unit:percentage&value:80";
    char token[] = "TEMP";
    char unit[20];
    int value;
    
    printf("====================================\n");

    int res = extract_data(str, token, unit, &value);
    printf("%d:%s,%d\n", res, unit, value); // Expected output: 1 : celsius , 20

    char token2[] = "AAA";
    res = extract_data(str, token2, unit, &value);
    printf("%d:%s,%d\n", res, unit, value); // Expected output: 0 : , 0
    
    printf("====================================\n");

    return 0;
}
