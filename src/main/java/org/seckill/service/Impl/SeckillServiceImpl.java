package org.seckill.service.Impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

//@Component  @Service @Dao @Controller

@Service
public class SeckillServiceImpl implements SeckillService{
//SLF4J结合Logback
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	//注入Service依赖
	@Autowired//@Resource,@Inject这些都是J2EE规范注解
	private SeckillDao seckillDao;
	@Autowired
	private SuccessKilledDao successKilledDao;
	//md5 盐值 字符串，用于混淆MD5
	private final String slat = "sadkfjalsdjfalksj23423^&*^&%&!EBJH#e";
	
	public List<Seckill> getSeckillList() {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("offset", 0);
//		map.put("limit", 4);
//		return seckillDao.queryAll(map);
		return seckillDao.queryAll(0, 4);
		//return null;
	}

	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	public Exposer exportSeckillUrl(long seckillId) {
		Seckill seckill = seckillDao.queryById(seckillId);
		if(seckill == null){
			return new Exposer(false, seckillId);
		}
		Date start = seckill.getStartTime();
		Date end = seckill.getEndTime();
		//系统当前时间
		Date now = new Date();
		if(now.getTime() < start.getTime() || now.getTime() > end.getTime()){
			return new Exposer(false, seckillId, now.getTime(), start.getTime(), end.getTime());
		}
		//转换特定字符串的过程，md5加密是不可逆
		String md5 = getMd5(seckillId);//TODO
		return new Exposer(true, md5,seckillId);
	}

	/**
	 * 使用注解控制事务方法的有点：
	 * 1、开发团队达成一致约定，明确标注事务方法的编程风格。
	 * 2、保证事务方法的执行时间尽可能短，不要穿插其他网络操作，RPC/HTTP请求/缓存/  
	 * 		如果要用的话  可以剥离到事务方法外部
	 * 3、不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
	 */
	@Transactional
	public SeckillExecution executeSeckill(@Param("seckillId") long seckillId, @Param("userPhone") String userPhone,
			String md5) throws SeckillException, RepeatKillException,
			SeckillCloseException {
		
		if(md5 == null || !md5.equals(getMd5(seckillId))){
			throw new SeckillException("seckill data rewrite");
		}
		//执行秒杀逻辑：减库存 + 记录购买行位
		Date nowTime = new Date();
		try {
			//减库存
			int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
			if(updateCount <= 0){
				//没有更新到记录, 秒杀结束
				throw new SeckillCloseException("没有更新到记录, 秒杀结束!");
			}else{
				//记录购买行为
				int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
				//唯一：seckillId，userPhone
				if(insertCount <= 0){
					//重复秒杀
					throw new RepeatKillException("重复秒杀该商品！！！");
				}else{
					//秒杀成功
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
//					return new SeckillExecution(seckillId, 1, "秒杀成功！", successKilled);
				}
			}
		} catch (SeckillCloseException e1) {
			throw e1;
		} catch (RepeatKillException e2) {
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//所有编译期异常 转化为运行期异常，这样spring 自动帮我们回滚了
			throw new SeckillException("seckill inner error:" + e.getMessage());
		}
	}
	
	//自己设定规则 然后利用spring的md5，加密成md5,
	@SuppressWarnings("unused")
	private String getMd5(long seckillId){
		String base = seckillId + "/" + slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}
	
}
