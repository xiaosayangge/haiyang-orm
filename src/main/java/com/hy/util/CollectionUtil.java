package com.hy.util;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Collection;

/**  
* CollectionUtil
* Creater by chenhaiyang on 2020年5月15日
*/
public class CollectionUtil {

	public static void printCollecion(Collection<?> collection){
		collection.forEach(System.out::print);
	}
}
