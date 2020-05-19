package com.hy.service.proxy;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import com.hy.annotation.CodeBearMapperScanner;
import com.hy.util.RegistrarBean;

/**  
* CodeBearMapperScannerRegistrar
* Creater by chenhaiyang on 2020年5月12日
*/

public class CodeBearMapperScannerRegistrar implements ImportBeanDefinitionRegistrar {

	 @Override
	    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
	        RegistrarBean.registrar(importingClassMetadata, registry, CodeBearMapperScanner.class,CodeBeanFactoryBean.class);
	    }
}
