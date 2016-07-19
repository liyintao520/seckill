package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
 * 业务接口：站在“使用者”角度设计接口
 * 三方面：方法定义粒度，参数，返回类型(return 类型/异常)
 * @author Administrator
 *
 */
public interface SeckillService {
	/**
	 * 查询所有秒杀记录
	 * @return
	 */
	List<Seckill> getSeckillList();
	/**
	 * 查询单个秒杀记录
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);
	/**
	 * 秒杀开始时输出秒杀接口地址，
	 * 否则输出系统时间和秒杀时间
	 * 防止用户  拼出秒杀地址，然后下载插件进行秒杀
	 * @param seckillId
	 * @return
	 */
	Exposer exportSeckillUrl(long seckillId);
	/**
	 * 执行秒杀操作
	 * @param seckillId
	 * @param userPhaone
	 * @param md5
	 * @return
	 */
	SeckillExecution executeSeckill(long seckillId, String userPhone, String md5)
		throws SeckillException, RepeatKillException, SeckillCloseException;
	
	
	
	
}
