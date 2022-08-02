	.data
n:
	10
	.text
main:
	load %x0, $n, %x3
	addi %x0, 1, %x11
	addi %x0, 65535, %x4
	subi %x4, 1, %x6
	store %x0, 0, %x4
	beq %x3, %x11, complete
	store %x11, 0, %x6
	addi %x0, 2, %x5
loop:
	beq %x5, %x3, complete
	load %x4, 0, %x7
	load %x6, 0, %x8
	subi %x6, 1, %x10
	add %x7, %x8, %x9
	store %x9, 0, %x10
	addi %x5, 1, %x5
	subi %x6, 1, %x6
	subi %x4, 1, %x4
	jmp loop
complete:
	end
