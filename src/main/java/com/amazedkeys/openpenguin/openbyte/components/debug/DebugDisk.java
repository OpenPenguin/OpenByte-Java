package com.amazedkeys.openpenguin.openbyte.components.debug;

import com.amazedkeys.openpenguin.openbyte.components.generics.StorageDevice;
import com.amazedkeys.openpenguin.openbyte.utils.shared.Maths;

public class DebugDisk extends StorageDevice {
    private int[] storage;

    public DebugDisk(int capacity) {
        this.storage = new int[capacity];
    }

    public DebugDisk(int[] storage) {
        this.storage = storage;
    }

    @Override
    public int readByte(int address) {
        return Maths.clamp(255, 0, this.storage[address]);
    }

    @Override
    public void writeByte(int address, int value) {
        this.storage[address] = Maths.clamp(255, 0, value);
    }

    @Override
    public int getCapacity() {
        return this.storage.length;
    }
}
