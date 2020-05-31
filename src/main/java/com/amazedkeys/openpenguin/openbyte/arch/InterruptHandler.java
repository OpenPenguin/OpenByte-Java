package com.amazedkeys.openpenguin.openbyte.arch;

import com.amazedkeys.openpenguin.openbyte.utils.types.Vector2;

public class InterruptHandler {
    private final Processor cpu;

    public InterruptHandler(Processor processor) {
        this.cpu = processor;
    }

    protected void gpu_interrupt() {
        // Graphics Interrupt
        if (this.cpu.registers[0] == 0x1) {
            // Set Pixel
            Vector2 resolution = this.cpu.mainDisplay.getResolution();

            int x = this.cpu.registers[0x1]; // BH
            int y = this.cpu.registers[0x2]; // CH
            int r = this.cpu.registers[0x9]; // EH
            int g = this.cpu.registers[0xA]; // FH
            int b = this.cpu.registers[0xB]; // GH

            if (x >= resolution.getX()) {
                System.out.println("GPU_OVER_X");
            }
            if (y >= resolution.getY()) {
                System.out.println("GPU_OVER_Y");
            }

            if (x < resolution.getX() && y < resolution.getY()) {
                this.cpu.mainDisplay.setRGB(x, y, r, g, b);
            }
            this.cpu.gpu.updateRaster();
        } else if (this.cpu.registers[0] == 0x2) {
            // Get Pixel
            //TODO
        } else if (this.cpu.registers[0] == 0x03) {
            // Get resolution
            Vector2 resolution = this.cpu.mainDisplay.getResolution();
            this.cpu.registers[1] = (int) resolution.getX();
            this.cpu.registers[2] = (int) resolution.getY();
        }
    }

    public void throw_interrupt() {
        int int_id = this.cpu.argument_1;

        if (int_id == 0x10) {
            gpu_interrupt();
        }
    }

}
