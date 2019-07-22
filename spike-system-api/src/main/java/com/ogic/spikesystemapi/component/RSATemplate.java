package com.ogic.spikesystemapi.component;

import java.io.UnsupportedEncodingException;
import java.security.*;

import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

@Component
public class RSATemplate {

    private Integer keySize = 2048;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    public void createKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    public byte[] encrypt(String msg, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(msg.getBytes("UTF-8"));
    }

    public byte[] decrypt(String msg, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(msg.getBytes("UTF-8"));
    }

    public Integer getKeySize() {
        return keySize;
    }

    public void setKeySize(Integer keySize) {
        this.keySize = keySize;
    }


    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
