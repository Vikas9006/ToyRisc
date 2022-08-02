package processor.pipeline;

import processor.pipeline.cu.CU;
import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}

	public void performOF() {
		if (IF_OF_Latch.isOF_enable()) {
			// Get control unit from processor
			CU control_unit = containingProcessor.get_control_unit();

			// Get instruction from latch
			int inst = IF_OF_Latch.getInstruction();

			int opcode = 0, rs1 = 0, rs2 = 0, rd = 0, immediate = 0, op1, op2;
			
			// Extract opcode of instruction
			opcode = (1 << 5) - 1;
			opcode = (inst >> 27) & opcode;

			// Set opcode in control unit
			control_unit.set_opcode(opcode);

			// Handling extraction of operands casewise
			// If instruction is R3 type
			if (control_unit.isR3_type()) {
				// Extracting rs1, rs2 and rd

				// Bitwise operations to get operands
				rs1 = (1 << 5) - 1;
				rs1 = (inst >> 22) & rs1;

				rs2 = (1 << 5) - 1;
				rs2 = (inst >> 17) & rs2;
				
				rd = (1 << 5) - 1;
				rd = (inst >> 12) & rd;
				
				// Get their value from register file
				op1 = containingProcessor.getRegisterFile().getValue(rs1);
				op2 = containingProcessor.getRegisterFile().getValue(rs2);

				// Set these values in next latch
				OF_EX_Latch.set_op1(op1);
				OF_EX_Latch.set_op2(op2);
				OF_EX_Latch.set_rd(rd);
			}

			// If instruction is R2I type
			else if (control_unit.isR2I_type()) {
				// Extract values of rs1, rd, immediate
				// setting rs1 , rd and immediate
				rs1 = (1 << 5) - 1;
				rs1 = (inst >> 22) & rs1;
				
				rd = (1 << 5) - 1;
				rd = (inst >> 17) & rd;
				
				immediate = (1 << 17) - 1;
				immediate = inst & immediate;
				
				// Handling of case if immediate is negative
				if (immediate >= (1 << 16))
					immediate -= (1 << 17);

				// set value of rs1 register into next latch irrespective of instruction type
				op1 = containingProcessor.getRegisterFile().getValue(rs1);
				OF_EX_Latch.set_op1(op1);

				// If it is branch instruction then pass value of register rd otherwise pass rd
				if (control_unit.isBranch())
					OF_EX_Latch.set_op2(containingProcessor.getRegisterFile().getValue(rd));
				else
					OF_EX_Latch.set_op2(immediate);

				// if it is write back instruction then pass rd value
				// otherwise pass its value in register
				if (control_unit.isWb())
					OF_EX_Latch.set_rd(rd);
				else
					OF_EX_Latch.set_rd(containingProcessor.getRegisterFile().getValue(rd));

				// Set immediate
				OF_EX_Latch.set_immx(immediate);
			}

			// If instruction is R2I type
			if (control_unit.isRI_type()) {
				// Extraction of rd and immediate

				// convert these into integer
				// setting rd and immediate
				rd = (1 << 5) - 1;
				rd = (inst >> 22) & rd;
				immediate = (1 << 22) - 1;
				immediate = inst & immediate;

				// if it is negative
				// Handling of case if immediate is negative
				if (immediate >= (1 << 21))
					immediate -= (1 << 22);

				// Set these in next latches
				OF_EX_Latch.set_rd(rd);
				OF_EX_Latch.set_immx(immediate);
			}
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
