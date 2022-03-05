package com.aeo.test;


import org.junit.Test;
import org.springframework.util.Base64Utils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

public class TEST {
    @Test
    public void test() {
        OutputStream out = null;
        try {
            InputStreamReader input=new InputStreamReader(new FileInputStream(new File("C:\\Users\\lenovo\\Desktop\\2020-03-24_16-14-42_310.txt")));
                    BufferedReader reader = new BufferedReader(input);
                    StringBuilder sbf = new StringBuilder();
                    String tempStr;
                    while ((tempStr = reader.readLine()) != null) {
                        sbf.append(tempStr);
                    }
                    reader.close();
            byte[] b = Base64Utils.decodeFromUrlSafeString(sbf.toString());
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }

            out = new FileOutputStream("C:\\Users\\lenovo\\Desktop\\2020-03-24_16-14-42_310.jpg");
            out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
