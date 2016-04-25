package org.jmatrix.proxy.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @author jmatrix
 * @date 16/4/20
 */
public class FileUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    /**
     * read file content
     *
     * @param filename
     * @return
     */
    public static String readFile(String filename) {
        StringBuilder resultBuild = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = null;
            while ((line = reader.readLine()) != null) {
                resultBuild.append(line);
            }
        } catch (Exception e) {
            try {
                reader.close();
            } catch (Exception ex) {
                LOGGER.error("close reader error", e);
            }
        }
        return resultBuild.toString();
    }

}
