import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TimeSlot represents a period of time with a defined start and end. TimeSlots are immutable.
 * This is a helper class written for personal use and carries no guarantees or restrictions.
 *
 * Lucas Gauk - 2020
 */
public final class TimeSlot {

  /**
   * The start of the time slot.
   */
  private final LocalDateTime start;

  /**
   * The end of the time slot.
   */
  private final LocalDateTime end;

  /**
   * Get the start of this time slot.
   * Remains immutable because LocalDateTime is immutable.
   *
   * @return the start of this
   */
  public LocalDateTime getStart() {
    return this.start;
  }

  /**
   * Get the end of this time slot.
   * Remains immutable because LocalDateTime is immutable.
   *
   * @return the end of this
   */
  public LocalDateTime getEnd() {
    return this.end;
  }

  /**
   * Obtains an instance of TimeSlot from a start and an end.
   *
   * @param start the start of the time slot
   * @param end the end of the time slot
   * @return the TimeSlot
   */
  public static TimeSlot of(LocalDateTime start, LocalDateTime end) {
    return new TimeSlot(start, end);
  }

  /**
   * Obtains an instance of TimeSlot from a start, a duration, and a duration unit.
   * The duration unit must be supported by LocalDateTime.
   *
   * @param start the start of the time slot
   * @param duration the duration of the time slot
   * @param unit the unit of the duration
   * @return the TimeSlot
   */
  public static TimeSlot of(LocalDateTime start, long duration, ChronoUnit unit) {
    return TimeSlot.of(start, start.plus(duration, unit));
  }

