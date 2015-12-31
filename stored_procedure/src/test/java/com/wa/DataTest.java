package com.wa;


import com.wa.edu.service.DataService;
import com.wa.edu.utils.ConvertDateUtil;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 15-11-5.
 **/
public class DataTest extends TestCase{

    private DataService dataService;

    protected void setUp(){
        ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
        dataService = (DataService) ctx.getBean("dataService");
    }

    @Test
    public void testSynPaper2() throws Exception{
       List<Map<String,Object>> list=dataService.getAllPlan();
       for(Map map:list){
           System.out.println(map.get("id")+":"+ ConvertDateUtil.StrToDate(map.get("create_time").toString()));
       }
    }

    @Test
    public void testMaxUser() throws Exception{
        System.out.println(dataService.getMaxUserId());
    }


}
