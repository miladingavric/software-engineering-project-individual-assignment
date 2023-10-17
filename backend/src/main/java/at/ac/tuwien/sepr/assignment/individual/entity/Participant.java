package at.ac.tuwien.sepr.assignment.individual.entity;

import java.time.LocalDate;

public class Participant implements Comparable<Participant> {

  long id;
  String name;
  LocalDate dateOfBirth;
  long entryNumber;
  long roundReached;

  public long getId() {
    return id;
  }

  public Participant setId(long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Participant setName(String name) {
    this.name = name;
    return this;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public Participant setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  public long getEntryNumber() {
    return entryNumber;
  }

  public Participant setEntryNumber(long entryNumber) {
    this.entryNumber = entryNumber;
    return this;
  }

  public long getRoundReached() {
    return roundReached;
  }

  public Participant setRoundReached(long roundReached) {
    this.roundReached = roundReached;
    return this;
  }

  @Override
  public String toString() {
    return "Horse{"
        + "id=" + id
        + ", name='" + name + '\''
        + ", dateOfBirth=" + dateOfBirth
        + ", entry number=" + entryNumber
        + ", round reached=" + roundReached
        + '}';
  }

  @Override
  public int compareTo(Participant o) {
    return o.getName().compareTo(this.getName());
  }
}
