package com.kankanews.search.core;

 
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class JacksonUtil {
	public static String toString(Object object)   {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	} 

	public static <T> T toObject(String str, Class<T> returnType)  {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		ObjectReader reader = mapper.reader(returnType);
		try {
			return reader.readValue(str);
		} catch (Exception e) { 
			e.printStackTrace();
		} 
		return null;
	}


}
