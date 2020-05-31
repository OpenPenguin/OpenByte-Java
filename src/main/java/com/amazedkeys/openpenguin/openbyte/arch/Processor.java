package com.amazedkeys.openpenguin.openbyte.arch;

import com.amazedkeys.openpenguin.openbyte.components.generics.StorageDevice;
import com.amazedkeys.openpenguin.openbyte.utils.graphics.DisplayProvider;
import com.amazedkeys.openpenguin.openbyte.utils.graphics.PixelRasterView;
import com.amazedkeys.openpenguin.openbyte.utils.shared.Maths;
import com.amazedkeys.openpenguin.openbyte.utils.types.Vector2;

public class Processor {
    protected StorageDevice bootDisk, memory;
    protected PixelRasterView mainDisplay;
    protected DisplayProvider gpu;

    protected InterruptHandler ih;

    protected static final int cycle_delay_period = 0;

    protected int flags = 0;
    protected int[] registers;
    /*
        REGISTERS:
            - 0: AH
            - 1: BH
            - 2: CH
            - 3: DH
            - 4: SP
            - 5: BP
            - 6: SI
            - 7: DI
            - 8: IP - Execution Pointer
            - 9: EH
            - A: FH
            - B: GH
            - C: IH
            - D: FR - Flag Register
     */

    protected int instruction, argument_1, argument_2;
    protected boolean carry;
    protected boolean isRunning;

    protected boolean comparison_equal, comparison_greater_than, comparison_less_than;

    public Processor(StorageDevice memory, StorageDevice bootDisk, PixelRasterView mainDisplay, DisplayProvider gpu) {
        this.memory = memory;
        this.bootDisk = bootDisk;
        this.mainDisplay = mainDisplay;
        this.gpu = gpu;

        this.registers = new int[0xE];

        this.ih = new InterruptHandler(this);

        setup_system_default();
    }
    private void setup_system_default() {
        this.registers[8] = 0;
        this.registers[4] = this.memory.getCapacity() - 1;
        this.registers[5] = this.registers[4];
    }

    // Stack methods
    protected void stack_push(int value) {
        int clamped_value = Maths.clamp(255, 0, value);
        this.memory.writeByte(this.registers[4], value);
        this.registers[4] = this.registers[4] - 1;
    }
    protected int stack_pop() {
        if (this.registers[4] == this.registers[5]) {
            return 0;
        } else {
            int value = this.memory.readByte(this.registers[4]);
            this.registers[4] = this.registers[4] + 1;
            return value;
        }
    }
    protected void stack_push_all() {
        for (int i=0; i<this.registers.length; i++)
            stack_push(this.registers[i]);
    }
    protected void stack_pop_all() {
        for (int i=0; i<this.registers.length; i++)
            this.registers[i] = stack_pop();
    }

    // Command handlers
    protected void interrupt_handler() {
        this.ih.throw_interrupt();
    }
    protected void run_comparison() {
        comparison_equal = false;
        comparison_greater_than = false;
        comparison_less_than = false;

        if (argument_1 > argument_2) {
            comparison_greater_than = true;
        } else if (argument_1 < argument_2) {
            comparison_less_than = true;
        } else {
            comparison_equal = true;
        }

        System.out.println("Compared " + argument_1 + " and " + argument_2 + " -> " +
                ((comparison_greater_than) ? "G" : "") +
                ((comparison_less_than) ? "L" : "") +
                ((comparison_equal) ? "E" : "")
        );
    }
    protected void jump_if_equal() {
        if (comparison_equal) {
            this.registers[8] = argument_1;
            System.out.println("Branched(JEQ): " + this.registers[8]);
        } else {
            // Move to the next instruction!
            this.registers[8] = this.registers[8] + 2;
        }
    }
    protected void jump_if_not_equal() {
        if (!comparison_equal) {
            this.registers[8] = argument_1;
            System.out.println("Branched(JNEQ): " + this.registers[8]);
        } else {
            // Move to the next instruction!
            this.registers[8] = this.registers[8] + 2;
        }
    }
    protected void jump_if_greater_than() {
        if (comparison_greater_than) {
            this.registers[8] = argument_1;
            System.out.println("Branched(JGT): " + this.registers[8]);
        } else {
            // Move to the next instruction!
            this.registers[8] = this.registers[8] + 2;
        }
    }
    protected void jump_if_less_than() {
        if (comparison_less_than) {
            this.registers[8] = argument_1;
            System.out.println("Branched(JLT): " + this.registers[8]);
        } else {
            // Move to the next instruction!
            this.registers[8] = this.registers[8] + 2;
        }
    }
    protected void jump_if_greater_than_or_equal_to() {
        if (comparison_greater_than || comparison_equal) {
            this.registers[8] = argument_1;
            System.out.println("Branched(JGTEQ): " + this.registers[8]);
        } else {
            // Move to the next instruction!
            this.registers[8] = this.registers[8] + 2;
        }
    }
    protected void jump_if_less_than_or_equal_to() {
        if (comparison_less_than || comparison_equal) {
            this.registers[8] = argument_1;
            System.out.println("Branched(JLTEQ): " + this.registers[8]);
        } else {
            // Move to the next instruction!
            this.registers[8] = this.registers[8] + 2;
        }
    }

