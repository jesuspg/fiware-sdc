package com.telefonica.euro_iaas.sdc.util;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.*;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;

/**
 * Default MixlibAuthenticationDigester implementation.
 *
 * @author Sergio Arroyo
 *
 */
public class MixlibAuthenticationDigesterImpl
    implements MixlibAuthenticationDigester{

    private Signer signer;
    private SystemPropertiesProvider propertiesProvider;

    public final static String AUTH_HEADER_PREFIX = "X-Ops-Authorization-";
    public final static String CONTENT_HASH_HEADER = "X-Ops-Content-Hash";
    public final static String TIMESTAMP_HEADER = "X-Ops-Timestamp";
    public final static String USER_HEADER = "X-Ops-Userid";
    public final static String SIGN_VERSION_HEADER = "X-Ops-Sign";
    public final static String SIGN_VERSION_VALUE = "version=1.0";


    private final static String BODY_TEMPLATE =
        "Method:{0}\nHashed Path:{1}\nX-Ops-Content-Hash:{2}\n"
        + "X-Ops-Timestamp:{3}\nX-Ops-UserId:{4}";

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> digest(String method, String path, String body,
            Date timestamp, String id, String pkUrl) {
        try {
            DateFormat df = new SimpleDateFormat(
                    propertiesProvider.getProperty(CHEF_DATE_FORMAT));
            df.setTimeZone(TimeZone.getTimeZone(
                    propertiesProvider.getProperty(CHEF_TIME_ZONE)));

            Map<String, String> headers = new HashMap<String, String>();
            headers.put(CONTENT_HASH_HEADER, getHash(body));
            headers.put(TIMESTAMP_HEADER, df.format(timestamp));
            headers.put(USER_HEADER, id);
            headers.put(SIGN_VERSION_HEADER, SIGN_VERSION_VALUE);

            String digest = MessageFormat.format(BODY_TEMPLATE,
                    method.toUpperCase(), getHash(path), getHash(body),
                    df.format(timestamp), id);
            digest = signer.sign(digest, new File(pkUrl));
            Integer length;
            Integer iteration = 1;
            while (!digest.isEmpty()) {
                length = digest.length() >= 60? 60:digest.length();
                headers.put(AUTH_HEADER_PREFIX + iteration, digest.substring(
                        0, length));
                digest = digest.substring(length);
                iteration = iteration + 1;
            }
            return headers;
        } catch (Exception e) {
            throw new SdcRuntimeException();
        }
    }

    private String getHash(String body) {

        String b64 = Base64
                .encodeBase64String(DigestUtils.sha(body.getBytes()));
        return b64;
    }


    /**
     * @param signer the signer to set
     */
    public void setSigner(Signer signer) {
        this.signer = signer;
    }

    /**
     * @param propertiesProvider the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

}

