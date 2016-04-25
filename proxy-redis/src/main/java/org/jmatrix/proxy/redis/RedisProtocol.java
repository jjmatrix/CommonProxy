package org.jmatrix.proxy.redis;

/**
 * @author jmatrix
 * @date 16/4/4
 */
public class RedisProtocol {
    public static final byte CR = (byte) 13;
    public static final byte LF = (byte) 10;
    public static final byte[] CRLF = {(byte) 13, (byte) 10};
    public static final byte[] SPACE = {(byte) 32};
    public static final int CRLF_LEN = CRLF.length;
    public static final int DELIMETER_LEN = SPACE.length;
    public static final byte ERR_BYTE = (byte) 45; // -
    public static final byte OK_BYTE = (byte) 43; // +
    public static final byte COUNT_BYTE = (byte) 42; // *
    public static final byte SIZE_BYTE = (byte) 36; // $
    public static final byte NUM_BYTE = (byte) 58; // :
    public static final byte ASCII_ZERO = (byte) 48; // 0
}
