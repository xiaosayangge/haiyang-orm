package com.hy.parse;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.alibaba.druid.util.JdbcUtils;
import com.hy.annotation.Insert;
import com.hy.annotation.Param;
import com.hy.annotation.TableName;
import com.hy.annotation.Update;
import com.hy.model.ActivityModel;
import com.hy.parse.SqlInfo.ParamInfo;
import com.hy.util.CollectionUtil;
import com.hy.util.FieldUtil;
import com.hy.util.TypeUtil;

/**  
* BuildSql
* Creater by chenhaiyang on 2020年5月15日
*/
public class BuildSql {
	
	private static final Logger logger=LoggerFactory.getLogger(BuildSql.class);

	
	private static final String INSERT_SQL="INSERT INTO ";
	
	private static final String UPDATE_SQL="UPDATE ";
	
	
	public String buildSql(SqlInfo sqlInfo){
		Object[] args = sqlInfo.getArgs();
		validParam(args);
		Object object = args[0];
		String tableName = getTableName(object);
		if(sqlInfo.getAnnotation().annotationType()==Insert.class){
			setInsertSql(tableName, object, sqlInfo);
		}else if(sqlInfo.getAnnotation().annotationType()==Update.class){
			setUpdateSql(tableName, object, sqlInfo);
		}
		return sqlInfo.getSql();
		
	}

	private void setUpdateSql(String tableName,Object obj,SqlInfo sqlInfo){
		try {
			ParamInfo primary = TypeUtil.getPrimary(obj,true);
			Field[] fields = FieldUtils.getAllFields(obj.getClass());
			StringBuilder sb=new StringBuilder(UPDATE_SQL);
			sb.append(tableName).append(" SET ");
			List<ParamInfo> paramInfoList = sqlInfo.getParamInfoList();
			for (int i = 0; i < fields.length; i++) {
				if(!primary.getName().equals(fields[i].getName())){
					fields[i].setAccessible(true);
					Object attr = fields[i].get(obj);
					if(!ObjectUtils.isEmpty(attr)){
						ParamInfo paramInfo=sqlInfo.new ParamInfo();
						paramInfo.setType(fields[i].getType());
						paramInfo.setVal(attr);
						sb.append(FieldUtil.fieldConvertUnderline(fields[i].getName())).append("= ? ,");
						paramInfoList.add(paramInfo);
					}
				}
			}
			sb.delete(sb.length()-1, sb.length()).append(" where ");
			sb.append(primary.getName()).append("= ?");
			paramInfoList.add(primary);
			logger.debug("sql:{}",sb.toString());
			logger.debug("参数列表{}",paramInfoList.toString());
			sqlInfo.setSql(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("update获取对象属性失败");
		}
	}
	
	
	
	
	private void setInsertSql(String tableName,Object obj,SqlInfo sqlInfo){
		try {
			Field[] fields = FieldUtils.getAllFields(obj.getClass());
			StringBuilder sb=new StringBuilder(INSERT_SQL);
			sb.append(tableName).append("(");
			List<ParamInfo> paramInfoList = sqlInfo.getParamInfoList();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				Object attr = fields[i].get(obj);
				if(!ObjectUtils.isEmpty(attr)){
					ParamInfo paramInfo=sqlInfo.new ParamInfo();
					paramInfo.setType(fields[i].getType());
					paramInfo.setVal(attr);
					sb.append(FieldUtil.fieldConvertUnderline(fields[i].getName())).append(",");
					paramInfoList.add(paramInfo);
				}
			}
			sb.delete(sb.length()-1, sb.length());
			sb.append(") VALUES (");
			paramInfoList.forEach(info->sb.append("?,"));
			String str=sb.substring(0, sb.length()-1);
			sb.setLength(0);
			sb.append(str).append(")");
			logger.debug("sql:{}",sb.toString());
			logger.debug("参数列表{}",paramInfoList.toString());
			sqlInfo.setSql(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("insert获取对象属性失败");
		}
	}
	
	private String getTableName(Object obj){
		TableName annotation = obj.getClass().getAnnotation(TableName.class);
		String tableName=obj.getClass().getSimpleName();
		String suffix="Model";
		if(annotation!=null){
			tableName=annotation.value();
		}else{
			int index = tableName.indexOf(suffix);
			if(index!=-1){
				tableName=tableName.substring(0,index);
			}
		}
		return tableName;
	}
	
	private  boolean validParam(List<Param> list){
		Assert.isTrue(!list.isEmpty(),"参数为空");
		return true;
	}
	
	private boolean validParam(Object[] args){
		Assert.isTrue(!ArrayUtils.isEmpty(args),"参数为空");
		return true;
	}
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		BuildSql b=new BuildSql();
		 ActivityModel model = new ActivityModel();
		 model.setActivityName("a");
		 model.setActivityType(1);
		b.setInsertSql("activity", model, new SqlInfo());
		
	}
		
}
