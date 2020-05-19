package com.hy.util;

import java.util.Set;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
/**  
* RegistrarBean
* Creater by chenhaiyang on 2020年5月12日
*/
public class RegistrarBean {
	
	public static void registrar(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry,Class cls,Class beanClass){
		try {
            AnnotationAttributes annoAttrs =
                    AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(cls.getName()));
            
            String packageValue = annoAttrs.getString("value");
            Set<Class> set= ClassScaner.scan(packageValue);
            set.forEach(aClass->{
            	if (aClass.isInterface()&&!aClass.isAnnotation()) {
                    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition();
                    AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
                    beanDefinition.setBeanClass(beanClass);
                    beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(aClass.getName());
                    registry.registerBeanDefinition(aClass.getName(), beanDefinition);
                }
            });
            
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
	}

}
