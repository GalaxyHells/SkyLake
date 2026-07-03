package com.galaxyhells.skylake.data;

import com.galaxyhells.skylake.utils.OptionType;

public class Option<T> {

    private final OptionType type;
    private T value;

    public Option(OptionType type, T defaultValue) {
        this.type = type;
        this.value = defaultValue;
    }

    public OptionType getType() {
        return type;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}