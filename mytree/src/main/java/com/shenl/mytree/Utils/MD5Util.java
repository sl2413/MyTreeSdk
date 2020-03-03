package com.shenl.mytree.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	/**
	 * TODO 功能：md5加密
	 *
	 * 参数说明:
	 * 作    者:   沈  亮
	 * 创建时间:   2020/2/26
	 */
	public static  String md5(String msg){
		StringBuilder sb = new StringBuilder();
		//1.数据摘要器
		//参数:加密方式
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			//将一个数据进行加密,返回一个进行过二进制哈希算法的加密数组
			byte[] digest = messageDigest.digest(msg.getBytes());
			for (int i = 0; i < digest.length; i++) {
				int result = digest[i] & 0xff;//0xff : int 255 byte :-128-127  MD5:将负的byte值与上一个int类型的255得到一个int的正整数
				//将一个int类型的值,转化成16进制字符串,一般为2位,如果是0开头会省略0
				String hexString = Integer.toHexString(result);//不规则加密,加盐操作
				if (hexString.length() < 2) {
					//添加0
					sb.append("0");
				}
				sb.append(hexString);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
