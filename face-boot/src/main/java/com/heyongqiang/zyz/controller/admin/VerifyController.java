package com.heyongqiang.zyz.controller.admin;


import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heyongqiang.zyz.params.AdminXunJianParams;
import com.heyongqiang.zyz.params.DapingOssreportRight;
import org.apache.http.Consts;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/verify")
public class VerifyController {

    private static final SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

    @Value("#{'${notice.groups}'.split('\\|')}")
    private List<String> groups;

    public static void main(String[] args) {
        String busiType="OSSREPORT.VOLTEProv";
        String gdType="UNFINISH";
        String area="all";
        String areaProv="all";
        String busiCode="OSSREPORT.VOLTEProv";
        if(("OSSREPORT.WX".equals(busiType) || "OSSREPORT.ITV".equals(busiType) || "OSSREPORT.TYYP".equals(busiType)
                || "OSSREPORT.JTY".equals(busiType) || "OSSREPORT.TYKJ".equals(busiType) || "OSSREPORT.YUNGAME".equals(busiType)
                || "OSSREPORT.YUNDN".equals(busiType) || "OSSREPORT.YUNHY".equals(busiType) || "OSSREPORT.NSP".equals(busiType)
                || "OSSREPORT.VOLTE".equals(busiType) || "OSSREPORT.4GGM".equals(busiType) || "OSSREPORT.GCGM".equals(busiType)
                || "OSSREPORT.ZS".equals(busiType)) && ("1000".equals(area) || "all".equals(area)) && (("100001".equals(areaProv) || "all".equals(areaProv)))){
            System.out.println("tongguo");
        } else {
            System.out.println("不通过");
        }
    }




    @GetMapping("/test")
    public void test(){
        int timeout = 120000;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).build();
        HttpPost httpPost = null;
        List<BasicNameValuePair> nvps = null;
        CloseableHttpResponse responses = null;// 命名冲突，换一个名字，response
        ResponseEntity resEntity = null;
        String result = null;
        try {
            httpPost = new HttpPost("http://localhost:8979/verify/get");
            httpPost.setConfig(defaultRequestConfig);
            nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("consumerAppId", "test"));
            nvps.add(new BasicNameValuePair("serviceName", "queryMerchantService"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

            responses = httpClient.execute(httpPost);
// 读取响应内容
            String responseBody = EntityUtils.toString(responses.getEntity());
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<AdminXunJianParams> myObjects = objectMapper.readValue(responseBody, new TypeReference<List<AdminXunJianParams>>() {});
                System.out.println(myObjects);
                // 在这里对myObjects进行操作
            } catch (IOException e) {
                // 处理解析异常
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                responses.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @PostMapping("/get")
    public ResponseEntity getVerfiy(){
        List<DapingOssreportRight> list = new ArrayList<>();
        DapingOssreportRight adminXunJianParams = new DapingOssreportRight();
        adminXunJianParams.setCount(55);
        adminXunJianParams.setProvinceName("福建");
        adminXunJianParams.setServname("定时任务测试服务名称");
        adminXunJianParams.setState(0);
        list.add(adminXunJianParams);
        return new ResponseEntity<>(list , HttpStatus.OK );
    }

    @GetMapping
    public ResponseEntity<String> verifyDate(@RequestParam("code") String code){
        if(code.equals(date.format(new Date()))){
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unauthorized" , HttpStatus.UNAUTHORIZED);
        }
    }

}
