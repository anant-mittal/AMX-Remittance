package com.amx.jax.utils;

import java.security.MessageDigest;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;



/**
 * 
 * @author Rabil
 *
 */
public class Util {

	  public static final HashMap PASS_QUESTION = new HashMap();
	    public static final HashMap<Integer, String> PASS_IMAGE = new HashMap<Integer, String>();
	    public static final int SET_SIZE_REQUIRED = 3;
	    public static final int NUMBER_RANGE = 8;
	    
	    
	    public static String getHash(String userid, String password) {

	        byte[] output = null;
	        String salt = null;
	        String newpassoword = null;

	        try {
	            MessageDigest md = MessageDigest.getInstance("SHA-256");
	            salt = new StringBuffer(userid).reverse().toString();
	            newpassoword = null;
	            salt = salt.substring(0, 4);
	            newpassoword = addSalt(password, salt);
	            md.update(newpassoword.getBytes());
	            output = md.digest();
	            newpassoword = bytesToHex(output);
	        } catch (NullPointerException ne) {
	                ne.printStackTrace();
	            throw new NullPointerException ("invalid values: "); 
	                
	        
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException("Unexpected error occured: "); 
	        }
	        return newpassoword;
	  }

	     public static boolean isNullOrBlank(String param) {
	        return param == null || param.trim().length() == 0;
	    }

	     
	    public static String getHash_admin(String userid,String password){
	       
	        byte[] output = null;
	        String newpassoword = null;
	    
	        try {
	            MessageDigest md = MessageDigest.getInstance("SHA-256");
	            newpassoword = null;
	            newpassoword = addSalt(password, userid);
	            md.update(newpassoword.getBytes()); 
	            output = md.digest();
	            newpassoword =  bytesToHex(output);
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	        return newpassoword;
	     }
	     
	    public static String addSalt(String password,String salt){
	        final int aLength = password.length();
	        final int bLength = salt.length();
	        final StringBuilder sb = new StringBuilder(aLength + bLength);
	         try {
	            final int min = Math.min(aLength, bLength);
	            for(int i = 0; i < min; i++) {
	                sb.append(password.charAt(i));
	                sb.append(salt.charAt(i));
	            }
	            if (aLength > bLength) {
	                sb.append(password, bLength, aLength);
	            } else if (aLength < bLength) {
	                sb.append(salt, aLength, bLength);
	            }
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            throw new NullPointerException ("Invalid values : ");
	        }
	        return sb.toString();
	    }
	     
	    public static String bytesToHex(byte[] b) {
	       char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
	                          '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	       StringBuffer buf = new StringBuffer();
	       for (int j=0; j<b.length; j++) {
	          buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
	          buf.append(hexDigit[b[j] & 0x0f]);
	       }
	       return new String(buf.toString());
	    }
	    
	    
	    // De-Cryption of the given string
	    public static String getDECrypted(String userid,String password) {
	           
	      String salt = new StringBuffer(userid).reverse().toString();
	      salt = salt.substring(0,4);
	      String key = "almullaexchangeonlineremitt2010" + salt;
	      
	      Connection con = null;
	     
	      String Decrypted = null;
	       CallableStatement c = null;
	       try{
	          con = null;//ex.view.util.DBUtils.getConnection();
	          String proc = "Begin ? := PKG_ENCRYPT.decrypt_string ( ?, ?); End;";
	           c = con.prepareCall(proc);
	          

	          // Decrypt the password to compare with users password
	          c.setString(2, password);
	          c.setString(3, key );
	          c.registerOutParameter(1, Types.VARCHAR); // Encrypted
	          c.execute();
	          
	          Decrypted = c.getString(1);
	          c.close();
	         } catch (Exception e) {
	              e.printStackTrace();
	           }finally{
	              try{ if(c!=null){
	                  c.close();
	              }
	              if(con!=null){
	                  con.close();
	              }
	               
	                }catch(SQLException e){
	                  e.printStackTrace();
	                }
	              }
	       return Decrypted;       
	   }
	   
	      // En-Cryption of the given string
	      public static String getENCrypted(String userid,String password) {
	             
	        String salt = new StringBuffer(userid).reverse().toString();
	        salt = salt.substring(0,4);
	        String key = "almullaexchangeonlineremitt2010" + salt;
	        String encrypted = null;
	        
	        Connection con = null;
	    
	          CallableStatement c=null;
	         try{
	            con = null; // ex.view.util.DBUtils.getConnection();
	            String proc = "Begin ? := PKG_ENCRYPT.encrypt_string ( ? , ? ); End;";
	             c = con.prepareCall(proc);
	            String resultString;

	            c.setString(2, password);
	            c.setString(3, key);
	            c.registerOutParameter(1, Types.VARCHAR);
	            c.execute();
	            encrypted = c.getString(1);
	            c.close();
	           } catch (Exception e) {
	                e.printStackTrace();
	             }finally{
	              try{ if(c!=null){
	                  c.close();
	              }
	              if(con!=null){
	                  con.close();
	              }
	               
	                }catch(SQLException e){
	                  e.printStackTrace();
	                }
	              }
	          return encrypted;
	      }
	      
	      public static Set getThreeRandNo() {
	          Random random = new Random();
	          Set set = new HashSet<Integer>(SET_SIZE_REQUIRED);
	          while (set.size() < SET_SIZE_REQUIRED) {
	          while (set.add(random.nextInt(NUMBER_RANGE)) != true);
	          }

	            assert set.size() == SET_SIZE_REQUIRED;

	            System.out.println(set);
	                return set;
	      }
	      
	      public static String CreateRandomPassword() {
	          // ////System.out.println("here  in CreateRandomPassword:");
	          int PasswordLength = 6;
	          //String validChars = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ1234567890";
	          String validChars = "1234567890";
	          String password = "";
	          for (int i = 0; i < PasswordLength; i++) {
	              password = 
	                      password + String.valueOf(validChars.charAt((int)(Math.random() * validChars.length())));
	          }
	          return password;
	      }
	      
	      
	      public static void main(String[] strs) {
	    	  System.out.println("Random  Password :"+Util.CreateRandomPassword());
	    	  System.out.println("Encrypt Password :"+Util.CreateRandomPassword());
	    	  
	      }
	      
	    
}
