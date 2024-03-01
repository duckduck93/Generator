package com.duck.generator;

import lombok.RequiredArgsConstructor;

/**
 * @author sdhan
 * @since 2024.02.24
 */
@RequiredArgsConstructor(staticName = "from")
public class Id {

    private final long value;

    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if ((null == obj) || obj.getClass() != Id.class)
            return false;
        Id id = (Id)obj;
        return value == id.value;
    }
}
