package com.itheima.service.front.impl;

import com.itheima.dao.front.MemberDao;
import com.itheima.domain.front.Member;
import com.itheima.factory.MapperFactory;
import com.itheima.service.front.impl.front.MemberService;
import com.itheima.utils.JedisUtils;
import com.itheima.utils.MD5Util;
import com.itheima.utils.TransactionUtil;
import org.apache.ibatis.session.SqlSession;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.UUID;

public class MemberServiceImpl implements MemberService {



    public boolean register(Member member) {
        SqlSession sqlSession = null;
        try{
            //1.获取SqlSession
            sqlSession = MapperFactory.getSqlSession();
            //2.获取Dao
            MemberDao memberDao = MapperFactory.getMapper(sqlSession,MemberDao.class);
            //id使用UUID的生成策略来获取
            String id = UUID.randomUUID().toString();
            member.setId(id);

            member.setRegisterDate(new Date());
            member.setState("1");
            member.setPassword(MD5Util.md5(member.getPassword()));

            //3.调用Dao层操作
            int row = memberDao.save(member);
            //4.提交事务
            TransactionUtil.commit(sqlSession);

            return row>0;
        }catch (Exception e){
            TransactionUtil.rollback(sqlSession);
            throw new RuntimeException(e);
            //记录日志
        }finally {
            try {
                TransactionUtil.close(sqlSession);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Member login(String email, String password) {
        SqlSession sqlSession = null;
        try{
            //1.获取SqlSession
            sqlSession = MapperFactory.getSqlSession();
            //2.获取Dao
            MemberDao memberDao = MapperFactory.getMapper(sqlSession,MemberDao.class);
            password = MD5Util.md5(password);
            Member member =memberDao.findByEmailAndPwd(email,password);

            //3. 将登陆人的信息保存到redis中
            Jedis jedis = JedisUtils.getResource();
            //使用登录人的id作为key，设定3600秒的过期时间，value值待定
            jedis.setex(member.getId(),3600,member.getNickName());
            jedis.close();




            return member;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
            //记录日志
        }finally {
            try {
                TransactionUtil.close(sqlSession);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public String getLoginInfo(String id) {
        //使用给定的id去redis中查找是否存在当前数据
        Jedis jedis = JedisUtils.getResource();
        //使用登录人的id作为key，设定3600秒的过期时间，value值待定
        String nickName = jedis.get(id);
        jedis.close();
        return nickName;

    }

    public boolean logout(String id) {

        Jedis jedis = JedisUtils.getResource();
        Long row = jedis.del(id);
        jedis.close();
        return row>0;
    }
}
