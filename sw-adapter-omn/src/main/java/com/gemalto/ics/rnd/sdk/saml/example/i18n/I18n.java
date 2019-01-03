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
package com.gemalto.ics.rnd.sdk.saml.example.i18n;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;

/**
 * The {@code I18n} class implements methods supporting internationalization
 * (I18N).
 */
public class I18n {

	private I18n() {
		super();
	}

	public static Map<String, String> enumAsMap(Class<? extends Enum<?>> enumClass, MessageSource messageSource, Locale locale) {
		final Map<String, String> types = new LinkedHashMap<>();
		try {
			Method method = enumClass.getMethod("values");
			Object[] values = (Object[]) method.invoke(null);
			for (Object value : values) {
				Method nameMethod = value.getClass().getMethod("name");
				String name = (String) nameMethod.invoke(value);
				String messageCode;
				messageCode = enumClass.getSimpleName() + "." + name;
				types.put(name, messageSource.getMessage(messageCode, null, locale));
			}
			return types;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	}

}
