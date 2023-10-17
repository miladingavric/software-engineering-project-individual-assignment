package at.ac.tuwien.sepr.assignment.individual.entity;
import java.time.LocalDate;

/**
 * Represents a tournament in the persistent data store.
 */
public class Tournament {

  private Long id;
  private String name;
  private LocalDate startDate;
  private LocalDate endDate;
  private Participant[] participants;

  public Participant[] getParticipants() {
    return participants;
  }

  public Tournament setParticipants(Participant[] participants) {
    this.participants = participants;
    return this;
  }

  public Long getId() {
    return id;
  }

  public LocalDate getStartDate() {
    return startDate;
  }
  public LocalDate getEndDate() {
    return endDate;
  }
  public Tournament setId(Long id) {
    this.id = id;
    return this;
  }


  public Tournament setStartDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  public Tournament setEndDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }


  public String getName() {
    return name;
  }

  public Tournament setName(String name) {
    this.name = name;
    return this;
  }
}
