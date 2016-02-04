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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti78;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti78.Iti78ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti78.Iti78ServerAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponent;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponentConfiguration;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

/**
 * Component for PDQm (ITI-78)
 *
 * @since 3.1
 */
public class Iti78Component extends FhirComponent<FhirQueryAuditDataset> {

    private static final FhirComponentConfiguration DEFAULT_CONFIGURATION = new Iti78Configuration();

    private static final AuditStrategy<FhirQueryAuditDataset> CLIENT_AUDIT_STRATEGY = new Iti78ClientAuditStrategy();
    private static final AuditStrategy<FhirQueryAuditDataset> SERVER_AUDIT_STRATEGY = new Iti78ServerAuditStrategy();

    public Iti78Component() {
        super(DEFAULT_CONFIGURATION);
    }

    public Iti78Component(CamelContext context) {
        super(context);
        setFhirComponentConfiguration(DEFAULT_CONFIGURATION);
    }

    public Iti78Component(FhirComponentConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected Iti78Endpoint doCreateEndpoint(String uri, FhirEndpointConfiguration<FhirQueryAuditDataset> config) {
        return new Iti78Endpoint(uri, this, config);
    }

    @Override
    public AuditStrategy<FhirQueryAuditDataset> getServerAuditStrategy() {
        return SERVER_AUDIT_STRATEGY;
    }

    @Override
    public AuditStrategy<FhirQueryAuditDataset> getClientAuditStrategy() {
        return CLIENT_AUDIT_STRATEGY;
    }
}