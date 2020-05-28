package com.amazedkeys.openpenguin.openbyte;

import com.amazedkeys.openpenguin.openbyte.utils.graphics.DisplayProvider;
import com.amazedkeys.openpenguin.openbyte.utils.graphics.PixelRasterView;
import com.amazedkeys.openpenguin.openbyte.utils.shared.Tiers;
import com.amazedkeys.openpenguin.openbyte.utils.types.Vector2;

public class OpenByteLauncher {
    public static void main(String[] args) {
        PixelRasterView pixelview = new PixelRasterView(Tiers.TIER_ONE);
        Vector2 res = pixelview.getResolution();

        for (int y = 0; y < res.getY(); y++) {
            for (int x = 0; x < res.getX(); x++) {
                pixelview.setRGB(x, y, 0, 0, 0);
            }
        }

        DisplayProvider view = new DisplayProvider(pixelview);



    }
}
