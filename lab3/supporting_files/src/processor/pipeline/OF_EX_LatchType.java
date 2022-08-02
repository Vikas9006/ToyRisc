package processor.pipeline;

public class OF_EX_LatchType {

	boolean EX_enable;
	int immx, op1, op2, rd, branch_target;

	public OF_EX_LatchType() {
		EX_enable = false;
	}

	public int get_rd() {
		// gets rd value
		return this.rd;
	}

	public void set_rd(int rdVal) {
		// sets rd value from operandfetch
		this.rd = rdVal;
	}

	public int get_branchTarget() {
		// gets the branch target where to go
		return this.branch_target;
	}

	public void set_brachTarget(int Branch_Target) {
		// update branch target
		this.branch_target = Branch_Target;
	}
	// get and set ex enable
	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}
	// get and set immediate value
	public int get_immx() {
		return immx;
	}

	public void set_immx(int immVal) {
		this.immx = immVal;
	}
	// get and set operand 1
	public int get_op1() {
		return this.op1;
	}

	public void set_op1(int op1Val) {
		this.op1 = op1Val;
	}
	// get and set operand 2
	public int get_op2() {
		return this.op2;
	}

	public void set_op2(int op2Val) {
		this.op2 = op2Val;
	}
}
