package com.wx.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.wx.po.News;
import com.wx.po.NewsMessage;
import com.wx.po.TextMessage;

public class MessageUtil
{
    public static final String MESSAGE_TEXT = "text";
    
    public static final String MESSAGE_IMAGE = "image";
    
    public static final String MESSAGE_VOICE = "voice";
    
    public static final String MESSAGE_VIDEO = "video";
    
    public static final String MESSAGE_LINK = "link";
    
    public static final String MESSAGE_LOCATION = "location";
    
    public static final String MESSAGE_EVENT = "event";
    
    public static final String MESSAGE_SUBSCRIBE = "subscribe";
    
    public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
    
    public static final String MESSAGE_CLICK = "CLICK";
    
    public static final String MESSAGE_VIEW = "VIEW";
    
    public static final String MESSAGE_NEWS = "news";
    
    /**
     * xml2Map
     * 
     * @param request
     * @return
     * @throws Exception
     */
    public static Map<String, String> xml2Map(HttpServletRequest request)
        throws Exception
    {
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();
        
        InputStream ins = request.getInputStream();
        Document doc = reader.read(ins);
        
        Element root = doc.getRootElement();
        
        List<Element> list = root.elements();
        
        for (Element e : list)
        {
            map.put(e.getName(), e.getText());
        }
        
        ins.close();
        return map;
    }
    
    /**
     * 将文本类型转换为xml对象
     * 
     * @param textMessage
     * @return
     */
    public static String textMessage2Xml(TextMessage textMessage)
    {
        XStream xStream = new XStream();
        xStream.alias("xml", textMessage.getClass());
        return xStream.toXML(textMessage);
    }
    
    /**
     * 回复信息初始化
     * 
     * @param toUserName
     * @param fromUserName
     * @param content
     * @return
     */
    public static String initText(String toUserName, String fromUserName, String content)
    {
        TextMessage text = new TextMessage();
        text.setFromUserName(toUserName);
        text.setToUserName(fromUserName);
        text.setMsgType(MessageUtil.MESSAGE_TEXT);
        text.setCreateTime(String.valueOf(new Date().getTime()));
        text.setContent(content);
        return textMessage2Xml(text);
    }
    
    /**
     * 主菜单
     * 
     * @return
     */
    public static String menuText()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("欢迎您的关注，请按照菜单提示进行操作：\n\n");
        sb.append("回复1 查看公众号介绍\n");
        sb.append("回复2 查看华思联创科技公司介绍\n\n");
        sb.append("回复?调出此菜单。");
        return sb.toString();
    }
    
    public static String firstMenu()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("本公众号主要用于华思联创科技有限公司的测试工作！");
        return sb.toString();
    }
    
    public static String secondMenu()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("华思联创是由俞华阳和周颖 \n");
        sb.append("共同创建的牛逼哄哄的互联网公司！");
        return sb.toString();
    }
    
    /**
     * 图文消息转换为xml
     * 
     * @param newsMessage
     * @return
     */
    public static String newsMessage2Xml(NewsMessage newsMessage)
    {
        XStream xStream = new XStream();
        xStream.alias("xml", newsMessage.getClass());
        xStream.alias("item", new News().getClass());
        return xStream.toXML(newsMessage);
    }
    
    /**
     * 图文消息组装
     * 
     * @param toUserName
     * @param fromUserName
     * @return
     */
    public static String initNewsMessage(String toUserName, String fromUserName)
    {
        String message = null;
        List<News> newsList = new ArrayList<News>();
        NewsMessage newsMessage = new NewsMessage();
        
        News news = new News();
        news.setTitle("Go能够撼动C的地位吗？");
        news.setDescription("1.部署简单。2.并发性好。3.良好的语言设计。4.执行性能好。");
        news.setPicUrl("http://myapp3.ngrok.natapp.cn/WX/image/test.jpg");
        news.setUrl("www.imooc.com");
        
        newsList.add(news);
        
        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUserName);
        newsMessage.setCreateTime(String.valueOf(new Date().getTime()));
        newsMessage.setArticles(newsList);
        newsMessage.setMsgType(MESSAGE_NEWS);
        newsMessage.setArticleCount(newsList.size());
        
        message = newsMessage2Xml(newsMessage);
        return message;
    }
    
}
