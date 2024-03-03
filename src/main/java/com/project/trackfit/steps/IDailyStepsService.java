package com.project.trackfit.steps;

import java.util.List;
import java.util.UUID;

public interface IDailyStepsService {
    UUID createDailySteps(DailyStepsRequest dailyStepsRequest);
    DailyStepsResponse retrieveDailyStepsById(UUID dailyStepsId);
}