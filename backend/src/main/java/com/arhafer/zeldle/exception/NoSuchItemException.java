package com.arhafer.zeldle.exception;

public class NoSuchItemException extends RuntimeException {

    public NoSuchItemException(int itemId) {
        super("There exists no item with ID [" + itemId + "]");
    }
}
