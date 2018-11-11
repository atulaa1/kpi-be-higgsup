package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiEvent;
import com.higgsup.kpi.entity.KpiEventUser;
import com.higgsup.kpi.entity.KpiSeminarSurvey;
import com.higgsup.kpi.entity.KpiUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KpiSeminarSurveyRepo extends CrudRepository<KpiSeminarSurvey, Integer> {
    List<KpiSeminarSurvey> findByEvaluatedUsernameAndEvent(KpiUser user, KpiEvent event);
    List<KpiSeminarSurvey> findByEvent(KpiEvent event);
}
