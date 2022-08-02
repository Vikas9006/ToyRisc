package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.EventQueue;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import generic.Event.EventType;
import processor.Clock;
import processor.Processor;
import processor.pipeline.alu.ALU;
import processor.pipeline.cu.CU;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	public EX_MA_LatchType EX_MA_Latch;
	public MA_RW_LatchType MA_RW_Latch;

	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch) {
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}

	public void performMA() {
		// TODO
		if (EX_MA_Latch.get_is_MA_busy()) {
			return;
		}
		if (EX_MA_Latch.isMA_enable()) {
			System.out.print("D ");

			// getting variables from defined functions in other files
			CU controlUnit = containingProcessor.get_control_unit();
			int opcode = EX_MA_Latch.get_opcode();
			controlUnit.set_opcode(opcode);
			
			// for debugging
			if (opcode == -1) {
				System.out.println("nop");
				MA_RW_Latch.set_opcode(-1);
				MA_RW_Latch.set_rd(-1);
			} else {
				MA_RW_Latch.set_opcode(opcode);
				controlUnit.print_instruction_type();
				ALU Alu = containingProcessor.get_arithmatic_logic_unit();
				int rd = EX_MA_Latch.get_rd();
				MA_RW_Latch.set_rd(rd);

				int aluResult = EX_MA_Latch.get_AluResult();
				MA_RW_Latch.set_AluResult(aluResult);
				// if instruction is of load
				if (controlUnit.isLoad()) {
					System.out.println("MA address");
					// set ld result and take it in MA_RW latch
					// int ldResult = containingProcessor.getMainMemory().getWord(aluResult);
					Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
							Clock.getCurrentTime() + containingProcessor.get_l1d_cache().get_latency(),
							this,
							containingProcessor.get_l1d_cache(),aluResult
							)
					);
					EX_MA_Latch.set_is_MA_busy(true);
				}
				
				// if instruction is store
				else if (controlUnit.isStore()) {
					// take rs1, immediate , target
					int rs1 = Alu.get_operand1();
					int imm = Alu.get_operand2();
					int target = rd + imm;

					// set rs1 in targer address
					// containingProcessor.getMainMemory().setWord(target, rs1);
					System.out.println(containingProcessor.getMainMemory().getWord(rd + imm)
					+ " Value stored in memory [" + target + "]");
					Simulator.getEventQueue().addEvent(
						new MemoryWriteEvent(
							Clock.getCurrentTime() + containingProcessor.get_l1d_cache().get_latency(),
							this,
							containingProcessor.get_l1d_cache(),
							target,rs1
							)
					);
				}
			}
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.set_rd(-1);
			EX_MA_Latch.set_opcode(-1);
		}
	}

	@Override
	public void handleEvent(Event e) {
		if(e.getEventType()==EventType.MemoryResponse)
		{
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			int ldresult = event.getValue();
			int opcode = (event.getValue()) >>> 27;
			System.out.println("opcode in IF handle = " + opcode);
			MA_RW_Latch.set_ldResult(ldresult);
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.set_is_MA_busy(false);
		}
		
		
	}
}
