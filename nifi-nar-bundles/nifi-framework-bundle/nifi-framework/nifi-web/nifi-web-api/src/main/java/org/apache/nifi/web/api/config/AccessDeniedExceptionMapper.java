/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.web.api.config;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.nifi.authorization.AccessDeniedException;
import org.apache.nifi.authorization.user.NiFiUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.authorization.user.NiFiUserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps access denied exceptions into a client response.
 */
@Provider
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

    private static final Logger logger = LoggerFactory.getLogger(AccessDeniedExceptionMapper.class);

    @Override
    public Response toResponse(AccessDeniedException exception) {
        // get the current user
        NiFiUser user = NiFiUserUtils.getNiFiUser();
        if (user != null) {
            logger.info(String.format("%s does not have permission to access the requested resource. Returning %s response.", user.getIdentity(), Response.Status.FORBIDDEN));
        } else {
            logger.info(String.format("User does not have permission to access the requested resource. Returning %s response.", Response.Status.FORBIDDEN));
        }

        if (logger.isDebugEnabled()) {
            logger.debug(StringUtils.EMPTY, exception);
        }

        return Response.status(Response.Status.FORBIDDEN).entity("Unable to perform the desired action because access is denied.").type("text/plain").build();
    }

}
