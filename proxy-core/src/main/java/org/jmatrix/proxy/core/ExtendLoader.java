package org.jmatrix.proxy.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author jmatrix
 * @date 16/4/23
 */
public class ExtendLoader<S> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExtendLoader.class);

    private final static String PREFIX = "META-INF/proxy/services/";

    private String serviceName;

    private Class<S> serviceCls;

    private ClassLoader classLoader;

    private boolean lazyLoad = false;

    private Map<String, Class<S>> serviceClsMap = new LinkedHashMap<>();

    private Map<String, S> serviceInstantsMap = new LinkedHashMap<>();

    private ExtendLoader(String serviceName, Class<S> serviceCls, ClassLoader classLoader) {
        this.serviceName = serviceName;
        this.serviceCls = serviceCls;
        this.classLoader = classLoader == null ? ClassLoader.getSystemClassLoader() : classLoader;
        serviceClsMap.clear();
        serviceInstantsMap.clear();
        lazyLoad = false;
    }

    /**
     * Acquire preferred provider, create new instance default.
     *
     * @param preferred
     * @return
     */
    public S  acquireProvider(String preferred) {
        return acquireProvider(preferred, true);
    }

    /**
     * Acquire preferred provider
     *
     * @param preferred
     * @param newInstantiate true:create a new instance every time, false: share instance
     * @return
     */
    public S acquireProvider(String preferred, boolean newInstantiate) {
        if (!lazyLoad) {
            loadProvider();
        }
        Class<S> cls = serviceClsMap.get(preferred);

        if (newInstantiate) {
            S provider = instantiateProvider(cls);
            if (serviceInstantsMap.get(preferred) == null) {
                serviceInstantsMap.put(preferred, provider);
            }
            return provider;
        } else {
            S provider = serviceInstantsMap.get(preferred);
            if (provider == null) {
                provider = instantiateProvider(cls);
                serviceInstantsMap.put(preferred, provider);
            }
            return provider;
        }
    }

    private void loadProvider() {
        try {
            Enumeration<URL> urlEnums = classLoader.getResources(PREFIX + serviceName);
            while (urlEnums.hasMoreElements()) {
                URL url = urlEnums.nextElement();
                parse(url);
            }
        } catch (IOException e) {
            LOGGER.error("load provider config failed.", e);
        }
    }

    private void parse(URL url) {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = url.openStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] providers = line.split("=");
                if (providers.length == 2) {
                    serviceClsMap.put(providers[0], (Class<S>) Class.forName(providers[1], false, classLoader));
                }
            }
        } catch (Exception e) {
            LOGGER.error("parse provider config failed.");
        } finally {
            try {
                inputStream.close();
                bufferedReader.close();
            } catch (Exception e) {
                LOGGER.error("can not close config file.");
            }
        }
    }

    private S instantiateProvider(Class<S> cls) {
        try {
            if (!this.serviceCls.isAssignableFrom(cls)) {
                LOGGER.error("provider[{}] is not the sub class of {}", cls.getName(), serviceCls.getName());
            }
            return (S) this.serviceCls.cast(cls.newInstance());
        } catch (Exception e) {
            LOGGER.error("instantiate provider failed.", e);
        }
        return null;
    }


    public static ExtendLoader load(String serviceName, Class<?> serviceCls, ClassLoader classLoader) {
        return new ExtendLoader(serviceName, serviceCls, classLoader);
    }

    public static ExtendLoader load(String serviceName, Class<?> serviceCls) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return load(serviceName, serviceCls, classLoader);
    }

    public static ExtendLoader load(Class<?> serviceCls) {
        return load(serviceCls.getName(), serviceCls);
    }

}
