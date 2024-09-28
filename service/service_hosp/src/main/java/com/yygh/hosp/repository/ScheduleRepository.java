package com.yygh.hosp.repository;

import com.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.repository
 * @Description
 * @date 2024-09-21 15:43
 */
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    /**
     * 继承MongoRepository 符合Spring Data规范即可，不需要写方法实现
     *
     * @param hoscode
     * @param hosScheduleId
     * @return
     */
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    List<Schedule> findScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date workDate);
}
