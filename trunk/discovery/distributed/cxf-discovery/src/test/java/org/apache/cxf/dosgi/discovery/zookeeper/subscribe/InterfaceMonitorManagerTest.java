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
package org.apache.cxf.dosgi.discovery.zookeeper.subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.ZooKeeper;
import org.easymock.IAnswer;
import org.easymock.classextension.EasyMock;
import org.easymock.classextension.IMocksControl;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.remoteserviceadmin.EndpointListener;

import static org.junit.Assert.assertEquals;

public class InterfaceMonitorManagerTest {

    @Test
    public void testEndpointListenerTrackerCustomizer() {
        IMocksControl c = EasyMock.createNiceControl();

        BundleContext ctx = c.createMock(BundleContext.class);
        ZooKeeper zk = c.createMock(ZooKeeper.class);

        @SuppressWarnings("unchecked")
        ServiceReference<EndpointListener> sref = c.createMock(ServiceReference.class);
        @SuppressWarnings("unchecked")
        ServiceReference<EndpointListener> sref2 = c.createMock(ServiceReference.class);

        final Map<String, ?> p = new HashMap<String, Object>();

        EasyMock.expect(sref.getPropertyKeys()).andAnswer(new IAnswer<String[]>() {
            public String[] answer() throws Throwable {
                return p.keySet().toArray(new String[p.size()]);
            }
        }).anyTimes();

        EasyMock.expect(sref.getProperty((String)EasyMock.anyObject())).andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                String key = (String)(EasyMock.getCurrentArguments()[0]);
                return p.get(key);
            }
        }).anyTimes();

        EasyMock.expect(sref2.getPropertyKeys()).andAnswer(new IAnswer<String[]>() {
            public String[] answer() throws Throwable {
                return p.keySet().toArray(new String[p.size()]);
            }
        }).anyTimes();

        EasyMock.expect(sref2.getProperty((String)EasyMock.anyObject())).andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                String key = (String)(EasyMock.getCurrentArguments()[0]);
                return p.get(key);
            }
        }).anyTimes();

        final List<IMocksControl> controls = new ArrayList<IMocksControl>();

        InterfaceMonitorManager eltc = new InterfaceMonitorManager(ctx, zk);

        c.replay();

        // sref has no scope -> nothing should happen

        assertEquals(0, eltc.getEndpointListenerScopes().size());
        assertEquals(0, eltc.getInterests().size());

        //p.put(EndpointListener.ENDPOINT_LISTENER_SCOPE, );

        eltc.addInterest(sref, "(objectClass=mine)", "mine");

        assertEquals(1, eltc.getEndpointListenerScopes().size());
        assertEquals(1, eltc.getEndpointListenerScopes().get(sref).size());
        assertEquals("(objectClass=mine)", eltc.getEndpointListenerScopes().get(sref).get(0));
        assertEquals(1, eltc.getInterests().size());

        eltc.addInterest(sref, "(objectClass=mine)", "mine");

        assertEquals(1, eltc.getEndpointListenerScopes().size());
        assertEquals(1, eltc.getEndpointListenerScopes().get(sref).size());
        assertEquals("(objectClass=mine)", eltc.getEndpointListenerScopes().get(sref).get(0));
        assertEquals(1, eltc.getInterests().size());

        eltc.addInterest(sref2, "(objectClass=mine)", "mine");

        assertEquals(2, eltc.getEndpointListenerScopes().size());
        assertEquals(1, eltc.getEndpointListenerScopes().get(sref).size());
        assertEquals(1, eltc.getEndpointListenerScopes().get(sref2).size());
        assertEquals("(objectClass=mine)", eltc.getEndpointListenerScopes().get(sref).get(0));
        assertEquals("(objectClass=mine)", eltc.getEndpointListenerScopes().get(sref2).get(0));
        assertEquals(1, eltc.getInterests().size());

        eltc.removeInterest(sref);

        assertEquals(1, eltc.getEndpointListenerScopes().size());
        assertEquals(1, eltc.getEndpointListenerScopes().get(sref2).size());
        assertEquals("(objectClass=mine)", eltc.getEndpointListenerScopes().get(sref2).get(0));
        assertEquals(1, eltc.getInterests().size());

        eltc.removeInterest(sref);

        assertEquals(1, eltc.getEndpointListenerScopes().size());
        assertEquals(1, eltc.getEndpointListenerScopes().get(sref2).size());
        assertEquals("(objectClass=mine)", eltc.getEndpointListenerScopes().get(sref2).get(0));
        assertEquals(1, eltc.getInterests().size());

        eltc.removeInterest(sref2);

        assertEquals(0, eltc.getEndpointListenerScopes().size());
        assertEquals(0, eltc.getInterests().size());

        c.verify();
        for (IMocksControl control : controls) {
            control.verify();
        }
    }
}
