package processor.pipeline;

import processor.Processor;

public class InstructionFetch {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable())
		{
			// Get program counter
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();

			// If a branch is taken then we have to choose branch PC
			if (EX_IF_Latch.get_isBranchTaken()) {
				currentPC = EX_IF_Latch.get_branchPC();
			}

			// Get word corresponding to PC value
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);

			// Set instruction in latch and update PC in processor
			IF_OF_Latch.setInstruction(newInstruction);
			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
			
			IF_EnableLatch.setIF_enable(false);
			IF_OF_Latch.setOF_enable(true);
		}
	}

}
