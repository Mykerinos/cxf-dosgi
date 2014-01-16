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
package org.apache.cxf.dosgi.discovery.zookeeper.server;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.apache.cxf.dosgi.discovery.zookeeper.server.util.Utils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;

public class ZookeeperStarter implements org.osgi.service.cm.ManagedService {

    private static final Logger LOG = Logger.getLogger(ZookeeperStarter.class); //NOPMD - using log4j here

    protected ZookeeperServer main;
    private final BundleContext bundleContext;
    private Thread zkMainThread;

    public ZookeeperStarter(BundleContext ctx) {
        bundleContext = ctx;
    }

    synchronized void shutdown() {
        if (main != null) {
            LOG.info("Shutting down ZooKeeper server");
            try {
                main.shutdown();
                if (zkMainThread != null) {
                    zkMainThread.join();
                }
            } catch (Throwable e) {
                LOG.log(Level.ERROR, e.getMessage(), e);
            }
            main = null;
            zkMainThread = null;
        }
    }

    private void setDefaults(Dictionary<String, String> dict) throws IOException {
        Utils.removeEmptyValues(dict); // to avoid NumberFormatExceptions
        Utils.setDefault(dict, "tickTime", "2000");
        Utils.setDefault(dict, "initLimit", "10");
        Utils.setDefault(dict, "syncLimit", "5");
        Utils.setDefault(dict, "clientPort", "2181");
        Utils.setDefault(dict, "dataDir", new File(bundleContext.getDataFile(""), "zkdata").getCanonicalPath());
    }

    @SuppressWarnings("unchecked")
    public synchronized void updated(Dictionary<String, ?> dict) throws ConfigurationException {
        shutdown();
        if (dict == null) {
            return;
        }
        try {
            setDefaults((Dictionary<String, String>)dict);
            QuorumPeerConfig config = parseConfig(dict);
            startFromConfig(config);
            LOG.info("Applied configuration update: " + dict);
        } catch (Exception th) {
            LOG.error("Problem applying configuration update: " + dict, th);
        }
    }

    private QuorumPeerConfig parseConfig(Dictionary<String, ?> dict) throws IOException, ConfigException {
        QuorumPeerConfig config = new QuorumPeerConfig();
        config.parseProperties(Utils.toProperties(dict));
        return config;
    }

    protected void startFromConfig(final QuorumPeerConfig config) {
        int numServers = config.getServers().size();
        main = numServers > 1 ? new MyQuorumPeerMain(config) : new MyZooKeeperServerMain(config);
        zkMainThread = new Thread(new Runnable() {
            public void run() {
                try {
                    main.startup();
                } catch (Throwable e) {
                    LOG.error("Problem running ZooKeeper server.", e);
                }
            }
        });
        zkMainThread.start();
    }

    interface ZookeeperServer {
        void startup() throws IOException;
        void shutdown();
    }

    static class MyQuorumPeerMain extends QuorumPeerMain implements ZookeeperServer {

        private QuorumPeerConfig config;

        public MyQuorumPeerMain(QuorumPeerConfig config) {
            this.config = config;
        }

        public void startup() throws IOException {
            runFromConfig(config);
        }

        public void shutdown() {
            if (null != quorumPeer) {
                quorumPeer.shutdown();
            }
        }
    }

    static class MyZooKeeperServerMain extends ZooKeeperServerMain implements ZookeeperServer {

        private QuorumPeerConfig config;

        public MyZooKeeperServerMain(QuorumPeerConfig config) {
            this.config = config;
        }

        public void startup() throws IOException {
            ServerConfig serverConfig = new ServerConfig();
            serverConfig.readFrom(config);
            runFromConfig(serverConfig);
        }

        public void shutdown() {
            try {
                super.shutdown();
            } catch (Exception e) {
                LOG.error("Error shutting down ZooKeeper", e);
            }
        }
    }
}
