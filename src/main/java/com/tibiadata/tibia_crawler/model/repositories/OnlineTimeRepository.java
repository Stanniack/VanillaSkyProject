package com.tibiadata.tibia_crawler.model.repositories;

import com.tibiadata.tibia_crawler.model.entities.OnlineTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Devmachine
 */
public interface OnlineTimeRepository extends JpaRepository<OnlineTime, Long> {

    @Query(value = "SELECT seconds FROM online_time ot WHERE parent_id = :personageId ORDER BY ot.registered_date DESC LIMIT 1",
            nativeQuery = true)
    Integer findLastOnlineTime(@Param("personageId") Integer personageId);
    
    //traz a última tabela pela data de registro
    @Query(value = "SELECT * FROM online_time ot WHERE parent_id = :personageId ORDER BY ot.registered_date DESC LIMIT 1",
            nativeQuery = true)
    OnlineTime findLastTimeOnlineById(@Param("personageId") Integer personageId);

}
