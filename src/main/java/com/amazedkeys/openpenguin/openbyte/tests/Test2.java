package com.amazedkeys.openpenguin.openbyte.tests;

import com.amazedkeys.openpenguin.openbyte.arch.Processor;
import com.amazedkeys.openpenguin.openbyte.components.debug.DebugDisk;
import com.amazedkeys.openpenguin.openbyte.utils.graphics.DisplayProvider;
import com.amazedkeys.openpenguin.openbyte.utils.graphics.PixelRasterView;
import com.amazedkeys.openpenguin.openbyte.utils.shared.Tiers;
import com.amazedkeys.openpenguin.openbyte.utils.types.Vector2;

public class Test2 {
    public static void main(String[] args) {
        PixelRasterView pixelview = new PixelRasterView(Tiers.TIER_THREE);
        Vector2 res = pixelview.getResolution();

        for (int y = 0; y < res.getY(); y++) {
            for (int x = 0; x < res.getX(); x++) {
                pixelview.setRGB(x, y, 0, 0, 0);
            }
        }

        // Declare our objects!
        DisplayProvider view = new DisplayProvider(pixelview);
        DebugDisk ram = new DebugDisk(500);
        DebugDisk boot_disk = null;

        // Now compile our assembly!


        // Now boot!
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
