
package com.study.gmall0513logger.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.atguigu.gmall0513.common.constant.GmallConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j    //这个注解用于写日志
@RestController  //@Controller+ @RequesBody
public class LoggerController {

    /**
     * 由于我们的gmall0513\gmall0513-logger\pom.xml文件下引入的是spring的kafka，不是原生的kafka
     *
     * <dependency>
     *             <groupId>org.springframework.kafka</groupId>
     *             <artifactId>spring-kafka-test</artifactId>
     *             <scope>test</scope>
     *  </dependency>
     *
     * 所以我们需要声明kafka接口
     */

    //声明kafka接口
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @PostMapping("log")
    public String doLog( @RequestParam("logString") String logString){
        //1、补充时间戳
        JSONObject jsonObject = JSON.parseObject(logString);
        jsonObject.put("ts",System.currentTimeMillis());

        //2、写日志（用于离线采集）
        String logJSON = jsonObject.toJSONString();
        log.info(logJSON);    //第一次使用可能log会报错，需要加入lombok插件，在idea下载插件即可

        //3、发送到kafka
        if("startup".equals(jsonObject.getString("type")) ){
            kafkaTemplate.send(GmallConstant.KAFKA_TOPIC_STARTUP,jsonObject.toJSONString());
        }else{
            kafkaTemplate.send(GmallConstant.KAFKA_TOPIC_EVENT,jsonObject.toJSONString());
        }

        return "success";
    }

}

