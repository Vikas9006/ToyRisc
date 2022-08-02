package processor.pipeline;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	int ldResult, rd;
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
	}

	public int get_rd(){
		return this.rd;
	}

	public void set_rd(int rd_val){
		this.rd = rd_val;
	}

	public int get_ldResult(){
		return this.ldResult;
	}

	public void set_ldResult(int res){
		this.ldResult = res;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

}
