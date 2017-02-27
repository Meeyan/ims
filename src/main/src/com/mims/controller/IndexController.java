package com.mims.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.mims.dao.UserDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaojy on 2017/2/27.
 */
@Controller
@RequestMapping(value = "")
public class IndexController {

    @Resource
    private UserDao userDao;

    @RequestMapping(value = "")
    public ModelAndView index_def() {
        // List userById = userDao.getUserById();
        ModelAndView view = new ModelAndView();
        view.setViewName("index");
        return view;
    }

    @RequestMapping(value = "/")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView();
        // List userById = userDao.getUserById();
        view.setViewName("index");
        return view;
    }


    @RequestMapping(value = "/test")
    public ModelAndView test() {
        ModelAndView view = new ModelAndView();
        view.setViewName("index");
        return view;
    }

    @RequestMapping(value = "/demo")
    public ModelAndView demoPages() {
        ModelAndView view = new ModelAndView();
        view.setViewName("demo");
        return view;
    }

    @RequestMapping(value = "jsonData")
    @ResponseBody
    public String getData() {
        List list = new ArrayList<>();
        Map map = new HashMap<>();
        map.put("itemid", 1);
        map.put("productid", "项目类型1");
        map.put("productname", "商品图书1");
        map.put("listprice", 1000.00);
        map.put("unitcost", "图书管理费");
        map.put("item", "客户张先生");
        map.put("borrowMon", 1000.00);
        map.put("lendMoney", 1000.00);
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        String s = JSONUtils.toJSONString(list);

        return s;
    }

    @RequestMapping(value = "getDataPro")
    @ResponseBody
    public String getDataPro() {
        List list = new ArrayList<>();
        Map map = new HashMap<>();
        map.put("productid", "FI-SW-01");
        map.put("productname", "商品图书1");
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        String s = JSONUtils.toJSONString(list);
        System.out.println(s);
        return s;
    }
}
