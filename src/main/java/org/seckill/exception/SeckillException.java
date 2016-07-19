package org.seckill.exception;
/**
 * 秒杀出错了
 * @author Administrator
 *
 */
public class SeckillException extends RuntimeException{
	
	public SeckillException(String message){
		super(message);
	}
	public SeckillException(String message, Throwable cause){
		super(message, cause);
	}
}
