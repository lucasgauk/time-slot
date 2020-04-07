import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class TimeSlotTest {

  @Test
  void of_dateTimes() {
    TimeSlot t = TimeSlot.of(this.dateAtHour(0), this.dateAtHour(1));
    assertNotNull(t);
  }

  @Test
  void of_startAndDuration() {
    TimeSlot t = TimeSlot.of(this.dateAtHour(0), 1, ChronoUnit.HOURS);
    assertNotNull(t);
  }

  @Test
  void of_date() {
    TimeSlot t = TimeSlot.of(LocalDate.of(2020, 1, 1));
    assertNotNull(t);
  }

  @Test
  void untilNow() {
    TimeSlot t = TimeSlot.untilNow(this.dateAtHour(0));
    assertNotNull(t);
  }

  @Test
  void setStart() {
    TimeSlot t = this.betweenHours(0, 2);
    TimeSlot t1 = t.setStart(this.dateAtHour(1));
    assertEquals(t1.getStart(),  this.dateAtHour(1));
    assertEquals(t1.getEnd(),  this.dateAtHour(2));
  }

  @Test
  void setEnd() {
    TimeSlot t = this.betweenHours(0, 2);
    TimeSlot t1 = t.setEnd(this.dateAtHour(1));
    assertEquals(t1.getStart(),  this.dateAtHour(0));
    assertEquals(t1.getEnd(),  this.dateAtHour(1));
  }

  @Test
  void shift() {
    TimeSlot t = this.betweenHours(1, 2);
    TimeSlot t1 = t.shift(1, ChronoUnit.HOURS);
    assertEquals(t1.getStart(),  this.dateAtHour(2));
    assertEquals(t1.getEnd(),  this.dateAtHour(3));
  }

  @Test
  void shiftEnd() {
    TimeSlot t = this.betweenHours(1, 2);
    TimeSlot t1 = t.shiftEnd(1, ChronoUnit.HOURS);
    assertEquals(t1.getStart(),  this.dateAtHour(1));
    assertEquals(t1.getEnd(),  this.dateAtHour(3));
  }

  @Test
  void shiftStart() {
    TimeSlot t = this.betweenHours(1, 3);
    TimeSlot t1 = t.shiftStart(1, ChronoUnit.HOURS);
    assertEquals(t1.getStart(),  this.dateAtHour(2));
    assertEquals(t1.getEnd(),  this.dateAtHour(3));
  }

  @Test
  void contains_date() {
    TimeSlot t = this.betweenHours(0, 3);
    LocalDateTime dt = this.dateAtHour(1);
    assertTrue(t.contains(dt));
    dt = this.dateAtHour(3);
    assertFalse(t.contains(dt));
    dt = this.dateAtHour(0);
    assertFalse(t.contains(dt));
  }

  @Test
  void compareTo_date() {
    TimeSlot t = this.betweenHours(1, 3);
    LocalDateTime dt = this.dateAtHour(2);
    assertEquals(0, t.compareTo(dt));
    dt = this.dateAtHour(3);
    assertEquals(-1, t.compareTo(dt));
    dt = this.dateAtHour(1);
    assertEquals(1, t.compareTo(dt));
  }

  @Test
  void length() {
    TimeSlot t = this.betweenHours(1, 3);
    assertEquals(2, t.length(ChronoUnit.HOURS));
  }

  @Test
  void compareLength() {
    TimeSlot t = this.betweenHours(1, 3);
    TimeSlot t1 = this.betweenHours(1, 3);
    assertEquals(0, t.compareLength(t1));
    t = this.betweenHours(1, 3);
    t1 = this.betweenHours(1, 4);
    assertEquals(-1, t.compareLength(t1));
    t = this.betweenHours(1, 3);
    t1 = this.betweenHours(1, 2);
    assertEquals(1, t.compareLength(t1));

  }

  @Test
  void equals() {
    TimeSlot t = this.betweenHours(1, 3);
    TimeSlot t1 = this.betweenHours(1, 3);
    assertTrue(t.equals(t1));
  }

  @Test
  void contains_timeSlot() {
    TimeSlot t = this.betweenHours(0, 3);
    TimeSlot t1 = this.betweenHours(1, 2);
    assertTrue(t.contains(t1));
    assertFalse(t1.contains(t));
    t = this.betweenHours(1, 3);
    t1 = this.betweenHours(1, 2);
    assertTrue(t.contains(t1));
    assertFalse(t1.contains(t));
    t = this.betweenHours(0, 2);
    t1 = this.betweenHours(1, 2);
    assertTrue(t.contains(t1));
    assertFalse(t1.contains(t));
    t = this.betweenHours(1, 2);
    t1 = this.betweenHours(1, 2);
    assertFalse(t.contains(t1));
    assertFalse(t1.contains(t));
  }

  @Test
  void overlaps() {
    TimeSlot t = this.betweenHours(1, 3);
    TimeSlot t1 = this.betweenHours(1, 3);
    assertTrue(t.overlaps(t1));
    t = this.betweenHours(1, 3);
    t1 = this.betweenHours(2, 4);
    assertTrue(t.overlaps(t1));
    t = this.betweenHours(1, 3);
    t1 = this.betweenHours(0, 3);
    assertTrue(t.overlaps(t1));
    t = this.betweenHours(1, 3);
    t1 = this.betweenHours(0, 1);
    assertFalse(t.overlaps(t1));
    t = this.betweenHours(1, 3);
    t1 = this.betweenHours(4, 5);
    assertFalse(t.overlaps(t1));
  }

  @Test
  void add() {
    TimeSlot t = this.betweenHours(1, 3);
    TimeSlot t1 = this.betweenHours(4, 6);
    TimeSlot t2 = this.betweenHours(5, 7);
    List<TimeSlot> addition = t.add(t1, t2);
    assertTrue(addition.get(0).equals(t));
    assertTrue(addition.get(1).equals(this.betweenHours(4, 7)));
    addition = TimeSlot.sum(Arrays.asList(t, t1, t2));
    assertTrue(addition.get(0).equals(t));
    assertTrue(addition.get(1).equals(this.betweenHours(4, 7)));
    TimeSlot t3 = this.betweenHours(2, 5);
    addition = t.add(t1, t2, t3);
    assertTrue(addition.get(0).equals(this.betweenHours(1, 7)));
  }

  @Test
  void subtract() {
    TimeSlot t = this.betweenHours(1, 8);
    TimeSlot t1 = this.betweenHours(0, 2);
    TimeSlot t2 = this.betweenHours(3, 4);
    TimeSlot t3 = this.betweenHours(3, 4);
    TimeSlot t4 = this.betweenHours(6, 7);
    List<TimeSlot> subtraction = t.subtract(t1, t2, t3, t4);
    assertTrue(subtraction.get(0).equals(this.betweenHours(2, 3)));
    assertTrue(subtraction.get(1).equals(this.betweenHours(4, 6)));
    assertTrue(subtraction.get(2).equals(this.betweenHours(7, 8)));
    t = this.betweenHours(1, 7);
    t1 = this.betweenHours(1, 7);
    assertEquals(0, t.subtract(t1).size());
  }

  @Test
  void intersect() {
    TimeSlot t = this.betweenHours(1, 5);
    TimeSlot t1 = this.betweenHours(2, 7);
    TimeSlot intersection = t.intersect(t1);
    assertTrue(intersection.equals(this.betweenHours(2, 5)));
    t = this.betweenHours(1, 7);
    t1 = this.betweenHours(1, 7);
    assertTrue(t.intersect(t1).equals(t));
    t = this.betweenHours(1, 5);
    t1 = this.betweenHours(4, 7);
    TimeSlot t2 = this.betweenHours(0, 3);
    assertNull(t.intersect(t1, t2));
    t = this.betweenHours(0, 7);
    t1 =this.betweenHours(3, 5);
    t2 = this.betweenHours(4, 6);
    assertTrue(t.intersect(t1, t2).equals(this.betweenHours(4, 5)));
  }

  private TimeSlot betweenHours(int h1, int h2) {
    return TimeSlot.of(this.dateAtHour(h1), this.dateAtHour(h2));
  }

  private LocalDateTime dateAtHour(int hour) {
    return LocalDateTime.of(2020, 1, 1, hour, 0);
  }
}