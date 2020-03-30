package com.chinaunicom.hsf.support;


import com.chinaunicom.hsf.annotation.ProxyConsumer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2019/10/11 15:40
 * @description:
 */
public class RemoteConsumer {
    private String beanId;
    private String version;
    private long clientTimeout;
    private int connectionNum;
    private String intface;
    private String group;
    private String target;

    public RemoteConsumer(Class<?> consumerClass) {
        ProxyConsumer proxyConsumer = (ProxyConsumer) consumerClass.getAnnotation(ProxyConsumer.class);
        this.version = proxyConsumer.version();
        this.clientTimeout = proxyConsumer.clientTimeout();
        this.connectionNum = proxyConsumer.connectionNum();
        this.intface = consumerClass.getName();
        this.beanId = proxyConsumer.beanId();
        this.group = proxyConsumer.group();
        this.target = proxyConsumer.target();
    }

    public String getTarget() {
        return this.target;
    }

    public String getBeanId() {
        return this.beanId;
    }

    public String getVersion() {
        return this.version;
    }

    public long getClientTimeout() {
        return this.clientTimeout;
    }

    public int getConnectionNum() {
        return this.connectionNum;
    }

    public String getInterface() {
        return this.intface;
    }

    public String getGroup() {
        return this.group;
    }

    @Override
    public String toString() {
        return "RemoteConsumer{" +
                "beanId='" + beanId + '\'' +
                ", version='" + version + '\'' +
                ", clientTimeout=" + clientTimeout +
                ", connectionNum=" + connectionNum +
                ", intface='" + intface + '\'' +
                ", group='" + group + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
