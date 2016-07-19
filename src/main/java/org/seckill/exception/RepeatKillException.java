package org.seckill.exception;
/**
 * 重复秒杀异常（运行期异常）--不需要 手动try catch
 * 
 * spring 事务 他只接受运行期异常  回滚 策略
 * 当我们抛出一个 非运行期异常时候 不能回滚了
 * @author Administrator
 *
 */
public class RepeatKillException extends SeckillException{
	
	public RepeatKillException(String message){
		super(message);
	}
	public RepeatKillException(String message, Throwable cause){
		super(message, cause);
	}
}
