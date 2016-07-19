package org.seckill.test;


import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 配置spring 和 junit 整合，junit启动时加载springIOC容器
 * spring-test,junit
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//高速junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
	
	//注入Dao实现类依赖
	@Resource
	private SuccessKilledDao successKilledDao;
	public void insertSuccessKilled() throws Exception{
		long id = 1000L;
		String userPhone = "13838573895";//电话我定义的是varchar类型
		int num = successKilledDao.insertSuccessKilled(id, userPhone);
		/**
		 * 由于是联合主键 并且插入的时候定义了 ignore 所以主键冲突不会报错，返回值只是 0  无影响行数
		 * 正确情况：第一次插入的时候 num = 1；
		 * 同样的id，直接执行，第二次的时候 num = 0；
		 */
		System.out.println("num = " + num);
	}
	public void  queryByIdWithSeckill() throws Exception{
		long id = 1000L;
		String userPhone = "13838573895";//电话我定义的是varchar类型
		SuccessKilled s = successKilledDao.queryByIdWithSeckill(id, userPhone);
		/**
		 * 由于是联合主键 并且插入的时候定义了 ignore 所以主键冲突不会报错，返回值只是 0  无影响行数
		 * 正确情况：第一次插入的时候 num = 1；
		 * 同样的id，直接执行，第二次的时候 num = 0；
		 */
		System.out.println("s = " + s.toString());
		System.out.println("s.getSeckill() = " + s.getSeckill());
	}
}
