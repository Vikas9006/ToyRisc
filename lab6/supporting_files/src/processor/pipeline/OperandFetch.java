package processor.pipeline;

import processor.pipeline.cu.CU;
import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_Enable_Latch;
	boolean is_end_found;

	int[][] r3_nop;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch,
			EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType if_enable_latch) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_Enable_Latch = if_enable_latch;
	}

	boolean isConflict(int opcode, int rs) {
		// If end is found there can't be any conflict further
		if (this.is_end_found) 
		{
			return false;
		}
		// Otherwise get rd values from latches
		int ma_rd = EX_MA_Latch.get_rd();
		int rw_rd = MA_RW_Latch.get_rd();
		// If any rd values gets matched then there is a conflict of data
		if ((EX_MA_Latch.isMA_enable() && rs == ma_rd) || (MA_RW_Latch.isRW_enable() && rs == rw_rd)) {
			return true;
		}
		return false;
	}

	public void performOF() {
		if (IF_OF_Latch.isOF_enable()) {
			if (IF_Enable_Latch.get_isIF_busy())
			{
				return;
			}
			// Get control unit from processor
			System.out.print("B ");
			CU control_unit = containingProcessor.get_control_unit();
			// If nop is coming from IF send nop in EX
			if (IF_OF_Latch.get_opcode() == -1) {
				System.out.println("nop");
				OF_EX_Latch.set_opcode(-1);
				OF_EX_Latch.set_rd(-1);
				
				OF_EX_Latch.setEX_enable(true);
				return;
			}
			if (OF_EX_Latch.get_is_branch_taken()) {
				System.out.println("nop");
				OF_EX_Latch.set_opcode(-1);
				OF_EX_Latch.set_rd(-1);
				OF_EX_Latch.set_is_branch_taken(false);
				OF_EX_Latch.setEX_enable(true);	
				return;
			}
			// Get instruction from latch
			int inst = IF_OF_Latch.getInstruction();

			int opcode = 0, rs1 = 0, rs2 = 0, rd = 0, immediate = 0, op1, op2;

			// Extract opcode of instruction
			opcode = (1 << 5) - 1;
			opcode = (inst >> 27) & opcode;

			// Set opcode in control unit
			control_unit.set_opcode(opcode);
			control_unit.print_instruction_type();
			IF_OF_Latch.set_opcode(-1);
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
				
				boolean conflict = isConflict(opcode, rs1) || isConflict(opcode, rs2);
				// Check for conflict for rs1, if conflict send nop
				System.out.println("isConfilct = " + conflict);
				if (conflict) {
					IF_OF_Latch.set_need_nop(true);
					OF_EX_Latch.set_opcode(-1);
					OF_EX_Latch.set_rd(-1);
				} else {
					IF_OF_Latch.set_need_nop(false);
					OF_EX_Latch.set_opcode(opcode);
					System.out.println("in OF opcode = " + opcode);

					op1 = containingProcessor.getRegisterFile().getValue(rs1);
					op2 = containingProcessor.getRegisterFile().getValue(rs2);

					// Set these values in next latch
					OF_EX_Latch.set_op1(op1);
					OF_EX_Latch.set_op2(op2);
					OF_EX_Latch.set_rd(rd);
					
				}
			}

			// If instruction is R2I type
			else if (control_unit.isR2I_type()) {
				// Extract values of rs1, rd, immediate
				// setting rs1 , rd and immediate
				if (OF_EX_Latch.get_is_branch_taken()) {
					OF_EX_Latch.set_opcode(-1);
					OF_EX_Latch.set_rd(-1);
					OF_EX_Latch.set_is_branch_taken(false);
				}
				rs1 = (1 << 5) - 1;
				rs1 = (inst >> 22) & rs1;

				rd = (1 << 5) - 1;
				rd = (inst >> 17) & rd;

				immediate = (1 << 17) - 1;
				immediate = inst & immediate;

				// Handling of case if immediate is negative
				if (immediate >= (1 << 16)) {
					immediate -= (1 << 17);
				}

				boolean conflict = isConflict(opcode, rs1);
				if (control_unit.isBranch()) {
					conflict = conflict || isConflict(opcode, rd);
				}
				// Check for conflict of rs1 and rs2
				System.out.println("isConfilct = " + conflict);
				if (conflict) {
					IF_OF_Latch.set_need_nop(true);
					OF_EX_Latch.set_opcode(-1);
					OF_EX_Latch.set_rd(-1);
				} else {
					IF_OF_Latch.set_need_nop(false);
					OF_EX_Latch.set_opcode(opcode);
					System.out.println("in OF opcode = " + opcode);

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
				// End found
				if(control_unit.isEnd())
				{
					this.is_end_found=true;
				}

				// if it is negative
				// Handling of case if immediate is negative
				if (immediate >= (1 << 21))
					immediate -= (1 << 22);

				// Set these in next latches
				OF_EX_Latch.set_rd(rd);
				OF_EX_Latch.set_immx(immediate);
				OF_EX_Latch.set_opcode(opcode);
			}
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
