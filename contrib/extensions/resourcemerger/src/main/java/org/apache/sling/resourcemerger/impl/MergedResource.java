/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.resourcemerger.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.AbstractResource;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

/**
 * {@inheritDoc}
 */
public class MergedResource extends AbstractResource {

    private final ResourceResolver resolver;
    private final String path;
    private final String relativePath;
    private final List<String> mappedResources = new ArrayList<String>();

    private final ResourceMetadata metadata = new ResourceMetadata();

    private final String resourceType;

    /** Cache value map. */
    private ValueMap properties;

    /**
     * Constructor
     *
     * @param resolver      Resource resolver
     * @param mergeRootPath Merge root path
     * @param relativePath  Relative path
     */
    MergedResource(final ResourceResolver resolver,
                   final String mergeRootPath,
                   final String relativePath) {
        this(resolver, mergeRootPath, relativePath, null);
    }

    /**
     * Constructor
     *
     * @param resolver      Resource resolver
     * @param mergeRootPath   Merge root path
     * @param relativePath    Relative path
     * @param mappedResources List of physical mapped resources' paths
     */
    MergedResource(final ResourceResolver resolver,
                   final String mergeRootPath,
                   final String relativePath,
                   final List<String> mappedResources) {
        this.resolver = resolver;
        this.path = (relativePath.length() == 0 ? mergeRootPath : mergeRootPath + "/" + relativePath);
        this.relativePath = (relativePath.length() == 0 ? "/" : relativePath);
        if ( mappedResources != null ) {
            this.mappedResources.addAll(mappedResources);
        }
        this.resourceType = this.adaptTo(ValueMap.class).get(ResourceResolver.PROPERTY_RESOURCE_TYPE, this.relativePath);
        metadata.put("sling.mergedResource", true);
        metadata.put("sling.mappedResources", mappedResources.toArray(new String[mappedResources.size()]));
    }


    // ---- MergedResource interface ------------------------------------------

    public String getRelativePath() {
        return relativePath;
    }

    /**
     * {@inheritDoc}
     */
    public Iterable<String> getMappedResources() {
        return mappedResources;
    }


    // ---- Resource interface ------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String getPath() {
        return this.path;
    }

    /**
     * {@inheritDoc}
     */
    public String getResourceType() {
        return this.resourceType;
    }

    /**
     * {@inheritDoc}
     */
    public String getResourceSuperType() {
        // So far, there's no concept of resource super type for a merged resource
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public ResourceMetadata getResourceMetadata() {
        return metadata;
    }

    /**
     * {@inheritDoc}
     */
    public ResourceResolver getResourceResolver() {
        return resolver;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
        if (type == ValueMap.class) {
            if ( this.properties == null ) {
                this.properties = new MergedValueMap(this);
            }
            return (AdapterType) this.properties;
        }

        return super.adaptTo(type);
    }


    // ---- Object ------------------------------------------------------------

    /**
     * Merged resources are considered equal if their paths are equal,
     * regardless of the list of mapped resources.
     *
     * @param o Object to compare with
     * @return Returns <code>true</code> if the two merged resources have the
     *         same path.
     */
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o.getClass() != getClass()) {
            return false;
        }

        final Resource r = (Resource) o;
        return r.getPath().equals(getPath());
    }

    @Override
    public int hashCode() {
        return this.getPath().hashCode();
    }

    @Override
    public String toString() {
        return "MergedResource [path=" + this.path +
               ", resources=" + this.mappedResources + "]";
    }
}
