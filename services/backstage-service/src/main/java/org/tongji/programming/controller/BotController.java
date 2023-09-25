package org.tongji.programming.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tongji.programming.DTO.cqhttp.cqhttp.Status;
import org.tongji.programming.config.BackStageConfig;
import org.tongji.programming.dto.APIDataResponse;
import org.tongji.programming.dto.APIResponse;
import org.tongji.programming.dto.BotService.BotStatus;
import org.tongji.programming.http.BotGoCqHttpService;
import org.tongji.programming.http.BotGroupService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("bot")
public class BotController {

    @Resource
    BotGoCqHttpService botGoCqHttpService;

    @Resource
    BotGroupService botGroupService;

    @RequestMapping(value = "status", method = RequestMethod.GET)
    APIResponse getBotStatus() {
        var result = BotStatus.builder().build();

        // 获取统计数据
        try {
            var statusResp = botGoCqHttpService.getStatus();
            if (!Objects.equals(statusResp.getStatus(), "ok")) {
                result.setStatus("机器人异常");
                result.setType("warn");
                return APIDataResponse.Success(result);
            }

            var status = statusResp.getData().toJavaObject(Status.class);
            if (status == null) {
                result.setStatus("机器人异常");
                result.setType("warn");
                return APIDataResponse.Success(result);
            }

            if(!status.isGood() || !status.isOnline()){
                result.setStatus("已掉线");
                result.setType("warn");
                return APIDataResponse.Success(result);
            }

            result.setLost(status.getStat().getLostTimes());

            var lostPackage = (long) status.getStat().getPacketLost() / status.getStat().getPacketSent();
            result.setLosePackage(lostPackage);

            result.setLastMessage(status.getStat().getLastMessageTime());

        } catch (Exception e) {
            log.error("Bot疑似崩溃，错误信息：{}", e.getLocalizedMessage());
            result.setStatus("已崩溃");
            result.setType("fail");
            return APIDataResponse.Success(result);
        }

        var glResp = botGroupService.getGroupList(false);
        if (!Objects.equals(glResp.getStatus(), "ok")) {
            result.setStatus("机器人异常");
            result.setType("warn");
            return APIDataResponse.Success(result);
        }

        List<?> groups = glResp.getData().toJavaObject(List.class);
        result.setGroups(groups.size());

        result.setStatus("正常");
        result.setType("ok");
        return APIDataResponse.Success(result);
    }

    @Autowired
    BackStageConfig backStageConfig;

    @RequestMapping(value = "/msg",method = RequestMethod.GET)
    public String getMessage(){
        return backStageConfig.getTestMessage();
    }
}
