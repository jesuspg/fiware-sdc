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

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

/**
 * Unit test for RSA Signer
 * 
 * @author Sergio Arroyo
 */
public class RsaSignerTest {

    private final static String SIGNED = "H7BcEHFptR1YDm3S1cnTLYl+5lAkpU8m/PPgs4Xsexes2KiU4LYbZQ/Ib/un"
            + "LpuZ/AeoQuCw/retBmDCx4EEll6AC83Pr+3kmpq05nEMN5wWIz0zhkqLu7Kp"
            + "bTppDtjBGCDckaB5GQ0J7Upowy3BKF+ov2AUqNI2gRIISGuQtEeuSHzE1wP3"
            + "Qt2jeL1Xo8sdqEUIBcgWG200iPTQGKsqlk3KSIL89rdgbEPkm/mL+nbVYWWL"
            + "rLjPz4TcqB/pwZuwx+7g29dGfQKcxTEOTR3ilqpn8Dl7mB/t0BlNiTLogwGD"
            + "DW9v+X5Wf03fvM3gFTraw///jpfAw10B0HP6wipgEw==";

    private final static String BODY = "Method:GET\n" + "Hashed Path:bEM+o9tCkqf41JkAi8jgkkr5ZvU=\n"
            + "X-Ops-Content-Hash:2jmj7l5rSw0yVb/vlWAYkK/YBwk=\n" + "X-Ops-Timestamp:2011-07-14T10:10:55Z\n"
            + "X-Ops-UserId:serch";

    @Test
    public void testSign() throws Exception {
        assertEquals(SIGNED,
                new RSASignerImpl().sign(BODY, new File(this.getClass().getResource("/private.pem").toURI())));
    }

    @Test
    public void testUnsign() throws Exception {
        assertEquals(BODY,
                new RSASignerImpl().unsign(SIGNED, new File(this.getClass().getResource("/public.pem").toURI())));
    }

}
