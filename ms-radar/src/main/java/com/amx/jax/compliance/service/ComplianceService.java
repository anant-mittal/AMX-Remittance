package com.amx.jax.compliance.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.amx.jax.complaince.LoginDeatils;
import com.amx.jax.rest.RestService;
import com.amx.utils.JsonPath;
import com.google.gson.JsonObject;

@Component
public class ComplianceService {
	
	private static final Logger LOGGER = Logger.getLogger(ComplianceService.class);
	private static final JsonPath PARSED_TEXT = new JsonPath("ParsedResults/[0]/ParsedText");
	
	@Autowired
	RestService restService;

	public String tokenGenaration(@RequestBody LoginDeatils loginDeatils)throws Exception { {
		String  response ;
				
		JsonObject loginDeatilss = new JsonObject();
		loginDeatilss.addProperty("username", loginDeatils.getUserName());
		loginDeatilss.addProperty("password", loginDeatils.getPassword());
		loginDeatilss.addProperty("tokenlifetime", loginDeatils.getTokenLife());
		
		String content = loginDeatilss.toString();
		
		DataOutputStream out = null;
		try {

			URL url = new URL("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Authenticate/GetToken");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("charset", "UTF-8");

			// sending request
			OutputStream wr = connection.getOutputStream();
			wr.write(content.getBytes());
			wr.flush();

			// reading response
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuffer response_buffer = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response_buffer.append(line);
			}

			rd.close();
			response = response_buffer.toString();

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					LOGGER.info(""+e.getMessage());
				}
			}
		}

		return response;
	}
	

}
		
	
		public Map<String, Object> uploadComplainceReportFile(MultipartFile file, @RequestParam String token) throws IOException {
			
			Map<String, Object> resp = restService.ajax("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Reports/PostReport")
					
					.field("charset", "UTF-8")
					.field("ContentType", "application/octet-stream")
					.field("zipfile", file)
					.cookie(new Cookie("SqlAuthCookie",token))
					
					.postForm()
					.as(new ParameterizedTypeReference<Map<String, Object>>() {
					});

			
			System.out.println("STR Id " +resp);
		
			return resp;
		}

	
}




