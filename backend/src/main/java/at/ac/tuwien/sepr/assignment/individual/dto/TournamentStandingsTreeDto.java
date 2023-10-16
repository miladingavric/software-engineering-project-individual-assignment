package at.ac.tuwien.sepr.assignment.individual.dto;
public record TournamentStandingsTreeDto(
    TournamentDetailParticipantDto thisParticipant,
    TournamentStandingsTreeDto[] branches) {
  public TournamentStandingsTreeDto(TournamentDetailParticipantDto thisParticipant) {
    this(thisParticipant, new TournamentStandingsTreeDto[2]);
  }
}
