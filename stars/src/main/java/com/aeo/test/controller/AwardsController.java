package com.aeo.test.controller;


import com.aeo.test.bean.DeclarationInfo;
import com.aeo.test.bean.Result;
import com.aeo.test.bean.VerifyResult;
import com.aeo.test.service.AwardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AwardsController {
    @Autowired
    AwardsService awardsService;

    @GetMapping("/application/admin/participants")
    public Result returnParticipants(@RequestHeader("Authorization")String token, @RequestParam("query") String query, @RequestParam("pagenum") int pagenum, @RequestParam("pagesize") int pagesize){
        token = token.replaceAll(" ", "\\+");
        return awardsService.returnParticipants(token,query,pagenum,pagesize);
    }

    @GetMapping("/application/admin/participants/{stuId}/declaration")
    public Result returnDeclaration(@PathVariable("stuId")int stuId,@RequestHeader("Authorization")String token){
        token = token.replaceAll(" ", "\\+");
        return awardsService.returnDeclaration(stuId,token);
    }

    @PutMapping("/application/admin/participants/{stuId}/verify")
    public Result verify(@PathVariable("stuId")int stuId, @RequestHeader("Authorization")String token, @RequestBody VerifyResult verifyResult){
        token = token.replaceAll(" ", "\\+");
        return awardsService.verify(stuId,token,verifyResult);
    }

    @GetMapping("/application/admin/recording")
    public Result recording(@RequestHeader("Authorization")String token,@RequestParam("query") String query,@RequestParam("pagenum")int pagenum, @RequestParam("pagesize") int pagesize){
        token = token.replaceAll(" ", "\\+");
        return awardsService.recording(token,query,pagenum,pagesize);
    }

    @GetMapping("/application/admin/report")
    public Result report(@RequestHeader("Authorization")String token){
        token = token.replaceAll(" ", "\\+");
        return awardsService.report(token);
    }

    @GetMapping("/application/admin/participants/{stuId}/edit")
    public Result getInfo(@PathVariable("stuId")int stuId,@RequestHeader("Authorization")String token){
        token = token.replaceAll(" ", "\\+");
        return awardsService.getInfo(stuId,token);
    }

    @PutMapping("/application/admin//participants/{stuId}/edit")
    public Result changeInfo(@PathVariable("stuId")int stuId, @RequestHeader("Authorization")String token, @RequestBody DeclarationInfo declarationInfo){
        token = token.replaceAll(" ", "\\+");
        return awardsService.changeInfo(stuId,token,declarationInfo);
    }

    @GetMapping("/application/admin/newparticipants")
    public Result returnNewParticipants(@RequestHeader("Authorization")String token, @RequestParam("query") String query, @RequestParam("pagenum") int pagenum, @RequestParam("pagesize") int pagesize){
        token = token.replaceAll(" ", "\\+");
        return awardsService.returnNewParticipants(token,query,pagenum,pagesize);
    }
}
