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

import java.security.cert.X509Certificate;
import java.util.Iterator;

/**
 * The {@code X509Function} class implements JSP tag library functions.
 */
public class X509Function {

    private X509Function() {
        super();
    }

    /**
     * Converts a X.509 certificate object/a collection of X.509 certificates into a certificate bean. In case of a
     * collection, only the first certificate is selected.
     *
     * @param input
     *            the input to be converted
     * @return the certificate bean or {@code null}
     */
    public static CertificateBean certificate(Object input) {
        if (input == null) {
            return null;
        }
        if (input instanceof X509Certificate) {
            return new CertificateBean((X509Certificate) input);
        }
        if (input instanceof Iterable) {
            @SuppressWarnings("rawtypes")
            final Iterator it = ((Iterable) input).iterator();
            if (it.hasNext()) {
                final Object item = it.next();
                if (item instanceof X509Certificate) {
                    return new CertificateBean((X509Certificate) item);
                }
            }

        }
        return null;
    }

}
