package com.chinaunicom.hsf.support;


import com.chinaunicom.hsf.annotation.ProxyProvider;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2019/10/11 15:39
 * @description:
 */
public class RemoteProvider {
    private String version;
    private long clientTimeout;
    private String serializeType;
    private int corePoolSize;
    private int maxPoolSize;
    private String intface;
    private String group;
    private String ref;

    public RemoteProvider(Class<?> providerClazz, String beanName) {
        ProxyProvider proxyProvider = providerClazz.getAnnotation(ProxyProvider.class);
        this.version = proxyProvider.version();
        this.clientTimeout = proxyProvider.clientTimeout();
        this.serializeType = proxyProvider.serializeType();
        this.corePoolSize = proxyProvider.corePoolSize();
        this.maxPoolSize = proxyProvider.maxPoolSize();
        this.intface = providerClazz.getInterfaces()[0].getName();
        this.group = proxyProvider.group();
        this.ref = beanName;
    }

    public String getRef() {
        return this.ref;
    }

    public String getVersion() {
        return this.version;
    }

    public long getClientTimeout() {
        return this.clientTimeout;
    }

    public String getSerializeType() {
        return this.serializeType;
    }

    public int getCorePoolSize() {
        return this.corePoolSize;
    }

    public int getMaxPoolSize() {
        return this.maxPoolSize;
    }

    public String getInterface() {
        return this.intface;
    }

    public String getGroup() {
        return this.group;
    }

    @Override
    public String toString() {
        return "RemoteProvider{" +
                "version='" + version + '\'' +
                ", clientTimeout=" + clientTimeout +
                ", serializeType='" + serializeType + '\'' +
                ", corePoolSize=" + corePoolSize +
                ", maxPoolSize=" + maxPoolSize +
                ", intface='" + intface + '\'' +
                ", group='" + group + '\'' +
                ", ref='" + ref + '\'' +
                '}';
    }
}
