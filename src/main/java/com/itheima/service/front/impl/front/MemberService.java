package com.itheima.service.front.impl.front;

import com.itheima.domain.front.Member;

public interface MemberService {
    boolean register(Member member);

    /**
     * 根据email和密码登录
     * @param email
     * @param password
     * @return
     */
    Member login(String email, String password);

    /**
     * 根据登陆人id获取对应的昵称，从redis获取
     * @param id
     * @return
     */
    String getLoginInfo(String id);

    boolean logout(String id);
}
