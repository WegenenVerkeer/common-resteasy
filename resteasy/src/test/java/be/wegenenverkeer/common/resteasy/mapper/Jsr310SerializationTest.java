package be.wegenenverkeer.common.resteasy.mapper;

import be.wegenenverkeer.common.resteasy.json.RestJsonMapper;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verify serialization and deserialization of LocalDate and LocalDateTime.
 */
public class Jsr310SerializationTest {

    private RestJsonMapper mapper = new RestJsonMapper();

    @Test
    public void serializeLocalDate() throws Exception {
        LocalDateTo to = new LocalDateTo();
        to.setDate(LocalDate.of(2014, 2, 14));

        String res = mapper.writeValueAsString(to);

        assertThat(res).isEqualTo("{\"date\":\"2014-02-14\"}");
    }

    @Test
    public void serializeLocalDateTime() throws Exception {
        LocalDateTimeTo to = new LocalDateTimeTo();
        to.setTimestamp(LocalDateTime.of(2014, 2, 14, 8, 32, 10));

        String res = mapper.writeValueAsString(to);

        assertThat(res).isEqualTo("{\"timestamp\":\"2014-02-14T08:32:10\"}");
    }

    @Test
    public void deserializeLocalDate() throws Exception {

        LocalDateTo res = mapper.readValue("{\"date\":\"2014-02-14\"}", LocalDateTo.class);

        assertThat(res.getDate()).isEqualTo(LocalDate.of(2014, 2, 14));
    }

    @Test
    public void deserializeLocalDateTime() throws Exception {

        LocalDateTimeTo res = mapper.readValue("{\"timestamp\":\"2014-02-14T08:32:10\"}", LocalDateTimeTo.class);

        assertThat(res.getTimestamp()).isEqualTo(LocalDateTime.of(2014, 2, 14, 8, 32, 10));
    }

}
