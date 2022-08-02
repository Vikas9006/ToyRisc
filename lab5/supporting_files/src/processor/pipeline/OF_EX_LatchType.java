package processor.pipeline;

public class OF_EX_LatchType {

	boolean EX_enable, isEX_busy;
	int immx, op1, op2, rd, branch_target, opcode;
	boolean is_branch_taken, is_next_nop;

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

	public int get_opcode() {
		return this.opcode;
	}

	public void set_opcode(int code) {
		this.opcode = code;
	}

	public boolean get_is_branch_taken() 
	{
		return this.is_branch_taken;
	}

	public void set_is_branch_taken(boolean branchTake) {
		this.is_branch_taken = branchTake;
	}

	public boolean get_next_nop() {
		return this.is_next_nop;
	}

	public void set_is_next_nop(boolean isNextNop) {
		this.is_next_nop = isNextNop;
	}

	public boolean get_isEXbusy() {
        return this.isEX_busy;
    }

    public void setEX_busy(boolean ex_busy) {
		this.isEX_busy = ex_busy;
    }
}
