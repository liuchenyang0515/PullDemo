package com.example.pulldemo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Weather {
    /**
     * 服务器是以流的形式把数据返回的
     */
    public static List<Channel> parserXml(InputStream in) throws Exception {
        List<Channel> weatherLists = null;
        Channel channel = null;
        // 1.获取XmlPullParser解析的实例
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();

        // 也可以直接一步写XmlPullParser parser = Xml.newPullParser();
        // 通过Xml.newPullParser()获得的解析器可能会有一个bug：调用nextText（）并不总是前进到END_TAG
        // 一些app可能围绕着这个问题，额外的调用next()或nextTag()方法：
        // 在Android Ice Cream Sandwich版本中，删除了ExpatPullParser类来修复这个bug,
        // 不幸的是，app在Android4.0版本下使用它可能会导致应用crash
        // 官方说明文档是用的2步，如我上面写的，就当做是推荐这种写法吧

        // 2.设置XmlPullParser参数
        parser.setInput(in, "utf-8");
        // 3.获取事件类型
        int type = parser.getEventType();
        while (type != XmlPullParser.END_DOCUMENT) {
            String nodeName = parser.getName();
            // 4.具体判断一下解析到哪里了
            switch (type) {
                case XmlPullParser.START_TAG:
                    if ("weather".equals(nodeName)) {
                        // 5.创建一个集合对象
                        weatherLists = new ArrayList<Channel>();
                    } else if ("channel".equals(nodeName)) {
                        // 6.创建Channel对象
                        channel = new Channel();
                        // 7.获取id值
                        String id = parser.getAttributeValue(null, "id"); // 0号属性
                        channel.setId(id);
                    } else if ("city".equals(nodeName)) {
                        // 8.获取city的数据
                        String temp = parser.nextText();// 刚刚测试过了getText()是获得本次指向的内容
                        // 这里nextText()就是指向下一次指向的内容，即开始和结束标签之间的内容
                        channel.setTemp(temp);
                    } else if ("wind".equals(nodeName)) {
                        // 9.获取wind数据
                        String wind = parser.nextText();
                        channel.setWind(wind);
                    } else if ("pm250".equals(nodeName)) {
                        // 9.获取wind数据
                        String pm250 = parser.nextText();
                        channel.setPm250(pm250);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("channel".equals(nodeName)) { // 已经测试过了，结束标签不会读取前面的/
                        // 把javabean对象存到集合中
                        weatherLists.add(channel);
                    }
                    break;
            }
            // 不停的向下解析
            type = parser.next();
        }
        return weatherLists;
    }
}
