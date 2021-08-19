package com.atguigu.springcloud.alibaba.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.alibaba.service.PaymentService;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class CircleBreakerController {

   public static final String SERVICE_URL ="http://nacos-payment-provider";

   @Resource
   private RestTemplate restTemplate;


   @GetMapping("/consumer/fallback/{id}")
//   @SentinelResource(value = "fallback")//没有配置
//   @SentinelResource(value = "fallback",fallback = "handlerFallback")//fallback只负责业务异常
//   @SentinelResource(value = "fallback",blockHandler = "blockHandler")//blockHandler只负责sentinel控制台配置违规
//   @SentinelResource(value = "fallback",fallback = "handlerFallback",blockHandler = "blockHandler")//两个都配置
   @SentinelResource(value = "fallback",fallback = "handlerFallback",
           blockHandler = "blockHandler",
           exceptionsToIgnore = IllegalArgumentException.class)//忽略其中某种异常
   public CommonResult<Payment> fallback(@PathVariable("id") Long id){
       CommonResult result = restTemplate.getForObject(SERVICE_URL + "/paymentSQL/" + id, CommonResult.class, id);

       if (id == 4){
           throw new IllegalArgumentException("IllegalArgumentException,非法参数异常");
       }else if (result.getData()== null){
           throw new NullPointerException("NullPointerException,该id没有对应记录，空指针异常");
       }

       return result;
   }

//   本例是fallback
    public CommonResult handlerFallback(@PathVariable Long id,Throwable t){
        Payment payment = new Payment(id, "null");
        return new CommonResult(444,"兜底异常handlerFallback，exception内容："+t.getMessage(),payment);
    }

    public CommonResult blockHandler(@PathVariable Long id, BlockException e){
        Payment payment = new Payment(id, "null");
        return new CommonResult(445,"blockHandler-sentinel,无此流水,BlockException: "+e.getMessage());
    }


    //============================================================================openfeign
    @Resource
    private PaymentService paymentService;

   @GetMapping("/consumer/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id) {
        return paymentService.paymentSQL(id);
    }

}
