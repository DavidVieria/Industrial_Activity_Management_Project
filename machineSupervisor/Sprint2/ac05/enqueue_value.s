.section .text
 .global enqueue_value

 # Function: enqueue_value
 #
 # Description: adds a new value to a circular buffer
 #
 # Parameters:
 #   rdi = buffer array pointer (int*)
 #   esi = length of buffer (int)
 #   rdx = pointer to tail index (int*)
 #   rcx = pointer to head index (int*)
 #   r8d = value to insert (int)
 #
 # Returns:
 #   0 = Success, buffer not full
 #   1 = Success, buffer was full (overwrote oldest value)
 #   5 = Error, invalid input

 enqueue_value:
	# prologue
    pushq %rbp
    movq %rsp, %rbp

    # preserve registers
    pushq %r12
    pushq %r13

    # load and validate tail value
    movl (%rdx), %r12d

    cmpl $0, %r12d         # if tail < 0, return error code
    jl error

    cmpl %esi, %r12d       # if tail >= length, return error code
    jge error

    # load and validate head value
    movl (%rcx), %r13d

    cmpl $0, %r13d         # if head < 0, return error code
    jl error

    cmpl %esi, %r13d       # if head >= length, return error code
    jge error

    # store new value at head position
    movl %r8d, (%rdi, %r13, 4)

    # increment head
    incl %r13d

    # check if head reached length (head = length)
    cmpl %r13d, %esi
    jne check_full

    # reset head to 0 if it reached length
    xorl %r13d, %r13d

 check_full:
    # update head value in memory
    movl %r13d, (%rcx)

    # check if buffer is now full (tail = head)
    cmpl %r12d, %r13d
    jne not_full

    # buffer is now full, increment tail
    incl %r12d

    # check if tail reached length
    cmpl %esi, %r12d
    jne store_tail

    # reset tail to 0 if it reached length
    xorl %r12d, %r12d

 store_tail:
    movl %r12d, (%rdx)
	movl $1, %eax
	jmp done

 not_full:
    movl $0, %eax
    jmp done

 error:
	movl $5, %eax

 done:
    # restore preserved registers
    popq %r13
    popq %r12

    # epilogue
    movq %rbp, %rsp
    popq %rbp

    ret
