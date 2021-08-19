package com.atguigu.springcloud.alibaba.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class FlowLimitController {


    @GetMapping("/testA")
    public String testA() {
      /*  try {
            TimeUnit.MILLISECONDS.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return "-------------testA";
    }

    @GetMapping("/testB")
    public String testB() {

        log.info(Thread.currentThread().getName() + "\t" + "....testB");
        return "-------------testB";
    }

    @GetMapping("/testD")
    public String testD() {
        /*try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("测试 RT");*/
        log.info("testD 异常比例");
//        int age = 10 / 0;
        return "-------------testD";
    }

    @GetMapping("/testE")
    public String testE() {
        log.info("测试异常数");
        int age = 10/0;
        return "-------------testE测试异常数";
    }


    @GetMapping("/testHotKey")//rest风格
    @SentinelResource(value="testHotKey",blockHandler = "deal_HotKey")
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2",required = false) String p2)
    {
        return "-------------testHotKey...";
    }

    public String deal_HotKey(String p1, String p2, BlockException exception){
        return "-------------deal_testHotKey， o(╥﹏╥)o";//sentinel系统的默认提示是：Blocked by Sentinel (flow limiting)
    }
}
