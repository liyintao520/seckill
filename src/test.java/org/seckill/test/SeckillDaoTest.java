package org.seckill.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
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
public class SeckillDaoTest {
	
	//注入Dao实现类依赖
	@Resource
	private SeckillDao seckillDao;
	
	@Test
	public void testQueryById() throws Exception{
		System.out.println("11111111111111111");
		System.out.println("测试！！！");
		long id = 1000;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill.getName());
		System.out.println(seckill.toString());
	}
	
	@Test
	public void testReduceNumber() throws Exception{
		System.out.println("222222222222222222");
	}
}
