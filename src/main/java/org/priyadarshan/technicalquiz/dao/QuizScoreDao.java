package org.priyadarshan.technicalquiz.dao;

import org.priyadarshan.technicalquiz.pojo.QuizScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuizScoreDao extends JpaRepository<QuizScore, Integer> {

}
