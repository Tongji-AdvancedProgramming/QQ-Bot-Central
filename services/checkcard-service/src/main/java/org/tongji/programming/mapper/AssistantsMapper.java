package org.tongji.programming.mapper;

import com.dtflys.forest.annotation.Query;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AssistantsMapper {
    void insertAssistants(Assistants assistants);

    Assistants selectById(@Query("id") Long id);
}
