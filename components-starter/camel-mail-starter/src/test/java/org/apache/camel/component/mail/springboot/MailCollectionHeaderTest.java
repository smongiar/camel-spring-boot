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
package org.apache.camel.component.mail.springboot;



import static org.apache.camel.test.junit5.TestSupport.assertIsInstanceOf;

import java.util.List;


import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.boot.CamelAutoConfiguration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.jvnet.mock_javamail.Mailbox;


@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@CamelSpringBootTest
@SpringBootTest(
    classes = {
        CamelAutoConfiguration.class,
        MailCollectionHeaderTest.class,
        MailCollectionHeaderTest.TestConfiguration.class
    }
)
public class MailCollectionHeaderTest {

    
    @Autowired
    ProducerTemplate template;
    
    @Autowired
    CamelContext context;
    
    @EndpointInject("mock:result")
    MockEndpoint mock;
    
    @Test
    public void testMailHeaderWithCollection() throws Exception {
        Mailbox.clearAll();

        String[] foo = new String[] { "Carlsberg", "Heineken" };
        template.sendBodyAndHeader("direct:a", "Hello World", "beers", foo);

        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived("Hello World");
        mock.message(0).header("beers").isNotNull();

        mock.assertIsSatisfied();

        Object beers = mock.getReceivedExchanges().get(0).getIn().getHeader("beers");
        assertNotNull(beers);
        List<?> list = assertIsInstanceOf(List.class, beers);
        assertEquals("Carlsberg", list.get(0));
        assertEquals("Heineken", list.get(1));
    }

    
    // *************************************
    // Config
    // *************************************

    @Configuration
    public class TestConfiguration {

        @Bean
        public RouteBuilder routeBuilder() {
            return new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:a").to("smtp://localhost?username=james@localhost");

                    from("pop3://localhost?username=james&password=secret&initialDelay=100&delay=100").to("mock:result");
                }
            };
        }
    }
    
   

}
