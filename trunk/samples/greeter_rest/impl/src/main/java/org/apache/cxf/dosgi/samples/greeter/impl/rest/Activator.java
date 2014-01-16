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
package org.apache.cxf.dosgi.samples.greeter.impl.rest;

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.cxf.dosgi.samples.greeter.rest.GreeterService;
import org.apache.cxf.dosgi.samples.greeter.rest.GreeterService2;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

    private ServiceRegistration registration;
    private ServiceRegistration registration2;

    public void start(BundleContext bc) throws Exception {
        Dictionary<String, Object> props = getProperties("/greeter");
        registration = bc.registerService(GreeterService.class.getName(),
                                          new GreeterServiceImpl(), props);

        props = getProperties("/greeter2");
        registration2 = bc.registerService(GreeterService2.class.getName(),
                                          new GreeterServiceImpl2(), props);
    }

    private Dictionary<String, Object> getProperties(String address) {
        Dictionary<String, Object> props = new Hashtable<String, Object>();

        props.put("service.exported.interfaces", "*");
        props.put("service.exported.configs", "org.apache.cxf.rs");
        props.put("service.exported.intents", "HTTP");
        props.put("org.apache.cxf.rs.httpservice.context", address);
        return props;
    }

    public void stop(BundleContext bc) throws Exception {
        registration.unregister();
        registration2.unregister();
    }
}
