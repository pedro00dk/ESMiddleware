package esm.distribution.serialization;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author Pedro Henrique
 */
public final class Crypto {

    /**
     * The internal crypto key.
     */
    private static final SecretKey secretKey = new SecretKeySpec(
            new byte[]{
                    '8', 'q', '3', '-', 'm', '4', '*', '8', '_', 'q', 'm', 'e', '|', 'r', 'h', '%'
            }, "AES"
    );

    /**
     * Prevents instantiation.
     */
    private Crypto() {
    }

    /**
     * Encrypts the received data.
     *
     * @param data the data to encrypt
     * @return the encrypted data
     */
    public static byte[] encrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(data);
            byte[] encryptedBytes = Base64.getEncoder().encode(cipherText);
            return encryptedBytes;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException e) {
            e.printStackTrace();
            throw new Error();
        }
    }

    /**
     * Decrypts the received data.
     *
     * @param data the data to decrypt
     * @return the decrypted data
     */
    public static byte[] decrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = Base64.getDecoder().decode(data);
            return cipher.doFinal(decryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException e) {
            e.printStackTrace();
            throw new Error();
        }
    }
}
