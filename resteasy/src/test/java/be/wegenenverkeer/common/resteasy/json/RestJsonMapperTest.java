package be.wegenenverkeer.common.resteasy.json;

import junit.framework.TestCase;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test proper serialization and deserialization of dates and times.
 */
public class RestJsonMapperTest extends TestCase {

    @Test
    public void testSerialization() throws Exception {
        RestJsonMapper mapper = new RestJsonMapper();
        DatesAndTimesTo to = new DatesAndTimesTo();
        to.setDate(new Date(114 /*+1900 = 2014*/, 1 /* this is februari */, 14, 10, 11, 12));
        to.setLocalDate(LocalDate.of(2014, 2, 14));
        to.setLocalDateTime(LocalDateTime.of(2014, 2, 14, 10, 11, 12));
        to.setJodaDateTime(new DateTime(2014, 2, 14, 10, 11, 12));
        to.setJodaDate(new DateMidnight(2014, 2, 14));

        String res = mapper.writeValueAsString(to);

        System.out.println(res);
        assertThat(res).contains("\"date\":\"2014-02-14T10:11:12\"");
        assertThat(res).contains("\"localDate\":\"2014-02-14\"");
        assertThat(res).contains("\"localDateTime\":\"2014-02-14T10:11:12\"");
        assertThat(res).contains("\"jodaDateTime\":\"2014-02-14T"); // ignore time as this changes with timezone
        assertThat(res).contains("\"jodaDate\":\"2014-02-"); // ignore date as this is the date after conversion to GMT
    }

}