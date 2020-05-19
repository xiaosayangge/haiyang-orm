package com.hy.parse;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.hy.annotation.Param;
import com.hy.util.GsonUtils;

/**  
* SqlInfo
* Creater by chenhaiyang on 2020年5月13日
*/
public class SqlInfo {
	private List<ParamInfo> paramInfoList=new ArrayList<SqlInfo.ParamInfo>();
	
	//装主键信息
	private ParamInfo primary;
	
	private String sql;
	
	private Annotation annotation;
	
	private Object[] args;

	private List<Param> paramList;
	
	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public List<Param> getParamList() {
		return paramList;
	}

	public void setParamList(List<Param> paramList) {
		this.paramList = paramList;
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	

	public List<ParamInfo> getParamInfoList() {
		return paramInfoList;
	}

	public void setParamInfoList(List<ParamInfo> paramInfoList) {
		this.paramInfoList = paramInfoList;
	}

	/**
	 * @param paramObj
	 * @param sql
	 * @param annotation
	 * @param args
	 * @param paramList
	 */
	public SqlInfo( Annotation annotation, Object[] args,
			List<Param> paramList) {
		super();
		this.annotation = annotation;
		this.args = args;
		this.paramList = paramList;
	}


	/**
	 * 
	 */
	public SqlInfo() {
		super();
	}

	@Override
	public String toString() {
		return GsonUtils.toJson(this);
	}

	public class ParamInfo{
		
		private String name;
		private Class<?> type;
		
		private Object val;

		public Class<?> getType() {
			return type;
		}

		public void setType(Class<?> type) {
			this.type = type;
		}

		public Object getVal() {
			return val;
		}

		public void setVal(Object val) {
			this.val = val;
		}
		
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}


		/**
		 * 
		 */
		public ParamInfo() {
			super();
		}

		/**
		 * @param name
		 * @param type
		 */
		public ParamInfo(String name, Class<?> type,Object val) {
			super();
			this.name = name;
			this.type = type;
			this.val=val;
		}

		@Override
		public String toString() {
			if(org.springframework.util.ObjectUtils.isEmpty(val)){
				return null;
			}
			return val.toString();
		}
	}
}
