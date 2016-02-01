package com.wx.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wx.po.TextMessage;
import com.wx.util.CheckUtil;
import com.wx.util.MessageUtil;

/**
 * 
 * @Package: [com.wx.servlet.WeixinServlet.java]
 * @Title: [WeixinServlet]
 * @Description: [一句话描述该类的功能]
 * @Author: [Echo]
 * @CreateDate: [2016-1-14 上午12:10:39]
 * @UpdateRemark: [说明本次修改内容]
 * @Version: [v1.0]
 * 
 */
public class WeixinServlet extends HttpServlet
{
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        
        PrintWriter out = response.getWriter();
        if (CheckUtil.checkSignature(signature, timestamp, nonce))
        {
            out.print(echostr);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try
        {
            Map<String, String> map = MessageUtil.xml2Map(request);
            String toUserName = map.get("ToUserName");
            String fromUserName = map.get("FromUserName");
            String msgType = map.get("MsgType");
            String content = map.get("Content");
            
            String message = null;
            if (MessageUtil.MESSAGE_TEXT.equals(msgType))
            {
                if ("1".equals(content))
                {
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.firstMenu());
                }
                else if ("2".equals(content))
                {
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.secondMenu());
                }
                else if ("?".equals(content) || "？".equals(content))
                {
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
                }else if("3".equals(content)){
                    message = MessageUtil.initNewsMessage(toUserName, fromUserName);
                }else
                {
                    message = MessageUtil.initText(toUserName, fromUserName, "不存在此菜单,请回复？查看菜单！");
                }
                
                /*
                 * TextMessage text = new TextMessage();
                 * text.setFromUserName(toUserName);
                 * text.setToUserName(fromUserName);
                 * text.setMsgType("text");
                 * text.setCreateTime(String.valueOf(new Date().getTime()));
                 * text.setContent("您发送的消息是：" + content.toUpperCase());
                 * message = MessageUtil.textMessage2Xml(text);
                 */
            }
            else if (MessageUtil.MESSAGE_EVENT.equals(msgType))
            {
                String evenType = map.get("Event");
                if (MessageUtil.MESSAGE_SUBSCRIBE.equals(evenType))
                {
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
                }
            }
            out.print(message);
            System.out.println(message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            out.close();
        }
        
    }
    
}
