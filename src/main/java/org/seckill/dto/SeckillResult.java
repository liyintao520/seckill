package org.seckill.dto;

/**
 * 这是一个泛型类型的
 * 所有 的 ajax 请求返回的类型，统一 封装 json结果
 * @author Administrator
 *
 * @param <T>
 */
public class SeckillResult<T> {
	
	private boolean success;//代表的是请求是否成功的【而不是秒杀是否成功】
	private T data;
	private String error;
	/**
	 * 如果success= true  就有返回数据
	 * @param success
	 * @param data
	 */
	public SeckillResult(boolean success, T data) {
		this.success = success;
		this.data = data;
	}
	/**
	 * 如果success = false  就返回错误信息
	 * @param success
	 * @param data
	 */
	public SeckillResult(boolean success, String error) {
		this.success = success;
		this.error = error;
	}
	@Override
	public String toString() {
		return "SeckillResult [success=" + success + ", data=" + data
				+ ", error=" + error + "]";
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
}
