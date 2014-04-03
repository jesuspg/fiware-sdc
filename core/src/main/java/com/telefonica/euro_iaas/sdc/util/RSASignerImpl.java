/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
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
