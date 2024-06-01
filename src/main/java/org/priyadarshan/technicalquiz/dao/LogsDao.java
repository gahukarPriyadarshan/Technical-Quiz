package org.priyadarshan.technicalquiz.dao;

import org.priyadarshan.technicalquiz.pojo.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogsDao extends JpaRepository<Logs, Long> {
}
