package processor.memorysystem;

import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import generic.Event.EventType;
import processor.Clock;
import processor.pipeline.MA_RW_LatchType;
import processor.pipeline.MemoryAccess;

public class MainMemory implements Element {
	int[] memory;
	MA_RW_LatchType MA_RW_Latch;

	public MainMemory() {
		memory = new int[65536];
	}

	public int getWord(int address) {
		return memory[address];
	}

	public void setWord(int address, int value) {
		memory[address] = value;
	}

	public String getContentsAsString(int startingAddress, int endingAddress) {
		if (startingAddress == endingAddress)
			return "";

		StringBuilder sb = new StringBuilder();
		sb.append("\nMain Memory Contents:\n\n");
		for (int i = startingAddress; i <= endingAddress; i++) {
			sb.append(i + "\t\t: " + memory[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public void handleEvent(Event e) {
		if (e.getEventType() == EventType.MemoryRead) {
			MemoryReadEvent event = (MemoryReadEvent) e;
			System.out.println("address = " + event.getAddressToReadFrom());
			System.out.println("word = "+ getWord(event.getAddressToReadFrom()));
			Simulator.getEventQueue().addEvent(
					new MemoryResponseEvent(
							Clock.getCurrentTime(),
							this,
							event.getRequestingElement(),
							getWord(event.getAddressToReadFrom())));
		}
		else if(e.getEventType()==EventType.MemoryWrite)
		{
			MemoryWriteEvent event = (MemoryWriteEvent) e;
			int address = event.getAddressToWriteTo();
			int value = event.getValue();
			// int opcode = (event.getValue()) >>> 27;
			// System.out.println("opcode in IF handle = " + opcode);
			// containingProcessor.getMainMemory().setWord(address, value);
			memory[address] = value;
			// MA_RW_Latch.set_ldResult(ldresult);
			MemoryAccess memory_Access = (MemoryAccess) event.getRequestingElement();
			memory_Access.MA_RW_Latch.setRW_enable(true);
			memory_Access.EX_MA_Latch.set_is_MA_busy(false);
			MA_RW_Latch.setRW_enable(true);
		}
	}
	
}
