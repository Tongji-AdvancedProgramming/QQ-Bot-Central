package org.tongji.programming.controller;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tongji.programming.dto.APIDataResponse;
import org.tongji.programming.dto.APIResponse;
import org.tongji.programming.mapper.QQGroupMapper;
import org.tongji.programming.pojo.QQGroup;

@RestController
@RequestMapping("group")
public class GroupController {

    private QQGroupMapper qqGroupMapper;

    @Autowired
    public void setQqGroupMapper(QQGroupMapper qqGroupMapper) {
        this.qqGroupMapper = qqGroupMapper;
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public APIResponse getById(@PathVariable String id){
        var group = qqGroupMapper.selectById(id);
        return APIDataResponse.Success(group);
    }
}
