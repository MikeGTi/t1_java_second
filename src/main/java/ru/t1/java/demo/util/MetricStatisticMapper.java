package ru.t1.java.demo.util;

import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.MetricStatistic;

@Component
public class MetricStatisticMapper {

    public static MetricStatistic toEntity(ru.t1.java.demo.model.dto.MetricStatisticDto dto) {
        return MetricStatistic.builder()
                .executionTime(dto.getExecutionTime())
                .exceededOnTime(dto.getExceededOnTime())
                .methodName(dto.getMethodName())
                .methodArgs(dto.getMethodArgs())
                .build();
    }

    public static ru.t1.java.demo.model.dto.MetricStatisticDto toDto(MetricStatistic entity) {
        return ru.t1.java.demo.model.dto.MetricStatisticDto.builder()
                .id(entity.getId())
                .executionTime(entity.getExecutionTime())
                .exceededOnTime(entity.getExceededOnTime())
                .methodName(entity.getMethodName())
                .methodArgs(entity.getMethodArgs())
                .build();
    }

}
