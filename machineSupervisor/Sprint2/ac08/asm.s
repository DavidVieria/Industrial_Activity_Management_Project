.section .text

.global move_n_to_array

move_n_to_array:
    # Carrega os valores de *head e *tail e calcula o número de elementos disponíveis
    movl (%rcx), %eax           # Carrega o valor de *head em %eax
    subl (%rdx), %eax           # Calcula head - tail
    addl %esi, %eax             # Adiciona length: %eax = head - tail + length

    # Ajusta o número de elementos disponíveis aplicando o módulo manualmente
    cmpl %esi, %eax             # Verifica se %eax >= length
    jb skip_adjustment          # Se %eax < length, pula o ajuste
    subl %esi, %eax             # Caso contrário, ajusta: %eax -= length

skip_adjustment:
    # Verifica se existem pelo menos ‘n’ elementos disponíveis para mover
    cmpl %r8d, %eax             # Compara o número de elementos (%eax) com ‘n’ (%r8d)
    jl insufficient_elements    # Se %eax < %r8d, salta para falha (não há elementos suficientes)

    # Inicia a movimentação de ‘n’ elementos do buffer para o array
    movl (%rdx), %r10d          # Carrega o valor de *tail em %r10d (ponto inicial para leitura)
    movl $0, %ecx               # Zera %ecx para usar como contador do loop

move_loop:
    cmpl %r8d, %ecx             # Verifica se já foram movidos ‘n’ elementos
    jge move_done               # Se %ecx >= %r8d, termina o loop

    # Move buffer[tail] para array[i]
    movl (%rdi, %r10, 4), %eax  # Carrega buffer[tail] em %eax
    movl %eax, (%r9, %rcx, 4)   # Escreve %eax em array[i]

    # Incrementa e atualiza tail de forma circular
    incl %r10d                  # tail++
    cmpl %esi, %r10d            # Verifica se tail == length
    jb skip_reset_tail          # Se tail < length, pula a reinicialização
    movl $0, %r10d              # Reinicia tail para 0

skip_reset_tail:
    incl %ecx                   # Incrementa o contador (i++)
    jmp move_loop               # Repete o loop

move_done:
    # Atualiza *tail com o novo valor
    movl %r10d, (%rdx)          # Salva o novo valor de tail em *tail

    # Retorna sucesso (1)
    movl $1, %eax
    ret

insufficient_elements:
    # Retorna falha (0) se não houver elementos suficientes
    movl $0, %eax               # Define %eax como 0
    ret
