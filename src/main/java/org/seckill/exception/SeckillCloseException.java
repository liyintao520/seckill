package org.seckill.exception;
/**
 * 秒杀关闭异常（卖完了、时间到了......就不用不抢了，）
 * @author Administrator
 *
 */
public class SeckillCloseException extends SeckillException{
	
	public SeckillCloseException(String message){
		super(message);
	}
	public SeckillCloseException(String message, Throwable cause){
		super(message, cause);
	}
}
