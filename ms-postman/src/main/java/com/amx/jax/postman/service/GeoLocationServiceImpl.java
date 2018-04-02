package com.amx.jax.postman.service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.GeoLocationService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.model.GeoLocation;
import com.amx.utils.ArgUtil;
import com.amx.utils.FileUtil;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

@Component
public class GeoLocationServiceImpl implements GeoLocationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeoLocationServiceImpl.class);

	private DatabaseReader dbReader;

	public GeoLocationServiceImpl() {
		File database = FileUtil.getFile("ext-resources/GeoLite2-City.mmdb");
		try {
			dbReader = new DatabaseReader.Builder(database).build();
		} catch (IOException e) {
			LOGGER.error("File : ext-resources/GeoLite2-City.mmdb is missing put it relative to jar ", e);
		}
	}

	@Override
	public GeoLocation getLocation(String ip) throws PostManException {
		GeoLocation loc = new GeoLocation(ip);
		try {
			CityResponse response = this.getCity(ip);
			loc.setCityName(ArgUtil.parseAsString(response.getCity().getGeoNameId()));
			loc.setCityId(response.getCity().getName());
			loc.setStateCode(response.getMostSpecificSubdivision().getIsoCode());
			loc.setCountryCode(response.getCountry().getIsoCode());
			loc.setContinentCode(response.getContinent().getCode());
		} catch (Exception e) {
			throw new PostManException(e);
		}
		return loc;// new GeoLocation(ip);
	}

	public CityResponse getCity(String ip) throws IOException, GeoIp2Exception {
		InetAddress ipAddress = InetAddress.getByName(ip);
		return dbReader.city(ipAddress);
	}
}