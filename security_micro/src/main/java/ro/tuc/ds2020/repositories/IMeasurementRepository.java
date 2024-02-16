package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.Measurement;

import java.util.Date;
import java.util.List;

@Repository
public interface IMeasurementRepository extends JpaRepository<Measurement,Long> {

        @Modifying
    @Query("UPDATE Measurement m SET m.val = 0.0, m.sum = 0.0 WHERE m.timest < :hour")
    void resetMeasurementsBeforeHour(@Param("hour") Date hour);
    List<Measurement> findByTimestBetween(Date startOfHour, Date endOfHour);

}
