package com.easemob.developer.github.rest.filters;

import org.apache.cassandra.utils.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author stliu @ apache.org
 */
public class HMacSha1Encoder {
    private final Mac mac;

    public HMacSha1Encoder(String key) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] keyBytes = key.getBytes();
        SecretKeySpec  signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
        // Get an hmac_sha1 Mac instance and initialize with the signing key
         mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
    }

    public String encode(String value) throws Exception{
        // Compute the hmac on input data bytes
        byte[] rawHmac = mac.doFinal(value.getBytes());
        mac.reset();
        return Hex.bytesToHex(rawHmac);
    }
}
