package com.hy.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import com.hy.model.ActivityModel;
import com.hy.parse.SqlInfo;
import com.hy.parse.SqlInfo.ParamInfo;

/**  
* TypeUtil
* Creater by chenhaiyang on 2020年5月13日
*/
public class TypeUtil {
	private static final Logger logger=LoggerFactory.getLogger(TypeUtil.class);

	public static Class<?> getType(Object obj){
		Class cls=null;
		if(obj instanceof Integer){
			cls=Integer.class;
		}else if(obj instanceof String){
			cls=String.class; 
		}else if(obj instanceof Long){
			cls=Long.class;
		}else if( obj instanceof Collection){
			cls=Collection.class;
		}else{
			Assert.isTrue(false,"类型获取失败!");
		}
		return cls;
	}
	
	
	public static void setParamType(PreparedStatement statement,List<ParamInfo> paramInfoList){
		
		for (int i = 0; i < paramInfoList.size(); i++) {
			ParamInfo info = paramInfoList.get(i);
			Class<?> type = info.getType();
			Object val = info.getVal();
			try {
				int index=i+1;
				if(Integer.class==type){
					statement.setInt(index,Integer.parseInt(val.toString()));
				}else if(String.class==type){
					statement.setString(index, val.toString());
				}else if(Long.class==type){
					statement.setLong(index, Long.parseLong(val.toString()));
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("类型设置失败!");
			}
		}
		
	}
	
	public static void setPrimary(SqlInfo sqlInfo,ResultSet resultSet) {
		try {
			Object object = sqlInfo.getArgs()[0];
			ParamInfo primary = getPrimary(object,false);
			getGeneratedKey(resultSet,primary);
			Method setPrimaryMethod = object.getClass().
					getDeclaredMethod(FieldUtil.getMethodCapitalize(primary.getName(), "set"), primary.getType());
			setPrimaryMethod.invoke(object,  primary.getVal());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("设置返回主键错误哦!");
		}
	}
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		ActivityModel activityModel = new ActivityModel();
		Field class1 = activityModel.getClass().getDeclaredField("id");
		System.out.println(class1.getType());
		Method declaredMethod = activityModel.getClass().getDeclaredMethod("setId", class1.getType());
		
		
		declaredMethod.invoke(activityModel, 1l);
		System.out.println(activityModel.getId());
	}
	
	
	public static void getGeneratedKey(ResultSet resultSet,ParamInfo primary) throws SQLException{
		if(resultSet.next()){
			Object object = resultSet.getObject(1);
			if(object instanceof BigInteger && primary.getType()==BigInteger.class){
				primary.setVal(object);
			}else if(object instanceof BigInteger && primary.getType()==Long.class){
				primary.setVal(Long.parseLong(object.toString()));
			}else if(object instanceof Integer && primary.getType()==Long.class){
				primary.setVal((Long)object);
			}else if(object instanceof Integer && primary.getType()==Integer.class){
				primary.setVal(object);
			}
		}
	}
	
	/**
	 * 
	 * @param obj 对象
	 * @param required 是否必须主键有值
	 */
	public static ParamInfo getPrimary(Object obj,boolean required){
		try {
			ParamInfo info=null;
			Field[] fields = FieldUtils.getAllFields(obj.getClass());
			for (int i = 0; i < fields.length; i++) {
				com.hy.annotation.Field annotation = fields[i].getAnnotation(com.hy.annotation.Field.class);
				if(annotation!=null && annotation.primary()){
					fields[i].setAccessible(true);
					//找到设置主键的属性
					String primaryName = FieldUtil.fieldConvertUnderline(fields[i].getName());
					Class<?> type = fields[i].getType();
					Object val = fields[i].get(obj);
					info=new SqlInfo().new ParamInfo(primaryName,type,val);
				}
			}
			if(info==null){
				info= new SqlInfo().new ParamInfo("id",Long.class,null);
			}
			Assert.isTrue(required&& !ObjectUtils.isEmpty(info.getVal()),"必须指定主键id值");
			return info;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static List<Object> setResult(Class<?> cls,ResultSet resultSet){
		try {
			List<Object> list=new ArrayList<>(resultSet.getRow());
			Field[] fields = FieldUtils.getAllFields(cls);
			while(resultSet.next()){
				Object obj = cls.newInstance();
				for (int i = 0; i < fields.length; i++) {
					String fieldName = fields[i].getName();
					Class<?> type = fields[i].getType();
					String underlineName = FieldUtil.fieldConvertUnderline(fieldName);
					Object resultField = resultSet.getObject(underlineName);
					if(resultField!=null){
						String setMethod = FieldUtil.getMethodCapitalize(fieldName, "set");
						Method method = cls.getMethod(setMethod, type);
						method.invoke(obj, resultField);
					}
				}
				list.add(obj);
			}
			return list;
		} catch (Exception e) {
			System.out.println("处理查询结果集错误!");
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
	
	public static void closeResource(Connection c,PreparedStatement p,ResultSet s){
		try {
			if(s!=null){
				s.close();
			}
			if(p!=null){
				p.close();
			}
			if(c!=null){
				c.close();
			}
		} catch (Exception e) {
			System.out.println("释放资源错误!");
			e.printStackTrace();
		}
	}
	
}
