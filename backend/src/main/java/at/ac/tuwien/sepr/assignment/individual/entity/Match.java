package at.ac.tuwien.sepr.assignment.individual.entity;

/**
 * Represents a match in the persistent data store.
 */
public class Match {

  private Long id;
  private Long tournamentID;
  private Long matchRound;
  private Long horseID1;
  private Long horseID2;
  private Long winnerHorseID;

  public Long getId() {
    return id;
  }

  public Match setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getTournamentID() {
    return tournamentID;
  }

  public Match setTournamentID(Long tournamentID) {
    this.tournamentID = tournamentID;
    return this;
  }

  public Long getMatchRound() {
    return matchRound;
  }

  public Match setMatchRound(Long matchRound) {
    this.matchRound = matchRound;
    return this;
  }

  public Long getHorseID1() {
    return horseID1;
  }

  public Match setHorseID1(Long horseID1) {
    this.horseID1 = horseID1;
    return this;
  }

  public Long getHorseID2() {
    return horseID2;
  }

  public Match setHorseID2(Long horseID2) {
    this.horseID2 = horseID2;
    return this;
  }

  public Long getWinnerHorseID() {
    return winnerHorseID;
  }

  public Match setWinnerHorseID(Long winnerHorseID) {
    this.winnerHorseID = winnerHorseID;
    return this;
  }
}
