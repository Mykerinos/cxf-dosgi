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
package org.apache.cxf.dosgi.dsw.osgi;

public final class Constants {

    // WSDL
    public static final String WSDL_CONFIG_TYPE = "wsdl";
    public static final String WSDL_CONFIG_PREFIX = "osgi.remote.configuration" + "." + WSDL_CONFIG_TYPE;
    public static final String WSDL_SERVICE_NAMESPACE = WSDL_CONFIG_PREFIX + ".service.ns";
    public static final String WSDL_SERVICE_NAME = WSDL_CONFIG_PREFIX + ".service.name";
    public static final String WSDL_PORT_NAME = WSDL_CONFIG_PREFIX + ".port.name";
    public static final String WSDL_LOCATION = WSDL_CONFIG_PREFIX + ".location";
    public static final String WSDL_HTTP_SERVICE_CONTEXT = WSDL_CONFIG_PREFIX + ".httpservice.context";

    // WS
    public static final String WS_CONFIG_TYPE = "org.apache.cxf" + ".ws";
    public static final String WS_ADDRESS_PROPERTY = WS_CONFIG_TYPE + ".address";
    public static final String WS_PORT_PROPERTY = WS_CONFIG_TYPE + ".port";
    public static final String WS_HTTP_SERVICE_CONTEXT = WS_CONFIG_TYPE + ".httpservice.context";

    public static final String WS_FRONTEND_PROP_KEY = WS_CONFIG_TYPE + ".frontend";
    public static final String WS_FRONTEND_JAXWS = "jaxws";
    public static final String WS_FRONTEND_SIMPLE = "simple";

    public static final String WS_IN_INTERCEPTORS_PROP_KEY = WS_CONFIG_TYPE + ".in.interceptors";
    public static final String WS_OUT_INTERCEPTORS_PROP_KEY = WS_CONFIG_TYPE + ".out.interceptors";
    public static final String WS_OUT_FAULT_INTERCEPTORS_PROP_KEY = WS_CONFIG_TYPE + ".out.fault.interceptors";
    public static final String WS_IN_FAULT_INTERCEPTORS_PROP_KEY = WS_CONFIG_TYPE + ".in.fault.interceptors";
    public static final String WS_CONTEXT_PROPS_PROP_KEY = WS_CONFIG_TYPE + ".context.properties";
    public static final String WS_FEATURES_PROP_KEY = WS_CONFIG_TYPE + ".features";

    public static final String WS_DATABINDING_PROP_KEY = WS_CONFIG_TYPE + ".databinding";
    public static final String WS_DATABINDING_BEAN_PROP_KEY = WS_DATABINDING_PROP_KEY + ".bean";
    public static final String WS_DATA_BINDING_JAXB = "jaxb";
    public static final String WS_DATA_BINDING_AEGIS = "aegis";

    public static final String WS_WSDL_SERVICE_NAMESPACE = WS_CONFIG_TYPE + ".service.ns";
    public static final String WS_WSDL_SERVICE_NAME = WS_CONFIG_TYPE + ".service.name";
    public static final String WS_WSDL_PORT_NAME = WS_CONFIG_TYPE + ".port.name";
    public static final String WS_WSDL_LOCATION = WS_CONFIG_TYPE + ".wsdl.location";

    // Rest
    public static final String RS_CONFIG_TYPE = "org.apache.cxf" + ".rs";
    public static final String RS_ADDRESS_PROPERTY = RS_CONFIG_TYPE + ".address";
    public static final String RS_HTTP_SERVICE_CONTEXT = RS_CONFIG_TYPE + ".httpservice.context";
    public static final String RS_DATABINDING_PROP_KEY = RS_CONFIG_TYPE + ".databinding";
    public static final String RS_IN_INTERCEPTORS_PROP_KEY = RS_CONFIG_TYPE + ".in.interceptors";
    public static final String RS_OUT_INTERCEPTORS_PROP_KEY = RS_CONFIG_TYPE + ".out.interceptors";
    public static final String RS_IN_FAULT_INTERCEPTORS_PROP_KEY = RS_CONFIG_TYPE + ".in.fault.interceptors";
    public static final String RS_OUT_FAULT_INTERCEPTORS_PROP_KEY = RS_CONFIG_TYPE + ".out.fault.interceptors";
    public static final String RS_CONTEXT_PROPS_PROP_KEY = RS_CONFIG_TYPE + ".context.properties";
    public static final String RS_FEATURES_PROP_KEY = RS_CONFIG_TYPE + ".features";
    public static final String RS_PROVIDER_PROP_KEY = RS_CONFIG_TYPE + ".provider";
    public static final String RS_PROVIDER_EXPECTED_PROP_KEY = RS_PROVIDER_PROP_KEY + ".expected";
    public static final String RS_PROVIDER_GLOBAL_PROP_KEY = RS_PROVIDER_PROP_KEY + ".globalquery";
    public static final String RS_WADL_LOCATION = RS_CONFIG_TYPE + ".wadl.location";

    // POJO (old value for WS)
    public static final String WS_CONFIG_TYPE_OLD = "pojo";
    public static final String WS_CONFIG_OLD_PREFIX = "osgi.remote.configuration." + WS_CONFIG_TYPE_OLD;
    public static final String WS_ADDRESS_PROPERTY_OLD = WS_CONFIG_OLD_PREFIX + ".address";
    public static final String WS_HTTP_SERVICE_CONTEXT_OLD = WS_CONFIG_OLD_PREFIX + ".httpservice.context";

    private Constants() {
        // never constructed
    }
}
