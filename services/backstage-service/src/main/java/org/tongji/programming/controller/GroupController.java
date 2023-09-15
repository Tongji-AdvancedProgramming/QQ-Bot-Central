package org.tongji.programming.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tongji.programming.DTO.cqhttp.group.GetGroupInfoRequest;
import org.tongji.programming.DTO.cqhttp.group.GetGroupInfoResponse;
import org.tongji.programming.dto.APIDataResponse;
import org.tongji.programming.dto.APIResponse;
import org.tongji.programming.http.BotGroupService;
import org.tongji.programming.mapper.QQGroupMapper;
import org.tongji.programming.pojo.QQGroup;

import javax.annotation.Resource;

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

    @RequestMapping(method = RequestMethod.POST)
    public APIResponse editOrInsert(@RequestBody QQGroup group){
        var ri = qqGroupMapper.insertOrUpdate(group);
        if(ri>0){
            return APIResponse.Success();
        }else{
            return APIResponse.Fail("4005","运行结果非预期，操作可能失败");
        }
    }

    @Resource
    private BotGroupService botGroupService;

    @RequestMapping(value = "validate", method = RequestMethod.POST)
    public APIResponse validateGroup(@RequestParam("group_id") String groupId){
        var response = botGroupService.getGroupInfo(Long.parseLong(groupId), true);

        if(!response.getStatus().equals("ok")){
            return APIResponse.Fail("4000",response.getMsg());
        }

        var data = JSONObject.toJavaObject(response.getData(), GetGroupInfoResponse.class);
        var inGroup = data.getMaxMemberCount() > 0;

        return APIDataResponse.Success(inGroup);
    }
}
