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
package org.apache.camel.dataformat.soap;


import java.io.IOException;

import com.example.customerservice.GetCustomersByName;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.dataformat.soap.name.ElementNameStrategy;
import org.apache.camel.dataformat.soap.name.TypeNameStrategy;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;


@DirtiesContext
@CamelSpringBootTest
@SpringBootTest(
    classes = {
        CamelAutoConfiguration.class,
        SoapRoundtripTest.class,
        SoapRoundtripTest.TestConfiguration.class
    }
)

public class SoapRoundtripTest {

    @EndpointInject("mock:result")
    protected MockEndpoint resultEndpoint;

    @Produce("direct:start")
    protected ProducerTemplate producer;

    @Test
    public void testRoundTrip() throws IOException, InterruptedException {
        resultEndpoint.expectedMessageCount(1);
        GetCustomersByName request = new GetCustomersByName();
        request.setName("Mueller");
        producer.sendBody(request);
        resultEndpoint.assertIsSatisfied();
        Exchange exchange = resultEndpoint.getExchanges().get(0);
        GetCustomersByName received = exchange.getIn().getBody(
                GetCustomersByName.class);
        assertNotNull(received);
        assertEquals("Mueller", received.getName());
    }

    
    // *************************************
    // Config
    // *************************************

    @Configuration
    public class TestConfiguration {
        
        @Bean
        public RouteBuilder routeBuilder() {
            return new RouteBuilder() {

                String jaxbPackage = GetCustomersByName.class.getPackage().getName();

                @Override
                public void configure() throws Exception {
                    ElementNameStrategy elNameStrat = new TypeNameStrategy();
                    from("direct:start").marshal().soap(jaxbPackage, elNameStrat).unmarshal()
                        .soap(jaxbPackage, elNameStrat).to("mock:result");
                }
            };
        }
    }
}
