package com.chinaunicom.hsf.utils;

import com.chinaunicom.hsf.support.RemoteConsumer;
import com.chinaunicom.hsf.support.RemoteProvider;
import com.chinaunicom.hsf.annotation.ProxyConsumer;
import com.chinaunicom.hsf.annotation.ProxyProvider;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2020/3/28 15:00
 * @description:
 */
public class HsfFileUtils {
    private static Logger logger = LoggerFactory.getLogger(HsfFileUtils.class);
    private static final PathMatchingResourcePatternResolver PMRPR = new PathMatchingResourcePatternResolver(Thread.class.getClassLoader());

    private static Environment environment;
    private static final List<RemoteProvider> PROVIDERS = new Vector<RemoteProvider>();
    private static final List<RemoteConsumer> CONSUMERS = new Vector<RemoteConsumer>();

    private static final String PROVIDER_FILE_NAME = "hsf-provider.xml";

    private static final String CONSUMER_FILE_NAME = "hsf-consumer.xml";

    public static void setEnvironment(Environment environment) {
        HsfFileUtils.environment = environment;
    }

    public static void loadHsfProviderBeanDefinition(BeanDefinitionRegistry beanDefinitionRegistry) {
        try {
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanDefinitionRegistry;
            Environment environment = (Environment) defaultListableBeanFactory.getBean("environment");
            setEnvironment(environment);
            extractProviderList(beanDefinitionRegistry);
            File providerFile = new File(getClassPath() + PROVIDER_FILE_NAME);
            providerFile.deleteOnExit();
            providerFile.createNewFile();
            FileOutputStream foutput = new FileOutputStream(providerFile);
            foutput.write(generateProviderBeans(PROVIDERS));
            foutput.close();
            XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanDefinitionRegistry);
            xmlBeanDefinitionReader.loadBeanDefinitions("classpath:" + PROVIDER_FILE_NAME);
        } catch (Exception e) {
            logger.error("load hsf provider bean definition error", e);
        }
    }

    public static void loadHsfCousumerBeanDefinition(BeanDefinitionRegistry beanDefinitionRegistry) {
        try {
            extractConsumerList();
            File consumerFile = new File(getClassPath() + CONSUMER_FILE_NAME);
            consumerFile.deleteOnExit();
            consumerFile.createNewFile();
            FileOutputStream foutput = new FileOutputStream(consumerFile);
            foutput.write(genenrateConsumerBeans(CONSUMERS, PROVIDERS));
            foutput.close();
            XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanDefinitionRegistry);
            xmlBeanDefinitionReader.loadBeanDefinitions("classpath:" + CONSUMER_FILE_NAME);
        } catch (IOException e) {
            logger.error("load hsf consumer bean definition error", e);
        }
    }


    public static byte[] genenrateConsumerBeans(List<RemoteConsumer> consumers, List<RemoteProvider> providerList) {
        byte[] springBeans = getRemoteStrategy().equals("HSF") ? generateHsfConsumerBeans(consumers, providerList) : generateHsfConsumerBeans(consumers, providerList);
        if (logger.isInfoEnabled()) {
            logger.info(new String(springBeans));
        }

        return springBeans;
    }

    private static byte[] generateHsfConsumerBeans(List<RemoteConsumer> consumers, List<RemoteProvider> providerList) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream print = new PrintStream(output);
        Map<String, String> providerInterfaces = new HashMap();
        Iterator var5 = providerList.iterator();

        String consumerVersion;
        String consumerGroup;
        while (var5.hasNext()) {
            RemoteProvider provider = (RemoteProvider) var5.next();
            consumerVersion = getRealValue(provider.getVersion());
            consumerGroup = getRealValue(provider.getGroup());
            providerInterfaces.put(provider.getInterface() + ":" + consumerVersion + ":" + consumerGroup, provider.getRef());
        }
        print.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        print.println("<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        print.println("xmlns:hsf=\"http://www.taobao.com/hsf\"");
        print.println("xmlns=\"http://www.springframework.org/schema/beans\"");
        print.println("xsi:schemaLocation=\"http://www.springframework.org/schema/beans");
        print.println("http://www.springframework.org/schema/beans/spring-beans-2.5.xsd");
        print.println("http://www.taobao.com/hsf");
        print.println("http://www.taobao.com/hsf/hsf.xsd\">");
        var5 = consumers.iterator();

        while (true) {
            while (var5.hasNext()) {
                RemoteConsumer consumer = (RemoteConsumer) var5.next();
                consumerVersion = getRealValue(consumer.getVersion());
                consumerGroup = getRealValue(consumer.getGroup());
                if (!providerInterfaces.containsKey(consumer.getInterface() + ":" + consumerVersion + ":" + consumerGroup)) {
                    String beanId = getRealValue(consumer.getBeanId());
                    if (beanId == null || "".equals(beanId)) {
                        throw new RuntimeException("consumerId required[" + consumer.getInterface());
                    }

                    if (providerInterfaces.containsValue(beanId)) {
                        if (logger.isInfoEnabled()) {
                            logger.info("consumer[" + beanId + ":" + consumer.getInterface() + ":" + consumer.getVersion() + ":" + consumer.getGroup() + "]'s conflict.");
                        }
                    } else {
                        print.print("<hsf:consumer");
                        print.print(" id=\"");
                        print.print(beanId);
                        print.print("\"");
                        print.print(" interface=\"");
                        print.print(getRealValue(consumer.getInterface()));
                        print.print("\"");
                        print.print(" version=\"");
                        print.print(getRealValue(consumer.getVersion()));
                        print.print("\"");
                        if (consumer.getGroup() != null && !"".equals(consumer.getGroup())) {
                            print.print(" group=\"");
                            print.print(getRealValue(consumer.getGroup()));
                            print.print("\"");
                        }

                        if (consumer.getTarget() != null && !"".equals(consumer.getTarget())) {
                            print.print(" target=\"");
                            print.print(getRealValue(consumer.getTarget()));
                            print.print("\"");
                        }

                        if (consumer.getClientTimeout() != 0L) {
                            print.print(" clientTimeout=\"");
                            print.print(consumer.getClientTimeout());
                            print.print("\"");
                        }

                        if (consumer.getConnectionNum() != 0) {
                            print.print(" connectionNum=\"");
                            print.print(consumer.getConnectionNum());
                            print.print("\"");
                        }

                        print.println("></hsf:consumer>");
                    }
                } else if (logger.isInfoEnabled()) {
                    logger.info("consumer[" + consumer.getInterface() + ":" + consumerVersion + ":" + consumerGroup + "]'s provider exists; Do not publish consumer.");
                }
            }

            print.println("</beans>");
            print.flush();
            print.close();
            return output.toByteArray();
        }
    }

    private static void extractConsumerList() {
        try {
            List<String> consumerList = extractAnnotationClazzes(ProxyConsumer.class, "classpath*:com/*/**/*.class");
            for (String consumer : consumerList) {
                CONSUMERS.add(new RemoteConsumer(Class.forName(consumer)));
            }
        } catch (
                Exception var2) {
            throw new RuntimeException("extract proxy consumer error", var2);
        }
    }

    private static List<String> extractAnnotationClazzes(final Class<?> annotationClazz, String scanPath) throws IOException, ClassNotFoundException {
        Resource[] clazzes = PMRPR.getResources(scanPath);
        final List<String> clazzList = new ArrayList();
        ClassVisitor anaotationClassVisitor = new ClassVisitor(327680) {
            private final String PROXY_REMOTE_INTERFACE = "L" + annotationClazz.getName().replaceAll("\\.", "/") + ";";
            private String clazz;

            @Override
            public void visit(int i, int i1, String s, String s1, String s2, String[] strings) {
                super.visit(i, i1, s, s1, s2, strings);
                this.clazz = s;
            }

            @Override
            public AnnotationVisitor visitAnnotation(String s, boolean b) {
                if (this.PROXY_REMOTE_INTERFACE.equals(s)) {
                    clazzList.add(this.clazz.replaceAll("/", "."));
                }

                return super.visitAnnotation(s, b);
            }
        };
        Resource[] var5 = clazzes;
        int var6 = clazzes.length;

        for (int var7 = 0; var7 < var6; ++var7) {
            Resource clazz = var5[var7];
            ClassReader cr = new ClassReader(clazz.getInputStream());
            cr.accept(anaotationClassVisitor, 0);
        }

        return clazzList;
    }

    private static void extractProviderList(BeanDefinitionRegistry beanDefinitionRegistry) {
        try {
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanDefinitionRegistry;
            String[] beanNames = defaultListableBeanFactory.getBeanNamesForAnnotation(ProxyProvider.class);
            StringUtils.sortStringArray(beanNames);
            if (beanNames.length > 0) {
                for (String beanName : beanNames) {
                    PROVIDERS.add(new RemoteProvider(defaultListableBeanFactory.getType(beanName), beanName));
                }
            }
        } catch (Exception var2) {
            throw new RuntimeException("extract proxy provider error", var2);
        }
    }

    private static String getClassPath() throws FileNotFoundException {
        String classpath = ResourceUtils.getURL("classpath:").getPath();
        ;
        return classpath;
    }

    private static byte[] generateProviderBeans(List<RemoteProvider> providers) {
        byte[] springBeans = getRemoteStrategy().equals("HSF") ? generateHsfProviderBeans(providers) : generateHsfProviderBeans(providers);
        if (logger.isInfoEnabled()) {
            logger.info(new String(springBeans));
        }

        return springBeans;
    }


    private static String getRemoteStrategy() {
        String remoteStrategy = environment.getProperty("remote.strategy");
        if (remoteStrategy == null) {
            remoteStrategy = "HSF";
        }

        return remoteStrategy;
    }

    private static byte[] generateHsfProviderBeans(List<RemoteProvider> providers) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream print = new PrintStream(output);
        print.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        print.println("<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        print.println("xmlns:hsf=\"http://www.taobao.com/hsf\"");
        print.println("xmlns=\"http://www.springframework.org/schema/beans\"");
        print.println("xsi:schemaLocation=\"http://www.springframework.org/schema/beans");
        print.println("http://www.springframework.org/schema/beans/spring-beans-2.5.xsd");
        print.println("http://www.taobao.com/hsf");
        print.println("http://www.taobao.com/hsf/hsf.xsd\">");
        Iterator var4 = providers.iterator();
        while (true) {
            while (var4.hasNext()) {
                RemoteProvider provider = (RemoteProvider) var4.next();
                String providerRef = getRealValue(provider.getRef());
                if (providerRef != null && !"".equals(providerRef)) {
                    print.print("<hsf:provider");
                    print.print(" id=\"");
                    print.print(providerRef + "-" + getRealValue(provider.getVersion()) + "-provider");
                    print.print("\"");
                    print.print(" interface=\"");
                    print.print(getRealValue(provider.getInterface()));
                    print.print("\"");
                    print.print(" ref=\"");
                    print.print(getRealValue(provider.getRef()));
                    print.print("\"");
                    print.print(" version=\"");
                    print.print(getRealValue(provider.getVersion()));
                    print.print("\"");
                    if (provider.getGroup() != null && !"".equals(provider.getGroup())) {
                        print.print(" group=\"");
                        print.print(getRealValue(provider.getGroup()));
                        print.print("\"");
                    }

                    if (provider.getClientTimeout() != 0L) {
                        print.print(" clientTimeout=\"");
                        print.print(provider.getClientTimeout());
                        print.print("\"");
                    }

                    if (provider.getCorePoolSize() != 0) {
                        print.print(" corePoolSize=\"");
                        print.print(provider.getCorePoolSize());
                        print.print("\"");
                    }

                    if (provider.getMaxPoolSize() != 0) {
                        print.print(" maxPoolSize=\"");
                        print.print(provider.getMaxPoolSize());
                        print.print("\"");
                    }

                    if (provider.getSerializeType() != null && !"".equals(provider.getSerializeType())) {
                        print.print(" serializeType=\"");
                        print.print(getRealValue(provider.getSerializeType()));
                        print.print("\"");
                    }

                    print.println("></hsf:provider>");
                } else if (logger.isInfoEnabled()) {
                    logger.info("Provider [" + getRealValue(provider.getInterface()) + ":" + getRealValue(provider.getVersion()) + "] ref not exists.");
                }
            }

            print.println("</beans>");
            print.flush();
            print.close();
            return output.toByteArray();
        }
    }

    private static String getRealValue(String value) {
        String result = null;
        if (value != null) {
            if (value.startsWith("${") && value.endsWith("}")) {
                String key = value.substring(2, value.length() - 1);
                result = environment.getProperty(key);
            } else {
                result = value;
            }
        }
        return result;
    }

}
