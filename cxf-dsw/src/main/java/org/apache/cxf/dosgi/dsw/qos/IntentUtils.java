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
package org.apache.cxf.dosgi.dsw.qos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.cxf.dosgi.dsw.osgi.Constants;
import org.apache.cxf.dosgi.dsw.util.OsgiUtils;
import org.osgi.service.remoteserviceadmin.RemoteConstants;

public final class IntentUtils {

    private IntentUtils() {
        // never constructed
    }

    public static String[] mergeArrays(String[] a1, String[] a2) {
        if (a1 == null) {
            return a2;
        }
        if (a2 == null) {
            return a1;
        }

        List<String> list = new ArrayList<String>(a1.length + a2.length);
        Collections.addAll(list, a1);
        for (String s : a2) {
            if (!list.contains(s)) {
                list.add(s);
            }
        }

        return list.toArray(new String[list.size()]);
    }

    public static Set<String> getRequestedIntents(Map<String, Object> sd) {
        Collection<String> intents = OsgiUtils.getMultiValueProperty(sd.get(RemoteConstants.SERVICE_EXPORTED_INTENTS));
        Collection<String> intents2
            = OsgiUtils.getMultiValueProperty(sd.get(RemoteConstants.SERVICE_EXPORTED_INTENTS_EXTRA));
        @SuppressWarnings("deprecation")
        Collection<String> oldIntents = OsgiUtils.getMultiValueProperty(sd.get(Constants.EXPORTED_INTENTS_OLD));
        Set<String> allIntents = new HashSet<String>();
        if (intents != null) {
            allIntents.addAll(parseIntents(intents));
        }
        if (intents2 != null) {
            allIntents.addAll(parseIntents(intents2));
        }
        if (oldIntents != null) {
            allIntents.addAll(parseIntents(oldIntents));
        }

        return allIntents;
    }

    private static Collection<String> parseIntents(Collection<String> intents) {
        List<String> parsed = new ArrayList<String>();
        for (String intent : intents) {
            parsed.addAll(Arrays.asList(intent.split("[ ]")));
        }
        return parsed;
    }
}
