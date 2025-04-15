.section .text
.global extract_data

# %rdi = str / %rsi = token / %rdx = unit / %rcx = value

extract_data:
    movq %rbx, %r9
    movl $0, %eax
	movq %rdi, %rbx
    movq %rsi, %r8
    movb $0, (%rdx)
    movl $0, (%rcx)

comparing:

    movb (%rbx), %al
    cmpb $0, %al
    je not_found

    cmpq %rbx, %rdi
    je verify_match

    movb -1(%rbx), %al
    cmpb $'#', %al
    je verify_match

    incq %rbx
    jmp comparing

verify_match:
    movq %rsi, %r8

next_token:
    movb (%r8), %al
    cmpb $0, %al
    je check_separation

    cmpb (%rbx), %al
    jne comparing_next_token

    incq %r8
    incq %rbx
    jmp next_token

check_separation:
    cmpb $'&', (%rbx)
    je unit

    jmp comparing_next_token

comparing_next_token:
    incq %rbx
    jmp comparing

unit:
    add $6, %rbx
    movq %rdx, %rdi

put_unit:
    movb (%rbx), %al
    cmpb $'&', %al
    je value

    movb %al, (%rdi)
    incq %rdi
    incq %rbx
    jmp put_unit

value:
    movb $0, (%rdi)
    add $7, %rbx
    movl $0, %eax
    movl $0, %edx

loop_put:
    movb (%rbx), %al

    cmpb $'#', %al
    je end_value
    cmpb $0, %al
    je end_value

    subb $'0', %al
    imul $10, %edx
    addl %eax, %edx
    incq %rbx
    jmp loop_put

end_value:
    movl %edx, (%rcx)
    movl $1, %eax
    movq %r9, %rbx
    jmp end

not_found:
    movq %r9, %rbx
    ret

end:
	ret


