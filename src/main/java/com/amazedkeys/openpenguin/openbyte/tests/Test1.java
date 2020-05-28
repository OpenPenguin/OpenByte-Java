package com.amazedkeys.openpenguin.openbyte.tests;

import com.amazedkeys.openpenguin.openbyte.arch.Processor;
import com.amazedkeys.openpenguin.openbyte.components.debug.DebugDisk;
import com.amazedkeys.openpenguin.openbyte.utils.graphics.DisplayProvider;
import com.amazedkeys.openpenguin.openbyte.utils.graphics.PixelRasterView;
import com.amazedkeys.openpenguin.openbyte.utils.shared.Tiers;
import com.amazedkeys.openpenguin.openbyte.utils.types.Vector2;

public class Test1 {
    public static void main(String[] args) {
        PixelRasterView pixelview = new PixelRasterView(Tiers.TIER_THREE);
        Vector2 res = pixelview.getResolution();

        for (int y = 0; y < res.getY(); y++) {
            for (int x = 0; x < res.getX(); x++) {
                pixelview.setRGB(x, y, 0, 0, 0);
            }
        }

        DisplayProvider view = new DisplayProvider(pixelview);
        DebugDisk ram = new DebugDisk(500);
        DebugDisk boot_disk = new DebugDisk(new int[]{
                0x03, 0x00, 0x03,
                0x01, 0x10,
                0x04, 0x03, 0x01,
                0x04, 0x0C, 0x02,
                0x03, 0x01, 0x00,
                0x03, 0x02, 0x00,
                0x08, 0x06, 0x00,
                0x0A, 0x1B,
                0x03, 0x09, 0xFF,
                0x3A, 0x1E,
                0x03, 0x09, 0x00,
                0x04, 0x0A, 0x09,
                0x04, 0x0B, 0x09,
                0x03, 0x00, 0x01,
                0x01, 0x10,
                0x24, 0x06,
                0x12, 0x01, 0x01,
                0x09, 0x01, 0x03,
                0x2E, 0x35,
                0x3A, 0x11,
                0x03, 0x01, 0x00,
                0x12, 0x02, 0x01,
                0x24, 0x06,
                0x09, 0x02, 0x0C,
                0x2E, 0x44,
                0x3A, 0x11,
                0x25
        });

        System.out.println("Starting CPU in ten seconds...");
        try {
            Thread.sleep(10000);
            System.out.println("Creating CPU");
            Processor cpu = new Processor(ram, boot_disk, pixelview, view);

            System.out.println("Preparing CPU");
            cpu.start();

            System.out.println("Starting CPU");
            cpu.run();

            System.out.println("CPU finished running!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
