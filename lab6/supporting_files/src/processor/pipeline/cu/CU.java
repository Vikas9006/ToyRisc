package processor.pipeline.cu;

public class CU {
    int opcode;

    public CU(int code) {
        this.opcode = code;
    }

    // getting and setting opcode
    public int get_opcode() {
        return opcode;
    }

    public void set_opcode(int OpCode) {
        this.opcode = OpCode;
    }

    // return the ranges of opcode when the types are given
    public boolean isR3_type() {
        return ((this.opcode <= 20) && ((this.opcode & 1) == 0));
    }

    public boolean isR2I_type() {
        return ((opcode <= 21) && ((opcode & 1) == 1)) || (opcode != 24 && opcode >= 22 && opcode <= 28);
    }

    public boolean isRI_type() {
        return (opcode == 24 || opcode == 29);
    }

    public boolean isWb() {
        return (opcode <= 22);
    }

    public boolean isBranch() {
        return (opcode >= 24 && opcode <= 28);
    }

    // return opcodes from the given instructions
    public boolean isAdd() {
        return (this.opcode == 0);
    }

    public boolean isAddi() {
        return (this.opcode == 1);
    }

    public boolean isSub() {
        return (this.opcode == 2);
    }

    public boolean isSubi() {
        return (this.opcode == 3);
    }

    public boolean isMul() {
        return (this.opcode == 4);
    }

    public boolean isMuli() {
        return (this.opcode == 5);
    }

    public boolean isDiv() {
        return (this.opcode == 6);
    }

    public boolean isDivi() {
        return (this.opcode == 7);
    }

    public boolean isAnd() {
        return (this.opcode == 8);
    }

    public boolean isAndi() {
        return (this.opcode == 9);
    }

    public boolean isOr() {
        return (this.opcode == 10);
    }

    public boolean isOri() {
        return (this.opcode == 11);
    }

    public boolean isXor() {
        return (this.opcode == 12);
    }

    public boolean isXori() {
        return (this.opcode == 13);
    }

    public boolean isSlt() {
        return (this.opcode == 14);
    }

    public boolean isSlti() {
        return (this.opcode == 15);
    }

    public boolean isSll() {
        return (this.opcode == 16);
    }

    public boolean isSlli() {
        return (this.opcode == 17);
    }

    public boolean isSrl() {
        return (this.opcode == 18);
    }

    public boolean isSrli() {
        return (this.opcode == 19);
    }

    public boolean isSra() {
        return (this.opcode == 20);
    }

    public boolean isSrai() {
        return (this.opcode == 21);
    }

    public boolean isLoad() {
        return (this.opcode == 22);
    }

    public boolean isStore() {
        return (this.opcode == 23);
    }

    public boolean isJmp() {
        return (this.opcode == 24);
    }

    public boolean isBeq() {
        return (this.opcode == 25);
    }

    public boolean isBne() {
        return (this.opcode == 26);
    }

    public boolean isBlt() {
        return (this.opcode == 27);
    }

    public boolean isBgt() {
        return (this.opcode == 28);
    }

    public boolean isEnd() {
        return (this.opcode == 29);
    }

    public void print_instruction_type() {
        switch (this.opcode) {
            case -1:
                System.out.println("nop");
                break;
            case 0:
                System.out.println("add");
                break;
            case 1:
                System.out.println("addi");
                break;
            case 2:
                System.out.println("sub");
                break;
            case 3:
                System.out.println("subi");
                break;
            case 4:
                System.out.println("mul");
                break;
            case 5:
                System.out.println("muli");
                break;
            case 6:
                System.out.println("div");
                break;
            case 7:
                System.out.println("divi");
                break;
            case 8:
                System.out.println("and");
                break;
            case 9:
                System.out.println("andi");
                break;
            case 10:
                System.out.println("or");
                break;
            case 11:
                System.out.println("ori");
                break;
            case 12:
                System.out.println("xor");
                break;
            case 13:
                System.out.println("xori");
                break;
            case 14:
                System.out.println("slt");
                break;
            case 15:
                System.out.println("slti");
                break;
            case 16:
                System.out.println("sll");
                break;
            case 17:
                System.out.println("slli");
                break;
            case 18:
                System.out.println("srl");
                break;
            case 19:
                System.out.println("srli");
                break;
            case 20:
                System.out.println("sra");
                break;
            case 21:
                System.out.println("srai");
                break;
            case 22:
                System.out.println("load");
                break;
            case 23:
                System.out.println("store");
                break;
            case 24:
                System.out.println("jmp");
                break;
            case 25:
                System.out.println("beq");
                break;
            case 26:
                System.out.println("bne");
                break;
            case 27:
                System.out.println("blt");
                break;
            case 28:
                System.out.println("bgt");
                break;
            case 29:
                System.out.println("end");
                break;
            default:
                break;
        }
    }
}
