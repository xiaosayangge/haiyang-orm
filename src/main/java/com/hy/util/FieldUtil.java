package com.hy.util;

import org.apache.commons.lang3.StringUtils;

/**  
* FieldUtil
* Creater by chenhaiyang on 2020年5月13日
*/
public class FieldUtil {

	
	public static String fieldConvertUnderline(String fieldName){
		
		StringBuilder sb=new StringBuilder(fieldName);
		
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if(Character.isUpperCase(c)){
				sb.replace(i, i+1, String.valueOf("_"+Character.toLowerCase(c)));
			}
		}
		return sb.toString();
	}
	
	//首字母转大写加前缀 比如 get set
	public static String getMethodCapitalize(String methodName,String PreStr){
		return PreStr+StringUtils.capitalize(methodName);
	}
	
	public static void main(String[] args) {
		
		String fieldName="activityConditionPrice";
		String name = fieldConvertUnderline(fieldName);
		System.out.println(name);
	}
}

