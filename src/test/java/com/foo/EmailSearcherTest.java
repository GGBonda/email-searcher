package com.foo;

import com.alibaba.fastjson.JSON;
import com.foo.common.EmailDTO;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class EmailSearcherTest {

    @Test
    public void testSenderSearch() {
        List<EmailDTO> res = new EmailSearcher().searchEmails("张三",null, null, null);

        System.out.println(JSON.toJSONString(res));
    }

    @Test
    public void testTitleKeyWordSearch() {
        List<EmailDTO> res = new EmailSearcher().searchEmails(null,"篮球", null, null);

        System.out.println(JSON.toJSONString(res));
    }

    @Test
    public void testSenderAndTitleKeyWordSearch() {
        List<EmailDTO> res = new EmailSearcher().searchEmails("蔡徐坤","篮球", null, null);

        System.out.println(JSON.toJSONString(res));
    }

    @Test
    public void testDateCreateSearch() throws ParseException {
        Date dateCreateStart = DateUtils.parseDate("2020/02/01 00:00:00", "yyyy/MM/dd HH:mm:ss");
        Date dateCreateEnd = DateUtils.parseDate("2020/03/01 00:00:00", "yyyy/MM/dd HH:mm:ss");
        List<EmailDTO> res = new EmailSearcher().searchEmails(null,null, dateCreateStart.getTime(), dateCreateEnd.getTime());

        System.out.println(JSON.toJSONString(res));
    }
}
