package com.ogic.spikesystemapi.exception;

/**
 * @author ogic
 */
public class ProductAmountLessThanZeroException extends Exception {

    public ProductAmountLessThanZeroException(String message){
        super("amount of product[" + message + "] is less than zero");
    }

}
