/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.aws2.ses.springboot;

import javax.annotation.Generated;
import org.apache.camel.component.aws2.ses.Ses2Component;
import org.apache.camel.component.aws2.ses.Ses2Configuration;
import org.apache.camel.spring.boot.ComponentConfigurationPropertiesCommon;
import org.springframework.boot.context.properties.ConfigurationProperties;
import software.amazon.awssdk.core.Protocol;
import software.amazon.awssdk.services.ses.SesClient;

/**
 * Send e-mails through AWS SES service using AWS SDK version 2.x.
 * 
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@Generated("org.apache.camel.springboot.maven.SpringBootAutoConfigurationMojo")
@ConfigurationProperties(prefix = "camel.component.aws2-ses")
public class Ses2ComponentConfiguration
        extends
            ComponentConfigurationPropertiesCommon {

    /**
     * Whether to enable auto configuration of the aws2-ses component. This is
     * enabled by default.
     */
    private Boolean enabled;
    /**
     * To use the AmazonSimpleEmailService as the client. The option is a
     * software.amazon.awssdk.services.ses.SesClient type.
     */
    private SesClient amazonSESClient;
    /**
     * component configuration. The option is a
     * org.apache.camel.component.aws2.ses.Ses2Configuration type.
     */
    private Ses2Configuration configuration;
    /**
     * Whether the producer should be started lazy (on the first message). By
     * starting lazy you can use this to allow CamelContext and routes to
     * startup in situations where a producer may otherwise fail during starting
     * and cause the route to fail being started. By deferring this startup to
     * be lazy then the startup failure can be handled during routing messages
     * via Camel's routing error handlers. Beware that when the first message is
     * processed then creating and starting the producer may take a little time
     * and prolong the total processing time of the processing.
     */
    private Boolean lazyStartProducer = false;
    /**
     * Set the need for overidding the endpoint. This option needs to be used in
     * combination with uriEndpointOverride option
     */
    private Boolean overrideEndpoint = false;
    /**
     * To define a proxy host when instantiating the SES client
     */
    private String proxyHost;
    /**
     * To define a proxy port when instantiating the SES client
     */
    private Integer proxyPort;
    /**
     * To define a proxy protocol when instantiating the SES client
     */
    private Protocol proxyProtocol = Protocol.HTTPS;
    /**
     * The region in which SES client needs to work. When using this parameter,
     * the configuration will expect the lowercase name of the region (for
     * example ap-east-1) You'll need to use the name Region.EU_WEST_1.id()
     */
    private String region;
    /**
     * List of comma separated reply-to email address(es) for the message,
     * override it using 'CamelAwsSesReplyToAddresses' header.
     */
    private String replyToAddresses;
    /**
     * The email address to which bounce notifications are to be forwarded,
     * override it using 'CamelAwsSesReturnPath' header.
     */
    private String returnPath;
    /**
     * The subject which is used if the message header 'CamelAwsSesSubject' is
     * not present.
     */
    private String subject;
    /**
     * List of comma separated destination email address. Can be overriden with
     * 'CamelAwsSesTo' header.
     */
    private String to;
    /**
     * If we want to trust all certificates in case of overriding the endpoint
     */
    private Boolean trustAllCertificates = false;
    /**
     * Set the overriding uri endpoint. This option needs to be used in
     * combination with overrideEndpoint option
     */
    private String uriEndpointOverride;
    /**
     * Set whether the Ses client should expect to load credentials through a
     * default credentials provider or to expect static credentials to be passed
     * in.
     */
    private Boolean useDefaultCredentialsProvider = false;
    /**
     * Whether autowiring is enabled. This is used for automatic autowiring
     * options (the option must be marked as autowired) by looking up in the
     * registry to find if there is a single instance of matching type, which
     * then gets configured on the component. This can be used for automatic
     * configuring JDBC data sources, JMS connection factories, AWS Clients,
     * etc.
     */
    private Boolean autowiredEnabled = true;
    /**
     * Amazon AWS Access Key
     */
    private String accessKey;
    /**
     * Amazon AWS Secret Key
     */
    private String secretKey;

    public SesClient getAmazonSESClient() {
        return amazonSESClient;
    }

    public void setAmazonSESClient(SesClient amazonSESClient) {
        this.amazonSESClient = amazonSESClient;
    }

    public Ses2Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Ses2Configuration configuration) {
        this.configuration = configuration;
    }

    public Boolean getLazyStartProducer() {
        return lazyStartProducer;
    }

    public void setLazyStartProducer(Boolean lazyStartProducer) {
        this.lazyStartProducer = lazyStartProducer;
    }

    public Boolean getOverrideEndpoint() {
        return overrideEndpoint;
    }

    public void setOverrideEndpoint(Boolean overrideEndpoint) {
        this.overrideEndpoint = overrideEndpoint;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public Protocol getProxyProtocol() {
        return proxyProtocol;
    }

    public void setProxyProtocol(Protocol proxyProtocol) {
        this.proxyProtocol = proxyProtocol;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getReplyToAddresses() {
        return replyToAddresses;
    }

    public void setReplyToAddresses(String replyToAddresses) {
        this.replyToAddresses = replyToAddresses;
    }

    public String getReturnPath() {
        return returnPath;
    }

    public void setReturnPath(String returnPath) {
        this.returnPath = returnPath;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Boolean getTrustAllCertificates() {
        return trustAllCertificates;
    }

    public void setTrustAllCertificates(Boolean trustAllCertificates) {
        this.trustAllCertificates = trustAllCertificates;
    }

    public String getUriEndpointOverride() {
        return uriEndpointOverride;
    }

    public void setUriEndpointOverride(String uriEndpointOverride) {
        this.uriEndpointOverride = uriEndpointOverride;
    }

    public Boolean getUseDefaultCredentialsProvider() {
        return useDefaultCredentialsProvider;
    }

    public void setUseDefaultCredentialsProvider(
            Boolean useDefaultCredentialsProvider) {
        this.useDefaultCredentialsProvider = useDefaultCredentialsProvider;
    }

    public Boolean getAutowiredEnabled() {
        return autowiredEnabled;
    }

    public void setAutowiredEnabled(Boolean autowiredEnabled) {
        this.autowiredEnabled = autowiredEnabled;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}