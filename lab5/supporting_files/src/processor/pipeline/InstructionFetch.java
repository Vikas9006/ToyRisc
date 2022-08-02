package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import generic.Statistics;
import generic.Event.EventType;
import processor.Clock;
import processor.Processor;
import processor.pipeline.cu.CU;

public class InstructionFetch implements Element {

	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	EX_MA_LatchType EX_MA_Latch;
	int count_inst;

	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch,
			IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch, EX_MA_LatchType eX_MA_Latch) {
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.EX_MA_Latch =eX_MA_Latch;
		this.count_inst = 0;
	}

	public void performIF() {
		if (IF_EnableLatch.isIF_enable()) {
			if (IF_EnableLatch.get_isIF_busy()) {
				return;
			}
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
			Simulator.getEventQueue().addEvent(
					new MemoryReadEvent(
							Clock.getCurrentTime() + Configuration.mainMemoryLatency,
							this,
							containingProcessor.getMainMemory(),
							containingProcessor.getRegisterFile().getProgramCounter()));
			// Get word corresponding to PC value
			// int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			// control_unit.set_opcode(newInstruction >> 27);
			// control_unit.print_instruction_type();
			System.out.println("PC = " + currentPC);
			IF_EnableLatch.setIF_busy(true);
			// Set instruction in latch and update PC in processor
			// pass these values if nop is not to be sent
			if ((IF_OF_Latch.get_need_nop())) {
				stats.IncrementStalls();
				System.out.println("stalled if");
			} else {
				// IF_OF_Latch.setInstruction(newInstruction);
				containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);

			}
			// IF_EnableLatch.setIF_enable(false);
			// IF_OF_Latch.setOF_enable(true);
		}
	}

	@Override
	public void handleEvent(Event e) {
		if (IF_OF_Latch.isOF_busy() || (EX_MA_Latch.get_is_MA_busy())) {
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		} else if (e.getEventType() == EventType.MemoryResponse){
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			IF_OF_Latch.setInstruction(event.getValue());
			int opcode = (event.getValue()) >>> 27;
			System.out.println("opcode in IF handle = " + opcode);
			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.setIF_busy(false);
			// if (e.getEventTime() > Clock.getCurrentTime())  
			// {
				// Statistics.increment_numberOfInstructions();
				(this.count_inst)++;
				System.out.println("Number of inst = "+ this.count_inst);
			// }
		}
	}
}
