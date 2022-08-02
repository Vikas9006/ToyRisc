package generic;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import generic.EventQueue;

import processor.Clock;
import processor.Processor;

public class Simulator {

	static Processor processor;
	static boolean simulationComplete;
	static int i;
	static EventQueue eventQueue;

	public static void setupSimulation(String assemblyProgramFile, Processor p) {
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		eventQueue = new EventQueue();
		simulationComplete = false;
	}

	public static EventQueue getEventQueue ()
	{
		return eventQueue ;
	}

	static void loadProgram(String assemblyProgramFile) {
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 * in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 * x0 = 0
		 * x1 = 65535
		 * x2 = 65535
		 */

		/*
		 * 1. Header (address of the first instruction)
		 * 2. Data (static data)
		 * 3. Text (encoded instructions)
		 */

		// Step 1 - load the program into memory
		FileInputStream f;
		DataInputStream d;
		int mainAddr = 0;

		try {
			// create input stream
			f = new FileInputStream(assemblyProgramFile);
			d = new DataInputStream(new BufferedInputStream(f));
			try {
				// Read main address
				mainAddr = d.readInt();
				int data = 0;

				// Eof => End of file
				boolean isEof = false;
				while (!isEof) {
					try {
						// Read a value and store it in memory
						data = d.readInt();
						processor.getMainMemory().setWord(i, data);
						System.out.println(String.format("%2X", data));
						i++;
					}
					// If cursor is at the end of the file the set isEof to true
					catch (EOFException e) {
						// handle exception
						System.out.println("Cursor reached at the end of the file");
						isEof = true;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Step 2 - set PC to the address of the first instruction in the main
		processor.getRegisterFile().setProgramCounter(mainAddr);

		// Step 3 - set the registers x0, x1, x2
		processor.getRegisterFile().setValue(0, 0);
		processor.getRegisterFile().setValue(1, 65535);
		processor.getRegisterFile().setValue(2, 65535);
	}

	public static void simulate() {
		i = 1;
		Statistics stat = processor.get_statistics();
		while (simulationComplete == false) {
			System.out.println();
			Clock.incrementClock();
			// if ((i % 2) == 0)
				// continue;
			System.out.println("Cycle " + i);
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			eventQueue.processEvents();
			processor.getOFUnit().performOF();
			processor.getIFUnit().performIF();
			// Increment i in each cycle
			i++;
			// if (i == 100)
				// break;
		}

		// TODO
		// set statistics
		// Both values will be same because we are processing one instruction per cycle
		stat.setNumberOfCycles(i - 1);
		stat.setNumberOfInstructions(i - 1);
		
	}

	public static void setSimulationComplete(boolean value) {
		simulationComplete = value;
	}
}
