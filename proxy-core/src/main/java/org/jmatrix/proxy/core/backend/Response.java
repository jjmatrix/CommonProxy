package org.jmatrix.proxy.core.backend;

/**
 * @author jmatrix
 * @date 17/1/14
 */
public interface Response {

    byte[] getResponseAsBytes();

    String getResponseAsString();
}