  /**
   * Returns an instance of TimeSlot that lasts the entirety of a date.
   *
   * @param date the date of the time slot
   * @return the TimeSlot
   */
  public static TimeSlot of(LocalDate date) {
    return TimeSlot.of(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
  }

  /**
   * Returns an instance of TimeSlot that starts at a defined time and ends at the current system time.
   *
   * @param start the start of the time slot
   * @return the TimeSlot
   */
  public static TimeSlot untilNow(LocalDateTime start) {
    return TimeSlot.of(start, LocalDateTime.now());
  }

  /**
   * Returns an instance of TimeSlot from a start and an end.
   * If the end is before the start, it will be the start.
   *
   * @param start the start of the time slot
   * @param end the end of the time slot
   */
  private TimeSlot(LocalDateTime start, LocalDateTime end) {
    if (end.isBefore(start)) {
      this.start = end;
      this.end = start;
    } else {
      this.start = start;
      this.end = end;
    }
  }

  /**
   * Returns a new TimeSlot with the same end as this but a new start.
   *
   * @param start the new start of the time slot
   * @return the TimeSlot
   */
  public TimeSlot setStart(LocalDateTime start) {
    return TimeSlot.of(start, this.end);
  }

  /**
   * Returns a new TimeSlot with the same start as this but a new end.
   *
   * @param end the new end of the time slot
   * @return the TimeSlot
   */
  public TimeSlot setEnd(LocalDateTime end) {
    return TimeSlot.of(this.start, end);
  }

  /**
   * Returns a new TimeSlot with the same duration as this but shifted by a number of units.
   * The unit must be supported by LocalDateTime.
   *
   * @param amountToShift the amount to shift
   * @param unit the unit to shift by
   * @return the TimeSlot
   */
  public TimeSlot shift(long amountToShift, ChronoUnit unit) {
    return new TimeSlot(this.start.plus(amountToShift, unit),
                        this.end.plus(amountToShift, unit));
  }

  /**
   * Returns a new TimeSlot with the end shifted by a number of units.
   * The unit must be supported by LocalDateTime.
   *
   * @param amountToShift the amount to shift
   * @param unit the unit to shift by
   * @return the TimeSlot
   */
  public TimeSlot shiftEnd(long amountToShift, ChronoUnit unit) {
    return new TimeSlot(this.start,
                        this.end.plus(amountToShift, unit));
  }

  /**
   * Returns a new TimeSlot with the start shifted by a number of units.
   * The unit must be supported by LocalDateTime.
   *
   * @param amountToShift the amount to shift
   * @param unit the unit to shift by
   * @return the TimeSlot
   */
  public TimeSlot shiftStart(long amountToShift, ChronoUnit unit) {
    return new TimeSlot(this.start.plus(amountToShift, unit),
                        this.end);
  }

  /**
   * Checks to see if this contains a specific date time.
   *
   * In the case of time slots, a date time that is equal to the start or end
   * of a time slot is not considered to be inside the time slot.
   *
   * @param dateTime the date time to compare to, not null.
   * @return true if the date time is within this, false if it is outside, or equal to one of the ends.
   */
  public boolean contains(LocalDateTime dateTime) {
    return dateTime.isAfter(this.start) && dateTime.isBefore(this.end);
  }

  /**
   * Compares this to a specific date time.
   * Returns 0 if the date time is within this.
   * Returns 1 if the date time is before this starts.
   * Returns -1 if the date time is after this ends.
   *
   * @param dateTime this date time to compare
   * @return 1 if the date time is before, 0 if within, -1 if after
   */
  public int compareTo(LocalDateTime dateTime) {
    if (this.contains(dateTime)) {
      return 0;
    }
    if (dateTime.isBefore(this.start) || dateTime.isEqual(this.start)) {
      return 1;
    }
    return -1;
  }

  /**
   * Returns the length of this in a specified unit.
   * The unit must be supported by LocalDateTime.
   *
   * @param unit the unit to return
   * @return the number of units within this
   */
  public long length(ChronoUnit unit) {
    return this.start.until(this.end, unit);
  }

  /**
   * Compare the length of this to the length of another TimeSlot
   *
   * @param other the other TimeSlot to compare against
   * @return -1 if this is smaller, 0 if it is equal, 1 if this is larger
   */
  public int compareLength(TimeSlot other) {
    return Long.compare(this.length(ChronoUnit.NANOS), other.length(ChronoUnit.NANOS));
  }

  /**
   * Checks if this time slot is equal to another time slot.
   * Compares this TimeSlot with another ensuring that the start and end are the same.
   *
   * @param other the other time slot to compare against
   * @return true if this is equal to the other time slot
   */
  public boolean equals(TimeSlot other) {
    return this.startsAtTheSameTimeAs(other) && this.endsAtTheSameTimeAs(other);
  }

  /**
   * Checks to see if this contains another time slot entirely.
   *
   * @param other the other time slot to compare against
   * @return true if the other time slot starts within this and ends within this
   */
  public boolean contains(TimeSlot other) {
    if (this.equals(other)) {
      return false;
    }
    return (this.contains(other.start) || this.startsAtTheSameTimeAs(other))
        && (this.contains(other.end) || this.endsAtTheSameTimeAs(other));
  }

  /**
   * Returns true if this overlaps another time slot.
   * A time slot does not overlap if it starts at the exact time the other ends, or if it ends at the exact time
   * the other starts.
   *
   * @param other the other time slot to compare against
   * @return true if any part of this is within any part of the other time slot
   */
  public boolean overlaps(TimeSlot other) {
    return !this.checkOverlap(other).equals(OverlapPossibility.NO_OVERLAP);
  }

  /**
   * Returns true if this starts at the same time as another time slot.
   *
   * @param other the other time slot to compare against
   * @return true if this starts at the same time as the other
   */
  private boolean startsAtTheSameTimeAs(TimeSlot other) {
    return this.start.isEqual(other.start);
  }

  /**
   * Returns true if this ends at the same time as another time slot.
   *
   * @param other the other time slot to compare against
   * @return true if this ends at the same time as the other
   */
  private boolean endsAtTheSameTimeAs(TimeSlot other) {
    return this.end.isEqual(other.end);
  }

  /**
   * Returns true if this starts before another time slot but ends within another time slot.
   * It is useful to differentiate this from containing another time slot.
   *
   * Remember that for time slots, a date time that is equal to the start or end of another time slot is not
   * considered to be in that time slot.
   *
   * @param other the other time slot to compare against
   * @return true if this starts before but ends within the other time slot
   */
  private boolean startsBeforeEndsWithin(TimeSlot other) {
    return other.compareTo(this.start) > 0 && other.contains(this.end);
  }

  /**
   * Returns true if this starts within, and ends after, another time slot.
   * It is useful to differentiate this from containing another time slot.
   *
   * Remember that for time slots, a date time that is equal to the start or end of another time slot is not
   * considered to be in that time slot.
   *
   * @param other the other time slot to compare against
   * @return true if this starts within, but ends after, another time slot
   */
  private boolean startsWithinEndsAfter(TimeSlot other) {
    return other.startsBeforeEndsWithin(this);
  }

  /**
   * An enum that lists all of the overlap possibilities when comparing this to another time slot
   */
  private enum OverlapPossibility {
    EQUALS, // Starts and ends at the same time as other
    CONTAINS, // Other is completely within this
    IS_CONTAINED, // This is completely within other
    STARTS_BEFORE_ENDS_WITHIN, // This starts before other and ends within it
    STARTS_WITHIN_ENDS_AFTER, // This starts within other and ends after it
    NO_OVERLAP // This does not overlap other
  }

  /**
   * Returns an OverlapPossibility that represents the kind of overlap that happens when comparing this to
   * another time slot.
   *
   * @param other the other time slot to compare against
   * @return the OverlapPossibility that occurs when comparing this to the other time slot
   */
  private OverlapPossibility checkOverlap(TimeSlot other) {
    if (this.equals(other)) {
      return OverlapPossibility.EQUALS;
    }
    if (this.contains(other)) {
      return OverlapPossibility.CONTAINS;
    }
    if (other.contains(this)) {
      return OverlapPossibility.IS_CONTAINED;
    }
    if (this.startsWithinEndsAfter(other)) {
      return OverlapPossibility.STARTS_WITHIN_ENDS_AFTER;
    }
    if (this.startsBeforeEndsWithin(other)) {
      return OverlapPossibility.STARTS_BEFORE_ENDS_WITHIN;
    }
    return OverlapPossibility.NO_OVERLAP;
  }

  /**
   * Add multiple time slots to this. Consider this operation to yield the resulting combination of this and the other time slots.
   *
   * This is equivalent to combining this and all others with a logical OR operation.
   *
   * @param others other time slots to add to this
   * @return the combination of this and all others
   */
  public List<TimeSlot> add(List<TimeSlot> others) {
    List<TimeSlot> results = new ArrayList<>();
    results.add(this);
    for (TimeSlot other : others) {
      for (TimeSlot overlap : other.overlapsWith(results)) {
        other = other.combineWith(overlap);
        results.remove(overlap);
      }
      results.add(other);
    }
    return results;
  }

  /**
   * Convenience add with varargs. Check the docs for add(List).
   *
   * @param others other time slots to add to this
   * @return the combination of this and all others
   */
  public List<TimeSlot> add(TimeSlot... others) {
    return this.add(Arrays.asList(others));
  }

  /**
   * Sum together a list of time slots. Check docs for add(List).
   *
   * @param timeSlots time slots to sum together
   * @return the combination of all time slots
   */
  public static List<TimeSlot> sum(List<TimeSlot> timeSlots) {
    timeSlots = new ArrayList<>(timeSlots);
    if (timeSlots.size() < 2) {
      return timeSlots;
    }
    TimeSlot firstSlot = timeSlots.remove(0);
    return firstSlot.add(timeSlots);
  }

  /**
   * Combine with another time slot if the other time slot overlaps this.
   *
   * @param other the other time slot to combine with this
   * @return a time slot with the additional time of other in the case that other overlaps this
   */
  private TimeSlot combineWith(TimeSlot other) {
    switch (this.checkOverlap(other)) {
      case IS_CONTAINED:
        return other;
      case STARTS_BEFORE_ENDS_WITHIN:
        return TimeSlot.of(this.start, other.end);
      case STARTS_WITHIN_ENDS_AFTER:
        return TimeSlot.of(other.start, this.end);
      default:
        return this;
    }
  }

  /**
   * Checks to see which time slots overlap this and returns them.
   *
   * @param others the other time slots to check against this
   * @return the list of all others that overlap this
   */
  private List<TimeSlot> overlapsWith(List<TimeSlot> others) {
    return others.stream().filter(other -> other.overlaps(this)).collect(Collectors.toList());
  }

  /**
   * Cuts out any other time slots from this. Returns a list of time slots that represent the remaining time of this
   * that is not overlapped by any of the others.
   *
   * Say this is A and B represents the sum of all others. Then this operation would be
   * equivalent to: A NOT B.
   *
   * @param others the other time slots to remove from this
   * @return a list of time slots that represent the pieces of this that are not overlapped by any of the others
   */
  public List<TimeSlot> subtract(List<TimeSlot> others) {
    List<TimeSlot> results = new ArrayList<>();
    List<TimeSlot> overlaps = this.overlapsWith(others);
    if (overlaps.isEmpty()) {
      results.add(this);
    } else {
      TimeSlot firstOverlap = overlaps.remove(0);
      switch (this.checkOverlap(firstOverlap)) {
        case CONTAINS:
          results.addAll(TimeSlot.of(this.start, firstOverlap.start).subtract(overlaps));
          results.addAll(TimeSlot.of(this.end, firstOverlap.end).subtract(overlaps));
          break;
        case STARTS_BEFORE_ENDS_WITHIN:
          results.addAll(TimeSlot.of(this.start, firstOverlap.start).subtract(overlaps));
          break;
        case STARTS_WITHIN_ENDS_AFTER:
          results.addAll(TimeSlot.of(firstOverlap.end, this.end).subtract(overlaps));
          break;
      }
    }
    return results;
  }

  /**
   * Convenience subtract with varargs. Check docs for subtract(List).
   *
   * @param others the other time slots to remove from this
   * @return a list of time slots that represent the pieces of this that are not overlapped by any of the others
   */
  public List<TimeSlot> subtract(TimeSlot... others) {
    return this.subtract(Arrays.asList(others));
  }

  /**
   * Returns the intersection of this and a list of time slots. Can return null if there is no intersect.
   * If this is A, and the others are B, C, ... n then this is equivalent to A intersect B intersect C ... intersect n.
   *
   * @param others the other time slots to check for intersection
   * @return a TimeSlot that represents the intersection of this and all others, or null if there is no intersect
   */
  public TimeSlot intersect(List<TimeSlot> others) {
    if (others.size() == 0) {
      return null;
    }
    return intersectHelper(new ArrayList<>(others));
  }

  private TimeSlot intersectHelper(List<TimeSlot> others) {
    if (others.size() == 0) {
      return this;
    }
    TimeSlot firstOverlap = others.remove(0);
    switch (this.checkOverlap(firstOverlap)) {
      case STARTS_BEFORE_ENDS_WITHIN:
        return TimeSlot.of(firstOverlap.start, this.end).intersectHelper(others);
      case STARTS_WITHIN_ENDS_AFTER:
        return TimeSlot.of(this.start, firstOverlap.end).intersectHelper(others);
      case CONTAINS:
        return firstOverlap.intersectHelper(others);
      case NO_OVERLAP:
        return null;
      default:
        return this.intersectHelper(others);
    }
  }

  public TimeSlot intersect(TimeSlot ...others) {
    return this.intersect(Arrays.asList(others));
  }
}
