package com.hy.service.db;

import com.hy.annotation.Delete;
import com.hy.annotation.Insert;
import com.hy.annotation.Param;
import com.hy.annotation.SelectOne;
import com.hy.annotation.Update;
import com.hy.model.ActivityModel;

/**  
* ActivityService
* Creater by chenhaiyang on 2020年5月12日
*/
public interface ActivityService {
	@SelectOne("select * from activity where id=#{id}")
	ActivityModel getActivityById(@Param("id") Long id);

	//返回影响行数  必须传model对象 自动赋值主键
	@Insert
	int addActivity(ActivityModel model);
	
	//insert 不能控制实际想插入的属性  
	// 你说传对象判断不为空就插入吧 又有单插几个属性的  
	//性价比太低了 还是xml好一点感觉
	//@Insert("insert into activity")
	//int addActivity();
	
	
	// 开始写一个xml模式的orm吧
	//但是现在都讲究去xml模式
	// 也对 应该还是走 对象模式
	//所有insert还是走 插入对象
	
	//insert into activity ActivityModel
	//根据model的有效值 去插入
	//直接只用 insert 注解 获取参数对象的tableName
	
	@Update
	int updateActivity(ActivityModel model);
	
	//放弃delete  感觉没意思了 还有select 参数应该还有支持对象 拉到了 没意思了 换别的写了
	@Delete
	int deleteActivity(@Param("id") Long id);
	
	
}
