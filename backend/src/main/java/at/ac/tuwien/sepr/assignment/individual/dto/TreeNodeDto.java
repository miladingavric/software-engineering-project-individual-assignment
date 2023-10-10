package at.ac.tuwien.sepr.assignment.individual.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeNodeDto implements Serializable {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  TournamentDetailParticipantDto thisParticipant;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  TreeNodeDto[] branches;

  public TreeNodeDto(TournamentDetailParticipantDto thisParticipant) {
    this.thisParticipant = thisParticipant;
    this.branches = new TreeNodeDto[2];
  }


  public TournamentDetailParticipantDto getThisParticipant() {
    return thisParticipant;
  }

  public void setThisParticipant(TournamentDetailParticipantDto thisParticipant) {
    this.thisParticipant = thisParticipant;
  }
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public TreeNodeDto[] getBranches() {
    return branches;
  }

  public void setBranches(TreeNodeDto[] branches) {
    this.branches = branches;
  }

}