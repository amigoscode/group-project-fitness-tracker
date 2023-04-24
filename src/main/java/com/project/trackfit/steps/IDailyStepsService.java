package com.project.trackfit.steps;

import java.util.List;
import java.util.UUID;

public interface IDailyStepsService {
    UUID createDailySteps(CreateDailyStepsRequest createDailyStepsRequest);
    List<RetrieveDailyStepsRequest> getCustomerDailySteps(UUID customerId);
    RetrieveDailyStepsRequest retrieveDailyStepsById(UUID dailyStepsId);
}