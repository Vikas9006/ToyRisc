package processor.pipeline;

import processor.Processor;
import processor.pipeline.cu.CU;
import generic.Simulator;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	EX_MA_LatchType EX_MA_Latch;

	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch,
			IF_EnableLatchType iF_EnableLatch, EX_MA_LatchType eX_MA_Latch) {
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.EX_MA_Latch = eX_MA_Latch;
	}

	public void performRW() {
		if (MA_RW_Latch.isRW_enable()) {
			if(EX_MA_Latch.get_is_MA_busy() == true)
			{
				return;
			}
			// TODO
			System.out.print("E ");
			// Get components from processor
			CU control_unit = containingProcessor.get_control_unit();
			int opcode = MA_RW_Latch.get_opcode();
			// for debugging
			if (opcode == -1) {
				System.out.println("nop");
			} else {

				control_unit.set_opcode(opcode);

				// get rd from latch
				int ldResult;
				int rd = MA_RW_Latch.get_rd();

				control_unit.print_instruction_type();

				// If it is load instruction then get ldResult
				if (control_unit.isLoad())
					ldResult = MA_RW_Latch.get_ldResult();
				// otherwise (isWb but not load) then get aluResult
				else
				{
					ldResult = MA_RW_Latch.get_aluResult();
				}

				// If it is write back instruction then store ldResult in rd register
				if (control_unit.isWb()) {
					containingProcessor.getRegisterFile().setValue(rd, ldResult);
					System.out.println("PC = " + (containingProcessor.getRegisterFile().getProgramCounter() - 1)
							+ " : %x" + rd + " = " + containingProcessor.getRegisterFile().getValue(rd));
				}

				// if instruction being processed is an end instruction, remember to call

				// End instruction is found, stop simulation
				if (control_unit.isEnd()) {
					Simulator.setSimulationComplete(true);
					containingProcessor.getRegisterFile().setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter() - 1);
					System.out.println(
							"PC = " + (containingProcessor.getRegisterFile().getProgramCounter() - 1) + " : End found");
					
				}
			}
			MA_RW_Latch.set_rd(-1);
			MA_RW_Latch.set_opcode(-1);
		}
	}

}
