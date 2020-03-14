package com.chtc.util;

import java.util.Iterator;
import java.util.Map;


public class StringUtil {
	
	public static void escapeXmlTag(Map<String, Object> data) {
		if (data == null) {
			return;
		}
		Iterator<String> iter = data.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			Object obj = data.get(key);
			if (obj != null && obj instanceof String) {
				String value = replaceXmlTag((String)obj);
				data.put(key, value);
			}
		}
	}
	public static String replaceXmlTag(String s) {
		if(s == null){
			return "";
		}else{
			return s.replace("&", "&amp;").replace("'", "&apos;")
			.replace("<", "&lt;").replace(">", "&gt;")
			.replace("\"", "&quot;");
		}
	}
	
	public static boolean isNotNull(String s){
		if(s==null||s.trim().length()<1){
			return false;
		}
		return true;
	}
	
	public static String replaceIllegalChar(String s){
		s = s == null ? "" : s;
		s = s.replaceAll("\'", "\'\'");
		s = s.replaceAll("_", "\\_");
		s = s.replaceAll("%", "\\%");
		return s;
	}
	
	
}
