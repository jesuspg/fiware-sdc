/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.util;

import java.io.File;

/**
 * Provides the needed methods to encrypt and decrypt using private and public.
 * 
 * @author Sergio Arroyo
 */
public interface Signer {

    /**
     * Encrypt the message using the key
     * 
     * @param message
     *            the message to be signed
     * @param pemFile
     *            the file containing the key
     * @return the message digest
     */
    public String sign(String message, File pemFile);

    /**
     * Encrypt the digest message signed using the pair-key
     * 
     * @param message
     *            the message to be unsigned
     * @param pemFile
     *            the file containing the key
     * @return the decrypted message.
     */
    public String unsign(String message, File pemFile);
}
