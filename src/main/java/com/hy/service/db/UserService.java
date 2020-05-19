package com.hy.service.db;

import com.hy.annotation.SelectList;

/**  
* UserService
* Creater by chenhaiyang on 2020年5月11日
*/
public interface UserService {
	
	@SelectList("select * from user where id=?")
	Object getUserById(Long id);

}
