import java.time.LocalDateTime;

public class TestUtils {

  static TimeSlot betweenHours(int h1, int h2) {
    return TimeSlot.of(dateAtHour(h1), dateAtHour(h2));
  }

  static LocalDateTime dateAtHour(int hour) {
    return LocalDateTime.of(2020, 1, 1, hour, 0);
  }

}
