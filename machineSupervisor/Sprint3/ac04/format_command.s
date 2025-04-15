.section .text

    .global format_command 
	.global get_number_binary
	
get_number_binary:
	
	cmpq $0, %rdi
	jl .outsideInterval
	
	cmpq $31, %rdi
	jg .outsideInterval
	
	xor %edx, %edx 		
	movl %edi, %eax	
	movl $2, %ecx		
	
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

format_command:
    pushq %rbp
    movq %rsp, %rbp
    pushq %rdx 
    cmpl $30, %esi
    jns endFailure 
    cmpl $0, %esi
    js endFailure 

validateInputOperator:
    movb (%rdi), %r8b
    inc %rdi
    cmpb $0, %r8b
    je endFailure 
    cmpb $33, %r8b
    js validateInputOperator 
    cmpb $111, %r8b
    je checkSecondOperator 
    cmpb $79, %r8b
    je checkSecondOperator 
    jmp endFailure 

checkSecondOperator:
    movb $79, (%rdx)
    inc %rdx
    movb (%rdi), %r8b
    cmpb $110, %r8b
    je processSecondCharN 
    cmpb $78, %r8b
    je processSecondCharN 
    cmpb $112, %r8b
    je processSecondCharP 
    cmpb $80, %r8b
    je processSecondCharP 
    cmpb $102, %r8b
    je processSecondCharF 
    cmpb $70, %r8b
    je processSecondCharF 
    jmp endFailure 

processSecondCharN:
    movb $78, (%rdx)
    inc %rdx
    jmp trimRightSide 

processSecondCharP:
    movb $80, (%rdx)
    inc %rdx
    jmp trimRightSide 

processSecondCharF:
    inc %rdi
    movb (%rdi), %r8b
    cmpb $102, %r8b
    je processThirdCharF 
    cmpb $70, %r8b
    je processThirdCharF 
    jmp endFailure 

processThirdCharF:
    movb $70, (%rdx)
    inc %rdx
    movb $70, (%rdx)
    inc %rdx
    jmp trimRightSide 

trimRightSide:
    inc %rdi
    movb (%rdi), %r8b
    cmpb $0, %r8b
    je extractNumber 
    cmpb $33, %r8b
    js trimRightSide 
    jmp endFailure 

extractNumber:
    movb $44, (%rdx)
    inc %rdx
    pushq %rdx
    movq %rsi, %rdi
    movq %rdx, %rsi
    call get_number_binary 
    popq %rdx 
    cmpq $0, %rax 
    je endFailure 
    addb $48, (%rdx)
    inc %rdx 
    movb (%rdx), %r8b
    movb $44, (%rdx)
    inc %rdx 
    movb (%rdx), %r9b
    movb %r8b, (%rdx)
    addb $48, (%rdx)
    inc %rdx 
    movb (%rdx), %r8b
    movb $44, (%rdx)
    inc %rdx 
    movb (%rdx), %r10b
    movb %r9b, (%rdx)
    addb $48, (%rdx)
    inc %rdx 
    movb $44, (%rdx)
    inc %rdx 
    movb %r8b, (%rdx)
    addb $48, (%rdx)
    inc %rdx 
    movb $44, (%rdx)
    inc %rdx 
    movb %r10b, (%rdx)
    addb $48, (%rdx)
    movb (%rdx), %r10b
    movb -2(%rdx), %r9b
    movb -8(%rdx), %cl
    movb -6(%rdx), %r8b
    movb %cl, (%rdx)
    movb %r10b, -8(%rdx)
    movb %r9b, -6(%rdx)
    movb %r8b, -2(%rdx)
    inc %rdx 
    movb $0, (%rdx)
    movq $1, %rax 

    movq %rbp, %rsp
    popq %rbp
    ret 

.invalidIntervalCheck:
    movl $0, %eax
    ret
    
.conversionComplete:
    movl $1, %eax
    ret

endFailure:
    andq $0, %rax
    popq %rdx 
    movq $0, (%rdx)

	movq %rbp, %rsp
	popq %rbp

	ret
