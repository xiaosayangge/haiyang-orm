package com.hy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**  
 * update方法 必须传入和数据库对应的model
 * id 必须有 作为更新主键
 * 可以设置 Field主键中的 primary属性表名是否是主键
 * model对象不为空的属性就会更新
 *
* Creater by chenhaiyang on 2020年5月11日
*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Update {
}
