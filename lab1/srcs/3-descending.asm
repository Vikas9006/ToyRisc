.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
	load %x0, $n, %x3
	addi %x0, 7, %x17
loop1:
	subi %x17, 0, %x5
	add %x0, %x0, %x7
	blt %x4, %x3, loop2
	jmp complete
loop2:
	addi %x7, 1, %x8
	blt %x7, %x5, compare
	addi %x4, 1, %x4
	jmp loop1
compare:
	load %x7, $a, %x9
	load %x8, $a, %x10
	blt %x9, %x10, swap
	addi %x7, 1, %x7
	jmp loop2
swap:
	store %x9, $a, %x8
	store %x10, $a, %x7
	addi %x7, 1, %x7
	jmp loop2
complete:
	end
