package processor.pipeline.alu;

import processor.Processor;
import processor.pipeline.cu.CU;

public class ALU {
    int operand1, operand2, aluResult;
    CU ctrl_unit;
    boolean flag;
    Processor containingProcessor;

    public ALU(Processor containingProcessor) {
        this.containingProcessor = containingProcessor;
        operand1 = 0;
        operand2 = 0;
        flag = false;
        aluResult = 0;
    }

    public boolean get_flag() {
        return flag;
    }

    public void set_flag(boolean flag_val) {
        flag = flag_val;
    }

    public CU get_control_unit() {
        return this.ctrl_unit;
    }

    public void set_control_unit(CU controlUnit) {
        this.ctrl_unit = controlUnit;
    }

    public int get_operand1() {
        return operand1;
    }

    public void set_operand1(int op1) {
        operand1 = op1;
    }

    public int get_operand2() {
        return operand2;
    }

    public void set_operand2(int op2) {
        operand2 = op2;
    }

    public int get_aluResult() {
        return this.aluResult;
    }

    public void set_aluResult(int result) {
        this.aluResult = result;
    }
    
    // caalculating results according to various commands
    public void calc_result() {
        if (ctrl_unit.isAdd() || ctrl_unit.isAddi() || ctrl_unit.isLoad() || ctrl_unit.isStore()) {
            aluResult = operand1 + operand2;
        }

        else if (ctrl_unit.isSub() || ctrl_unit.isSubi()) {
            aluResult = operand1 - operand2;
        }

        else if (ctrl_unit.isMul() || ctrl_unit.isMuli()) {
            aluResult = operand1 * operand2;
        }

        else if (ctrl_unit.isDiv() || ctrl_unit.isDivi()) {
            aluResult = operand1 / operand2;
            containingProcessor.getRegisterFile().setValue(31, operand1 % operand2);
            System.out.println("%x31 = " + containingProcessor.getRegisterFile().getValue(31));
        }

        else if (ctrl_unit.isAnd() || ctrl_unit.isAndi()) {
            aluResult = operand1 & operand2;
        }

        else if (ctrl_unit.isOr() || ctrl_unit.isOri()) {
            aluResult = operand1 | operand2;
        }

        else if (ctrl_unit.isXor() || ctrl_unit.isXori()) {
            aluResult = operand1 ^ operand2;
        }

        else if (ctrl_unit.isSlt() || ctrl_unit.isSlti()) {
            if (operand1 < operand2)
                aluResult = 1;
            else
                aluResult = 0;
        }

        else if (ctrl_unit.isSll() || ctrl_unit.isSlli()) {
            aluResult = operand1 << operand2;
        }

        else if (ctrl_unit.isSrl() || ctrl_unit.isSrli()) {
            aluResult = operand1 >>> operand2;
        }

        else if (ctrl_unit.isSra() || ctrl_unit.isSrai()) {
            aluResult = operand1 >> operand2;
        }

        else if (ctrl_unit.isBranch()) {
            if (ctrl_unit.isBeq())
                flag = (operand1 == operand2);
            else if (ctrl_unit.isBne())
                flag = (operand1 != operand2);
            else if (ctrl_unit.isBlt())
                flag = (operand1 < operand2);
            else if (ctrl_unit.isBgt())
                flag = (operand1 > operand2);
        }

        else
            aluResult = 0;
    }
}
