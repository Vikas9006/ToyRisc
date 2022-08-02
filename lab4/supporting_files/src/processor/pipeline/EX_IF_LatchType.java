package processor.pipeline;

public class EX_IF_LatchType {
	boolean is_branch_taken, is_next_nop;
	int branch_pc;

	public EX_IF_LatchType()
	{
		// default value 
		is_branch_taken = false;
	}
	
	public boolean get_isBranchTaken() {
		// returns if branch is taken or not 
		return is_branch_taken;
	}

	public void set_isBranchTaken(boolean val) {
		// sets the value for example in jump  case
		is_branch_taken = val;
	}

	public int get_branchPC() {
		// return program counter in integer
		return branch_pc;
	}

	public void set_branchPC(int branch_val) {
		// sets program counter
		branch_pc = branch_val;
	}

	public boolean get_next_nop() {
		return this.is_next_nop;
	}

	public void set_is_next_nop(boolean isNextNop) {
		this.is_next_nop = isNextNop;
	}
}
