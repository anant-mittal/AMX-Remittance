package com.amx.jax.auth;


import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class Test {


    public static void main(String[] args)  {   		
    	
    	//Test.postRemittanceValues();
    	//Test.postFcPurchaseValues();   
    	rbackList();

}
    
   
    
    public static void postRemittanceValues(){    	
    	try{
			String url = "http://10.28.42.36:8087/rbaac/v1/roles/get?ipAddress=192.168.0.1";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestMethod("POST");
			
			OutputStream os = con.getOutputStream();
			os.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			if(responseCode == 200){
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				try {
					JSONObject jsonObj = new JSONObject(response.toString());
					String res = jsonObj.getString("results");
					//System.out.println(res);
					
					JSONArray jsonArr = new JSONArray(res);

			        for (int i = 0; i < jsonArr.length(); i++)
			        {
			            JSONObject jsonObj1 = jsonArr.getJSONObject(i);
			            System.out.println(jsonObj1.getString("id"));
			            System.out.println(jsonObj1.getString("role"));
			        }
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}	   	
    }
    
    
    public static void rbackList(){
    	try{
			String url = "http://10.28.42.36:8067/rbaac/v1/roles/alloc/get-role-map-for-employee?employeeId=891&ipAddress=192.168.0.1";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestMethod("POST");
			
			OutputStream os = con.getOutputStream();
			os.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			if(responseCode == 200){
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				try {
					JSONObject jsonObj = new JSONObject(response.toString());
					String res = jsonObj.getString("results");
					System.out.println(res);
					JSONArray array1 = new JSONArray(res);
					JSONObject jsonObj1 = (JSONObject) array1.get(0);
					
					String res1 = jsonObj1.getString("roleInfoMap");
					System.out.println(res1);
					
					JSONObject roleInfoMap = jsonObj1.getJSONObject("roleInfoMap");
					
					Iterator<String> keys = roleInfoMap.keys();
					
					//JSONArray jsonArr = new JSONArray(res);

					while (keys.hasNext())
			        {
						String key = keys.next();
			        	JSONObject jsonObj2 = (JSONObject) roleInfoMap.get(key);
			            
			            System.out.println((Integer)jsonObj2.get("id"));
			            System.out.println((String)jsonObj2.get("role"));
			        }
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
    }
    
    
    public static void postFcPurchaseValues(){
    	try{
    		String url = "http://10.28.42.36:8061/meta/device/fcpurchase?deviceType=SIGNATURE_PAD&countryBranchSystemInventoryId=362";
        	String json = "{\r\n" + 
        			"  \"ccyAndAmt\": \"12\",\r\n" + 
        			"  \"exchangeRate\": \"23\",\r\n" + 
        			"  \"kdAmount\": \"3\",\r\n" + 
        			"  \"purposeOfTxn\": \"GBHJ\",\r\n" + 
        			"  \"sourceOfFunds\": \"GD\",\r\n" + 
        			"  \"totalAmount\": \"6\"\r\n" + 
        			"}";
        	
    		URL obj = new URL(url);
    		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    		//add reuqest header
    		
    		//con.setRequestProperty("User-Agent", USER_AGENT);
    		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
    		con.setRequestProperty("Content-Type", "application/json");
    		con.setDoOutput(true);
    		con.setDoInput(true);
    		con.setRequestMethod("POST");	
    		
    		con.setRequestProperty("meta-info", 
    				"{\"countryId\":91,\"customerId\":5218,\"companyId\":1,\"channel\":\"ONLINE\" , \"countryBranchId\":\"78\", \"tenant\":\"KWT\",\"languageId\":1 }");
    		OutputStream os = con.getOutputStream();
    		os.write(json.getBytes());
    		os.close();
    		
    		int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
			System.out.println("JSON : " + json);
    		
			if(responseCode == 200){
				InputStream in = new BufferedInputStream(con.getInputStream());
	    		//String result  = IOUtils.toString(in , "UTF-8");
	    		//System.out.println(result);
	    		in.close();
	    		con.disconnect();
			}else{
				InputStream errorStream = con.getErrorStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(errorStream));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				try {
					JSONObject jsonObj = new JSONObject(response.toString());
					String value = jsonObj.getString("message");
					System.out.println(value);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
}
