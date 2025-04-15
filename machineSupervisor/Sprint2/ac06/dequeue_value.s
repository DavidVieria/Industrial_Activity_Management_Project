.section .text
 .global dequeue_value

 # Function: dequeue_value
 #
 # Description: removes the oldest element from the buffer and outputs it in the value pointer
 #
 # Parameters:
 #   rdi = buffer array pointer (int*)
 #   esi = length of buffer (int)
 #   rdx = pointer to tail index (int*)
 #   rcx = pointer to head index (int*)
 #   r8 = pointer to store retrieved value (int*)
 #
 # Returns:
 #   0 = Failure (buffer empty)
 #   1 = Success
 #   5 = Error, invalid input

 dequeue_value:
	# prologue
    pushq %rbp
    movq %rsp, %rbp

    # preserve registers
    pushq %r12
    pushq %r13

    # validate buffer length
    cmpl $0, %esi          # if buffer length is 0, return error code
    jle error

    # load and validate tail value
    movl (%rdx), %r12d     # r12d = tail

    cmpl $0, %r12d         # if tail < 0, return error code
    jl error

    cmpl %esi, %r12d       # if tail >= length, return error code
    jge error

    # load and validate head value
    movl (%rcx), %r13d     # r13d = head

    cmpl $0, %r13d         # if head < 0, return error code
    jl error

    cmpl %esi, %r13d       # if head >= length, return error code
    jge error

    # check if buffer is empty (tail == head)
    cmpl %r13d, %r12d
    je empty

    # read value at tail position
    movl (%rdi, %r12, 4), %eax
    movl %eax, (%r8)       # store value at destination

    # increment tail
    incl %r12d

    # check if tail reached length (tail = length)
    cmpl %r12d, %esi
    jne store_tail

    # reset tail to 0 if it reached length
    xorl %r12d, %r12d

 store_tail:
    movl %r12d, (%rdx)

    movl $1, %eax       # return success
    jmp done

 empty:
    movl $0, %eax       # return failure
    jmp done

 error:
    movl $5, %eax       # return unexpected error

 done:
    # restore preserved registers
    popq %r13
    popq %r12

    # epilogue
    movq %rbp, %rsp
    popq %rbp

    ret
