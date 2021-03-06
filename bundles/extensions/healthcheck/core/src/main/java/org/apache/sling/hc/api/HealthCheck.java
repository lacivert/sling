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
package org.apache.sling.hc.api;

import aQute.bnd.annotation.ConsumerType;


/**
 * Health Check services can be executed and
 * return an execution {@link Result}.
 *
 * Clients should not look up health checks directly but
 * rather use the {@link org.apache.sling.hc.api.execution.HealthCheckExecutor}
 * service and executed checks based on tags.
 *
 * If the {@link #MBEAN_NAME} service registration property is set,
 * the health check is registered as an mbean and can be invoked
 * by getting the MBean from the JMX registry.
 */
@ConsumerType
public interface HealthCheck {

    /**
     * Optional service property: the name of a health check.
     * This name should be unique, however there might be more than one
     * health check service with the same value for this property.
     * The value of this property must be of type String.
     */
    String NAME = "hc.name";

    /**
     * Optional service property: the name of the MBean for registering
     * the health check as an MBean. If this property is missing the
     * health check is not registered as a JMX MBean.
     * If there is more than one service with the same value for this
     * property, the one with the highest service ranking is registered
     * only.
     * The value of this property must be of type String.
     */
    String MBEAN_NAME = "hc.mbean.name";

    /**
     * Optional service property: tags for categorizing the health check
     * services.
     * The value of this property must be of type String or String array.
     */
    String TAGS = "hc.tags";

    /**
     * Execute this health check and return a {@link Result}
     * This is meant to execute quickly, access to external
     * systems, for example, should be managed asynchronously.
     */
    Result execute();
}
