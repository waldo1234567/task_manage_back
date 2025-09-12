package com.task_management.demo.utils;

import com.task_management.demo.dto.ActivityLogResponse;
import com.task_management.demo.entity.ActivityLogEntity;

public final class ActivityLogMapper {
    private ActivityLogMapper(){}

    public static ActivityLogResponse toDto(ActivityLogEntity a){
        if(a == null) return null;
        ActivityLogResponse r = new ActivityLogResponse();
        r.setId(a.getId());
        r.setTaskId(a.getTask() != null ? a.getTask().getId() : null);
        r.setAction(a.getAction());
        r.setActorAuth0Id(a.getActorAuth0Id());
        r.setDetails(a.getDetails());
        r.setCreatedAt(a.getCreatedAt());
        return r;

    }
}
