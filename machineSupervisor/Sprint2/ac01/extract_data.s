.section .text
.global extract_data

# %rdi = str / %rsi = token / %rdx = unit / %rcx = value

# Objective:
# This function aims to extract the value and unit from the string,
# based on a specified token (TEMP/HUM)

extract_data:

    # Initializations:
    movq %rbx, %r9
    movl $0, %eax
 movq %rdi, %rbx
    movq %rsi, %r8
    movb $0, (%rdx)
    movl $0, (%rcx)

comparing:

    movb (%rbx), %al

    # If end of string is reached:
    cmpb $0, %al
    je not_found

    # If position corresponds to the start of the string:
    cmpq %rbx, %rdi
    je verify_match

    # If not at the start, check if the previous character is '#' (mid-string):
    movb -1(%rbx), %al
    cmpb $'#', %al
    je verify_match

    # Move to the next character:
    incq %rbx
    jmp comparing

verify_match:
    # Reset the token:
    movq %rsi, %r8

next_token:

    movb (%r8), %al

    # Check if the end of the token is reached:
    cmpb $0, %al
    je check_separation

    # Compare token character with string character:
    # If not matching, go back to the initial comparison:
    cmpb (%rbx), %al
    jne comparing_next_token

    # Advance to the next character in the token:
    incq %r8
    incq %rbx
    jmp next_token

check_separation:
    # Check if the character after the token is '&':
    # If so, proceed to extract the unit:
    cmpb $'&', (%rbx)
    je unit

    # If not, the token is invalid, and continue with the next comparison:
    jmp comparing_next_token

comparing_next_token:

    # Return to the initial comparison:
    incq %rbx
    jmp comparing

unit:

    # Skip the "unit:" part:
    add $6, %rbx
    movq %rdx, %rdi

put_unit:
    movb (%rbx), %al

    # Check if the end of the unit is reached:
    cmpb $'&', %al
    je value

    # Continue copying the rest of the unit:
    movb %al, (%rdi)
    incq %rdi
    incq %rbx
    jmp put_unit

value:
    # Terminate the 'unit' string:
    movb $0, (%rdi)
    # Skip 'value:':
    add $7, %rbx

    movl $0, %eax
    movl $0, %edx

loop_put:
    movb (%rbx), %al

    # Check if the end of the value is reached ('#' or end of string):
    cmpb $'#', %al
    je end_value
    cmpb $0, %al
    je end_value

    # Convert character to digit:
    subb $'0', %al
    # Add digit to the accumulated value:
    imul $10, %edx
    addl %eax, %edx

 # Move to the next character:
    incq %rbx
    jmp loop_put

end_value:
    # Store the extracted value:
    movl %edx, (%rcx)

    # Return 1 (success):
    movl $1, %eax

    # Restore the original value of %rbx:
    movq %r9, %rbx
    jmp end

not_found:
    movq %r9, %rbx
    ret

end:
 ret