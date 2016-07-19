package org.seckill.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"classpath:spring/spring-dao.xml", 
	"classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
//	logback.qos.ch/manual/configuration.html
	@Autowired
	private SeckillService seckillService;
	@Test
	public void testGetSeckillList() throws Exception{
		List<Seckill> list = seckillService.getSeckillList();
		logger.info("list = {}",list);
		//Closing non transactional SqlSession
	}
	@Test
	public void testGetById() throws Exception{
		long id= 1000L;
		Seckill seckill = seckillService.getById(id);
		logger.info("seckill = {}",seckill);
	}
	//集成测试代码完整逻辑，注意可重复执行
	@Test
	public void testSeckillLogic() throws Exception{
		long id = 1000L;
		Exposer exposer = seckillService.exportSeckillUrl(id);
		if(exposer.isExposed()){
			logger.info("exposer = {}",exposer);//md5值
			String phone = "13838573895";
			String md5 = exposer.getMd5();
			try {
				SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
				logger.info("execution = {}",execution);
			} catch (RepeatKillException e) {
				logger.info(e.getMessage());
			} catch (SeckillCloseException e) {
				logger.info(e.getMessage());
			}
		}else {
			//秒杀未开启
//			id = 1001的时候 秒杀未开启
			logger.warn("exposer = {} ", exposer);
		}
	}
}
