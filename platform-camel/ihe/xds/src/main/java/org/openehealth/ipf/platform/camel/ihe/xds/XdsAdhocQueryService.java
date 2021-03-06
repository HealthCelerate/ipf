/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.xds;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.xds.core.converters.EbXML30Converters;


/**
 * Base class for XDS Adhoc Query services
 *
 * @since 3.1
 */
@Slf4j
public abstract class XdsAdhocQueryService extends AbstractWebService {

    private final String homeCommunityId;

    /**
     * @param homeCommunityId the home community OID or null if service is not cross-community
     */
    public XdsAdhocQueryService(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    protected AdhocQueryResponse processRequest(AdhocQueryRequest body) {
        Exchange result = process(body);
        Exception exception = Exchanges.extractException(result);
        if (exception != null) {
            log.debug(getClass().getSimpleName() + " service failed", exception);
            QueryResponse errorResponse = new QueryResponse(
                    exception,
                    ErrorCode.REGISTRY_METADATA_ERROR,
                    ErrorCode.REGISTRY_ERROR,
                    homeCommunityId);
            errorResponse.getErrors().get(0).setLocation(homeCommunityId);
            return EbXML30Converters.convert(errorResponse);
        }
        return Exchanges.resultMessage(result).getBody(AdhocQueryResponse.class);
    }
}
