
## `mhd-iti68` component

The mhd-iti68 component provides interfaces for actors of the *Retrieve Document* IHE transaction (ITI-68).
Note that this component implements the 2016/2017 version of this profile.

### Actors

The transaction defines the following actors:

![ITI-68 actors](images/iti68.png)

Producer side corresponds to the *Document Consumer* actor.
Consumer side corresponds to the *Document Responder* actor.

### Dependencies

In a Maven-based environment, the following dependency must be registered in `pom.xml`:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.platform-camel</groupId>
        <artifactId>ipf-platform-camel-ihe-fhir-dstu2-mhd</artifactId>
        <version>${ipf-version}</version>
    </dependency>
```

### Endpoint URI Format

#### Producer

The endpoint URI format of `mhd-iti68` component producers is:

```
mhd-iti68://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `mhd-iti68` component consumers is:

```
mhd-iti68:serviceName[?parameters]
```

The resulting URL of the exposed FHIR REST Service endpoint depends on the configuration of the [deployment container].

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /binary/*
```

then the mhd-iti68 consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/binary`

Additional URI parameters are optional and control special features as described in the corresponding section below.


### Example

This is an example on how to use the component on the consumer side:

```java
    from("mhd-iti68:mhdservice?audit=true")
      .process(myProcessor)
      // process the incoming request and create a response
```


### Basic Common Component Features

* [ATNA auditing]

### Basic FHIR Component Features

* [Message types and exception handling]
* [Security]

### Connection-related FHIR Component Features

* [Connection parameters]

[ATNA auditing]: ../ipf-platform-camel-ihe/atna.html
[Message types and exception handling]: ../ipf-platform-camel-ihe-fhir-core/messageTypes.html
[Security]: ../ipf-platform-camel-ihe-fhir-core/security.html
[Connection parameters]: ../ipf-platform-camel-ihe-fhir-core/connection.html

[deployment container]: ../ipf-platform-camel-ihe-fhir-core/deployment.html

