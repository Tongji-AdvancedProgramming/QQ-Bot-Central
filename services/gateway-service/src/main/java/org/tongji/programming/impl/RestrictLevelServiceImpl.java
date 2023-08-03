package org.tongji.programming.impl;

import lombok.var;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.tongji.programming.DTO.cqhttp.MessageUniversalReport;
import org.tongji.programming.config.BotConfiguration;
import org.tongji.programming.enums.GroupLevel;
import org.tongji.programming.service.RestrictLevelService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@DubboService
public class RestrictLevelServiceImpl implements RestrictLevelService {

    private Map<Long, GroupLevel> qqGroupMap = new HashMap<>();
    
    @Autowired
    BotConfiguration botConfiguration;

    @PostConstruct
    public void init(){
        var groupLevelMapping = botConfiguration.getGroup();
        for(var group: groupLevelMapping.getRestrict()){
            qqGroupMap.put(group, GroupLevel.RESTRICT);
        }
        for(var group: groupLevelMapping.getCareful()){
            qqGroupMap.put(group, GroupLevel.CAREFUL);
        }
        for(var group: groupLevelMapping.getNormal()){
            qqGroupMap.put(group, GroupLevel.NORMAL);
        }
        for(var group: groupLevelMapping.getTest()){
            qqGroupMap.put(group, GroupLevel.TEST);
        }
    }

    @Override
    public boolean restrictTo(GroupLevel target, long groupId) {
        var sourceLevel = qqGroupMap.getOrDefault(groupId, GroupLevel.NORMAL);
        return target.ordinal() >= sourceLevel.ordinal();
    }
}
