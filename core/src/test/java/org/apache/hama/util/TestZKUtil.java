/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hama.util;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

public class TestZKUtil extends TestCase {

  MockZK zk;
  String path;
  String[] parts;
  int pos = 0;
  StringBuffer sb = new StringBuffer();

  class MockZK extends ZooKeeper {

    public MockZK(String connectString, int timeout, Watcher watcher)
        throws IOException {
      super(connectString, timeout, watcher);
    }

    // create is called in for loop
    @Override
    public String create(String path, byte[] data, List<ACL> acl,
        CreateMode createMode) throws KeeperException, InterruptedException {
      parts[pos] = path;
      pos++;
      sb.append(ZKUtil.ZK_SEPARATOR + path);
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < pos; i++) {
        builder.append(ZKUtil.ZK_SEPARATOR + parts[i]);
      }
      assertEquals("Make sure path created is consistent.", sb.toString(),
          builder.toString());
      return path;
    }
  }

  @Override
  public void setUp() throws Exception {
    this.zk = new MockZK("localhost:2181", 3000, null);
    this.path = "/monitor/groom_lab01_61000/metrics/jvm";
    StringTokenizer token = new StringTokenizer(path, ZKUtil.ZK_SEPARATOR);
    int count = token.countTokens(); // should be 4
    assertEquals("Make sure token are 4.", count, 4);
    this.parts = new String[count]; //
  }

  @Override
  protected void tearDown() throws Exception {
    zk.close();
  }

  public void testCreatePath() throws Exception {
    // TODO not active because of connection excception
    // ZKUtil.create(this.zk, path);
  }

}
