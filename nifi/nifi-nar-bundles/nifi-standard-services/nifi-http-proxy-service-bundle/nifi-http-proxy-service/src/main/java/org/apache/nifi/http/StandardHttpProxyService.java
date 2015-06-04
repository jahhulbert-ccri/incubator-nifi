/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.http;

import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnEnabled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.controller.AbstractControllerService;
import org.apache.nifi.controller.ConfigurationContext;
import org.apache.nifi.processor.util.StandardValidators;
import org.apache.nifi.reporting.InitializationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Tags({"http", "https", "proxy", "webproxy", "server",})
@CapabilityDescription("Standard implementation of the HttpProxyService. Provides the ability to configure "
        + "an http/https web proxy once and reuse that configuration throughout the application")
public class StandardHttpProxyService extends AbstractControllerService implements HttpProxyService {

    public static final PropertyDescriptor PROXY_HOST = new PropertyDescriptor.Builder()
            .name("ProxyHost")
            .description("The hostname or ip of the proxy server")
            .defaultValue(null)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .sensitive(false)
            .build();
    public static final PropertyDescriptor PROXY_PORT = new PropertyDescriptor.Builder()
            .name("ProxyPort")
            .description("The port to use for the http proxy server")
            .defaultValue(null)
            .addValidator(StandardValidators.POSITIVE_INTEGER_VALIDATOR)
            .sensitive(false)
            .build();

    private static final List<PropertyDescriptor> properties;
    private ConfigurationContext configContext;

    static {
        List<PropertyDescriptor> props = new ArrayList<>();
        props.add(PROXY_HOST);
        props.add(PROXY_PORT);
        properties = Collections.unmodifiableList(props);
    }

    @OnEnabled
    public void onConfigured(final ConfigurationContext context) throws InitializationException {
        configContext = context;
    }

    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return properties;
    }


    @Override
    public String toString() {
        return "HttpProxyService[id=" + getIdentifier() + "]";
    }

    @Override
    public String getProxyHost() {
        return configContext.getProperty(PROXY_HOST).getValue();
    }

    @Override
    public int getProxyPort() {
        return Integer.parseInt(configContext.getProperty(PROXY_PORT).getValue());
    }
}
