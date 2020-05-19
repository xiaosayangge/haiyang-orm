package com.hy.parse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hy.annotation.Param;
import com.hy.annotation.SelectList;
import com.hy.annotation.SelectOne;
import com.hy.util.TypeUtil;

/**  
* ParseSql
* Creater by chenhaiyang on 2020年5月13日
*/
public class Parse {
	
	private static Logger logger=LoggerFactory.getLogger(Parse.class);
	
	public static Object parse(Method method, Object[] args,DataSource dataSource){
		Annotation annotation = ParseSql.getAnnotation(method);
		List<Param> paramList = getParamList(method.getParameterAnnotations());
		SqlInfo sqlInfo=new SqlInfo(annotation,args,paramList);
		sqlInfo.setSql(ParseSql.getSql(sqlInfo));
		Class<?> returnType = method.getReturnType();
		ParseSql.parse(sqlInfo);
		return connectionDB(dataSource, sqlInfo, returnType);
	}
	
	
	//getGeneratedKeys
	public static Object connectionDB(DataSource dataSource,SqlInfo sqlInfo,Class<?> returnType){
		Connection connection =null;
		PreparedStatement prepareStatement =null;
		ResultSet resultSet=null;
		try{
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sql = sqlInfo.getSql();
			logger.debug("hy-orm... 准备执行sql:{}",sql);
			prepareStatement = getPreparedStatement(connection, sql);
			logger.debug("hy-orm... 参数个数:{}",prepareStatement.getParameterMetaData().getParameterCount());
			//如果是数据这里就答不出来了
			String sqlVal= sqlInfo.getParamInfoList().toString();
			logger.debug("hy-orm... 参数值:{}",sqlVal);
			TypeUtil.setParamType(prepareStatement, sqlInfo.getParamInfoList());
			
			if(sql.matches(".*(select|SELECT).*")){
				resultSet = prepareStatement.executeQuery();
				List<Object> resultList = TypeUtil.setResult(returnType, resultSet);
				return getResult(resultList, sqlInfo.getAnnotation());
			}else{
				int row = execute(sqlInfo, prepareStatement);
				connection.commit();
				return row;
			}
		} catch (Exception e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}finally{
			TypeUtil.closeResource(connection, prepareStatement, resultSet);
		}
		return Collections.emptyList();
	}
	
	public static PreparedStatement getPreparedStatement(Connection connection,String sql) throws SQLException{
		if(sql.matches(".*(insert|INSERT).*")){
			return connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		}else{
			return connection.prepareStatement(sql);
		}
	}
	
	public static int execute(SqlInfo sqlInfo,PreparedStatement preparedStatement) throws SQLException{
		String sql=sqlInfo.getSql();
		if(sql.matches(".*(insert|INSERT).*")){
			//填充返回的主键
			int row = preparedStatement.executeUpdate();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			TypeUtil.setPrimary(sqlInfo, resultSet);
			return row;
		}else{
			return preparedStatement.executeUpdate();
		}
	}
	
	
	
	private static List<Param> getParamList(Annotation[][] an){
		List<Param> list=new ArrayList<Param>();
		for (int i = 0; i < an.length; i++) {
			Annotation[] annotations = an[i];
			if(annotations!=null && annotations.length>0){
				Class<? extends Annotation> annotationType = annotations[0].annotationType();
				if(Param.class==annotationType){
					list.add((Param) annotations[0]);
				}
			}
		}
		return list;
	}
	
	
	private static Object getResult(List<Object> list,Annotation annotation){
		if(SelectList.class == annotation.annotationType()){
			return list;
		}else if(SelectOne.class== annotation.annotationType()){
			if(!list.isEmpty()){
				return list.get(0);
			}
		}
		return null;
	}
	
}