    // ARGUMENT_1 is a POINTER to a REGISTER
    protected void add() {
        this.registers[argument_1] = this.registers[argument_1] + argument_2;
    }
    protected void subtract() {
        this.registers[argument_1] = this.registers[argument_1] - argument_2;
    }
    protected void multiply() {
        this.registers[argument_1] = this.registers[argument_1] * argument_2;
    }
    protected void divide() {
        this.registers[argument_1] = this.registers[argument_1] / argument_2;
    }
    protected void pow() {
        this.registers[argument_1] = this.registers[argument_1] ^ argument_2;
    }
    protected void and() {
        this.registers[argument_1] = this.registers[argument_1] & argument_2;
    }
    protected void or() {
        this.registers[argument_1] = this.registers[argument_1] | argument_2;
    }
    protected void nor() {
        this.registers[argument_1] = ~(this.registers[argument_1] | argument_2);
    }
    protected void xor() {
        this.registers[argument_1] = (this.registers[argument_1] & argument_2) | (~this.registers[argument_1] & argument_2);
    }
    protected void not() {
        this.registers[argument_1] = ~this.registers[argument_1];
    }
    protected void lshift() {
        int value = this.registers[argument_1];
        int places = argument_2;
        for (int i=0; i<=places; i++) {
            value = (value << 1);
        }
        this.registers[argument_1] = value;
    }
    protected void rshift() {
        int value = this.registers[argument_1];
        int places = argument_2;
        for (int i=0; i<=places; i++) {
            value = (value >> 1);
        }
        this.registers[argument_1] = value;
    }
    protected void jump() {
        this.registers[8] = this.argument_1;
        System.out.println("Branched: " + this.registers[8]);
    }
    protected void call() {
        stack_push(this.registers[8] + 2);
        this.registers[8] = this.argument_1;
    }
    protected void ret() {
        int ret_addr = stack_pop();
        this.registers[8] = this.argument_1;
    }
    protected void next_instr(int arg_count) {
        this.registers[8] = this.registers[8] + (arg_count + 1);
    }

