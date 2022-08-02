package processor.pipeline;

public class IF_EnableLatchType {

	boolean IF_enable, isIF_busy;

	public IF_EnableLatchType() {
		IF_enable = true;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	public boolean get_isIF_busy() {
		return this.isIF_busy;
	}

	public void setIF_busy(boolean ifBusy) {
		this.isIF_busy = ifBusy;
	}
}
