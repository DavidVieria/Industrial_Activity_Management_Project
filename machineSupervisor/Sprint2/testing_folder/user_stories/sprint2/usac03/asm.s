.section .text
.global get_number
get_number:
	# rdi tem str
	# rsi tem n
	
	xor %rax, %rax
	xor %rcx, %rcx
	xor %rbx, %rbx		# contador de caracteres
	xor %r9, %r9		# contador de espacos
	
.loop:
	movb (%rdi), %al
	cmpb $0, %al
	je .done
	
	cmpb $' ', %al
	je .skip
	
	subb $'0', %al
	cmpb $0, %al
	jl .unvalidCharacter
	
	cmpb $9, %al
	jg .unvalidCharacter
	
	addq $1, %rbx
	
	imulq $10, %rcx
	addq %rax, %rcx
	
	incq %rdi
	jmp .loop
	
.skip:
	addq $1, %r9
	incq %rdi
	jmp .loop
	
.unvalidCharacter:
	movl $0, %eax
	ret
	
.done:
	cmpq %rbx, %r9
	je .unvalidCharacter

	movq %rcx, (%rsi)
	movl $1, %eax
	ret
	
