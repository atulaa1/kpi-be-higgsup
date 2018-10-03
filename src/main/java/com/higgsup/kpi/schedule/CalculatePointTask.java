package com.higgsup.kpi.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by dungpx on 10/3/2018.
 */
@Service
public class CalculatePointTask {

    @Scheduled(cron = "0 3 16 3 * ?")
    public void scheduleFixedDelayTask() {
        System.out.println(
                "Fixed delay task - " + System.currentTimeMillis() / 1000);
    }
}
