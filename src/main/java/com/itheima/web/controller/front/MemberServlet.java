package com.itheima.web.controller.front;

import com.itheima.domain.front.Member;
import com.itheima.web.controller.BaseServlet;
import com.itheima.web.controller.Code;
import com.itheima.web.controller.Result;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/member/*")
public class MemberServlet extends BaseServlet {


    public Result register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Member member = getData(request, Member.class);
        boolean flag = memberService.register(member);
        return new Result("注册成功！", null);
    }

    public Result login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Member member = getData(request, Member.class);
        member = memberService.login(member.getEmail(),member.getPassword());
        if (member!= null){
            return  new Result("登录成功！",member);

        }else {
            return  new Result("用户名密码错误，请重试！",false,null, Code.LOGIN_FAIL);

        }

    }
    public Result checkLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Member member = getData(request, Member.class);
        //根据获取到的id去redis中查找，是否存在
        String nickName = memberService.getLoginInfo(member.getId());

        return new Result("", nickName);
    }

    public Result logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Member member = getData(request, Member.class);
        boolean flag = memberService.logout(member.getId());

        if (flag){
            return  new Result("退出成功！",flag);

        }else {
            return  new Result("用户名密码错误，请重试！",false,flag, Code.LOGOUT_FAIL);

        }

    }

}
