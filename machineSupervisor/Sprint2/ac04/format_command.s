.section .text

    .global format_command 
 .global get_number_binary

# Function get_number_binary (USAC02):

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

# Function get_number_binary (USAC04):

format_command:
    # Save values and set up stack:
    pushq %rbp
    movq %rsp, %rbp
    pushq %rdx

    # If >= 30, failure:
    cmpl $30, %esi
    jns endFailure
    # If negative, failure:
    cmpl $0, %esi
    js endFailure

validateInputOperator:
    # Load character from string:
    movb (%rdi), %r8b
    inc %rdi

    # If end of string:
    cmpb $0, %r8b
    je endFailure

    # If control characters (e.g., space):
    cmpb $33, %r8b
    js validateInputOperator

    # If character is 'o' or 'O':
    cmpb $111, %r8b
    je checkSecondOperator
    cmpb $79, %r8b
    je checkSecondOperator

    # If not found, failure:
    jmp endFailure

checkSecondOperator:
    # Store 'O':
    movb $79, (%rdx)

    # Move output pointer and next character:
    inc %rdx
    movb (%rdi), %r8b

    # If 'n' or 'N':
    cmpb $110, %r8b
    je processSecondCharN
    cmpb $78, %r8b
    je processSecondCharN

    # If 'p' or 'P':
    cmpb $112, %r8b
    je processSecondCharP
    cmpb $80, %r8b
    je processSecondCharP

    # If 'f' or 'F':
    cmpb $102, %r8b
    je processSecondCharF
    cmpb $70, %r8b
    je processSecondCharF

    # If it doesn't match any, failure:
    jmp endFailure

# Different cases based on the second letter of the string:

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
    # Load next character:
    inc %rdi
    movb (%rdi), %r8b

    # If end of string, process number:
    cmpb $0, %r8b
    je extractNumber

    # If control characters (e.g., space):
    cmpb $33, %r8b
    js trimRightSide

    # Otherwise, failure:
    jmp endFailure

extractNumber:
    # Place comma:
    movb $44, (%rdx)
    inc %rdx

    # Push value %rdx:
    pushq %rdx
    # Move values according to the argument positions in the function call:
    movq %rsi, %rdi
    movq %rdx, %rsi
    # Call function:
    call get_number_binary
    # Restore value %rdx:
    popq %rdx

    # If function result is 0:
    cmpq $0, %rax
    je endFailure

    # Move bytes to ASCII value and add commas:
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

    # Rearranging bytes due to the desired string format order:
    movb -2(%rdx), %r9b
    movb -8(%rdx), %cl
    movb -6(%rdx), %r8b
    movb %cl, (%rdx)
    movb %r10b, -8(%rdx)
    movb %r9b, -6(%rdx)
    movb %r8b, -2(%rdx)

    # End string:
    inc %rdx
    movb $0, (%rdx)

    # Success (1):
    movq $1, %rax

    # Restore stack pointer
    # Restore value %rbp
    movq %rbp, %rsp
    popq %rbp
    ret

# Interval is invalid (0):
.invalidIntervalCheck:
    movl $0, %eax
    ret

# Success (1):
.conversionComplete:
    movl $1, %eax
    ret

# Invalid string (0):
endFailure:
    andq $0, %rax
    popq %rdx
    movq $0, (%rdx)

 movq %rbp, %rsp
 popq %rbp

 ret