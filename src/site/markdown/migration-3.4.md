## IPF 3.4 Migration Guide

IPF 3.4 comes with some changes that must be considered when upgrading from IPF 3.3 to IPF 3.4.

### XDS SourcePatientInfo

The class `org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo` has been completely rewritten.  
Now it supports arbitrary PID fields (including multiple repetitions) and provides the possibility to access
and manipulate unparsed HL7 representations of them.  In addition, selected PID fields remain accessible
as XDS metadata objects.  These two representations (HL7 strings and XDS metadata) are synchronized with each other.

The method `getHl7FieldIterator(String fieldId)` returns an iterator over unparsed repetitions of an PID field
(also for non-repeatable fields).  Methods `getIds()`, `getNames()`, `getAddresses()` return iterators over
lists of XDS metadata objects `Identifiable` (for PID-3), `Name` (for PID-5), `Address` (for PID-11), respectively.
Whenever corresponding objects are inserted to or deleted from these lists, the corresponding items in the
HL7 string collections are automatically updated.  Whenever an object is modified in-place, it must be 
"committed" using of a call to `set()`, e.g:

```
ListIterator<Name> iter = patientInfo.getNames();
Name name = iter.next();
name.setFamilyName("Krusenstern");
iter.set(name);   // this statement is absolutely essential!

```

Methods `getDateOfBirth()/setDateOfBirth()` and `getGender()/setGender()` provide access to XDS metadata elements
for HL7 fields PID-7 and PID-8.  These methods touch only first elements in the corresponding HL7 string collections,
because PID-7 and PID-8 are not repeatable. 

For further information and code samples, please take a look at the classes 
`org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo` and
`org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.pid.PatientInfoTest`.

### IHE Profile Updates

tbd
