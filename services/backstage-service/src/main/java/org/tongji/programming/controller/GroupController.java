package org.tongji.programming.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tongji.programming.DTO.cqhttp.group.GroupInfo;
import org.tongji.programming.DTO.cqhttp.group.GroupMemberInfo;
import org.tongji.programming.dto.APIDataResponse;
import org.tongji.programming.dto.APIResponse;
import org.tongji.programming.dto.GroupService.ValidateGroupResult;
import org.tongji.programming.http.BotGroupService;
import org.tongji.programming.mapper.QQGroupMapper;
import org.tongji.programming.pojo.QQGroup;
import org.tongji.programming.service.BotSelfService;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("group")
public class GroupController {

    @Resource
    private BotGroupService botGroupService;

    private QQGroupMapper qqGroupMapper;

    @Autowired
    public void setQqGroupMapper(QQGroupMapper qqGroupMapper) {
        this.qqGroupMapper = qqGroupMapper;
    }

    private BotSelfService botSelfService;

    @Autowired
    public void setBotSelfService(BotSelfService botSelfService) {
        this.botSelfService = botSelfService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public APIResponse getById(@PathVariable String id) {
        var group = qqGroupMapper.selectById(id);
        return APIDataResponse.Success(group);
    }

    @RequestMapping(method = RequestMethod.POST)
    public APIResponse editOrInsert(@RequestBody QQGroup group) {
        var ri = qqGroupMapper.insertOrUpdate(group);
        if (ri > 0) {
            return APIResponse.Success();
        } else {
            return APIResponse.Fail("4005", "运行结果非预期，操作可能失败");
        }
    }

    @RequestMapping(value = "link", method = RequestMethod.POST)
    public APIResponse linkGroupWithCourse(@RequestPart String courseId, @RequestPart List<String> groupId) {
        AtomicInteger ri = new AtomicInteger();
        groupId.forEach(id -> {
            ri.addAndGet(qqGroupMapper.linkCourseAndGroup(courseId, id));
        });

        if (ri.get() == groupId.size()) {
            return APIResponse.Success();
        } else {
            return APIResponse.Fail("4005", String.format("成功连接%d个群，部分群连接失败，请刷新查看。", ri.get()));
        }
    }

    @RequestMapping(value = "validate", method = RequestMethod.POST)
    public APIResponse validateGroup(@RequestParam("group_id") String groupId) {

        var loginInfo = botSelfService.getSelfId();
        if (loginInfo == null) {
            return APIResponse.Fail("4000", "Bot未登录");
        }

        var response = botGroupService.getGroupInfo(Long.parseLong(groupId), true);

        if (!response.getStatus().equals("ok")) {
            if (response.getMsg().equals("GROUP_NOT_FOUND"))
                return APIDataResponse.Success(ValidateGroupResult.builder().botId(loginInfo.getUserId())
                        .nickname(loginInfo.getNickname())
                        .message("未找到该群，请检查群号是否正确或该群是否可被按群号搜索。").build());
            return APIResponse.Fail("4000", response.getWording());
        }

        var data = JSONObject.toJavaObject(response.getData(), GroupInfo.class);

        var inGroup = data.getMaxMemberCount() > 0;
        var isAdmin = true;

        if (inGroup) {
            response = botGroupService.getGroupMemberInfo(Long.parseLong(groupId), loginInfo.getUserId(), true);
            if (!response.getStatus().equals("ok")) {
                return APIResponse.Fail("4000", response.getWording());
            }

            var groupMemberInfoData = JSONObject.toJavaObject(response.getData(), GroupMemberInfo.class);
            if (groupMemberInfoData.getRole().equals("member")) {
                isAdmin = false;
            }
        }

        ValidateGroupResult result = null;

        if (!inGroup) {
            result = ValidateGroupResult.builder().botId(loginInfo.getUserId()).nickname(loginInfo.getNickname())
                    .message("Bot未在所填入的群内，请检查群号是否正确，或群号是否可被搜索。").build();
        } else if (isAdmin) {
            result = ValidateGroupResult.builder().botId(loginInfo.getUserId()).nickname(loginInfo.getNickname())
                    .message("Bot已拥有该群管理权限，配置正确！").build();
        } else {
            result = ValidateGroupResult.builder().botId(loginInfo.getUserId()).nickname(loginInfo.getNickname())
                    .message("Bot已在群内，但尚无管理权限。").build();
        }

        return APIDataResponse.Success(result);
    }

}
