package com.amazedkeys.openpenguin.openbyte.asmcompiler;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class AssemblyLookupTable {
    private HashMap<String, String> table_pattern_to_instr;

    private void tppi(String instr, String pattern) {
        this.table_pattern_to_instr.put(pattern, instr);
    }

    protected void generate_table() {
        tppi("mov_rl", "mov (\\$.+) (\\d+)");
        tppi("mov_rr", "mov (\\$.+) (\\$.+)");
        tppi("int_l", "int ([\\dxb]+)");
        tppi("int_r", "int (\\$.+)");
        tppi("comp_rl", "comp (\\$.+) ([0x\\d]+)");
        tppi("comp_rr", "comp (\\$.+) (\\$.+)");

        tppi("jmp_lbl", "jmp (.\\w+)");
        tppi("jmp_l", "jmp ([0x\\d]+)");
        tppi("jmp_r", "jmp (\\$.+)");

        tppi("jeq_lbl", "jeq (.\\w+)");
        tppi("jeq_l", "jeq ([0x\\d]+)");
        tppi("jeq_r", "jeq (\\$.+)");

        tppi("jneq_lbl", "jneq (.\\w+)");
        tppi("jneq_l", "jneq ([0x\\d]+)");
        tppi("jneq_r", "jneq (\\$.+)");

        tppi("jgt_lbl", "jgt (.\\w+)");
        tppi("jgt_l", "jgt ([0x\\d]+)");
        tppi("jgt_r", "jgt (\\$.+)");

        tppi("jlt_lbl", "jlt (.\\w+)");
        tppi("jlt_l", "jlt ([0x\\d]+)");
        tppi("jlt_r", "jlt (\\$.+)");

        tppi("jgteq_lbl", "jgteq (.\\w+)");
        tppi("jgteq_l", "jgteq ([0x\\d]+)");
        tppi("jgteq_r", "jgteq (\\$.+)");

        tppi("jlteq_lbl", "jlteq (.\\w+)");
        tppi("jlteq_l", "jlteq ([0x\\d]+)");
        tppi("jlteq_r", "jlteq (\\$.+)");

        tppi("not_r", "not (\\$.+)");
        tppi("add_rr", "add (\\$.+) (\\$.+)");
        tppi("add_rl", "add (\\$.+) ([0x\\d]+)");
        tppi("hlt", "hlt");
    }

    public AssemblyLookupTable() {
        this.table_pattern_to_instr = new HashMap<>();
        generate_table();
    }

    public String identify_opcode(String asm) {
        final String[] result = {null};
        this.table_pattern_to_instr.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String asm_opcode, String asm_pattern) {
                if (asm.matches(asm_pattern)) {
                    result[0] = asm_opcode;
                    return;
                }
            }
        });
        return result[0];
    }

}
