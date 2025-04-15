#include <stdio.h>
#include "format_command.h"

int value = 26;
char str[] = " oN ";
char cmd[20];

int main(void){
	
	printf("====================================\n");

	int res = format_command(str, value, cmd);
	printf("%d: %s\n" ,res , cmd ); // 1: ON ,1 ,1 ,0 ,1 ,0
	char str2[] = "aaa";
	res = format_command (str2, value, cmd) ;
	printf("%d: %s\n" ,res , cmd ); // 0:
	
	printf("====================================\n");

	return 0;
}
