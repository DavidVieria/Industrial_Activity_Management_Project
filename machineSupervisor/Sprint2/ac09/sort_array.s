.section .text
 .global sort_array

 # Function: sort_array
 #
 # Description: sorts the array in ascending or descending order
 #
 # Parameters:
 #   rdi = array pointer (int*)
 #   rsi = length of array (int)
 #   dl = order flag (0 = descending, 1 = ascending)
 #
 # Returns:
 #   0 = Failure (empty array)
 #   1 = Success
 #   5 = Error (unexpected error)

 sort_array:
    # prologue
    pushq %rbp
    movq %rsp, %rbp

    # preserve registers
    pushq %rcx
    pushq %r12
    pushq %r13
    pushq %r14
    pushq %r15

    # validate array length
    cmpq $0, %rsi          # if length = 0, array is empty
    je array_empty

    cmpq $0, %rsi          # if length < 0, unexpected error
    jl unexpected_error

    # initialize outer loop counter
    movq $0, %r12          # r12 = i
    decq %rsi              # length - 1 for bounds

 outer_loop:
    cmpq %rsi, %r12
    jge sort_success       # if i >= length - 1, sorting complete

    movq $0, %r13          # r13 = j (inner loop counter)

 inner_loop:
    movq %rsi, %r14
    subq %r12, %r14        # r14 = length - 1 - i (inner loop bound)

    cmpq %r14, %r13
    jge next_outer         # if j >= length -1 -i, next outer iteration

    # load elements to compare
    movq %r13, %r14
    incq %r14              # j + 1

    movl (%rdi,%r13,4), %ecx     # current element
    movl (%rdi,%r14,4), %r15d    # next element

    # check sort order
    cmpb $0, %dl           # check order flag
    je descend

 ascend:
    cmpl %r15d, %ecx       # if current > next
    jg swap
    jmp continue

 descend:
    cmpl %r15d, %ecx       # if current < next
    jl swap                # swap if current < next (for descending)
    jmp continue

 swap:
    # swap elements
    movl %ecx, (%rdi,%r14,4)
    movl %r15d, (%rdi,%r13,4)

 continue:
    incq %r13              # j++
    jmp inner_loop

 next_outer:
    incq %r12              # i++
    jmp outer_loop

 sort_success:
    movl $1, %eax          # return success (1)
    jmp done

 array_empty:
    movl $0, %eax          # return failure (0)
    jmp done

 unexpected_error:
    movl $5, %eax          # return error code (5)

 done:
    # restore preserved registers
    popq %r15
    popq %r14
    popq %r13
    popq %r12
    popq %rcx

    # epilogue
    movq %rbp, %rsp
    popq %rbp

    ret
