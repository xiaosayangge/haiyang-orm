package com.hy.parse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.hy.annotation.Insert;
import com.hy.annotation.Param;
import com.hy.annotation.SelectList;
import com.hy.annotation.SelectOne;
import com.hy.annotation.Update;
import com.hy.parse.SqlInfo.ParamInfo;
import com.hy.util.TypeUtil;

/**  
* ParseSql
* Creater by chenhaiyang on 2020年5月13日
*/
public class ParseSql {

	
	public static SqlInfo parse(SqlInfo sqlInfo){
		validParam(sqlInfo);
		String sql = sqlInfo.getSql();
		if(sql.matches(".*(insert|INSERT).*")){
			//parseObject(sqlInfo);
		}else if(sql.matches(".*(update|UPDATE).*")){
			//parseObject(sqlInfo);
		}else{
			parseSelect(sqlInfo);
		}
		return sqlInfo;
	}
	
	public static void parseSelect(SqlInfo sqlInfo){
		StringBuilder sb=new StringBuilder(sqlInfo.getSql());
		List<ParamInfo> paramInfoList = sqlInfo.getParamInfoList();
		List<Param> paramList = sqlInfo.getParamList();
		for (int i = 0; i <paramList.size(); i++) {
			ParamInfo paramInfo=sqlInfo.new ParamInfo();
			String param = paramList.get(i).value();
			String p="#{"+param+"}";
			int index = sb.indexOf(p);
			sb.replace(index, index+p.length(), "?");
			paramInfo.setType(TypeUtil.getType(sqlInfo.getArgs()[i]));
			paramInfo.setVal(sqlInfo.getArgs()[i]);
			paramInfoList.add(paramInfo);
		}
		sqlInfo.setSql(sb.toString());
		
	}
	
	public static void parseObject(SqlInfo sqlInfo){
		StringBuilder sb=new StringBuilder(sqlInfo.getSql());
		List<ParamInfo> paramInfoList = sqlInfo.getParamInfoList();
		List<Param> paramList = sqlInfo.getParamList();
		for (int i = 0; i <paramList.size(); i++) {
			ParamInfo paramInfo=sqlInfo.new ParamInfo();
			String param = paramList.get(i).value();
			String p="#{"+param+"}";
			int index = sb.indexOf(p);
			sb.replace(index, index+p.length(), "?");
			paramInfo.setType(TypeUtil.getType(sqlInfo.getArgs()[i]));
			paramInfo.setVal(sqlInfo.getArgs()[i]);
			paramInfoList.add(paramInfo);
		}
		sqlInfo.setSql(sb.toString());
	}
	
	public static String getSql(SqlInfo sqlInfo){
		Annotation annotation = sqlInfo.getAnnotation();
		if(SelectList.class==annotation.annotationType()){
			SelectList list=(SelectList) annotation;
			return list.value();
		}else if(SelectOne.class==annotation.annotationType()){
			SelectOne one=(SelectOne) annotation;
			return one.value();
		}else if(Insert.class==annotation.annotationType() || Update.class==annotation.annotationType()){
			BuildSql buildSql=new BuildSql();
			return buildSql.buildSql(sqlInfo);
		}
		return null;
	}
	
	public static Annotation getAnnotation(Method method){
		Annotation[] annotations = method.getAnnotations();
		for (int i = 0; i < annotations.length; i++) {
			if(SelectList.class==annotations[i].annotationType()){
				return annotations[i];
			}else if(SelectOne.class==annotations[i].annotationType()){
				return annotations[i];
			}else if(Insert.class==annotations[i].annotationType()){
				return annotations[i];
			}else if(Update.class==annotations[i].annotationType()){
				return annotations[i];
			}
		}
		return null;
	}
	
	private static void validParam(SqlInfo sqlInfo){
		Assert.hasText(sqlInfo.getSql(),"sql 为空!");
		if(sqlInfo.getAnnotation().annotationType()==SelectList.class||sqlInfo.getAnnotation().annotationType()==SelectOne.class){
			Assert.isTrue(sqlInfo.getSql().contains("#{")&&sqlInfo.getArgs().length!=0,"参数为空!");
			Assert.isTrue(sqlInfo.getParamList().size()==sqlInfo.getArgs().length,"参数数量不匹配");
		}else{
			Assert.isTrue(sqlInfo.getArgs().length==1,"对象参数为空");
		}
	}
	
	
}
