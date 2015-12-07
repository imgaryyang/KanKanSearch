package com.kankanews.search.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {
	
	
	
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {

		}finally{
			try {
				oos.close();
				baos.close();
			} catch (IOException e) {
			}
		}
		return null;
	}
	
	
	

	@SuppressWarnings("unchecked")
	public static    <T> T unserialize(byte[] bytes,Class<T> returnType) {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			T bean = (T)ois.readObject();
			return bean;
		} catch (Exception e) {

		}finally{
			try {
				ois.close();
				bais.close();
			} catch (IOException e) {
			}
		}
		return null;
	}
}
