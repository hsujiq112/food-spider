package com.foodspider.service;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * The Encryptor service for passwords.
 */
@Component
public class Encryptor {

    /**
     * Encrypt password.
     *
     * @param id     the id salt to have different encryption even for same password
     * @param string the password that we want to encrypt
     * @return the encrypted string
     * @throws Exception in case those classes cannot generate the encrypted string
     */
    public static String encrypt(UUID id, String string) throws Exception{
        var arrayBytes = (id.toString() + "secret discret").getBytes(StandardCharsets.UTF_8);
        var ks = new DESedeKeySpec(arrayBytes);
        var skf = SecretKeyFactory.getInstance("DESede");
        var cipher = Cipher.getInstance("DESede");
        var key = skf.generateSecret(ks);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        var plainText = string.getBytes();
        byte[] encryptedText = cipher.doFinal(plainText);
        return new String(Base64.getEncoder().encode(encryptedText));
    }

    /**
     * Decrypt password.
     *
     * @param id     the id, same thing
     * @param string the password
     * @return the decrypted string
     * @throws Exception if the decryption fails
     */
    public static String decrypt(UUID id, String string) throws Exception {
        var arrayBytes = (id.toString() + "secret discret").getBytes(StandardCharsets.UTF_8);
        var ks = new DESedeKeySpec(arrayBytes);
        var skf = SecretKeyFactory.getInstance("DESede");
        var cipher = Cipher.getInstance("DESede");
        var key = skf.generateSecret(ks);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedText = Base64.getDecoder().decode(string);
        byte[] plainText = cipher.doFinal(encryptedText);
        return new String(plainText);
    }
}
