/*
 * Copyright 2016 the original author or authors.
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

package org.openehealth.ipf.boot.atna;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.security.AbstractAuthenticationAuditListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
@EnableConfigurationProperties(IpfAtnaConfigurationProperties.class)
public class IpfAtnaAutoConfiguration {


    @Bean
    @ConfigurationProperties(prefix = "ipf.atna")
    @ConditionalOnMissingBean
    public AuditContext atnaAuditorModuleConfig(IpfAtnaConfigurationProperties config,
                                                @Value("${spring.application.name}") String appName) throws Exception {
        DefaultAuditContext auditContext = new DefaultAuditContext();
        auditContext.setAuditEnabled(config.isAuditEnabled());
        auditContext.setAuditSourceId(appName);
        auditContext.setAuditEnterpriseSiteId(config.getAuditEnterpriseSiteId());
        auditContext.setAuditRepositoryHost(config.getAuditRepositoryHost());
        auditContext.setAuditRepositoryPort(config.getAuditRepositoryPort());
        auditContext.setAuditSource(config.getAuditSourceType());
        auditContext.setSendingApplication(config.getAuditSendingApplication());
        auditContext.setIncludeParticipantsFromResponse(config.isIncludeParticipantsFromResponse());
        auditContext.setAuditRepositoryTransport(config.getAuditRepositoryTransport());

        if (config.getAuditQueueClass() != null) {
            auditContext.setAuditMessageQueue(config.getAuditQueueClass().newInstance());
        }

        if (config.getAuditExceptionHandlerClass() != null) {
            auditContext.setAuditExceptionHandler(config.getAuditExceptionHandlerClass().newInstance());
        }

        if (config.getAuditSenderClass() != null) {
            auditContext.setAuditTransmissionProtocol(config.getAuditSenderClass().newInstance());
        }

        if (config.getAuditMessagePostProcessorClass() != null) {
            auditContext.setAuditMessagePostProcessor(config.getAuditMessagePostProcessorClass().newInstance());
        }

        return auditContext;
    }


    @Bean
    @ConditionalOnProperty(value = "ipf.atna.auditor-enabled")
    @ConditionalOnMissingBean
    ApplicationStartEventListener applicationStartEventListener(AuditContext auditContext) {
        return new ApplicationStartEventListener(auditContext);
    }

    @Bean
    @ConditionalOnProperty(value = "ipf.atna.auditor-enabled")
    @ConditionalOnMissingBean
    ApplicationStopEventListener applicationStopEventListener(AuditContext auditContext) {
        return new ApplicationStopEventListener(auditContext);
    }

    @Bean
    @ConditionalOnProperty(value = "ipf.atna.auditor-enabled")
    @ConditionalOnClass(name = "org.springframework.security.authentication.event.AbstractAuthenticationEvent")
    @ConditionalOnMissingBean(AbstractAuthenticationAuditListener.class)
    AuthenticationListener loginListener(AuditContext auditContext) {
        return new AuthenticationListener(auditContext);
    }

}
