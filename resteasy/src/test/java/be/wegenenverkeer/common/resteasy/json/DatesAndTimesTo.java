package be.wegenenverkeer.common.resteasy.json;

import lombok.Data;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Simple TO which contains dates and timestamps.
 */
@Data
public class DatesAndTimesTo {

    private Date date;
    private LocalDate localDate;
    private LocalDateTime localDateTime;

    private DateTime jodaDateTime;
    private DateMidnight jodaDate;

}
