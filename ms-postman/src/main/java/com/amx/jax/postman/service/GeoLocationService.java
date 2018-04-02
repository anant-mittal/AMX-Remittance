package com.amx.jax.postman.service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import org.springframework.stereotype.Component;

import com.amx.jax.postman.model.GeoLocation;
import com.amx.utils.ArgUtil;
import com.amx.utils.FileUtil;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

@Component
public class GeoLocationService {
	private DatabaseReader dbReader;

	public GeoLocationService() throws IOException {
		File database = FileUtil.getFile("ext-resources/GeoLite2-City.mmdb");
		/// File database = new File("GeoLite2-City.mmdb");
		dbReader = new DatabaseReader.Builder(database).build();
	}

	public GeoLocation getLocation(String ip) throws IOException, GeoIp2Exception {
		CityResponse response = this.getCity(ip);
		GeoLocation loc = new GeoLocation(ip);
		loc.setCityName(ArgUtil.parseAsString(response.getCity().getGeoNameId()));
		loc.setCityId(response.getCity().getName());
		loc.setStateCode(response.getMostSpecificSubdivision().getIsoCode());
		loc.setCountryCode(response.getCountry().getIsoCode());
		loc.setContinentCode(response.getContinent().getCode());
		return loc;// new GeoLocation(ip);
	}

	public CityResponse getCity(String ip) throws IOException, GeoIp2Exception {
		InetAddress ipAddress = InetAddress.getByName(ip);
		return dbReader.city(ipAddress);
	}
}
