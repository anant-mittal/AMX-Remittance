.--------------------------------------------------------------------------.
|  (c) Copyright Gemalto, 2018                                             |
|  ALL RIGHTS RESERVED UNDER COPYRIGHT LAWS.                               |
|  CONTAINS CONFIDENTIAL AND TRADE SECRET INFORMATION.                     |
|                                                                          |
|  GEMALTO MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF |
|  THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED      |
|  TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A             |
|  PARTICULAR PURPOSE, OR NON-INFRINGEMENT. GEMALTO SHALL NOT BE           |
|  LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,       |
|  MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.             |
|  THIS SOFTWARE IS NOT DESIGNED OR INTENDED FOR USE OR RESALE AS ON-LINE  |
|  CONTROL EQUIPMENT IN HAZARDOUS ENVIRONMENTS REQUIRING FAIL-SAFE         |
|  PERFORMANCE, SUCH AS IN THE OPERATION OF NUCLEAR FACILITIES, AIRCRAFT   |
|  NAVIGATION OR COMMUNICATION SYSTEMS, AIR TRAFFIC CONTROL, DIRECT LIFE   |
|  SUPPORT MACHINES, OR WEAPONS SYSTEMS, IN WHICH THE FAILURE OF THE       |
|  SOFTWARE COULD LEAD DIRECTLY TO DEATH, PERSONAL INJURY, OR SEVERE       |
|  PHYSICAL OR ENVIRONMENTAL DAMAGE ("HIGH RISK ACTIVITIES"). GEMALTO      |
|  SPECIFICALLY DISCLAIMS ANY EXPRESS OR IMPLIED WARRANTY OF FITNESS FOR   |
|  HIGH RISK ACTIVITIES.                                                   |
`--------------------------------------------------------------------------`

Gemalto eGov Java7+ SAML SDK Sample

==================================

SAML SDK Example is a sample web application built on the top of the SAML SDK.


Steps
-----

1) Build the sample web application; Maven -> oman-khatm-saml-sdk-example.war

2) Download and extract the Apache Tomcat 8 (https://tomcat.apache.org/download-80.cgi)

3) [Optional] Configuration - all values can be set once application is deployed

3a) Edit %SDK_SAMPLE%/config/sp-metadata.xml - URLs, keys

3b) Request to create SP with the following configuration
    SP EntityID:http://sdk.sample.com
    SP ACS:https://sdk.sample.com:8443/oman-khatm-saml-sdk-example/saml/acs
    SP SLS:https://sdk.sample.com:8443/oman-khatm-saml-sdk-example/saml/sls

3c) Get idp-metadata.xml and copy it to the %SDK_SAMPLE%/config
    The Gemalto IDP has the configuration available under (https://<environment domain>/idp/public/saml/idp-metadata.xml)

4) Add real IDP SSL certificate signer into to %CONFIG%/server/sample.com.truststore.jks trust store.

4) Set CATALINA_OPTS=-Dsdk.config.location=%CONFIG% -Djavax.net.ssl.trustStore=%CONFIG%/server/sample.com.truststore.jks -Djavax.net.ssl.trustStorePassword=123456

5) Deploy the oman-khatm-saml-sdk-example.war

6) Navigate to https://sdk.sample.com:8443/oman-khatm-saml-sdk-example/


Gemalto (c) 2018