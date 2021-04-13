package com.github.wuzhihao7.springbootajax.controller;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AjaxController {

    private final List<Map<String, Object>> dbList = new ArrayList<>();

    public AjaxController(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", "100");
        map.put("name", "家用电器");
        map.put("parentId", null);//parentId为null则表示它是1级分类
        map.put("remark", "电器相关等");
        map.put("createdTime", LocalDateTime.now());
        dbList.add(map);
    }

    @GetMapping("/doAjaxStart")
    public String doAjaxStart(){
        return "Response Result Of Ajax Get Request 01 ";
    }

    @GetMapping("/doAjaxGet")
    public List<Map<String, Object>> doAjaxGet(){
        return dbList;
    }

    @PostMapping("/doAjaxPost")
    public String doAjaxPost(@RequestParam Map<String, Object> map){
        map.put("createdTime", LocalDateTime.now());
        dbList.add(map);
        return "save ok";
    }

    @DeleteMapping("/doAjaxDelete")
    public String doAjaxDelete(String id){
        dbList.removeIf(map -> map.get("id").equals(id));
        return "delete ok";
    }

    @PutMapping("/doAjaxPut")
    public String doAjaxPut(@RequestParam Map<String, Object> updateMap){
        for (Map<String, Object> map : dbList) {
            if (map.get("id").equals(updateMap.get("id"))) {
                map.put("name", updateMap.get("name"));
                map.put("remark", updateMap.get("remark"));
            }
        }
        return "put ok";
    }
}
