package com.bytetube.service;

import com.bytetube.dto.WechatAuthExecution;
import com.bytetube.entity.WechatAuth;
import com.bytetube.exceptions.WechatAuthOperationException;

public interface WechatAuthService {

	/**
	 * 通过openId查找平台对应的微信帐号
	 * 
	 * @param openId
	 * @return
	 */
	WechatAuth getWechatAuthByOpenId(String openId);

	/**
	 * 注册本平台的微信帐号
	 * 
	 * @param wechatAuth
	 * @param
	 * @return
	 * @throws RuntimeException
	 */
	WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;

}
