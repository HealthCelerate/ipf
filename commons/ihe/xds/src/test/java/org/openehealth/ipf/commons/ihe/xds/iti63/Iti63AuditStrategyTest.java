/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.xds.iti63;

import org.junit.Test;
import org.openehealth.ipf.commons.audit.codes.*;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorTestBase;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsQueryAuditDataset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Christian Ohr
 */
public class Iti63AuditStrategyTest extends AuditorTestBase {

    @Test
    public void testServerSide() {
        testRequest(true);
    }

    @Test
    public void testClientSide() {
        testRequest(false);
    }

    private void testRequest(boolean serverSide) {
        Iti63AuditStrategy strategy = new Iti63AuditStrategy(serverSide);
        XdsQueryAuditDataset auditDataset = getXdsAuditDataset(strategy);
        AuditMessage auditMessage = makeAuditMessage(strategy, auditContext, auditDataset);

        assertNotNull(auditMessage);
        auditMessage.validate();

        // System.out.println(printAuditMessage(auditMessage));

        assertCommonXdsAuditAttributes(auditMessage,
                EventOutcomeIndicator.Success,
                EventIdCode.Query,
                EventActionCode.Execute,
                serverSide,
                true);

        assertEquals(2, auditMessage.findParticipantObjectIdentifications(
                poit -> poit.getParticipantObjectTypeCode() == ParticipantObjectTypeCode.System)
                .get(0)
                .getParticipantObjectDetails().size());
    }

    private XdsQueryAuditDataset getXdsAuditDataset(Iti63AuditStrategy strategy) {
        XdsQueryAuditDataset auditDataset = strategy.createAuditDataset();
        auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
        // auditDataset.setLocalAddress(SERVER_URI);
        auditDataset.setRemoteAddress(CLIENT_IP_ADDRESS);
        auditDataset.setUserName(USER_NAME);
        auditDataset.setSourceUserId(REPLY_TO_URI);
        auditDataset.setDestinationUserId(SERVER_URI);
        auditDataset.setRequestPayload(QUERY_PAYLOAD);
        auditDataset.setPurposesOfUse(NEW_PURPOSES_OF_USE);
        auditDataset.getUserRoles().addAll(NEW_USER_ROLES);
        auditDataset.getPatientIds().add(PATIENT_IDS[0]);
        auditDataset.setQueryUuid(QUERY_ID);
        auditDataset.setHomeCommunityId(HOME_COMMUNITY_ID);

        return auditDataset;
    }
}