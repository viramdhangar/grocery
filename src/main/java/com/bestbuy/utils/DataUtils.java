package com.bestbuy.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.bestbuy.model.ConfigDTO;


/**
 * @author Viramm
 * 
 * will contain reusable methods
 *
 */
public class DataUtils {

	/**
	 * @param matches
	 */
	public static String getShortForm(String teamName) {
		String[] words = teamName.split("\\W+");
		StringBuffer sb = new StringBuffer();
		if(words.length == 1 && words[0].length() > 3) {
			return teamName.substring(0,  3).toUpperCase();
		}else if(words.length == 1 && words[0].length() < 3) {
			return teamName.substring(0,  2).toUpperCase();
		}
		for(String str : words) {
			//if(sb.length()<1) {
				//sb.append(str.substring(0,3));
			//}else {
				sb.append(str.substring(0,1));	
			//}
		}
		return sb.toString().toUpperCase();
	}
	
	public static String getOneName(String teamName) {
		String[] words = teamName.split("\\W+");
		if(words.length > 0) {
			return words[0];
		}
		return "";
	}
	
	public static double valueInDouble(String value) {
		if(StringUtils.isNotEmpty(value)){
			return Double.parseDouble(value);
		}else {
			return 0.0;
		}
	}
	
	public static double valueInDoubleFromObj(Object value) {
		if(StringUtils.isNotEmpty(value.toString())){
			return Double.parseDouble(value.toString());
		}else {
			return 0.0;
		}
	}
	
	public static Date getCurrentDateTime() {
		Date today = new Date();
		SimpleDateFormat istTimeFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		TimeZone istTime = TimeZone.getTimeZone("IST");
		istTimeFormat.setTimeZone(istTime);
		DateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		try {
			return format.parse(istTimeFormat.format(today));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static ConfigDTO getConfigObject(List<ConfigDTO> configList) {
		if(configList.size() > 0) {
			ConfigDTO str = configList.get(0);
			return str;
		}else {
			return null;
		}
	}
	
	// compress the image bytes before storing it in the database
	public static byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}
		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

		return outputStream.toByteArray();
	}

	// uncompress the image bytes before returning it to the angular application
	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}
	
}
