.section .text
.global get_n_element

get_n_element:
    movl (%rcx), %r8d       # Carrega *head em %r8d -> r8d = *head
    movl (%rdx), %r9d       # Carrega *tail em %r9d -> r9d = *tail

    # Se head >= tail
    cmpl %r9d, %r8d
    jge .case1

.case2:                     # Caso head < tail
    movl %esi, %eax         # eax = length
    subl %r9d, %eax         # eax = length - tail
    addl %r8d, %eax         # eax += head
    ret

.case1:                     # Caso head >= tail
    movl %r8d, %eax         # eax = head
    subl %r9d, %eax         # eax -= tail
ret
