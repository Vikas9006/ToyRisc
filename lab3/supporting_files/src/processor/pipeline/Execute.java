package processor.pipeline;

import processor.Processor;
import processor.pipeline.alu.ALU;
import processor.pipeline.cu.CU;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch,
			EX_IF_LatchType eX_IF_Latch) {
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}

	public void performEX() {
		if (OF_EX_Latch.isEX_enable()) {
			// Getting components from processor
			CU controlUnit = containingProcessor.get_control_unit();
			ALU arithmatic_logic_unit = containingProcessor.get_arithmatic_logic_unit();

			// Getting values from previous parts
			int op1 = OF_EX_Latch.get_op1();
			int op2 = OF_EX_Latch.get_op2();
			int rd = OF_EX_Latch.get_rd();
			int immx = OF_EX_Latch.get_immx();

			// set values in ALU and get result
			arithmatic_logic_unit.set_operand1(op1);
			arithmatic_logic_unit.set_operand2(op2);
			arithmatic_logic_unit.calc_result();
			int aluRes = arithmatic_logic_unit.get_aluResult();

			// Set values in linked latches
			EX_MA_Latch.set_rd(rd);
			EX_MA_Latch.set_AluResult(aluRes);
			EX_IF_Latch.set_isBranchTaken(false);
			System.out.println("Operand1 = " + op1 + ", Operand2 = " + op2);

			// Branch unit part
			if (controlUnit.isBranch()) {
				int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
				boolean flagVal = arithmatic_logic_unit.get_flag();
				
				// if instruction is jump
				if (controlUnit.isJmp()) {
					// setting values in ex_if latch 
					// updating program counter
					EX_IF_Latch.set_branchPC(currentPC + rd + immx - 1);
					EX_IF_Latch.set_isBranchTaken(true);
					System.out.println("PC = " + (containingProcessor.getRegisterFile().getProgramCounter() - 1)+ " : Jumped");
					// if instruction is for qual not equal lesser than greater than
				} else if ((controlUnit.isBeq() || controlUnit.isBne() || controlUnit.isBlt() || controlUnit.isBgt())
						&& flagVal) {
					// updating program counter and setting values in ex_if latch
					EX_IF_Latch.set_branchPC(currentPC + immx - 1);
					EX_IF_Latch.set_isBranchTaken(true);
					System.out.println("PC = " + (containingProcessor.getRegisterFile().getProgramCounter() - 1)+ " : Branch taken");
					
				} else {
					// otherwise we don't take any branch
					System.out.println("PC = " + (containingProcessor.getRegisterFile().getProgramCounter() - 1)+ " : Branch not taken");
					EX_IF_Latch.set_isBranchTaken(false);
					EX_IF_Latch.set_branchPC(containingProcessor.getRegisterFile().getProgramCounter());
				}
			}
			EX_MA_Latch.setMA_enable(true);
			OF_EX_Latch.setEX_enable(false);
		}

	}

}
