/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cxf.dosgi.systests2.multi;

import javax.inject.Inject;

import org.apache.cxf.dosgi.samples.greeter.rest.GreeterInfo;
import org.apache.cxf.dosgi.samples.greeter.rest.GreeterService;
import org.apache.cxf.dosgi.samples.greeter.rest.GreetingPhrase;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.BundleContext;

import static org.ops4j.pax.exam.CoreOptions.frameworkStartLevel;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

@RunWith(JUnit4TestRunner.class)
public class TestExportRestService extends AbstractDosgiTest {

    @Inject
    BundleContext bundleContext;
    
    String webPort = "9091";

    @Configuration
    public Option[] configure() throws Exception {
        return new Option[] {
                MultiBundleTools.getDistroWithDiscovery(),
                systemProperty("org.osgi.service.http.port").value(webPort),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("INFO"),
                mavenBundle().groupId("org.apache.servicemix.bundles")
                    .artifactId("org.apache.servicemix.bundles.junit").version("4.9_2"),
                mavenBundle().groupId("org.apache.cxf.dosgi.samples")
                    .artifactId("cxf-dosgi-ri-samples-greeter-rest-interface").versionAsInProject(),
                mavenBundle().groupId("org.apache.cxf.dosgi.samples")
                    .artifactId("cxf-dosgi-ri-samples-greeter-rest-impl").versionAsInProject(),
                frameworkStartLevel(100)
        };
    }

    @Test
    public void testEndpointAvailable() throws Exception {
        waitWebPage("http://localhost:" + webPort + "/greeter/greeter/greeting/Chris");
        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            GreeterService greeterService = JAXRSClientFactory.create("http://localhost:" + webPort + "/greeter",
                                                                      GreeterService.class);
            GreeterInfo result = greeterService.greetMe("Chris");
            GreetingPhrase greeting = result.getGreetings().get(0);
            Assert.assertEquals("Hello", greeting.getPhrase());
            Assert.assertEquals("Chris", greeting.getName());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
