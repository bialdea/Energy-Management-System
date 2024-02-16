package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.repositories.IMeasurementRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import ro.tuc.ds2020.entities.MeasurementDTO;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeasurementService {

    @Autowired
    IMeasurementRepository iMeasurementRepository;

    public Measurement saveMeasurement(Measurement measurement) {
        return iMeasurementRepository.save(measurement);
    }

    public List<Measurement> getAll() {
        return iMeasurementRepository.findAll();
    }

    public Measurement findLatestMeasurementWithinHour(Date timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfHour = cal.getTime();

        Date currentTimestamp = timestamp;

        List<Measurement> measurements = iMeasurementRepository.findByTimestBetween(startOfHour, currentTimestamp);
        return measurements.isEmpty() ? null : measurements.get(measurements.size() - 1);
    }

    @Transactional
    public void resetAllMeasurementsForNewHour() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        iMeasurementRepository.resetMeasurementsBeforeHour(calendar.getTime());
    }


    public List<MeasurementDTO> getMeasurementsForDay(LocalDate date) {
        Date startOfDay = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endOfDay = Date.from(date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Measurement> measurements = iMeasurementRepository.findByTimestBetween(startOfDay, endOfDay);

        Map<LocalTime, Double> energySumByHour = measurements.stream()
                .collect(Collectors.groupingBy(
                        measurement -> LocalDateTime.ofInstant(measurement.getTimest().toInstant(), ZoneId.systemDefault()).toLocalTime().truncatedTo(ChronoUnit.HOURS),
                        TreeMap::new,
                        Collectors.summingDouble(Measurement::getVal)
                ));

        List<MeasurementDTO> measurementDTOList = new ArrayList<>();
        energySumByHour.forEach((hour, energySum) -> measurementDTOList.add(new MeasurementDTO(hour, energySum)));

        return measurementDTOList;
    }

}
