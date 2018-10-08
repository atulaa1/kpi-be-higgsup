package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiEventUser;
import com.higgsup.kpi.entity.KpiEventUserPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiEventUserRepo extends CrudRepository<KpiEventUser, KpiEventUserPK> {
    List<KpiEventUser> findByKpiEventId(int eventId);

}
