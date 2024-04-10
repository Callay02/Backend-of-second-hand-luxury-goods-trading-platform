package icu.callay.controller;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import icu.callay.configure.AliPayConfig;
import icu.callay.entity.PlatformRevenueFlowForm;
import icu.callay.entity.RegularUser;
import icu.callay.service.PlatformRevenueFlowFormService;
import icu.callay.service.RegularUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * &#064;projectName:    springboot
 * &#064;package:        icu.callay.controller
 * &#064;className:      AliPayController
 * &#064;author:     Callay
 * &#064;description:  支付宝沙箱
 * &#064;date:    2024/4/9 11:51
 * &#064;version:    1.0
 */
@RestController
@RequestMapping("/alipay")
@Slf4j
public class AliPayController {

    private RegularUserService regularUserService;
    private PlatformRevenueFlowFormService platformRevenueFlowFormService;

    @Autowired
    public void PlatformRevenueFlowFormService(PlatformRevenueFlowFormService platformRevenueFlowFormService){
        this.platformRevenueFlowFormService=platformRevenueFlowFormService;
    }
    @Autowired
    public void RegularUserService(RegularUserService regularUserService){
        this.regularUserService=regularUserService;
    }

    private static final String GETWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT="JSON";
    private static final String CHARSET ="UTF-8";
    private static final String SIGN_TYPE="RSA2";

    @Resource
    private AliPayConfig aliPayConfig;

    @GetMapping("/recharge")
    public void recharge(String id,String money, HttpServletResponse httpResponse) throws Exception{
        //创建Client，通用SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(GETWAY_URL, aliPayConfig.getAppId(), aliPayConfig.getAppPrivateKey(),FORMAT,CHARSET,aliPayConfig.getAlipayPublicKey(),SIGN_TYPE);

        //创建request并设置request参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no","recharge_"+id+"_"+new Date().getTime());//订单编号
        bizContent.set("total_amount",money);//订单总金额
        bizContent.set("subject","充值");//支付名称
        bizContent.set("product_code","FAST_INSTANT_TRADE_PAY");//固定配置
        request.setBizContent(bizContent.toString());
        request.setReturnUrl("http://localhost:8080/#/pay/success");
        //执行请求，拿到响应的结果，返回给浏览器
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody();//调用SDK生成表单
        }catch (AlipayApiException e){
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset="+CHARSET);
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @PostMapping("/notify")
    public void payNotify(HttpServletRequest request) throws AlipayApiException {
        if(request.getParameter("trade_status").equals("TRADE_SUCCESS")){
            log.info("============ 支付宝异步回调 ============");

            Map<String,String> params = new HashMap<>();
            Map<String,String[]> requestParams = request.getParameterMap();
            for(String name:requestParams.keySet()){
                params.put(name,request.getParameter(name));
            }

            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature =AlipaySignature.rsa256CheckContent(content,sign,aliPayConfig.getAlipayPublicKey(),CHARSET);
            //支付宝验证签名
            if(checkSignature){
                //验证通过
                log.info("交易名称："+params.get("subject"));
                log.info("交易状态："+params.get("trade_status"));
                log.info("支付宝交易凭证号："+params.get("trade_no"));
                log.info("商户订单号："+params.get("out_trade_no"));
                log.info("交易金额："+params.get("total_amount"));
                log.info("买家支付宝唯一id："+params.get("buyer_id"));
                log.info("买家付款时间："+params.get("gmt_payment"));
                log.info("买家付款金额："+params.get("buyer_pay_amount"));

                String tradeNo = params.get("out_trade_no");
                //String gmtPayment=params.get("gmt_payment");
                String alipayTradeNo=params.get("trade_no");
                String buyerPayAmount = params.get("buyer_pay_amount");

                try {
                    //充值类型订单
                    if(Objects.equals(params.get("subject"), "充值")){
                        String id = tradeNo.split("_")[1];
                        log.info("用户id："+id);
                        RegularUser regularUser = regularUserService.getById(id);
                        Double total_money = regularUser.getMoney();
                        regularUser.setMoney(Double.valueOf(buyerPayAmount));
                        regularUserService.recharge(regularUser);
                        total_money+=Double.parseDouble(buyerPayAmount);
                        log.info("充值后余额："+total_money);
                    }
                    PlatformRevenueFlowForm platformRevenueFlowForm = new PlatformRevenueFlowForm();
                    platformRevenueFlowForm.setUserId(tradeNo.split("_")[1]);
                    platformRevenueFlowForm.setSubject(params.get("subject"));
                    platformRevenueFlowForm.setTradeNo(alipayTradeNo);
                    platformRevenueFlowForm.setTotalAmount(params.get("total_amount"));
                    platformRevenueFlowForm.setOutTradeNo(tradeNo);
                    platformRevenueFlowForm.setUpdateTime(new Date());
                    platformRevenueFlowForm.setSource("支付宝");
                    platformRevenueFlowFormService.save(platformRevenueFlowForm);
                }catch (Exception e){
                    throw new RuntimeException("充值失败");
                }

            }
        }
    }
}
