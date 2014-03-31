/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.JCERSAPublicKey;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;

import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;

/**
 * RSA Implementation of Signer using Private key to encrypt and Public to decrypt.
 * 
 * @author Sergio Arroyo
 */
public class RSASignerImpl implements Signer {

    /**
     * {@inheritDoc}
     */
  
    public String sign(String message, File pemFile) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            PrivateKey privateKey = readKeyPair(pemFile, "".toCharArray()).getPrivate();

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            byte[] digest = cipher.doFinal(message.getBytes());
            return Base64.encodeBase64String(digest);
        } catch (IOException e) {
            throw new SdcRuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new SdcRuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new SdcRuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new SdcRuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new SdcRuntimeException(e);
        } catch (BadPaddingException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String unsign(String message, File pemFile) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            JCERSAPublicKey publicKey = readPublicKey(pemFile, "".toCharArray());

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            // decryption:
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(cipher.doFinal(org.bouncycastle.util.encoders.Base64.decode(message.getBytes("UTF-8"))));
            return baos.toString();
        } catch (IOException e) {
            throw new SdcRuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new SdcRuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new SdcRuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new SdcRuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new SdcRuntimeException(e);
        } catch (BadPaddingException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * Get the private key
     * 
     * @param privateKey
     * @param keyPassword
     * @return
     * @throws IOException
     */
    private static KeyPair readKeyPair(File privateKey, char[] keyPassword) throws IOException {
        FileReader fileReader = new FileReader(privateKey);
        PEMReader r = new PEMReader(fileReader, new DefaultPasswordFinder(keyPassword));
        try {
            return (KeyPair) r.readObject();
        } catch (IOException ex) {
            throw new IOException("The private key could not be decrypted", ex);
        } finally {
            r.close();
            fileReader.close();
        }
    }

    /**
     * Get the public key
     * 
     * @param privateKey
     * @param keyPassword
     * @return
     * @throws IOException
     */
    private static JCERSAPublicKey readPublicKey(File privateKey, char[] keyPassword) throws IOException {
        FileReader fileReader = new FileReader(privateKey);
        PEMReader r = new PEMReader(fileReader, new DefaultPasswordFinder(keyPassword));
        try {
            return (JCERSAPublicKey) r.readObject();
        } catch (IOException ex) {
            throw new IOException("The private key could not be decrypted", ex);
        } finally {
            r.close();
            fileReader.close();
        }
    }

    private static class DefaultPasswordFinder implements PasswordFinder {
        private final char[] password;

        private DefaultPasswordFinder(char[] password) {
            this.password = password;
        }

        @Override
        public char[] getPassword() {
            return Arrays.copyOf(password, password.length);
        }
    }
}
