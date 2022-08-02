package generic;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.print.DocFlavor.STRING;

import generic.Operand.OperandType;

public class Simulator {

	static FileInputStream inputcodeStream = null;
	static String outputFile = null;

	private static void initialize(String objectProgramFile)
	{
		outputFile = objectProgramFile;
		return;
	}
	public static void setupSimulation(String assemblyProgramFile, String objectProgramFile) {
		initialize(objectProgramFile);
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}

	public static String rev(String str) {

		String rev = "";
		char c;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			rev = c + rev;
		}
		return rev;
	}

	private static int binaryToDec(String s) {
		// Bits from 31 to 0( total 32)
		int bits = 31;
		Integer ans = 0;
		if (s.charAt(0) == '1') {
			ans -= (2 << (bits -1));
		}
		for (int i = 1; i < bits; i++) {
			if (s.charAt(i) == '1') {
				ans += (2 << (bits - 1 - i));
			}
		}
		return ans;
	}

	private static String decToBinary(int n, int bits) {
		int temp = n;
		String res = "";
		for (int i = 0; i < bits; i++) {
			if ((temp & 1) == 0) {
				res = res + "0";
			} else {
				res = res + "1";
			}
			temp = temp >> 1;
		}
		String ans = rev(res);
		return ans;
	}

	private static Integer convert(Instruction instruct) {
		// System.out.println(instruct);
		String res = "";
		int ans = 0;
		int opcode = instruct.operationType.ordinal();
		res = res + decToBinary(opcode, 5);
		
		// R3 Type
		if (opcode <= 21 && ((opcode & 1) == 0)) {
			int s1 = instruct.getSourceOperand1().getValue();
			int s2 = instruct.getSourceOperand2().getValue();
			int d = instruct.getDestinationOperand().getValue();
			res = res + decToBinary(s1, 5);
			res = res + decToBinary(s2, 5);
			res = res + decToBinary(d, 5);
			res = res + decToBinary(0, 12);
		}
		// R2I Type (Arithmetic instructions)
		else if (opcode <= 21 && ((opcode & 1) != 0))
		{
			int s1 = instruct.getSourceOperand1().getValue();
			int s2 = instruct.getSourceOperand2().getValue();
			int d = instruct.getDestinationOperand().getValue();
			res = res + decToBinary(s1, 5);
			res = res + decToBinary(d, 5);
			res = res + decToBinary(s2, 17);
		}
		// R2I Type (Memory instructions)
		else if (opcode == 22 || opcode == 23)
		{
			int s1 = instruct.getSourceOperand1().getValue();
			String label = instruct.getSourceOperand2().getLabelValue();
			int s2 = ParsedProgram.symtab.get(label);
			int d = instruct.getDestinationOperand().getValue();
			res = res + decToBinary(s1, 5);
			res = res + decToBinary(d, 5);
			res = res + decToBinary(s2, 17);
		}
		// Jmp instruction (RI Type)
		else if (opcode == 24)
		{
			String label = instruct.getDestinationOperand().getLabelValue();
			int d = ParsedProgram.symtab.get(label);
			res = res + decToBinary(d, 5);
			res = res + decToBinary(0, 22);
		}
		// R2I Type (Control Flow instructions)
		else if (opcode >= 25 && opcode <= 28)
		{
			int s1 = instruct.getSourceOperand1().getValue();
			int s2 = instruct.getSourceOperand2().getValue();
			String label = instruct.getDestinationOperand().getLabelValue();
			int d = ParsedProgram.symtab.get(label);
			res = res + decToBinary(s1, 5);
			res = res + decToBinary(d, 5);
			res = res + decToBinary(s2, 17);	
		}
		// end (RI Type)
		else
		{
			res = res + decToBinary(0, 27);
		}
		ans = binaryToDec(res);
		return ans;
	}

	public static void assemble() {
		FileOutputStream f;
		DataOutputStream d;
		try {
			// Opening files
			f = new FileOutputStream(outputFile);
			d = new DataOutputStream(new BufferedOutputStream(f));
			try {
				d.writeInt(ParsedProgram.mainFunctionAddress);

				// Writing data
				for (int i = 0; i < (ParsedProgram.data).size(); i++){
					d.writeInt((ParsedProgram.data.get(i)));
				}

				for (int i = 0; i < ParsedProgram.code.size(); i++) {
					Instruction instruct = ParsedProgram.code.get(i);
					d.writeInt(convert(instruct));
				}

				d.flush();
				d.close();
				f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
