package at.ac.tuwien.sepr.assignment.individual.entity;
import java.time.LocalDate;

/**
 * Represents a tournament in the persistent data store.
 */
public class Tournament {

  private Long id;
  private String name;
  private String tournamentName;
  private LocalDate startDate;
  private LocalDate endDate;

  public Long getId() {
    return id;
  }

  public String getTournamentName() {
      return tournamentName;
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

  public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
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
