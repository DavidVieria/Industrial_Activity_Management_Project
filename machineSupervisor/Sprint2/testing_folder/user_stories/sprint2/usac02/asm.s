.section .text
.global get_number_binary
get_number_binary:
	# rdi tem n
	# rsi tem array de bits
	
	cmpq $0, %rdi
	jl .outsideInterval
	
	cmpq $31, %rdi
	jg .outsideInterval
	
	xor %edx, %edx 		# onde fica armazenado o resto
	movl %edi, %eax		# movemos para %eax n para conseguir fazer as divisoes
	movl $2, %ecx		# metemos %ecx com 2 para dividir sucessivamente por 2
	
.loop:
	cmp $0, %eax
	je .done

	divl %ecx
	movb %dl, (%rsi)
	addq $1, %rsi
	xor %edx, %edx
	jmp .loop
	
.outsideInterval:
	movl $0, %eax
	ret
	
.done:
	movl $1, %eax
	ret
