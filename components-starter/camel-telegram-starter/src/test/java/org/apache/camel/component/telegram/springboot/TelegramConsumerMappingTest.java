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
package org.apache.camel.component.telegram.springboot;


import java.time.Instant;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.telegram.TelegramConstants;
import org.apache.camel.component.telegram.model.Chat;
import org.apache.camel.component.telegram.model.IncomingMessage;
import org.apache.camel.component.telegram.model.MessageResult;
import org.apache.camel.component.telegram.model.User;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;


@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@CamelSpringBootTest
@SpringBootTest(
    classes = {
        CamelAutoConfiguration.class,
        TelegramConsumerMappingTest.class,
        TelegramConsumerMappingTest.TestConfiguration.class
    }
)
public class TelegramConsumerMappingTest extends TelegramTestSupport {

    
    static TelegramMockRoutes mockRoutes;
    
    @EndpointInject("mock:telegram")
    private MockEndpoint endpoint;

    @Test
    public void testMessageMapping() throws Exception {
        endpoint.expectedMinimumMessageCount(1);
        endpoint.expectedMessageCount(1);
        endpoint.assertIsSatisfied(5000);

        Exchange ex = endpoint.getExchanges().get(0);
        Message m = ex.getIn();

        assertNotNull(m);

        // checking headers
        assertEquals("-45658", m.getHeader(TelegramConstants.TELEGRAM_CHAT_ID));

        // checking body
        assertNotNull(m.getBody());
        assertTrue(m.getBody() instanceof IncomingMessage);
        IncomingMessage body = (IncomingMessage) m.getBody();

        assertEquals("a message", body.getText());
        assertEquals(Long.valueOf(179L), body.getMessageId());
        assertEquals(Instant.ofEpochSecond(1463436626L), body.getDate());

        // checking from
        User user = body.getFrom();
        assertNotNull(user);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals(Long.valueOf(1585844777), user.getId());

        // checking chat
        Chat chat = body.getChat();
        assertNotNull(chat);
        assertEquals("-45658", chat.getId());
        assertEquals("A chat group", chat.getTitle());
        assertEquals("group", chat.getType());

    }

    @Test
    public void testMessageResultMapping() {
        MessageResult messageResult = getJSONResource("messages/updates-sendLocation.json", MessageResult.class);

        assertEquals(true, messageResult.isOk());
        assertEquals(true, messageResult.isOk());
        assertEquals((Long) 33L, messageResult.getMessage().getMessageId());
        assertEquals(Instant.ofEpochSecond(1548091564).getEpochSecond(), messageResult.getMessage().getDate().getEpochSecond());
        assertEquals((Long) 665977497L, messageResult.getMessage().getFrom().getId());
        assertEquals(true, messageResult.getMessage().getFrom().isBot());
        assertEquals("camelbot", messageResult.getMessage().getFrom().getFirstName());
        assertEquals("camel_component_bot", messageResult.getMessage().getFrom().getUsername());

        assertEquals("-182520913", messageResult.getMessage().getChat().getId());
        assertEquals("testgroup", messageResult.getMessage().getChat().getTitle());
        assertEquals("group", messageResult.getMessage().getChat().getType());
        assertEquals(true, messageResult.getMessage().getChat().isAllMembersAreAdministrators());

        assertEquals(59.9386292, messageResult.getMessage().getLocation().getLatitude(), 1.0E-07);
        assertEquals(30.3141308, messageResult.getMessage().getLocation().getLongitude(), 1.0E-07);
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
                    from("telegram:bots?authorizationToken=mock-token").to("mock:telegram");
                }
            };
        }

    }
    
    @Override
    @Bean
    protected TelegramMockRoutes createMockRoutes() {
        mockRoutes =
            new TelegramMockRoutes(port)
            .addEndpoint(
                    "getUpdates",
                    "GET",
                    String.class,
                    TelegramTestUtil.stringResource("messages/updates-single.json"),
                    TelegramTestUtil.stringResource("messages/updates-empty.json"));
        return mockRoutes;
    }
}
