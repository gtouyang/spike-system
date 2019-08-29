package com.ogic.spikesystemapi.exception;

/**
 * @author ogic
 */
public class GoodAmountLessThanZeroException extends Exception {

    public GoodAmountLessThanZeroException(String message) {
        super("amount of good[" + message + "] is less than zero");
    }

}
