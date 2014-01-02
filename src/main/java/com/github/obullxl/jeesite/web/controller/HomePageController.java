/**
 * Author: obullxl@gmail.com
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.github.obullxl.jeesite.web.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.obullxl.jeesite.dal.dto.ReplyDTO;
import com.github.obullxl.jeesite.web.enums.TopicStateEnum;

/**
 * 主页控制器
 * 
 * @author obullxl@gmail.com
 * @version $Id: HomePageController.java, V1.0.1 2013年12月4日 上午9:44:12 $
 */
@Controller
public class HomePageController extends AbstractController {

    @RequestMapping("/")
    public String index() {
        return this.indexCatgPage("index", 1);
    }

    @RequestMapping("/index.htm")
    public String indexHtm() {
        return this.indexCatgPage("index", 1);
    }

    @RequestMapping("/index.html")
    public String indexHtml() {
        return this.indexCatgPage("index", 1);
    }

    /**
     * 分类分页主页
     */
    @RequestMapping("/index-{catg}-{page}.html")
    public String indexCatgPage(@PathVariable String catg, @PathVariable int page) {
        this.setWebData("catg", catg).setWebData("page", page);

        if(StringUtils.equals(catg, "album")) {
            return this.toFrontView("/album");
        }
        
        return this.toFrontView("/index");
    }

    /**
     * 主题详情页面
     */
    @RequestMapping("/topic/{catg}-{id}.html")
    public String topicDetail(@PathVariable String catg, @PathVariable String id) {
        this.setWebData("catg", catg).setWebData("topicId", id);

        // 更新主题访问次数
        this.topicDAO.updateVisit(id, 1);

        return this.toFrontView("/topic");
    }

    /**
     * 发表评论
     */
    @RequestMapping("/topic/post-reply.html")
    public String postReply(String uname, String uemail, String usite, String content, String ufrom, String topic, String title) {
        // 存储评论
        ReplyDTO reply = new ReplyDTO();
        reply.setState(TopicStateEnum.findDefault().code());
        reply.setTopic(topic);
        reply.setTitle(title);
        reply.setUname(uname);
        reply.setUemail(uemail);
        reply.setUsite(usite);
        reply.setContent(content);

        this.replyDAO.insert(reply);

        // 更新主题评论次数
        this.topicDAO.updateReply(topic, 1);

        // 页面跳转
        return this.redirectTo(ufrom);
    }

    /**
     * 关于
     */
    @RequestMapping("/about.html")
    public String about() {
        return this.toFrontView("/about");
    }
    
}
