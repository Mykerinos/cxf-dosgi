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

import static org.apache.cxf.dosgi.systests2.multi.GreeterServiceProxyFactory.createGreeterServiceProxy;
import static org.ops4j.pax.exam.CoreOptions.frameworkStartLevel;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.provision;
import static org.ops4j.pax.exam.CoreOptions.streamBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import java.io.InputStream;
import java.util.Map;

import javax.inject.Inject;

import org.apache.cxf.dosgi.samples.greeter.GreeterService;
import org.apache.cxf.dosgi.samples.greeter.GreetingPhrase;
import org.apache.cxf.dosgi.systests2.multi.customintent.AddGreetingPhraseInterceptor;
import org.apache.cxf.dosgi.systests2.multi.customintent.CustomFeature;
import org.apache.cxf.dosgi.systests2.multi.customintent.CustomIntentActivator;
import org.apache.cxf.dosgi.systests2.multi.customintent.service.EmptyGreeterService;
import org.apache.cxf.dosgi.systests2.multi.customintent.service.GreeterServiceWithCustomIntentActivator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.tinybundles.core.TinyBundles;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

@RunWith(PaxExam.class)
public class TestCustomIntent extends AbstractDosgiTest {

    @Inject
    BundleContext bundleContext;

    protected static InputStream getCustomIntentBundle() {
        return TinyBundles.bundle()
                .add(CustomIntentActivator.class)
                .add(CustomFeature.class)
                .add(AddGreetingPhraseInterceptor.class)
                .set(Constants.BUNDLE_SYMBOLICNAME, "CustomIntent")
                .set(Constants.BUNDLE_ACTIVATOR, CustomIntentActivator.class.getName()).build(TinyBundles.withBnd());
    }

    protected static InputStream getServiceBundle() {
        return TinyBundles.bundle()
                .add(GreeterServiceWithCustomIntentActivator.class)
                .add(EmptyGreeterService.class)
                .set(Constants.BUNDLE_SYMBOLICNAME, "EmptyGreeterService")
                .set(Constants.BUNDLE_ACTIVATOR, GreeterServiceWithCustomIntentActivator.class.getName())
                .build(TinyBundles.withBnd());
    }

    @Configuration
    public static Option[] configure() throws Exception {
        return new Option[] {
                MultiBundleTools.getDistro(),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("INFO"),
                mavenBundle().groupId("org.apache.servicemix.bundles")
                    .artifactId("org.apache.servicemix.bundles.junit").version("4.9_2"),
                mavenBundle().groupId("org.apache.cxf.dosgi.samples")
                    .artifactId("cxf-dosgi-ri-samples-greeter-interface").versionAsInProject(),
                mavenBundle().groupId("org.apache.cxf.dosgi.systests")
                    .artifactId("cxf-dosgi-ri-systests2-common").versionAsInProject(),
                streamBundle(getCustomIntentBundle()).noStart(),
                provision(getServiceBundle()),
                frameworkStartLevel(100) };
    }

    @Test
    public void testCustomIntent() throws Exception {
        // There should be warnings of unsatisfied intent myIntent in the log at debug level
        Thread.sleep(2000);
        getBundleByName(bundleContext, "CustomIntent").start();
        waitPort(9090);

        GreeterService greeterService = createGreeterServiceProxy("http://localhost:9090/greeter");
        Map<GreetingPhrase, String> result = greeterService.greetMe("Chris");
        Assert.assertEquals(1, result.size());
        GreetingPhrase phrase = result.keySet().iterator().next();
        Assert.assertEquals("Hi from custom intent", phrase.getPhrase());
    }
}
