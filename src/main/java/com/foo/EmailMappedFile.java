package com.foo;

import com.alibaba.fastjson.JSON;
import com.foo.common.EmailDTO;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EmailMappedFile {

    private Map<Integer, EmailDTO> mappedData = new ConcurrentHashMap<>();

    public Map<Integer, EmailDTO> getMappedData() {
        return mappedData;
    }

    public EmailMappedFile (URL fileUrl) {
        init(fileUrl);
    }

    void init(URL fileUrl) {
        if (fileUrl == null) throw new RuntimeException("邮件数据不存在");

        File f = new File(fileUrl.getPath());

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String lineData = null;
            while ((lineData = br.readLine()) != null) {
                try {
                    EmailDTO email = new EmailDTO();
                    email.copyPropertiesFromLineData(lineData);

                    if (email.getId() != null)
                        mappedData.put(email.getId(), email);
                } catch (ParseException e) {
                    continue;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
