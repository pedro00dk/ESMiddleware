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
     * @throws NoSuchPaddingException    if a encryption error occurs
     * @throws NoSuchAlgorithmException  if a encryption error occurs
     * @throws InvalidKeyException       if a encryption error occurs
     * @throws BadPaddingException       if a encryption error occurs
     * @throws IllegalBlockSizeException if a encryption error occurs
     */
    public static byte[] encrypt(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    /**
     * Decrypts the received data.
     *
     * @param data the data to decrypt
     * @return the decrypted data
     * @throws NoSuchPaddingException    if a encryption error occurs
     * @throws NoSuchAlgorithmException  if a encryption error occurs
     * @throws InvalidKeyException       if a encryption error occurs
     * @throws BadPaddingException       if a encryption error occurs
     * @throws IllegalBlockSizeException if a encryption error occurs
     */
    public static byte[] decrypt(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }
}
