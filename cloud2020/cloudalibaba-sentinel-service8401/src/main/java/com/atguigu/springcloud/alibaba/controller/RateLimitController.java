package com.atguigu.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.alibaba.myHandler.CustomerBlockHandler;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.netflix.ribbon.proxy.annotation.ResourceGroup;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RateLimitController {

    @GetMapping("/byResource")
    @SentinelResource(value = "byResource",blockHandler = "handlerException")
    public CommonResult byResource(){
        return new CommonResult(200,"按资源名称限流测试ok",new Payment(2020l,"serial001"));
    }

    public CommonResult handlerException(BlockException exception){

        return new CommonResult(400,exception.getClass().getCanonicalName()+"\t 服务不可用");
    }


    @GetMapping("/rateLimit/byUrl")
    public CommonResult byUrl(){
        return new CommonResult(400,"按url限流测试ok",new Payment(2020l,"serial002"));
    }


    @GetMapping("/rateLimit/customerBlockHandler")
    @SentinelResource(value = "customerBlockHandler",blockHandlerClass = CustomerBlockHandler.class,blockHandler = "handlerException2")
    public CommonResult customerBlockHandler(){
        return new CommonResult(400,"按客户自定义",new Payment(2020l,"serial003"));
    }
}
