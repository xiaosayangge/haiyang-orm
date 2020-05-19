package com.hy;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.DruidDataSourceUtils;
import com.hy.model.ActivityModel;
import com.hy.service.db.ActivityService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HaiyangBootVueApplicationTests {
	
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	ActivityService activityService;

	/*@Test
	public void t1() throws SQLException {
		ActivityModel activityById = activityService.getActivityById(74l);
		System.out.println(activityById);
		//明天来了写 insert update 然后把东西分离出去 做成jar包 可以调用
		//最好是真正写成自己的orm框架
		ActivityModel model = new ActivityModel();
		 model.setActivityName("海洋测试");
		 model.setActivityType(1);
		 model.setLineSeq("NP231");
		 model.setStoreCodes("1001");
		int addActivity = activityService.addActivity(model);
		System.out.println("行数:"+addActivity);
	}*/
	
	@Test
	public void t2(){
		ActivityModel activityModel=new ActivityModel();
		activityModel.setActivityName("haiyang_test2");
		activityModel.setId(74l);
		int row = activityService.updateActivity(activityModel);
		System.out.println("更新行数:"+row);
	}
	
	
	

}