    // Main execution method
    public void start() {
        this.isRunning = true;
        int target_len = 100;

        if (this.bootDisk.getCapacity() < target_len) {
            target_len = this.bootDisk.getCapacity();
        }

        for (int i=0; i<target_len; i++) {
            this.memory.writeByte(i, this.bootDisk.readByte(i));
        }
    }
    public void step() {
        execute_instruction();
    }
    public void run() {
        try {
            while (this.isRunning) {
                execute_instruction();
                Thread.sleep(cycle_delay_period);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String[] instruction_names = new String[]{
            "null",
            "int_l",
            "int_r",
            "mov_rl",
            "mov_rr",
            "mov_rm",
            "mov_mr",
            "comp_ll",
            "comp_rl",
            "comp_rr",
            "jeq_l",
            "jeq_r",
            "jlt_l",
            "jlt_r",
            "jgt_l",
            "jgt_r",
            "jneq_l",
            "jneq_r",
            "add_rl",
            "add_rr",
            "sub_rl",
            "sub_rr",
            "mul_rl",
            "mul_rr",
            "div_rl",
            "div_rr",
            "pow_rl",
            "pow_rr",
            "and_rl",
            "and_rr",
            "or_rl",
            "or_rr",
            "nor_rl",
            "nor_rr",
            "xor_rl",
            "xor_rr",
            "not_r",
            "halt",
            "lshift_rl",
            "lshift_rr",
            "inop_28",
            "inop_29",
            "rshift_rl",
            "rshift_rr",
            "inop_2c",
            "inop_2d",
            "jgteq_l",
            "jgteq_r",
            "jlteq_l",
            "jlteq_r",
            "lshiftc_rl",
            "lshiftc_rr",
            "inop_34",
            "inop_35",
            "rshiftc_rl",
            "rshiftc_rr",
            "inop_38",
            "inop_39",
            "jmp_l",
            "jmp_r",
            "call_l",
            "call_r",
            "ret"
    };

    protected void execute_instruction() {
        int exaddr = this.registers[8];
        int a1, a2;
        instruction = this.memory.readByte(exaddr);
        System.out.println("Executing instruction (" + instruction + " : \"" + instruction_names[instruction] + "\") @ "  + exaddr);

        switch(instruction) {
            default:
                next_instr(0);
                break;
            case 0x01:
                argument_1 = this.memory.readByte(exaddr + 1);
                interrupt_handler();
                next_instr(1);
                break;
            case 0x02:
                argument_1 = this.registers[this.memory.readByte(exaddr + 1)];
                interrupt_handler();
                next_instr(1);
                break;
            case 0x03:
                this.registers[this.memory.readByte(exaddr + 1)] = this.memory.readByte(exaddr + 2);
                next_instr(2);
                break;
            case 0x04:
                this.registers[this.memory.readByte(exaddr + 1)] = this.registers[this.memory.readByte(exaddr + 2)];
                next_instr(2);
                break;
            case 0x05:
                this.registers[this.memory.readByte(exaddr + 1)] = this.memory.readByte(this.memory.readByte(exaddr + 2));
                next_instr(2);
                break;
            case 0x06:
                this.memory.writeByte(this.memory.readByte(exaddr + 1), this.registers[this.memory.readByte(exaddr + 2)]);
                next_instr(2);
                break;
            case 0x07:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.memory.readByte(exaddr + 2);
                run_comparison();
                next_instr(2);
                break;
            case 0x08:
                argument_1 = this.registers[this.memory.readByte(exaddr + 1)];
                argument_2 = this.memory.readByte(exaddr + 2);
                run_comparison();
                next_instr(2);
                break;
            case 0x09:
                argument_1 = this.registers[this.memory.readByte(exaddr + 1)];
                argument_2 = this.registers[this.memory.readByte(exaddr + 2)];
                run_comparison();
                next_instr(2);
                break;
            case 0x0A:
                argument_1 = this.memory.readByte(exaddr + 1);
                jump_if_equal();
                break;
            case 0x0B:
                argument_1 = this.registers[this.memory.readByte(exaddr + 1)];
                jump_if_equal();
                break;
            case 0x0C:
                argument_1 = this.memory.readByte(exaddr + 1);
                jump_if_less_than();
                break;
            case 0x0D:
                argument_1 = this.registers[this.memory.readByte(exaddr + 1)];
                jump_if_less_than();
                break;
            case 0x0E:
                argument_1 = this.memory.readByte(exaddr + 1);
                jump_if_greater_than();
                break;
            case 0x0F:
                argument_1 = this.registers[this.memory.readByte(exaddr + 1)];
                jump_if_greater_than();
                break;
            case 0x10:
                argument_1 = this.memory.readByte(exaddr + 1);
                jump_if_not_equal();
                break;
            case 0x11:
                argument_1 = this.registers[this.memory.readByte(exaddr + 1)];
                jump_if_not_equal();
                break;
            case 0x2E:
                argument_1 = this.memory.readByte(exaddr + 1);
                jump_if_greater_than_or_equal_to();
                break;
            case 0x2F:
                argument_1 = this.registers[this.memory.readByte(exaddr + 1)];
                jump_if_greater_than_or_equal_to();
                break;
            case 0x30:
                argument_1 = this.memory.readByte(exaddr + 1);
                jump_if_less_than_or_equal_to();
                break;
            case 0x31:
                argument_1 = this.registers[this.memory.readByte(exaddr + 1)];
                jump_if_less_than_or_equal_to();
                break;
            case 0x12:
                argument_1 = this.memory.readByte(exaddr + 1); // DO NOT UNWRAP REGISTER
                argument_2 = this.memory.readByte(exaddr + 2);
                add();
                next_instr(2);
                break;
            case 0x13:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.registers[this.memory.readByte(exaddr + 2)];
                add();
                next_instr(2);
                break;
            case 0x14:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.memory.readByte(exaddr + 2);
                subtract();
                next_instr(2);
                break;
            case 0x15:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.registers[this.memory.readByte(exaddr + 2)];
                subtract();
                next_instr(2);
                break;
            case 0x16:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.memory.readByte(exaddr + 2);
                multiply();
                next_instr(2);
                break;
            case 0x17:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.registers[this.memory.readByte(exaddr + 2)];
                multiply();
                next_instr(2);
                break;
            case 0x18:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.memory.readByte(exaddr + 2);
                divide();
                next_instr(2);
                break;
            case 0x19:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.registers[this.memory.readByte(exaddr + 2)];
                divide();
                next_instr(2);
                break;
            case 0x1A:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.memory.readByte(exaddr + 2);
                pow();
                next_instr(2);
                break;
            case 0x1B:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.registers[this.memory.readByte(exaddr + 2)];
                pow();
                next_instr(2);
                break;
            case 0x1C:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.memory.readByte(exaddr + 2);
                and();
                next_instr(2);
                break;
            case 0x1D:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.registers[this.memory.readByte(exaddr + 2)];
                and();
                next_instr(2);
                break;
            case 0x1E:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.memory.readByte(exaddr + 2);
                or();
                next_instr(2);
                break;
            case 0x1F:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.registers[this.memory.readByte(exaddr + 2)];
                or();
                next_instr(2);
                break;
            case 0x20:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.memory.readByte(exaddr + 2);
                nor();
                next_instr(2);
                break;
            case 0x21:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.registers[this.memory.readByte(exaddr + 2)];
                nor();
                next_instr(2);
                break;
            case 0x22:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.memory.readByte(exaddr + 2);
                xor();
                next_instr(2);
                break;
            case 0x23:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.registers[this.memory.readByte(exaddr + 2)];
                xor();
                next_instr(2);
                break;
            case 0x24:
                argument_1 = this.memory.readByte(exaddr + 1);
                not();
                next_instr(1);
                break;
            case 0x25:
                isRunning = false;
                break;
            case 0x26:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.memory.readByte(exaddr + 2);
                carry = false;
                lshift();
                next_instr(2);
                break;
            case 0x27:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.registers[this.memory.readByte(exaddr + 2)];
                carry = false;
                lshift();
                next_instr(2);
                break;
            case 0x2A:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.memory.readByte(exaddr + 2);
                carry = false;
                rshift();
                next_instr(2);
                break;
            case 0x2B:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.registers[this.memory.readByte(exaddr + 2)];
                carry = false;
                rshift();
                next_instr(2);
                break;
            case 0x32:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.memory.readByte(exaddr + 2);
                carry = true;
                lshift();
                next_instr(2);
                break;
            case 0x33:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.registers[this.memory.readByte(exaddr + 2)];
                carry = true;
                lshift();
                next_instr(2);
                break;
            case 0x36:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.memory.readByte(exaddr + 2);
                carry = true;
                rshift();
                next_instr(2);
                break;
            case 0x37:
                argument_1 = this.memory.readByte(exaddr + 1);
                argument_2 = this.registers[this.memory.readByte(exaddr + 2)];
                carry = true;
                rshift();
                next_instr(2);
                break;
            case 0x3A:
                argument_1 = this.memory.readByte(exaddr + 1);
                jump();
                break;
            case 0x3B:
                argument_1 = this.registers[this.memory.readByte(exaddr + 1)];
                jump();
                break;
            case 0x3C:
                argument_1 = this.memory.readByte(exaddr + 1);
                call();
                break;
            case 0x3D:
                argument_1 = this.registers[this.memory.readByte(exaddr + 1)];
                call();
                break;
            case 0x3E:
                ret();
                break;
            case 0x40: // LDM_RL
                a1 = this.memory.readByte(exaddr + 1);
                a2 = this.memory.readByte(exaddr + 2);
                this.registers[a1] = this.memory.readByte(a2);
                break;
            case 0x41: // LDM_RR
                a1 = this.memory.readByte(exaddr + 1);
                a2 = this.memory.readByte(exaddr + 2);
                this.registers[a1] = this.memory.readByte(this.registers[a2]);
                break;
            case 0x42: // SVM_RL
                a1 = this.memory.readByte(exaddr + 1);
                a2 = this.memory.readByte(exaddr + 2);
                this.memory.writeByte(this.registers[a1], a2);
                break;
            case 0x43: // SVM_RR
                a1 = this.memory.readByte(exaddr + 1);
                a2 = this.memory.readByte(exaddr + 2);
                this.memory.writeByte(this.registers[a1], this.registers[a2]);
                break;
            case 0x44: // SVM_LL
                a1 = this.memory.readByte(exaddr + 1);
                a2 = this.memory.readByte(exaddr + 2);
                this.memory.writeByte(a1, a2);
                break;
            case 0x45: // SVM_LR
                a1 = this.memory.readByte(exaddr + 1);
                a2 = this.memory.readByte(exaddr + 2);
                this.memory.writeByte(a1, this.registers[a2]);
                break;
        }

        System.out.println(
                "Registers:[" +
                        this.registers[0x0] + "," +
                        this.registers[0x1] + "," +
                        this.registers[0x2] + "," +
                        this.registers[0x3] + "," +
                        this.registers[0x4] + "," +
                        this.registers[0x5] + "," +
                        this.registers[0x6] + "," +
                        this.registers[0x7] + "," +
                        this.registers[0x8] + "," +
                        this.registers[0x9] + "," +
                        this.registers[0xA] + "," +
                        this.registers[0xB] + "," +
                        this.registers[0xC] + "]"
        );
    }


}
