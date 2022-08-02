.data
count:
	0
	0
	0
	0
	0
	0
	0
	0
  	0
  	0
  	0
marks:
	2
	3
  	0
  	5
  	10
  	7
  	1
  	10
  	10
  	8
  	9
  	6
  	7
  	8
  	2
  	4
  	5
  	0
  	9
  	1
n:
  	20
	.text
main:
	load %x0, $n, %x3
loop:
	beq %x4, %x3, complete
	load %x4, $marks, %x5
	load %x5, $count, %x6
	addi %x6, 1, %x6
	store %x6, $count, %x5
	addi %x4, 1, %x4
	jmp loop
complete:
	end
