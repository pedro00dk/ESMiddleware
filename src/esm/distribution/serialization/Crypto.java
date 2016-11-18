package esm.distribution.serialization;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
            return cipher.doFinal(data);
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
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException e) {
            e.printStackTrace();
            throw new Error();
        }
    }
}
