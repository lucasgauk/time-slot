import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.Test;


public class TimeSetTest {

  @Test
  void empty() {
    TimeSet ts = TimeSet.empty();
    assertEquals(0, ts.getTimeSlots().size());
  }

  @Test
  void of() {
    TimeSlot t = this.betweenHours(0, 12);
    TimeSet ts = TimeSet.of(t);
    TimeSet ts1 = TimeSet.of(Collections.singletonList(t));
    assertEquals(1, ts.getTimeSlots().size());
    assertEquals(1, ts1.getTimeSlots().size());
  }

  @Test
  void add_timeSlot() {
    TimeSlot t = this.betweenHours(0, 5);
    TimeSlot t1 = this.betweenHours(4, 10);
    TimeSlot t2 = this.betweenHours(12, 18);
    TimeSet ts = TimeSet.of(t, t1, t2);
    assertEquals(2, ts.getTimeSlots().size());
    assertTrue(ts.getTimeSlots().get(0).equals(this.betweenHours(12, 18)));
    assertTrue(ts.getTimeSlots().get(1).equals(this.betweenHours(0, 10)));
  }

  @Test
  void add_list() {
    TimeSlot t = this.betweenHours(0, 5);
    TimeSlot t1 = this.betweenHours(8, 10);
    TimeSet ts = TimeSet.of(t, t1);
    TimeSlot t2 = this.betweenHours(9, 12);
    TimeSlot t3 = this.betweenHours(15, 22);
    TimeSet ts1 = TimeSet.of(t2, t3);
    TimeSlot t4 = this.betweenHours(20, 23);
    TimeSet ts2 = TimeSet.of(t4);
    ts.add(ts1, ts2);
    assertEquals(3, ts.getTimeSlots().size());
    assertTrue(ts.getTimeSlots().get(0).equals(this.betweenHours(15, 23)));
    assertTrue(ts.getTimeSlots().get(1).equals(this.betweenHours(8, 12)));
    assertTrue(ts.getTimeSlots().get(2).equals(this.betweenHours(0, 5)));
  }

  @Test
  void subtract_timeSlot() {
    TimeSlot t = this.betweenHours(0, 5);
    TimeSlot t1 = this.betweenHours(8, 10);
    TimeSlot t2 = this.betweenHours(12, 15);
    TimeSet ts = TimeSet.of(t, t1, t2);
    TimeSlot t3 = this.betweenHours(1, 8);
    TimeSlot t4 = this.betweenHours(13, 14);
    ts.subtract(t3);
    ts.subtract(t4);
    assertEquals(4, ts.getTimeSlots().size());
    assertTrue(ts.getTimeSlots().get(0).equals(this.betweenHours(12, 13)));
    assertTrue(ts.getTimeSlots().get(1).equals(this.betweenHours(14, 15)));
    assertTrue(ts.getTimeSlots().get(2).equals(this.betweenHours(8, 10)));
    assertTrue(ts.getTimeSlots().get(3).equals(this.betweenHours(0, 1)));
  }

  @Test
  void subtract_list() {
    TimeSlot t = this.betweenHours(0, 14);
    TimeSet ts = TimeSet.of(t);
    TimeSlot t1 = this.betweenHours(1, 5);
    TimeSlot t2 = this.betweenHours(14, 15);
    TimeSet ts1 = TimeSet.of(t1, t2);
    TimeSlot t3 = this.betweenHours(6, 12);
    TimeSet ts2 = TimeSet.of(t3);
    ts.subtract(ts1, ts2);
    assertEquals(3, ts.getTimeSlots().size());
    assertTrue(ts.getTimeSlots().get(0).equals(this.betweenHours(0, 1)));
    assertTrue(ts.getTimeSlots().get(1).equals(this.betweenHours(5, 6)));
    assertTrue(ts.getTimeSlots().get(2).equals(this.betweenHours(12, 14)));
  }

  @Test
  void intersect_timeSlot() {
    TimeSlot t = this.betweenHours(0, 5);
    TimeSlot t1 = this.betweenHours(12, 18);
    TimeSet ts = TimeSet.of(t, t1);
    TimeSlot t2 = this.betweenHours(4, 14);
    ts.intersect(t2);
    assertEquals(2, ts.getTimeSlots().size());
    assertTrue(ts.getTimeSlots().get(0).equals(this.betweenHours(12, 14)));
    assertTrue(ts.getTimeSlots().get(1).equals(this.betweenHours(4, 5)));
  }

  @Test
  void intersect_list() {
    TimeSlot t = this.betweenHours(0, 18);
    TimeSet ts = TimeSet.of(t);
    TimeSlot t1 = this.betweenHours(1, 5);
    TimeSlot t2 = this.betweenHours(14, 20);
    TimeSet ts1 = TimeSet.of(t1, t2);
    TimeSlot t3 = this.betweenHours(2, 3);
    TimeSlot t4 = this.betweenHours(12, 15);
    TimeSet ts2 = TimeSet.of(t3, t4);
    ts.intersect(ts1, ts2);
    assertEquals(2, ts.getTimeSlots().size());
    assertTrue(ts.getTimeSlots().get(0).equals(this.betweenHours(14, 15)));
    assertTrue(ts.getTimeSlots().get(1).equals(this.betweenHours(2, 3)));
  }

  @Test
  void equals() {
    TimeSlot t = this.betweenHours(2, 5);
    TimeSlot t1 = this.betweenHours(12, 15);
    TimeSet ts = TimeSet.of(t, t1);
    TimeSlot t2 = this.betweenHours(12, 15);
    TimeSlot t3 = this.betweenHours(2, 5);
    TimeSlot t4 = this.betweenHours(3, 5);
    TimeSet ts1 = TimeSet.of(t2, t3, t4);
    assertTrue(ts.equals(ts1));
    TimeSlot t5 = this.betweenHours(13, 16);
    ts1.add(t5);
    assertFalse(ts.equals(ts1));
  }

  private TimeSlot betweenHours(int h1, int h2) {
    return TimeSlot.of(this.dateAtHour(h1), this.dateAtHour(h2));
  }

  private LocalDateTime dateAtHour(int hour) {
    return LocalDateTime.of(2020, 1, 1, hour, 0);
  }

}
