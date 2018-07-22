package com.zach.common.utils;

/**
 * 作用：统一返回结果类
 * 
 * @author 庄佳弟
 * 
 * 2018年1月11日
 */
public class AppResponse{

	public Integer code;
	
	public String message;
	
	public Object object;
	
	/**
	 * 默认构造方法，成功请求
	 */
	public AppResponse() {
		this.code = ErrorCodeConstants.SUCCESS;
		this.message = "成功";
		this.object = null;
	}

	/**
	 * 默认构造方法，成功请求，设置返回对象
	 */
	public AppResponse(Object data) {
		this.code = ErrorCodeConstants.SUCCESS;
		this.message = "成功";
		this.object = data;
	}
	
	/**
	 * 默认setFail方法 错误码为2000
	 */
	public void setFail() {
		this.code = ErrorCodeConstants.COMMON_ERROR;
		this.message = "请求失败";
		this.object = null;
	}

	/**
	 * 错误码为2000，自定义错误信息
	 * @param info
	 */
	public void setFail(String info) {
		this.code = ErrorCodeConstants.COMMON_ERROR;
		this.message = info;
		this.object = null;
	}
	
	/**
	 * 自定义错误码以及错误信息
	 * @param code
	 * @param info
	 */
	public void setFail(Integer code,String info){
		this.code = code;
		this.message = info;
		this.object = null;
	}

	/**
	 * 自定义错误码，错误信息，返回对象
	 * @param code
	 * @param info
	 * @param data
	 */
	public void setFail(Integer code,String info, Object data) {
		this.code = code;
		this.message = info;
		this.object = data;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setData(Object data) {
		this.object = data;
	}

	@Override
	public String toString() {
		return "AppResponse [code=" + code + ", message=" + message + ", object=" + object + "]";
	}
}