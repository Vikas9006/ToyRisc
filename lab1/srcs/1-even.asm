	.data
n:
	5
l:
	2
	-1
	7
	5
	4
	.text
main:
	add %x0, %x0, %x3
	load %x0, $n, %x6
	add %x0, %x0, %x5
	addi %x11, 1, %x11
loop:
	beq %x5, %x6, endl
	load %x5, $l, %x4
	addi %x5, 1, %x5
	bgt %x4, 0, success1
	beq %x4, 0, success1
	jmp loop
success1:
	and %x4, %x11 ,%x7
	beq  %x7, 0, success2
	jmp loop
success2:
	addi %x3, 1, %x3
	jmp loop
endl:
	addi %x3, 0, %x10
	end