package processor.pipeline;

import generic.Statistics;
import processor.Processor;
import processor.pipeline.cu.CU;

public class InstructionFetch {

	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;

	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch,
			IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch) {
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}

	public void performIF() {
		if (IF_EnableLatch.isIF_enable()) {
			System.out.print("A ");
			Statistics stats = containingProcessor.get_statistics();
			// Get program counter
			CU control_unit = containingProcessor.get_control_unit();
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();

			// If a branch is taken then we have to choose branch PC send nop in OF
			if (EX_IF_Latch.get_isBranchTaken()) {
				currentPC = EX_IF_Latch.get_branchPC();
				// EX_IF_Latch.set_branchPC(currentPC);
				containingProcessor.getRegisterFile().setProgramCounter(currentPC - 1);
				IF_OF_Latch.set_opcode(-1);
				EX_IF_Latch.set_isBranchTaken(false);
				return;
			}
			// Otherwise don't send OF
			else {
				IF_OF_Latch.set_opcode(30);
			}

			// Get word corresponding to PC value
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			control_unit.set_opcode(newInstruction >> 27);
			control_unit.print_instruction_type();
			System.out.println("PC = " + currentPC);
			// Set instruction in latch and update PC in processor
			// pass these values if nop is not to be sent
			if ((IF_OF_Latch.get_need_nop())) {
				stats.IncrementStalls();
				System.out.println("stalled if");
			} else {
				IF_OF_Latch.setInstruction(newInstruction);
				containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);

			}
			// IF_EnableLatch.setIF_enable(false);
			IF_OF_Latch.setOF_enable(true);
		}
	}
}
