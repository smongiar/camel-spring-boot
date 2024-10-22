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
package org.apache.camel.component.platform.http.springboot;

import jakarta.activation.DataHandler;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.camel.Message;
import org.apache.camel.attachment.AttachmentMessage;
import org.apache.camel.attachment.CamelFileDataSource;
import org.apache.camel.component.platform.http.PlatformHttpEndpoint;
import org.apache.camel.http.base.HttpHelper;
import org.apache.camel.http.common.DefaultHttpBinding;
import org.apache.camel.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class SpringBootPlatformHttpBinding extends DefaultHttpBinding {
    private static final Logger LOG = LoggerFactory.getLogger(SpringBootPlatformHttpBinding.class);

    protected void populateRequestParameters(HttpServletRequest request, Message message) {
        super.populateRequestParameters(request, message);
        String path = request.getRequestURI();
        // skip leading slash
        if (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path != null) {
            PlatformHttpEndpoint endpoint = (PlatformHttpEndpoint) message.getExchange().getFromEndpoint();
            String consumerPath = endpoint.getPath();
            if (consumerPath != null && consumerPath.startsWith("/")) {
                consumerPath = consumerPath.substring(1);
            }
            if (useRestMatching(consumerPath)) {
                HttpHelper.evalPlaceholders(message.getHeaders(), path, consumerPath);
            }
        }
    }

    private boolean useRestMatching(String path) {
        return path.indexOf('{') > -1;
    }

    @Override
    protected void populateAttachments(HttpServletRequest request, Message message) {
        // check if there is multipart files, if so will put it into DataHandler
        if (request instanceof MultipartHttpServletRequest multipartHttpServletRequest) {
            File tmpFolder = (File) request.getServletContext().getAttribute(ServletContext.TEMPDIR);
            multipartHttpServletRequest.getFileMap().forEach((name, multipartFile) -> {
                try {
                    Path uploadedTmpFile = Paths.get(tmpFolder.getPath(), name);
                    multipartFile.transferTo(uploadedTmpFile);

                    if (name != null) {
                        name = name.replaceAll("[\n\r\t]", "_");
                    }

                    boolean accepted = true;

                    if (getFileNameExtWhitelist() != null) {
                        String ext = FileUtil.onlyExt(name);
                        if (ext != null) {
                            ext = ext.toLowerCase(Locale.US);
                            if (!getFileNameExtWhitelist().equals("*") && !getFileNameExtWhitelist().contains(ext)) {
                                accepted = false;
                            }
                        }
                    }

                    if (accepted) {
                        AttachmentMessage am = message.getExchange().getMessage(AttachmentMessage.class);
                        am.addAttachment(name, new DataHandler(new CamelFileDataSource(uploadedTmpFile.toFile(), name)));
                    } else {
                        LOG.debug(
                                "Cannot add file as attachment: {} because the file is not accepted according to fileNameExtWhitelist: {}",
                                name, getFileNameExtWhitelist());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

}
