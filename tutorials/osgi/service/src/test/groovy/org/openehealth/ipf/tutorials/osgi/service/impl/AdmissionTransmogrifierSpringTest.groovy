/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.tutorials.osgi.service.impl

import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.load

import static org.junit.Assert.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import ca.uhn.hl7v2.model.Type

import org.openehealth.ipf.commons.core.modules.api.Transmogrifier

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener

/**
 * @author Martin Krasser
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = [
   '/service-context.xml', '/service-test-context.xml'
])
@TestExecutionListeners([DependencyInjectionTestExecutionListener.class])
public class AdmissionTransmogrifierSpringTest {

     @Autowired
     Transmogrifier transmogrifier;
     
     def message
     
     @BeforeClass
     public static void setUpBeforeClass() throws Exception {
         ExpandoMetaClass.enableGlobally()
         Type.metaClass.mapGender = {-> 'X'}
     }

     @Before
     void setUp() throws Exception {
         message = load('messages/msg-01.hl7')
     }

     @Test
     void testDefaultResponse() {
         def result = transmogrifier.zap(message)
         assertEquals('DEF', result.MSH[4].value)
         assertEquals('X', result.PID[8].value)
     }

}
