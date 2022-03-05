package com.aeo.test.service;

import com.aeo.test.bean.DeclarationInfo;
import com.aeo.test.bean.Result;
import com.aeo.test.bean.VerifyResult;

public interface AwardsService {
    public Result returnParticipants(String token, String query, int pagenum, int pagesize);

    public Result returnDeclaration(int stuId, String token);

    public Result verify(int stuId, String token, VerifyResult verifyResult);

    public Result recording(String token, String query, int pagenum, int pagesize);

    public Result report(String token);

    public Result getInfo(int stuId, String token);

    public Result changeInfo(int stuId, String token, DeclarationInfo declarationInfo);

    public Result returnNewParticipants(String token, String query, int pagenum, int pagesize);
}
