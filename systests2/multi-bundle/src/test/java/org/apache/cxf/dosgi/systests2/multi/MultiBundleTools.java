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

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;

public final class MultiBundleTools {

    private MultiBundleTools() {
    }

    private static int getDistroBundles(File mdRoot,
                                        Map<Integer, String> bundles) throws Exception {
        File depRoot = new File(mdRoot, "target/dependency");
        File distroDir = depRoot.listFiles()[0];
                                
        Properties p = new Properties();
        File confFile = new File(distroDir, "conf/felix.config.properties.append");
        p.load(new FileInputStream(confFile));

        int startLevel = Integer.parseInt(p.getProperty("org.osgi.framework.startlevel.beginning"));
        for (int i = 0; i <= startLevel; i++) {
            String val = p.getProperty("felix.auto.start." + i);
            if (val != null) {
                if (val.startsWith("file:")) {
                    File fullDir = new File(distroDir, val.substring("file:".length()));
                    bundles.put(i, fullDir.toURI().toASCIIString());
                } else {
                    if (!val.contains("org.osgi.compendium")) {
                        // We're skipping that one as it's pulled in explicitly in the test
                        bundles.put(i, val);
                    }
                }
            }
        }
        return startLevel + 1; // Add 1 to start level to be on the safe side
    }

    private static File getRootDirectory() {
        String resourceName = "/" + MultiBundleTools.class.getName().replace('.', '/') + ".class";
        URL curURL = MultiBundleTools.class.getResource(resourceName);
        File curFile = new File(curURL.getFile());
        String curString = curFile.getAbsolutePath();
        File curBase = new File(curString.substring(0, curString.length() - resourceName.length()));
        return curBase.getParentFile().getParentFile();
    }

    private static Option[] getDistroBundleOptions() throws Exception {
        Map<Integer, String> bundles = new TreeMap<Integer, String>();
        File root = getRootDirectory();
        getDistroBundles(root, bundles);
        List<Option> opts = new ArrayList<Option>();
        for (Map.Entry<Integer, String> entry : bundles.entrySet()) {
            String bundleUri = entry.getValue();
            URL bundleURL = new URL(bundleUri);
            JarInputStream bundleJar = new JarInputStream(bundleURL.openStream());
            Manifest manifest = bundleJar.getManifest();
            Attributes host = manifest.getAttributes("Fragment-Host");
            if (host != null) {
                System.out.println(bundleUri);
            }
            bundleJar.close();
            if (!bundleUri.contains("pax-logging")) {
                opts.add(CoreOptions.bundle(bundleUri));
            }
        }
        return opts.toArray(new Option[opts.size()]);
    }

    public static Option getDistroWithDiscovery() throws Exception {
        return getDistro();
    }

    public static Option getDistro() throws Exception {
        return CoreOptions.composite(getDistroBundleOptions());
    }
}
