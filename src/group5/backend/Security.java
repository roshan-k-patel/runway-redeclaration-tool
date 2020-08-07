package group5.backend;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;

/**
 * This class is responsible for handling the encryption.
 */
public class Security {

    byte[] salt;
    SecureRandom secureRandom;

    public Security() {

        salt = new byte[64];
        secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);

    }

    /**
     * Code found on https://www.novixys.com/blog/aes-encryption-decryption-password-java/
     */
    public byte[] encrypt(String input, char[] password) {
        ArrayList<byte[]> bytes = new ArrayList<>();
        byte[] encrypted = new byte[0];

        SecretKeyFactory factory = null;

        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password, salt, 10000, 128);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec skey = new SecretKeySpec(tmp.getEncoded(), "AES");

            byte[] iv = new byte[128/8];
            secureRandom.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            bytes.add(salt);
            bytes.add(ivspec.getIV());

            Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ci.init(Cipher.ENCRYPT_MODE, skey, ivspec);

            encrypted = ci.doFinal(input.getBytes());




        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return encrypted;
    }

    /**
     * Function which joins two char arrays.
     * @param fst first char array
     * @param snd second char array
     */
    private char[] joinCharArry(char[] fst, char[] snd) {
        char[] rtnArry = new char[(fst.length + snd.length)];
        int pointer = 0;

        for (char c : fst) {
            rtnArry[pointer++] = c;
        }

        for (char c : snd) {
            rtnArry[pointer++] = c;
        }

        return rtnArry;
    }


}
