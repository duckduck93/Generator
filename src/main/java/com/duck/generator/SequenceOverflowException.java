package com.duck.generator;

/**
 * @author sdhan
 * @since 2024.02.21
 */
public class SequenceOverflowException extends RuntimeException {

    public SequenceOverflowException() {
        super("Sequence exhausted for this millisecond");
    }

}
