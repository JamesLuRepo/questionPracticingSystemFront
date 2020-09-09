package com.itheima.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

/**
 * Jedis工具类
 */
public class JedisUtils {

	private static JedisPoolConfig poolConfig = null;
	private static JedisPool jedisPool = null;
	private static Integer maxTotal = null;
	private static Integer maxIdle = null;
	private static String host = null;
	private static Integer port = null;

	//定义读取配置文件的对象
	private static ResourceBundle rb;
	/**
	 * 使用静态代码块初始化Jedis连接池
	 */
	static{
		//实例化ResourceBundle对象
		rb = ResourceBundle.getBundle("jedis");
		//读取配置文件 获得参数值
		maxTotal = Integer.parseInt(rb.getString("jedis.maxTotal"));
		maxIdle = Integer.parseInt(rb.getString("jedis.maxIdle"));
		port = Integer.parseInt(rb.getString("jedis.port"));
		host = rb.getString("jedis.host");
		poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(maxTotal);
		poolConfig.setMaxIdle(maxIdle);
		jedisPool = new JedisPool(poolConfig,host,port);
	}

	/**
	 * 获取Jedis对
	 * @return
	 */
	public static Jedis getResource(){
		return jedisPool.getResource();
	}
}
