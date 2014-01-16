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
package org.apache.cxf.dosgi.topologymanager.importer;

import java.util.Dictionary;
import java.util.Hashtable;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.remoteserviceadmin.RemoteConstants;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ListenerHookImplTest {

    @Test
    public void testUUIDFilterExtension() throws InvalidSyntaxException {
        String filter = "(a=b)";

        BundleContext bc = EasyMock.createNiceMock(BundleContext.class);
        EasyMock.expect(bc.getProperty(EasyMock.eq("org.osgi.framework.uuid"))).andReturn("MyUUID").atLeastOnce();
        EasyMock.replay(bc);

        filter = ListenerHookImpl.extendFilter(filter, bc);

        Filter f = FrameworkUtil.createFilter(filter);

        Dictionary<String, String> m = new Hashtable<String, String>();
        m.put("a", "b");

        assertTrue(filter + " filter must match as uuid is missing", f.match(m));
        m.put(RemoteConstants.ENDPOINT_FRAMEWORK_UUID, "MyUUID");
        assertFalse(filter + " filter must NOT match as uuid is the local one", f.match(m));
    }
}
