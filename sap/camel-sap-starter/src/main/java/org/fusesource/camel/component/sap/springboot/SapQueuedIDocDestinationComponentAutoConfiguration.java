package org.fusesource.camel.component.sap.springboot;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.spi.ComponentCustomizer;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.apache.camel.spring.boot.ComponentConfigurationProperties;
import org.apache.camel.spring.boot.util.CamelPropertiesHelper;
import org.apache.camel.spring.boot.util.ConditionalOnCamelContextAndAutoConfigurationBeans;
import org.apache.camel.spring.boot.util.ConditionalOnHierarchicalProperties;
import org.apache.camel.spring.boot.util.HierarchicalPropertiesEvaluator;
import org.fusesource.camel.component.sap.SapQueuedIDocDestinationComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@Configuration(proxyBeanMethods = false)
@Conditional(ConditionalOnCamelContextAndAutoConfigurationBeans.class)
@EnableConfigurationProperties({ComponentConfigurationProperties.class,SapQueuedIDocDestinationComponentConfiguration.class})
@ConditionalOnHierarchicalProperties({"camel.component", "camel.component.sap-qidoc-destination"})
@AutoConfigureAfter(CamelAutoConfiguration.class)
public class SapQueuedIDocDestinationComponentAutoConfiguration {

    @Autowired
    private ApplicationContext applicationContext;
    private final CamelContext camelContext;
    @Autowired
    private SapQueuedIDocDestinationComponentConfiguration configuration;

    public SapQueuedIDocDestinationComponentAutoConfiguration(
            org.apache.camel.CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Lazy
    @Bean
    public ComponentCustomizer configureSapQueuedIDocDestinationComponent() {
        return new ComponentCustomizer() {
            @Override
            public void configure(String name, Component target) {
                CamelPropertiesHelper.copyProperties(camelContext, configuration, target);
            }
            @Override
            public boolean isEnabled(String name, Component target) {
                return HierarchicalPropertiesEvaluator.evaluate(
                        applicationContext,
                        "camel.component.customizer",
                        "camel.component.sap-qidoc-destination.customizer")
                    && target instanceof SapQueuedIDocDestinationComponent;
            }
        };
    }
}