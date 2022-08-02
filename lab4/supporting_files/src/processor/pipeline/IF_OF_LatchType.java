package processor.pipeline;

public class IF_OF_LatchType {

	boolean OF_enable;
	int instruction;
	boolean need_nop;
	int opcode;

	public IF_OF_LatchType() {
		OF_enable = false;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public boolean get_need_nop() {
		return this.need_nop;
	}

	public void set_need_nop(boolean needNop) {
		this.need_nop = needNop;
	}
	public int get_opcode() {
		return this.opcode;
	}

	public void set_opcode(int code) {
		this.opcode = code;
	}
}
