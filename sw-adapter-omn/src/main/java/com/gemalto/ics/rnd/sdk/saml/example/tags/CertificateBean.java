/*
 *  (c) Copyright Gemalto, 2017
 *  ALL RIGHTS RESERVED UNDER COPYRIGHT LAWS.
 *  CONTAINS CONFIDENTIAL AND TRADE SECRET INFORMATION.
 *  GEMALTO MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 *  THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *  TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 *  PARTICULAR PURPOSE, OR NON-INFRINGEMENT. GEMALTO SHALL NOT BE
 *  LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
 *  MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *  THIS SOFTWARE IS NOT DESIGNED OR INTENDED FOR USE OR RESALE AS ON-LINE
 *  CONTROL EQUIPMENT IN HAZARDOUS ENVIRONMENTS REQUIRING FAIL-SAFE
 *  PERFORMANCE, SUCH AS IN THE OPERATION OF NUCLEAR FACILITIES, AIRCRAFT
 *  NAVIGATION OR COMMUNICATION SYSTEMS, AIR TRAFFIC CONTROL, DIRECT LIFE
 *  SUPPORT MACHINES, OR WEAPONS SYSTEMS, IN WHICH THE FAILURE OF THE
 *  SOFTWARE COULD LEAD DIRECTLY TO DEATH, PERSONAL INJURY, OR SEVERE
 *  PHYSICAL OR ENVIRONMENTAL DAMAGE ("HIGH RISK ACTIVITIES"). GEMALTO
 *  SPECIFICALLY DISCLAIMS ANY EXPRESS OR IMPLIED WARRANTY OF FITNESS FOR
 *  HIGH RISK ACTIVITIES.
 */
package com.gemalto.ics.rnd.sdk.saml.example.tags;

import static org.springframework.web.util.HtmlUtils.htmlEscape;

import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * The {@code CertificateAdapter} class implements a bean holding an X.509 certificate.
 */
public class CertificateBean {

    private final X509Certificate certificate;

    public CertificateBean(X509Certificate certificate) {
        super();
        this.certificate = certificate;
    }

    public String getSubject() {
        return htmlEscape(certificate.getSubjectX500Principal().getName());
    }

    public String getIssuer() {
        return htmlEscape(certificate.getIssuerX500Principal().getName());
    }

    public Date getNotBefore() {
        return certificate.getNotBefore();
    }

    public Date getNotAfter() {
        return certificate.getNotAfter();
    }

}
