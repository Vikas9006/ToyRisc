	.data
a:
	1
d:
	3
n:
	7
	.text
main:
	load %x0, $a, %x3
	load %x0, $d, %x4
	load %x0, $n, %x5
	addi %x0, 65535, %x6
	addi %x0, 1, %x10
loop:
	bgt %x10, %x5, complete
	subi %x10, 1, %x7
	mul %x7, %x4, %x8
	add %x3, %x8, %x9
	store %x9, 0, %x6
	addi %x10, 1, %x10
	subi %x6, 1, %x6
	jmp loop
complete:
	end