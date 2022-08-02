package processor;

import generic.Statistics;
import processor.memorysystem.MainMemory;
import processor.pipeline.EX_IF_LatchType;
import processor.pipeline.EX_MA_LatchType;
import processor.pipeline.Execute;
import processor.pipeline.IF_EnableLatchType;
import processor.pipeline.IF_OF_LatchType;
import processor.pipeline.InstructionFetch;
import processor.pipeline.MA_RW_LatchType;
import processor.pipeline.MemoryAccess;
import processor.pipeline.OF_EX_LatchType;
import processor.pipeline.OperandFetch;
import processor.pipeline.RegisterFile;
import processor.pipeline.RegisterWrite;
import processor.pipeline.alu.ALU;
import processor.pipeline.cu.CU;

public class Processor {

	RegisterFile registerFile;
	MainMemory mainMemory;

	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	MA_RW_LatchType MA_RW_Latch;

	InstructionFetch IFUnit;
	OperandFetch OFUnit;
	Execute EXUnit;
	MemoryAccess MAUnit;
	RegisterWrite RWUnit;

	Statistics statistics;

	// Added part
	CU ctrlunit;
	ALU arithmatic_logic_unit;

	public Processor() {
		registerFile = new RegisterFile();
		mainMemory = new MainMemory();

		IF_EnableLatch = new IF_EnableLatchType();
		IF_OF_Latch = new IF_OF_LatchType();
		OF_EX_Latch = new OF_EX_LatchType();
		EX_MA_Latch = new EX_MA_LatchType();
		EX_IF_Latch = new EX_IF_LatchType();
		MA_RW_Latch = new MA_RW_LatchType();

		IFUnit = new InstructionFetch(this, IF_EnableLatch, IF_OF_Latch, EX_IF_Latch);
		OFUnit = new OperandFetch(this, IF_OF_Latch, OF_EX_Latch, EX_MA_Latch, MA_RW_Latch);
		EXUnit = new Execute(this, OF_EX_Latch, EX_MA_Latch, EX_IF_Latch);
		MAUnit = new MemoryAccess(this, EX_MA_Latch, MA_RW_Latch);
		RWUnit = new RegisterWrite(this, MA_RW_Latch, IF_EnableLatch);

		// Set to 0 (add opcode)
		ctrlunit = new CU(0);
		arithmatic_logic_unit = new ALU(this);
		arithmatic_logic_unit.set_control_unit(ctrlunit);

		statistics = new Statistics();
	}

	public void printState(int memoryStartingAddress, int memoryEndingAddress) {
		System.out.println(registerFile.getContentsAsString());

		System.out.println(mainMemory.getContentsAsString(memoryStartingAddress, memoryEndingAddress));
	}

	public CU get_control_unit() {
		return this.ctrlunit;
	}

	public ALU get_arithmatic_logic_unit() {
		return this.arithmatic_logic_unit;
	}

	public RegisterFile getRegisterFile() {
		return registerFile;
	}

	public void setRegisterFile(RegisterFile registerFile) {
		this.registerFile = registerFile;
	}

	public MainMemory getMainMemory() {
		return mainMemory;
	}

	public void setMainMemory(MainMemory mainMemory) {
		this.mainMemory = mainMemory;
	}

	public InstructionFetch getIFUnit() {
		return IFUnit;
	}

	public OperandFetch getOFUnit() {
		return OFUnit;
	}

	public Execute getEXUnit() {
		return EXUnit;
	}

	public MemoryAccess getMAUnit() {
		return MAUnit;
	}

	public RegisterWrite getRWUnit() {
		return RWUnit;
	}

	public Statistics get_statistics() {
		return statistics;
	}
}
