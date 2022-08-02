package processor.pipeline;

public class EX_MA_LatchType {
	
	boolean MA_enable,MA_Value;
	int alu_result, rd, opcode;
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	// setting up rd value
	public void set_rd(int rd_val) {
		rd = rd_val;
	}
	
	// returning it
	public int get_rd(){
		return rd;
	}

	public int get_AluResult() {
		return this.alu_result;
	}

	public void set_AluResult(int res) {
		this.alu_result = res;
	}

	public int get_opcode() {
		return this.opcode;
	}

	public void set_opcode(int code) {
		this.opcode = code;
	}

    public boolean get_is_MA_busy() {
        return this.MA_Value;
    }

	public void set_is_MA_busy (boolean MA_Given) {
    	MA_Value = MA_Given;
    }
}
