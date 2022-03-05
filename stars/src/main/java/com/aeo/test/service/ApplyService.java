package com.aeo.test.service;

import com.aeo.test.bean.Recommend;
import com.aeo.test.bean.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface ApplyService {

    public Result login(String username, String password, HttpServletRequest request);

    public Result recommend(Recommend recommend, String token);

    public Result view(String token, String query, int pagenum, int pagesize);

    public Result vote(String token, int stuId);

//    public Result search(String token, String name);

    public Result check(String name, String stuNum, String token);

    public Result file(String token);

    public Result details(String token, int stuId);
}
