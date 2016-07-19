package org.seckill.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.util.JSONPObject;

@Controller //@Service  @Component
@RequestMapping("/seckill")
public class SeckillController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	/**
	 * 获取列表页信息--Restful 风格
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model){
		System.out.println("----------begin----------");
		//获取列表页
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list", list);
		System.out.println(list.get(0).getName());
		//list.jsp + model = ModelAndView
		System.out.println("----------end----------");
		return "list";//  /WEB-INF/jsp/"list".jsp
	}
	/**
	 * 数据对应的详情页面
	 * @param seckillId
	 * @param model
	 * Restful 风格： 
	 * 		/{seckillId}/detail
	 * 		RequestMethod.GET
	 * @return
	 */
	@RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model){
		if(seckillId == null){
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getById(seckillId);
		if(seckill == null){
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}
	//ajax请求，返回数据是 json字符串
	@RequestMapping(value = "/{seckillId}/exposer", 
			method = RequestMethod.POST, 
			produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable Long seckillId){
		SeckillResult<Exposer> result;
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}
		return result;
	}
	/**
	 * 执行秒杀的方法
	 * @param seckillId
	 * @param md5
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/{seckillId}/{md5}/execution", 
			method = RequestMethod.POST, 
			produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<SeckillExecution> execute(
			@PathVariable("seckillId") Long seckillId, 
			@PathVariable("md5") String md5, 
			@CookieValue(value = "killPhone", required = false) String phone){
		
		//也可以用 springmvc valid
		if(phone == null){
			return new SeckillResult<SeckillExecution>(false, "未注册");
		}
//		SeckillResult<SeckillExecution> result;
		try {
			SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (RepeatKillException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExecution>(true, execution);
//			return new SeckillResult<SeckillExecution>(false, execution);
		} catch (SeckillCloseException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
			return new SeckillResult<SeckillExecution>(true, execution);
//			return new SeckillResult<SeckillExecution>(false, execution);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
			return new SeckillResult<SeckillExecution>(true, execution);
//			return new SeckillResult<SeckillExecution>(false, execution);
		}
	}
	@RequestMapping(value = "/time/now", method = RequestMethod.GET, 
			produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<Long> time(){
		Date now = new Date();//当前系统时间，再转换成Long类型的毫秒数
//		return new SeckillResult<Long>(true, now.getTime());
		return new SeckillResult(true, now.getTime());
	}
}
