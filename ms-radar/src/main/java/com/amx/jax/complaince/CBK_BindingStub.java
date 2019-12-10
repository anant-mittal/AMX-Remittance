package com.amx.jax.complaince;
/*
 * package com.amx.jax.compalince;
 * 
 * import java.io.BufferedReader; import java.io.DataOutputStream; import
 * java.io.File; import java.io.FileOutputStream; import java.io.IOException;
 * import java.io.InputStreamReader; import java.io.OutputStream; import
 * java.math.BigDecimal; import java.net.HttpURLConnection; import java.net.URL;
 * import java.util.Date; import java.util.zip.ZipEntry; import
 * java.util.zip.ZipOutputStream; import org.apache.http.entity.ContentType;
 * import org.apache.http.HttpEntity; import org.apache.http.HttpResponse;
 * import org.apache.http.HttpStatus; import org.apache.http.StatusLine; import
 * org.apache.http.client.ClientProtocolException; import
 * org.apache.http.client.CookieStore; import org.apache.http.client.HttpClient;
 * import org.apache.http.client.methods.CloseableHttpResponse; import
 * org.apache.http.client.methods.HttpGet; import
 * org.apache.http.client.methods.HttpPost; //import
 * org.apache.http.entity.mime.MultipartEntityBuilder; import
 * org.apache.http.impl.client.BasicCookieStore; import
 * org.apache.http.impl.client.CloseableHttpClient; import
 * org.apache.http.impl.client.HttpClientBuilder; import
 * org.apache.http.impl.client.HttpClients; import
 * org.apache.http.impl.cookie.BasicClientCookie; import
 * org.apache.log4j.Logger;
 * 
 * public class CBK_BindingStub { Logger logger =
 * Logger.getLogger("CBK_BindingStub.class");
 * 
 * public boolean report_generate() throws ClientProtocolException, IOException
 * {
 * 
 * String submissiondate = "2019-05-16T00:00:00"; StringBuffer send_txn_Requset
 * = new StringBuffer(); String token =null; //framing xml report
 * send_txn_Requset.append("<report>")
 * .append("<rentity_id>").append("18").append("</rentity_id>")
 * .append("<submission_code>").append("E").append("</submission_code>")
 * .append("<report_code>").append("STR").append("</report_code>")
 * .append("<submission_date>").append(submissiondate).append(
 * "</submission_date>") .append("<currency_code_local>").append("KWD").append(
 * "</currency_code_local>") .append("<reporting_person>")
 * .append("<first_name>").append("almulla").append("</first_name>")
 * .append("<last_name>").append("admin").append("</last_name>")
 * .append("<nationality1>").append("KW").append("</nationality1>")
 * .append("<phones>") .append("<phone>")
 * .append("<tph_contact_type>").append("1").append("</tph_contact_type>")
 * .append("<tph_communication_type>").append("M").append(
 * "</tph_communication_type>")
 * .append("<tph_number>").append("123456").append("</tph_number>")
 * .append("</phone>") .append("</phones>")
 * .append("<email>").append("Mohammed.Jabarullah@almullaexchange.com").append(
 * "</email>")
 * .append("<occupation>").append("compliance officer").append("</occupation>")
 * .append("</reporting_person>") .append("<location>")
 * .append("<address_type>").append("1").append("</address_type>")
 * .append("<address>").append("Khaitan").append("</address>")
 * .append("<city>").append("Kuwait").append("</city>")
 * .append("<country_code>").append("KW").append("</country_code>")
 * .append("</location>")
 * .append("<reason>").append("Suspicious transaction ").append("</reason>")
 * .append("<action>").append("Blocked the transaction").append("</action>")
 * .append("<transaction>")
 * .append("<transactionnumber>").append("2019/123456").append(
 * "</transactionnumber>")
 * .append("<transaction_location>").append("Murgab Branch").append(
 * "</transaction_location>")
 * .append("<date_transaction>").append(submissiondate).append(
 * "</date_transaction>")
 * .append("<teller>").append("Kanmani").append("</teller>")
 * .append("<authorized>").append("Kanmani").append("</authorized>")
 * .append("<transmode_code>").append("C").append("</transmode_code>")
 * .append("<amount_local>").append("150").append("</amount_local>")
 * .append("<t_from_my_client>")
 * .append("<from_funds_code>").append("B").append("</from_funds_code>")
 * .append("<from_foreign_currency>")
 * .append("<foreign_currency_code>").append("INR").append(
 * "</foreign_currency_code>")
 * .append("<foreign_amount>").append("30000").append("</foreign_amount>")
 * .append("<foreign_exchange_rate>").append("230.25").append(
 * "</foreign_exchange_rate>") .append("</from_foreign_currency>")
 * .append("<from_person>") .append("<gender>").append("M").append("</gender>")
 * .append("<title>").append("Mr").append("</title>")
 * .append("<first_name>").append("Osaba").append("</first_name>")
 * .append("<last_name>").append("Bil Laden").append("</last_name>")
 * .append("<ssn>").append("2019/123456").append("</ssn>")
 * .append("<nationality1>").append("SA").append("</nationality1>")
 * .append("<phones>") .append("<phone>")
 * .append("<tph_contact_type>").append("1").append("</tph_contact_type>")
 * .append("<tph_communication_type>").append("M").append(
 * "</tph_communication_type>")
 * .append("<tph_country_prefix>").append("M").append("</tph_country_prefix>")
 * .append("<tph_number>").append("123456789").append("</tph_number>")
 * .append("</phone>") .append("</phones>") .append("<addresses>")
 * .append("<address>")
 * .append("<address_type>").append("1").append("</address_type>")
 * .append("<address>").append("Khaitan").append("</address>")
 * .append("<city>").append("Kuwait").append("</city>")
 * .append("<country_code>").append("KW").append("</country_code>")
 * .append("</address>") .append("</addresses>") .append("<identification>")
 * .append("<type>").append("B").append("</type>")
 * .append("<number>").append("2810502079628").append("</number>")
 * .append("<issue_country>").append("KW").append("</issue_country>")
 * .append("</identification>") .append("</from_person>")
 * .append("<from_country>").append("KW").append("</from_country>")
 * .append("</t_from_my_client>") .append("<t_to>")
 * .append("<to_funds_code>").append("B").append("</to_funds_code>")
 * .append("<to_foreign_currency>")
 * .append("<foreign_currency_code>").append("INR").append(
 * "</foreign_currency_code>")
 * .append("<foreign_amount>").append("30000").append("</foreign_amount>")
 * .append("<foreign_exchange_rate>").append("230.50").append(
 * "</foreign_exchange_rate>") .append("</to_foreign_currency>")
 * .append("<to_person>") .append("<gender>").append("M").append("</gender>")
 * .append("<title>").append("Mr").append("</title>")
 * .append("<first_name>").append("Test bene").append("</first_name>")
 * .append("<last_name>").append("bene").append("</last_name>")
 * .append("<ssn>").append("2019/123456").append("</ssn>")
 * .append("<nationality1>").append("IN").append("</nationality1>")
 * .append("<phones>") .append("<phone>")
 * .append("<tph_contact_type>").append("1").append("</tph_contact_type>")
 * .append("<tph_communication_type>").append("M").append(
 * "</tph_communication_type>")
 * .append("<tph_country_prefix>").append("0091").append(
 * "</tph_country_prefix>")
 * .append("<tph_number>").append("0091").append("</tph_number>")
 * .append("</phone>") .append("</phones>") .append("<addresses>")
 * .append("<address>")
 * .append("<address_type>").append("1").append("</address_type>")
 * .append("<address>").append("Tamil Nadu").append("</address>")
 * .append("<city>").append("Chennai").append("</city>")
 * .append("<country_code>").append("KW").append("</country_code>")
 * .append("</address>") .append("</addresses>") .append("<identification>")
 * .append("<type>").append("D").append("</type>")
 * .append("<number>").append("123456").append("</number>")
 * .append("<issue_country>").append("IN").append("</issue_country>")
 * .append("</identification>") .append("</to_person>")
 * .append("<to_country>").append("KW").append("</to_country>")
 * .append("</t_to>") .append("</transaction>") .append("<report_indicators>")
 * .append("<indicator>").append("PO-1").append("</indicator>")
 * .append("<indicator>").append("RS-2").append("</indicator>")
 * .append("</report_indicators>") .append("</report>");
 * 
 * StringBuilder sb = new StringBuilder(); sb.append(send_txn_Requset);
 * 
 * //making report in to a zip file... String file = "_Web_Report_ReportID";
 * Date date = new Date();
 * 
 * long time = date.getTime();
 * 
 * String filenamewithoutextension = file + time; File f = new
 * File("d:\\" + filenamewithoutextension + ".zip"); ZipOutputStream out = new
 * ZipOutputStream(new FileOutputStream(f)); String fileExtension = ".xml";
 * 
 * String fileName = filenamewithoutextension + fileExtension; ZipEntry e1 = new
 * ZipEntry(fileName); out.putNextEntry(e1);
 * 
 * byte[] data = sb.toString().getBytes(); out.write(data, 0, data.length);
 * out.closeEntry();
 * 
 * out.close();
 * 
 * //token generation try { token = generate_token("almullaadmin", "123456"); }
 * catch (Exception ex) {
 * 
 * logger.info("Error while generating token",ex); }
 * 
 * //sending report to the partner try {
 * 
 * String attachmentFileName = filenamewithoutextension + ".zip";
 * 
 * HttpPost post = new
 * HttpPost("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Reports/PostReport");
 * 
 * CookieStore cookieStore = new BasicCookieStore(); BasicClientCookie cookie =
 * new BasicClientCookie("SqlAuthCookie", token);
 * cookie.setDomain("goaml.kwfiu.gov.kw"); cookie.setPath("/");
 * cookieStore.addCookie(cookie); HttpClient client =
 * HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build(); //
 * MultipartEntityBuilder builder = MultipartEntityBuilder.create();
 * builder.addBinaryBody("file", f, ContentType.APPLICATION_OCTET_STREAM,
 * attachmentFileName); builder.addTextBody("charset", "UTF-8"); HttpEntity
 * multipart = builder.build(); post.setEntity(multipart);
 * 
 * HttpResponse response = client.execute(post);
 * 
 * StatusLine line = response.getStatusLine(); return line.getStatusCode() ==
 * HttpStatus.SC_CREATED;
 * 
 * } finally { if (out != null) { try { out.close(); } catch (IOException ex) {
 * logger.error("Exception while uploading zip file",ex); } } }
 * 
 * }
 * 
 * public String generate_token(String username, String password) throws
 * Exception {
 * 
 * String response = null; username = "almullaadmin"; password = "123456";
 * 
 * BigDecimal tokenlifetime = new BigDecimal(60);
 * 
 * String content = "{\"username\":\"" + username + "\",\"password\":\"" +
 * password + "\",\"tokenlifetime\":\"" + tokenlifetime + "\"}";
 * 
 * DataOutputStream out = null; try {
 * 
 * URL url = new
 * URL("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Authenticate/GetToken");
 * HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 * 
 * connection.setDoOutput(true); connection.setDoInput(true);
 * connection.setRequestMethod("POST");
 * connection.setRequestProperty("Content-Type", "application/json");
 * connection.setRequestProperty("charset", "UTF-8");
 * 
 * // sending request OutputStream wr = connection.getOutputStream();
 * wr.write(content.getBytes()); wr.flush();
 * 
 * // reading response BufferedReader rd = new BufferedReader(new
 * InputStreamReader(connection.getInputStream())); String line; StringBuffer
 * response_buffer = new StringBuffer(); while ((line = rd.readLine()) != null)
 * { response_buffer.append(line); }
 * 
 * rd.close(); response = response_buffer.toString();
 * 
 * } finally { if (out != null) { try { out.close(); } catch (IOException e) {
 * logger.info("Exception while generating token value",e); } } }
 * 
 * return response; }
 * 
 * }
 * 
 */