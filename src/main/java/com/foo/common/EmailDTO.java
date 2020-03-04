package com.foo.common;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Setter
@Getter
public class EmailDTO {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();

    private Integer id;

    private String sender;

    private String title;

    private Long dateCreate;

    public void copyPropertiesFromLineData (String lineData) throws ParseException {
        if (lineData == null || lineData.trim().length() == 0) return;

        String[] dataArray = lineData.split("\\s");

        if (dataArray != null) {
            this.setId(Integer.valueOf(dataArray[0]));
            this.setSender(dataArray[1]);
            this.setTitle(dataArray[2]);
            this.setDateCreate(DateUtils.parseDate(dataArray[3], "yyyy/MM/dd_HH:mm:ss").getTime());
        }
    }
}
