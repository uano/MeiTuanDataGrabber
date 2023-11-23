package com.yzl.demo;

import com.yzl.demo.dto.StampDay;
import com.yzl.demo.utils.basicTools.BasicTools;
import com.yzl.demo.utils.basicTools.DateTools;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.Date;

@SpringBootTest
class DemoApplicationTests {

    //1.解析fillder抓包的curl
    @Test
    void contextLoads() {
        //获取curl
        String str = BasicTools.getcURL("curl -k -i --raw -o 0.dat \"https://waimaieapp.meituan.com/bizdata/single/customer/overview?token=06cAdJJG3nRwUFV26e5-5fk9ro0hjZ6EXxzXPi4KoyvI*&wmPoiId=17502675&acctId=134135563&appType=3&customerType=1&durationType=2&circleType=0&yodaReady=h5&csecplatform=4&csecversion=2.3.1&mtgsig=%%7B%%22a1%%22%%3A%%221.1%%22%%2C%%22a2%%22%%3A1700548266583%%2C%%22a3%%22%%3A%%22zv9zwz18ww245615069460368w7w505881yxy4600869795887v8823v%%22%%2C%%22a5%%22%%3A%%22m2ersiBt%%2Bsw63ZoBaX%%2Fw%%22%%2C%%22a6%%22%%3A%%22hs1.4aOG4x69iuIGtADfqn9IKcbupQ7QvLtmtewoS5kn0sA2gk7oHxiEVxxft1fNJN4rCG2%%2Bag64hIjSS7kEebNPOCw%%3D%%3D%%22%%2C%%22x0%%22%%3A4%%2C%%22d1%%22%%3A%%2293950e77299a0cc564220347ba0f02b5%%22%%7D\" -H \"Host: waimaieapp.meituan.com\" -H \"Connection: keep-alive\" -H \"sec-ch-ua: \\\"Microsoft Edge\\\";v=\\\"119\\\", \\\"Chromium\\\";v=\\\"119\\\", \\\"Not?A_Brand\\\";v=\\\"24\\\"\" -H \"Accept: application/json, text/plain, */*\" -H \"sec-ch-ua-mobile: ?0\" -H \"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0\" -H \"sec-ch-ua-platform: \\\"Windows\\\"\" -H \"Sec-Fetch-Site: same-origin\" -H \"Sec-Fetch-Mode: cors\" -H \"Sec-Fetch-Dest: empty\" -H \"Referer: https://waimaieapp.meituan.com/igate/bizdata/customer?_source=PC&token=06cAdJJG3nRwUFV26e5-5fk9ro0hjZ6EXxzXPi4KoyvI*&acctId=134135563&wmPoiId=17502675&region_id=&device_uuid=!31080e64-8c2d-4714-aeb0-61c1af133f57&bsid=nfB7XS10XNy1pQrcbczmmEMnMf1-ky7_JQ7VvrN5lmwdlcO_ZyR4NtVrFgCHdA3WpI-NE_Guf3gfTaT0QVA9tA&appType=3&fromPoiChange=true&userId=&topOrigin=https%%253A%%252F%%252Fe.waimai.meituan.com\" -H \"Accept-Encoding: gzip, deflate, br\" -H \"Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6\" -H \"Cookie: _lxsdk_cuid=18bcb539753c8-077a277451177f-26031051-1fa400-18bcb539753c8; _lxsdk=18bcb539753c8-077a277451177f-26031051-1fa400-18bcb539753c8; WEBDFPID=zv9zwz18ww245615069460368w7w505881yxy4600869795887v8823v-2015283335487-1699923335057EUMQQGMfd79fef3d01d5e9aadc18ccd4d0c95073825; uuid=a9464e2f951ca7efcf77.1699923335.1.0.0; token=06cAdJJG3nRwUFV26e5-5fk9ro0hjZ6EXxzXPi4KoyvI*; bsid=nfB7XS10XNy1pQrcbczmmEMnMf1-ky7_JQ7VvrN5lmwdlcO_ZyR4NtVrFgCHdA3WpI-NE_Guf3gfTaT0QVA9tA; acctId=134135563; _source=PC; acctName=cqyzl666888; device_uuid=!31080e64-8c2d-4714-aeb0-61c1af133f57; bizad_cityId=500100; bizad_second_city_id=500100; bizad_third_city_id=500106; wmPoiName=%%E4%%B8%%80%%E4%%B9%%8B%%E8%%9E%%BA%%C2%%B7%%E6%%9F%%B3%%E5%%B7%%9E%%E8%%9E%%BA%%E8%%9B%%B3%%E7%%B2%%89%%EF%%BC%%88%%E7%%86%%99%%E8%%A1%%97%%E5%%BA%%97%%EF%%BC%%89; bizad_first_tag_id=1000; platform=0; _lx_utm=utm_source%%3Dbing%%26utm_medium%%3Dorganic; wmPoiId=17502675; _lxsdk_s=18bf082cda1-8bf-58f-3b0%%7C%%7C365\"\n");
        System.out.println("curL为：" + str);
    }

    //2.测试生成美团管家能用的时间戳
    @Test
    void testMeiTuanGJ() throws ParseException {
        Date date = DateTools.computingDay(new Date(), -2);
        StampDay stampDay = DateTools.stampOneDayMilliSecond(date);
        System.out.println("开始时间戳:" + stampDay.getStartStamp());
        System.out.println("结束时间戳:" + stampDay.getEndStamp());

    }

    //3.测试base64加密
    @Test
    void base64Encode() {
        String str = BasicTools.base64Encode("你好");
        System.out.println(str);

    }

    //4.测试base64解密
    @Test
    void base64Decode() {
        String str = BasicTools.base64Decode("");
        System.out.println(str);
    }

    //5.测试修改营业信息表中的信息
    @Test
    void upTurnOver() {

    }


}
