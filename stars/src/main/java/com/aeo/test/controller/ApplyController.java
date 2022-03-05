package com.aeo.test.controller;

import com.aeo.test.bean.Recommend;
import com.aeo.test.bean.Result;
import com.aeo.test.service.ApplyService;
import com.aeo.tools.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class ApplyController {
    @Autowired
    ApplyService applyService;

    @GetMapping("/test")               //后期自己的测试，测试IpUtil功能
    public String test( HttpServletRequest request){ //结果为0:0:0:0:0:0:0:1
        String ipAddress = IpUtil.getIpAddr(request);
        return ipAddress;
    }


    @PostMapping("/application/login")
    public Result login(@RequestBody Map<String,Object> map, HttpServletRequest request){
        return applyService.login(map.get("username").toString(),map.get("password").toString(),request);
    }

    @PostMapping("/application/check")
    public Result check(@RequestBody Map<String,Object> map,@RequestHeader("Authorization")String token){
        token = token.replaceAll(" ", "\\+");
        return applyService.check(map.get("name").toString(),map.get("stuNum").toString(),token);
    }

    @GetMapping("/application/files")
    public Result file(@RequestHeader("Authorization")String token){
        token = token.replaceAll(" ", "\\+");
        return applyService.file(token);
    }

    @PostMapping("/application/recommend")
    public Result recommend(@RequestBody Recommend recommend, @RequestHeader("Authorization")String token){
        token = token.replaceAll(" ", "\\+");
        return applyService.recommend(recommend,token);
    }

    @GetMapping("/application/view")
    public Result view(@RequestHeader("Authorization")String token,@RequestParam("query") String query,@RequestParam("pageNum") int pagenum, @RequestParam("pageSize") int pagesize){
        token = token.replaceAll(" ", "\\+");
        return applyService.view(token,query,pagenum,pagesize);
    }

    @PutMapping("application/{stuId}/vote")
    public Result vote(@RequestHeader("Authorization")String token,@PathVariable("stuId")int stuId){
        token = token.replaceAll(" ", "\\+");
        return applyService.vote(token,stuId);
    }

//    @GetMapping("application/search")
//    public Result search(@RequestHeader("Authorization")String token,@RequestParam("name")String name){
//        token = token.replaceAll(" ", "\\+");
//        return applyService.search(token,name);
//    }

    @GetMapping("/application/{stuId}/details")
    public Result details(@PathVariable("stuId")int stuId,@RequestHeader("Authorization")String token){
        token = token.replaceAll(" ", "\\+");
        return applyService.details(token,stuId);
    }

}
