	.data
a:
	11
	.text
main:
	load %x0, $a, %x4
	blt %x4, 1, divided
	div %x4, %x4, %x5
	divi %x4, 2, %x11
	addi %x5, 1, %x5
loop:
	bgt %x5, %x11, endll
	div %x4, %x5, %x6
	mul %x6, %x5, %x7
	beq %x4, %x7, divided
	addi %x5, 1, %x5
	jmp loop
divided:
	subi %x0, 1, %x10
	end
endll:
	addi %x0, 1, %x10
	end
