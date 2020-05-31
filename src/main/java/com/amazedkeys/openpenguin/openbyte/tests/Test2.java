package com.amazedkeys.openpenguin.openbyte.tests;

import com.amazedkeys.openpenguin.openbyte.arch.Processor;
import com.amazedkeys.openpenguin.openbyte.asmcompiler.AssemblyCompiler;
import com.amazedkeys.openpenguin.openbyte.components.debug.DebugDisk;
import com.amazedkeys.openpenguin.openbyte.utils.graphics.DisplayProvider;
import com.amazedkeys.openpenguin.openbyte.utils.graphics.PixelRasterView;
import com.amazedkeys.openpenguin.openbyte.utils.shared.Tiers;
import com.amazedkeys.openpenguin.openbyte.utils.types.Vector2;

public class Test2 {
    public static void main(String[] args) {
        // Now compile our assembly!
        AssemblyCompiler compiler = new AssemblyCompiler();

        compiler.load("; Get the dimensions\n" +
                "mov $AH, 3\n" +
                "int 0x10\n" +
                "mov $DH, $BH\n" +
                "mov $IH, $CH\n" +
                "\n" +
                "; Clear our 'variable' registers\n" +
                "mov $DH, 0  ; Set the switch\n" +
                "mov $BH, 0  ; Set X\n" +
                "mov $CH, 0  ; Set Y\n" +
                "\n" +
                "loop:\n" +
                "comp $DH, 0\n" +
                "jeq state_2\n" +
                "mov $EH, 255\n" +
                "jmp draw\n" +
                "\n" +
                "state_2:\n" +
                "mov $EH, 0\n" +
                "\n" +
                "draw:\n" +
                "mov $FH, $EH\n" +
                "mov $GH, $EH\n" +
                "mov $AH, 1\n" +
                "int 0x10\n" +
                "not $DH\n" +
                "add $BH, 1\n" +
                "comp $BH, $DH\n" +
                "jgt next_row\n" +
                "jmp loop\n" +
                "\n" +
                "next_row:\n" +
                "mov $BH, 0\n" +
                "add $CH, 1\n" +
                "comp $CH, $IH\n" +
                "jgt done\n" +
                "jmp loop\n" +
                "\n" +
                "done:\n" +
                "hlt");

        compiler.getBinary();
    }
}
