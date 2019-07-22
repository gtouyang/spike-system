package com.ogic.spikesystemapi.component;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class MD5Template {

    private MessageDigest messageDigest;

    public MD5Template() throws NoSuchAlgorithmException {
        messageDigest = MessageDigest.getInstance("MD5");
    }

    public MD5Template update(String msg) throws UnsupportedEncodingException {
        byte[] beforeBytes = msg.getBytes("UTF-8");
        messageDigest.update(beforeBytes);
        byte[] afterBytes = messageDigest.digest();
        return this;
    }

    public byte[] digest() {
        return messageDigest.digest();
    }
}
