package com.hy.service.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.hy.annotation.SelectList;
import com.hy.parse.Parse;

/**  
* CodeBeanFactoryBean
* Creater by chenhaiyang on 2020年5月11日
*/
public class CodeBeanFactoryBean<T> implements FactoryBean<T>, InvocationHandler{
    private Class<T> clazz;

    public CodeBeanFactoryBean(Class<T> clazz) {
        this.clazz = clazz;
    }
    
    @Autowired
    DataSource datasource;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    	if(method.getName().equals("toString")){
    		return clazz.toString();
    	}
    	Object obj = Parse.parse(method, args,datasource);
    	return obj;
    }

    @Override
    public T getObject() throws Exception {
    	return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Class<T> getObjectType() {
        return clazz;
    }
}
