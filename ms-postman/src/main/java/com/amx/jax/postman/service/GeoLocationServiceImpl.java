package com.amx.jax.postman.service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.Tenant;
import com.amx.jax.postman.GeoLocationService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.model.GeoLocation;
import com.amx.utils.ArgUtil;
import com.amx.utils.FileUtil;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

/**
 * The Class GeoLocationServiceImpl.
 */
@Component
public class GeoLocationServiceImpl implements GeoLocationService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(GeoLocationServiceImpl.class);

	/** The db reader. */
	private DatabaseReader dbReader = null;

	/**
	 * Gets the db.
	 *
	 * @return the db
	 */
	public DatabaseReader getDb() {
		if (dbReader == null) {
			File database = FileUtil.getExternalFile("ext-resources/GeoLite2-City.mmdb");
			try {
				dbReader = new DatabaseReader.Builder(database).build();
			} catch (IOException e) {
				LOGGER.warn("File : ext-resources/GeoLite2-City.mmdb is missing put it relative to jar {}",
						e.getMessage());
			}
		}
		return dbReader;
	}

	/**
	 * Instantiates a new geo location service impl.
	 */
	public GeoLocationServiceImpl() {
		this.getDb();
	}

	/** The default tennat id. */
	@Value("${default.tenant}")
	String defaultTennatId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.postman.GeoLocationService#getLocation(java.lang.String)
	 */
	@Override
	public GeoLocation getLocation(String ip) throws PostManException {

		Tenant tnt = Tenant.fromString(defaultTennatId, Tenant.KWT);

		GeoLocation loc = new GeoLocation(ip);
		try {
			CityResponse response = this.getCity(ip);
			loc.setCityName(ArgUtil.parseAsString(response.getCity().getGeoNameId()));
			loc.setCityId(response.getCity().getName());
			loc.setStateCode(response.getMostSpecificSubdivision().getIsoCode());
			loc.setCountryCode(response.getCountry().getIsoCode());
			loc.setContinentCode(response.getContinent().getCode());
			loc.setTenant(Tenant.fromString(response.getCountry().getIsoCode(), tnt, true));
		} catch (Exception e) {
			loc.setTenant(tnt);
			LOGGER.error("No location or IP " + ip, e);
		}
		return loc;// new GeoLocation(ip);
	}

	/**
	 * Gets the city.
	 *
	 * @param ip
	 *            the ip
	 * @return the city
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws GeoIp2Exception
	 *             the geo ip 2 exception
	 */
	public CityResponse getCity(String ip) throws IOException, GeoIp2Exception {
		this.getDb();
		InetAddress ipAddress = InetAddress.getByName(ip);
		return dbReader.city(ipAddress);
	}
}
