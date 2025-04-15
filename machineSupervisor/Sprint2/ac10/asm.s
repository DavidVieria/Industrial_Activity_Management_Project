.global median
.section .text

median:
    pushq %rbp                # Guardar o ponteiro de base
    movq %rsp, %rbp           # Estabelecer ponteiro de base para o ponteiro de pilha atual

    # Guardar os registradores
    pushq %rbx
    pushq %r12
    pushq %r13
    pushq %r14

    # Guardar parâmetros
    movq %rdi, %r12           # Ponteiro para o array
    movl %esi, %r13d          # Comprimento
    movq %rdx, %r14           # Ponteiro para o resultado

    # Verificar se o array está vazio
    testl %r13d, %r13d        # Testar se o comprimento é zero
    jle falha                 # Jump para falha se o comprimento for zero ou menor

    # Ordenação diretamente na função median
    cmpb $1, %dl              # Comparar se a ordem é crescente (1)
    je ordem_crescente         # Jump para ordem crescente se verdadeiro
    jmp ordem_decrescente      # Jump para ordem decrescente se falso

ordem_crescente:
    movl $0, %r11d            # Inicializar índice do primeiro loop

primeiro_loop_ascending:
    cmpl %r13d, %r11d         # Comparar se o índice atingiu o comprimento
    jge fim                   # Jump para o fim se o loop estiver completo

    movl %r11d, %r9d          # Definir o índice do loop interno para o índice do loop externo

segundo_loop_ascending:
    movl %r13d, %eax          # Definir eax para o comprimento
    subl $1, %eax             # Subtrair 1 de eax
    subl %r11d, %eax          # Subtrair o índice do loop externo de eax
    cmpl %eax, %r9d           # Comparar se o índice interno é maior que eax
    jge incremento_primeiro_loop  # Saltar para incrementar o primeiro loop se verdadeiro

    movl (%r12, %r9, 4), %ecx  # Carregar o elemento no índice interno em ecx
    movl 4(%r12, %r9, 4), %edx # Carregar o próximo elemento em edx

    cmpl %ecx, %edx           # Comparar se o primeiro elemento é maior que o segundo
    jge skip_ascending        # Jump para a troca se não for

    movl %edx, (%r12, %r9, 4)  # Trocar os elementos
    movl %ecx, 4(%r12, %r9, 4)

skip_ascending:
    incl %r9d                  # Incrementar o índice do loop interno
    jmp segundo_loop_ascending # Jump para a próxima iteração do loop interno

incremento_primeiro_loop:
    incl %r11d                 # Incrementar o índice do loop externo
    jmp primeiro_loop_ascending # Jump para a próxima iteração do loop externo

ordem_decrescente:
    movl $0, %r11d             # Inicializar o índice do primeiro loop para ordem decrescente

primeiro_loop_descending:
    cmpl %r13d, %r11d          # Comparar se o índice atingiu o comprimento
    jge fim                    # Jump para o fim se o loop estiver completo

    movl $0, %r9d              # Inicializar o índice do loop interno

segundo_loop_descending:
    movl %r13d, %eax           # Definir eax para o comprimento
    subl $1, %eax              # Subtrair 1 de eax
    subl %r11d, %eax           # Subtrair o índice do loop externo de eax
    cmpl %eax, %r9d            # Comparar se o índice interno é igual a eax
    je incremento_primeiro_loop_descending  # Saltar para incrementar o primeiro loop se igual

    movl (%r12, %r9, 4), %ecx  # Carregar o elemento no índice interno em ecx
    movl 4(%r12, %r9, 4), %edx # Carregar o próximo elemento em edx

    cmpl %ecx, %edx           # Comparar se o primeiro elemento é menor que o segundo
    jle skip_descending       # Jump para a troca se não for

    movl %edx, (%r12, %r9, 4)  # Trocar os elementos
    movl %ecx, 4(%r12, %r9, 4)

skip_descending:
    incl %r9d                  # Incrementar o índice do loop interno
    jmp segundo_loop_descending # Jump para a próxima iteração do loop interno

incremento_primeiro_loop_descending:
    incl %r11d                 # Incrementar o índice do loop externo
    jmp primeiro_loop_descending # Jump para a próxima iteração do loop externo

fim:
    # Obter o índice do elemento do meio
    movl %r13d, %ebx           # Definir comprimento em ebx
    shrl $1, %ebx              # Dividir o comprimento por 2

    # Verificar se o comprimento é ímpar
    testl $1, %r13d            # Testar se o comprimento é ímpar
    jnz comprimento_impar      # Saltar para o caso de comprimento ímpar se verdadeiro

    # Comprimento par - média dos dois elementos do meio
    decl %ebx                  # Obter o índice do primeiro elemento do meio (comprimento/2 - 1)
    movl (%r12, %rbx, 4), %eax  # Obter o primeiro elemento do meio
    incl %ebx                  # Obter o índice do segundo elemento do meio (comprimento/2)
    movl (%r12, %rbx, 4), %edx  # Obter o segundo elemento do meio
    addl %edx, %eax            # Somar o segundo elemento do meio

    # Lidar corretamente com números negativos para a divisão
    testl %eax, %eax           # Testar se a soma é negativa
    jns soma_positiva          # Saltar se a soma for positiva

    # Para soma negativa, subtrair 1 antes de dividir para arredondar para o infinito negativo
    subl $1, %eax

soma_positiva:
    sarl $1, %eax              # Dividir por 2
    movl %eax, (%r14)          # Armazenar o resultado no ponteiro de resultado
    jmp sucesso

comprimento_impar:
    # Comprimento ímpar - basta pegar o elemento do meio
    movl (%r12, %rbx, 4), %eax  # Obter o elemento do meio
    movl %eax, (%r14)          # Armazenar o resultado no ponteiro de resultado

sucesso:
    movl $1, %eax              # Definir valor de retorno como 1 (sucesso)
    jmp limpar

falha:
    xorl %eax, %eax            # Definir valor de retorno como 0 (falha)
    movl $-1, (%r14)           # Armazenar -1 como resultado

limpar:
    # Restaurar os registradores
    popq %r14
    popq %r13
    popq %r12
    popq %rbx

    movq %rbp, %rsp            # Restaurar o ponteiro de pilha
    popq %rbp                  # Restaurar o ponteiro de base
    ret
