	.data
a:
	10
b:
	20
	.text
main:
	load %x1, $a, %x4
	addi %x4, 2, %x4
	add %x4, 0, %x4
	jmp endl
endl:
	end
