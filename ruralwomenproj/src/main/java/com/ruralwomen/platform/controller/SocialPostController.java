package com.ruralwomen.platform.controller;

import com.ruralwomen.platform.model.SocialPost;
import com.ruralwomen.platform.service.SocialPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/naari")
public class SocialPostController {

    @Autowired
    private SocialPostService postService;

    /**
     * Trigger post generation+publish for a single woman.
     * POST /api/naari/generate
     * body: { "userId":"u1", "womanName":"Sita", "village":"Gaya", "businessType":"handicrafts" }
     */
    @PostMapping("/generate")
    public SocialPost generate(@RequestBody GenerateRequest req) {
        return postService.createAndPost(req.getUserId(), req.getWomanName(), req.getVillage(), req.getBusinessType());
    }

    public static class GenerateRequest {
        private String userId;
        private String womanName;
        private String village;
        private String businessType;
        // getters/setters
        public String getUserId(){return userId;} public void setUserId(String userId){this.userId=userId;}
        public String getWomanName(){return womanName;} public void setWomanName(String womanName){this.womanName=womanName;}
        public String getVillage(){return village;} public void setVillage(String village){this.village=village;}
        public String getBusinessType(){return businessType;} public void setBusinessType(String businessType){this.businessType=businessType;}
    }
}
