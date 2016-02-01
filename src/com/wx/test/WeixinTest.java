package com.wx.test;

import com.wx.po.AccessToken;
import com.wx.util.WeixinUtil;

public class WeixinTest {
	public static void main(String[] args) {
		AccessToken token = WeixinUtil.getAccessToken();
		System.out.println("票据" + token.getToken());
		System.out.println("有效时间" + token.getExpiresIn());
	}
}
