package com.zach.common.utils;

/**
 * @author 庄佳弟
 * @date 2017年1月4日
 */
public class ErrorCodeConstants {
	/**
	 * 成功
	 */
	public static final int SUCCESS = 200;
	
	/**
	 * 缺少参数或参数错误
	 */
	public static final int PARAM_ERROR = 400;
	
	/**
	 * http请求错误
	 */
	public static final int HTTP_CONN_ERROR = 401;

	/**
	 * 系统不可预估异常
	 */
	public static final int SERVER_ERROR = 500;
	
	/**
	 * 系统数据异常
	 */
	public static final int SERVER_DATA_ERROR = 501;
	
	/**
	 * token错误
	 */
	public static final int TOKEN_ERROR = 1000;
	
	/**
	 * 未登陆的错误码
	 */
	public static final int TOKEN_UNLOGIN=1111;
	
	/**
	 * token权限
	 */
	public static final int TOKEN_AUTHORITY_ERROR = 1001;
	
	/**
	 * token冲突被重置为无效
	 */
	public static final int TOKEN_INVALID = 1002;
	
	/**
	 * 登录失败
	 */
	public static final int LOGIN_ERROR = 1003;
	
	/**
	 * 验证码错误
	 */
	public static final int SMS_CAPTCHA_ERROR = 1004;
	
	/**
	 * 用户被禁用
	 */
	public static final int USER_DENY_ERROR = 1005;

	/**
	 * 通用错误码
	 */
	public static final int COMMON_ERROR = 2000;
	
	/**
	 * 超级管理员不可编辑
	 */
	public static final int ADMIN_NO_EDIT=1006;
	
	
}
