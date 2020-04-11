import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class TimeSlotTest {

  @Test
  void of_dateTimes() {
    TimeSlot t = TimeSlot.of(TestUtils.dateAtHour(0), TestUtils.dateAtHour(1));
    assertNotNull(t);
  }

  @Test
  void of_startAndDuration() {
    TimeSlot t = TimeSlot.of(TestUtils.dateAtHour(0), 1, ChronoUnit.HOURS);
    assertNotNull(t);
  }

  @Test
  void of_date() {
    TimeSlot t = TimeSlot.of(LocalDate.of(2020, 1, 1));
    assertNotNull(t);
  }

  @Test
  void untilNow() {
    TimeSlot t = TimeSlot.untilNow(TestUtils.dateAtHour(0));
    assertNotNull(t);
  }

  @Test
  void setStart() {
    TimeSlot t = TestUtils.betweenHours(0, 2);
    TimeSlot t1 = t.setStart(TestUtils.dateAtHour(1));
    assertEquals(t1.getStart(),  TestUtils.dateAtHour(1));
    assertEquals(t1.getEnd(),  TestUtils.dateAtHour(2));
  }

  @Test
  void setEnd() {
    TimeSlot t = TestUtils.betweenHours(0, 2);
    TimeSlot t1 = t.setEnd(TestUtils.dateAtHour(1));
    assertEquals(t1.getStart(),  TestUtils.dateAtHour(0));
    assertEquals(t1.getEnd(),  TestUtils.dateAtHour(1));
  }

  @Test
  void shift() {
    TimeSlot t = TestUtils.betweenHours(1, 2);
    TimeSlot t1 = t.shift(1, ChronoUnit.HOURS);
    assertEquals(t1.getStart(),  TestUtils.dateAtHour(2));
    assertEquals(t1.getEnd(),  TestUtils.dateAtHour(3));
  }

  @Test
  void shiftEnd() {
    TimeSlot t = TestUtils.betweenHours(1, 2);
    TimeSlot t1 = t.shiftEnd(1, ChronoUnit.HOURS);
    assertEquals(t1.getStart(),  TestUtils.dateAtHour(1));
    assertEquals(t1.getEnd(),  TestUtils.dateAtHour(3));
  }

  @Test
  void shiftStart() {
    TimeSlot t = TestUtils.betweenHours(1, 3);
    TimeSlot t1 = t.shiftStart(1, ChronoUnit.HOURS);
    assertEquals(t1.getStart(),  TestUtils.dateAtHour(2));
    assertEquals(t1.getEnd(),  TestUtils.dateAtHour(3));
  }

  @Test
  void contains_date() {
    TimeSlot t = TestUtils.betweenHours(0, 3);
    LocalDateTime dt = TestUtils.dateAtHour(1);
    assertTrue(t.contains(dt));
    dt = TestUtils.dateAtHour(3);
    assertFalse(t.contains(dt));
    dt = TestUtils.dateAtHour(0);
    assertFalse(t.contains(dt));
  }

  @Test
  void compareTo_date() {
    TimeSlot t = TestUtils.betweenHours(1, 3);
    LocalDateTime dt = TestUtils.dateAtHour(2);
    assertEquals(0, t.compareTo(dt));
    dt = TestUtils.dateAtHour(3);
    assertEquals(-1, t.compareTo(dt));
    dt = TestUtils.dateAtHour(1);
    assertEquals(1, t.compareTo(dt));
  }

  @Test
  void length() {
    TimeSlot t = TestUtils.betweenHours(1, 3);
    assertEquals(2, t.length(ChronoUnit.HOURS));
  }

  @Test
  void compareLength() {
    TimeSlot t = TestUtils.betweenHours(1, 3);
    TimeSlot t1 = TestUtils.betweenHours(1, 3);
    assertEquals(0, t.compareLength(t1));
    t = TestUtils.betweenHours(1, 3);
    t1 = TestUtils.betweenHours(1, 4);
    assertEquals(-1, t.compareLength(t1));
    t = TestUtils.betweenHours(1, 3);
    t1 = TestUtils.betweenHours(1, 2);
    assertEquals(1, t.compareLength(t1));

  }

  @Test
  void equals() {
    TimeSlot t = TestUtils.betweenHours(1, 3);
    TimeSlot t1 = TestUtils.betweenHours(1, 3);
    assertTrue(t.equals(t1));
  }

  @Test
  void contains_timeSlot() {
    TimeSlot t = TestUtils.betweenHours(0, 3);
    TimeSlot t1 = TestUtils.betweenHours(1, 2);
    assertTrue(t.contains(t1));
    assertFalse(t1.contains(t));
    t = TestUtils.betweenHours(1, 3);
    t1 = TestUtils.betweenHours(1, 2);
    assertTrue(t.contains(t1));
    assertFalse(t1.contains(t));
    t = TestUtils.betweenHours(0, 2);
    t1 = TestUtils.betweenHours(1, 2);
    assertTrue(t.contains(t1));
    assertFalse(t1.contains(t));
    t = TestUtils.betweenHours(1, 2);
    t1 = TestUtils.betweenHours(1, 2);
    assertFalse(t.contains(t1));
    assertFalse(t1.contains(t));
  }

  @Test
  void overlaps() {
    TimeSlot t = TestUtils.betweenHours(1, 3);
    TimeSlot t1 = TestUtils.betweenHours(1, 3);
    assertTrue(t.overlaps(t1));
    t = TestUtils.betweenHours(1, 3);
    t1 = TestUtils.betweenHours(2, 4);
    assertTrue(t.overlaps(t1));
    t = TestUtils.betweenHours(1, 3);
    t1 = TestUtils.betweenHours(0, 3);
    assertTrue(t.overlaps(t1));
    t = TestUtils.betweenHours(1, 3);
    t1 = TestUtils.betweenHours(0, 1);
    assertFalse(t.overlaps(t1));
    t = TestUtils.betweenHours(1, 3);
    t1 = TestUtils.betweenHours(4, 5);
    assertFalse(t.overlaps(t1));
  }

  @Test
  void add() {
    TimeSlot t = TestUtils.betweenHours(1, 3);
    TimeSlot t1 = TestUtils.betweenHours(4, 6);
    TimeSlot t2 = TestUtils.betweenHours(5, 7);
    List<TimeSlot> addition = t.add(t1, t2);
    assertTrue(addition.get(0).equals(t));
    assertTrue(addition.get(1).equals(TestUtils.betweenHours(4, 7)));
    addition = TimeSlot.sum(Arrays.asList(t, t1, t2));
    assertTrue(addition.get(0).equals(t));
    assertTrue(addition.get(1).equals(TestUtils.betweenHours(4, 7)));
    TimeSlot t3 = TestUtils.betweenHours(2, 5);
    addition = t.add(t1, t2, t3);
    assertTrue(addition.get(0).equals(TestUtils.betweenHours(1, 7)));
  }

  @Test
  void subtract() {
    TimeSlot t = TestUtils.betweenHours(1, 8);
    TimeSlot t1 = TestUtils.betweenHours(0, 2);
    TimeSlot t2 = TestUtils.betweenHours(3, 4);
    TimeSlot t3 = TestUtils.betweenHours(3, 4);
    TimeSlot t4 = TestUtils.betweenHours(6, 7);
    List<TimeSlot> subtraction = t.subtract(t1, t2, t3, t4);
    assertTrue(subtraction.get(0).equals(TestUtils.betweenHours(2, 3)));
    assertTrue(subtraction.get(1).equals(TestUtils.betweenHours(4, 6)));
    assertTrue(subtraction.get(2).equals(TestUtils.betweenHours(7, 8)));
    t = TestUtils.betweenHours(1, 7);
    t1 = TestUtils.betweenHours(1, 7);
    assertEquals(0, t.subtract(t1).size());
  }

  @Test
  void intersect() {
    TimeSlot t = TestUtils.betweenHours(1, 5);
    TimeSlot t1 = TestUtils.betweenHours(2, 7);
    TimeSlot intersection = t.intersect(t1);
    assertTrue(intersection.equals(TestUtils.betweenHours(2, 5)));
    intersection = TimeSlot.intersection(Arrays.asList(t, t1));
    assertTrue(intersection.equals(TestUtils.betweenHours(2, 5)));
    t = TestUtils.betweenHours(1, 7);
    t1 = TestUtils.betweenHours(1, 7);
    assertTrue(t.intersect(t1).equals(t));
    t = TestUtils.betweenHours(1, 5);
    t1 = TestUtils.betweenHours(4, 7);
    TimeSlot t2 = TestUtils.betweenHours(0, 3);
    assertNull(t.intersect(t1, t2));
    t = TestUtils.betweenHours(0, 7);
    t1 =TestUtils.betweenHours(3, 5);
    t2 = TestUtils.betweenHours(4, 6);
    assertTrue(t.intersect(t1, t2).equals(TestUtils.betweenHours(4, 5)));
  }
}