package com.amazedkeys.openpenguin.openbyte.components.generics;

public abstract class StorageDevice {
    public abstract int readByte(int address);
    public abstract void writeByte(int address, int value);
    public abstract int getCapacity();
}
