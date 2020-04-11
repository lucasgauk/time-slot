import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
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
    TimeSlot t = TestUtils.betweenHours(0, 12);
    TimeSet ts = TimeSet.of(t);
    TimeSet ts1 = TimeSet.of(Collections.singletonList(t));
    assertEquals(1, ts.getTimeSlots().size());
    assertEquals(1, ts1.getTimeSlots().size());
    TimeSet ts2 = TimeSet.of(ts);
    ts.add(TestUtils.betweenHours(14, 15));
    assertEquals(2, ts.getTimeSlots().size());
    assertEquals(1, ts2.getTimeSlots().size());
  }

  @Test
  void length() {
    TimeSlot t = TestUtils.betweenHours(0, 5);
    TimeSlot t1 = TestUtils.betweenHours(12, 15);
    TimeSet ts = TimeSet.of(t, t1);
    assertEquals(8, ts.length(ChronoUnit.HOURS));
    assertEquals(8 * 60, ts.length(ChronoUnit.MINUTES));
  }

  @Test
  void add_timeSlot() {
    TimeSlot t = TestUtils.betweenHours(0, 5);
    TimeSlot t1 = TestUtils.betweenHours(4, 10);
    TimeSlot t2 = TestUtils.betweenHours(12, 18);
    TimeSet ts = TimeSet.of(t, t1, t2);
    assertEquals(2, ts.getTimeSlots().size());
    assertTrue(ts.getTimeSlots().get(0).equals(TestUtils.betweenHours(12, 18)));
    assertTrue(ts.getTimeSlots().get(1).equals(TestUtils.betweenHours(0, 10)));
  }

  @Test
  void add_list() {
    TimeSlot t = TestUtils.betweenHours(0, 5);
    TimeSlot t1 = TestUtils.betweenHours(8, 10);
    TimeSet ts = TimeSet.of(t, t1);
    TimeSlot t2 = TestUtils.betweenHours(9, 12);
    TimeSlot t3 = TestUtils.betweenHours(15, 22);
    TimeSet ts1 = TimeSet.of(t2, t3);
    TimeSlot t4 = TestUtils.betweenHours(20, 23);
    TimeSet ts2 = TimeSet.of(t4);
    ts.add(ts1, ts2);
    assertEquals(3, ts.getTimeSlots().size());
    assertTrue(ts.getTimeSlots().get(0).equals(TestUtils.betweenHours(15, 23)));
    assertTrue(ts.getTimeSlots().get(1).equals(TestUtils.betweenHours(8, 12)));
    assertTrue(ts.getTimeSlots().get(2).equals(TestUtils.betweenHours(0, 5)));
  }

  @Test
  void subtract_timeSlot() {
    TimeSlot t = TestUtils.betweenHours(0, 5);
    TimeSlot t1 = TestUtils.betweenHours(8, 10);
    TimeSlot t2 = TestUtils.betweenHours(12, 15);
    TimeSet ts = TimeSet.of(t, t1, t2);
    TimeSlot t3 = TestUtils.betweenHours(1, 8);
    TimeSlot t4 = TestUtils.betweenHours(13, 14);
    ts.subtract(t3);
    ts.subtract(t4);
    assertEquals(4, ts.getTimeSlots().size());
    assertTrue(ts.getTimeSlots().get(0).equals(TestUtils.betweenHours(12, 13)));
    assertTrue(ts.getTimeSlots().get(1).equals(TestUtils.betweenHours(14, 15)));
    assertTrue(ts.getTimeSlots().get(2).equals(TestUtils.betweenHours(8, 10)));
    assertTrue(ts.getTimeSlots().get(3).equals(TestUtils.betweenHours(0, 1)));
  }

  @Test
  void subtract_list() {
    TimeSlot t = TestUtils.betweenHours(0, 14);
    TimeSet ts = TimeSet.of(t);
    TimeSlot t1 = TestUtils.betweenHours(1, 5);
    TimeSlot t2 = TestUtils.betweenHours(14, 15);
    TimeSet ts1 = TimeSet.of(t1, t2);
    TimeSlot t3 = TestUtils.betweenHours(6, 12);
    TimeSet ts2 = TimeSet.of(t3);
    ts.subtract(ts1, ts2);
    assertEquals(3, ts.getTimeSlots().size());
    assertTrue(ts.getTimeSlots().get(0).equals(TestUtils.betweenHours(0, 1)));
    assertTrue(ts.getTimeSlots().get(1).equals(TestUtils.betweenHours(5, 6)));
    assertTrue(ts.getTimeSlots().get(2).equals(TestUtils.betweenHours(12, 14)));
  }

  @Test
  void intersect_timeSlot() {
    TimeSlot t = TestUtils.betweenHours(0, 5);
    TimeSlot t1 = TestUtils.betweenHours(12, 18);
    TimeSet ts = TimeSet.of(t, t1);
    TimeSlot t2 = TestUtils.betweenHours(4, 14);
    ts.intersect(t2);
    assertEquals(2, ts.getTimeSlots().size());
    assertTrue(ts.getTimeSlots().get(0).equals(TestUtils.betweenHours(12, 14)));
    assertTrue(ts.getTimeSlots().get(1).equals(TestUtils.betweenHours(4, 5)));
  }

  @Test
  void intersect_list() {
    TimeSlot t = TestUtils.betweenHours(0, 18);
    TimeSet ts = TimeSet.of(t);
    TimeSlot t1 = TestUtils.betweenHours(1, 5);
    TimeSlot t2 = TestUtils.betweenHours(14, 20);
    TimeSet ts1 = TimeSet.of(t1, t2);
    TimeSlot t3 = TestUtils.betweenHours(2, 3);
    TimeSlot t4 = TestUtils.betweenHours(12, 15);
    TimeSet ts2 = TimeSet.of(t3, t4);
    ts.intersect(ts1, ts2);
    assertEquals(2, ts.getTimeSlots().size());
    assertTrue(ts.getTimeSlots().get(0).equals(TestUtils.betweenHours(14, 15)));
    assertTrue(ts.getTimeSlots().get(1).equals(TestUtils.betweenHours(2, 3)));
    TimeSet ts3 = TimeSet.of(t);
    assertTrue(TimeSet.intersection(Arrays.asList(ts1, ts2, ts3)).equals(ts));
  }

  @Test
  void equals() {
    TimeSlot t = TestUtils.betweenHours(2, 5);
    TimeSlot t1 = TestUtils.betweenHours(12, 15);
    TimeSet ts = TimeSet.of(t, t1);
    TimeSlot t2 = TestUtils.betweenHours(12, 15);
    TimeSlot t3 = TestUtils.betweenHours(2, 5);
    TimeSlot t4 = TestUtils.betweenHours(3, 5);
    TimeSet ts1 = TimeSet.of(t2, t3, t4);
    assertTrue(ts.equals(ts1));
    TimeSlot t5 = TestUtils.betweenHours(13, 16);
    ts1.add(t5);
    assertFalse(ts.equals(ts1));
  }

}
