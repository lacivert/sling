/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The SF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.sling.event.impl.jobs.jmx;

import java.util.Map;

import org.apache.sling.event.jobs.Queue;
import org.osgi.service.event.Event;

public class QueueStatusEvent extends Event {

    public static final String TOPIC = "org/apache/sling/event/Queue";
    private Queue queue;
    private Queue oldqueue;


    @SuppressWarnings({ "rawtypes" })
    public QueueStatusEvent(Queue queue, Queue oldqueue) {
        super(TOPIC, (Map<String, Object>)null);
        this.queue = queue;
        this.oldqueue = oldqueue;
    }
    public boolean isNew() {
        return this.oldqueue == null;
    }
    public boolean isUpdate() {
        return this.queue == this.oldqueue;
    }
    public boolean isRemoved() {
        return this.queue == null;
    }
    public Queue getQueue() {
        return queue;
    }
    public Queue getOldQueue() {
        return oldqueue;
    }


}