package org.jmatrix.proxy.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author jmatrix
 * @date 16/4/17
 */
public class RedisServerMock {

    private Logger logger = LoggerFactory.getLogger(RedisServerMock.class);

    private final static String MOCK_DATA_URL = "~/redis_resp.data";

    private Map<String, String> cmdResp = new HashMap<>();

    public void loadRespData() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(getMockDataUrl()));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] redisRespLine = line.split("\\\\s+");
                if (redisRespLine.length == 2) {
                    cmdResp.put(redisRespLine[0], redisRespLine[1]);
                }
            }
        } catch (Exception e) {
            logger.error("load redis resp data error.", e);
        }
    }

    protected String getMockDataUrl() {
        return MOCK_DATA_URL;
    }

    public Optional<String> mock(String req) {
        return Optional.ofNullable(this.cmdResp.get(req));
    }

}
