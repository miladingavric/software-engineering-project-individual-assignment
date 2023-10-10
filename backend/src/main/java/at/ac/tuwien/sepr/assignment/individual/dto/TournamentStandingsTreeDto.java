package at.ac.tuwien.sepr.assignment.individual.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TournamentStandingsTreeDto implements Serializable {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonUnwrapped
  TreeNodeDto root;

  public TournamentStandingsTreeDto() {
    root = null;
  }
  public TreeNodeDto getRoot() {
    return root;
  }

  public void setRoot(TreeNodeDto root) {
    this.root = root;
  }

  // Insert a participant into the binary tree
  public void insert(TournamentDetailParticipantDto winner,
                     TournamentDetailParticipantDto finalist1, TournamentDetailParticipantDto finalist2,
                     TournamentDetailParticipantDto halfl1, TournamentDetailParticipantDto halfl2, TournamentDetailParticipantDto halfl3, TournamentDetailParticipantDto halfl4,
                     TournamentDetailParticipantDto quarter1, TournamentDetailParticipantDto quarter2, TournamentDetailParticipantDto quarter3, TournamentDetailParticipantDto quarter4,
                     TournamentDetailParticipantDto quarter5, TournamentDetailParticipantDto quarter6, TournamentDetailParticipantDto quarter7, TournamentDetailParticipantDto quarter8)

  {
    root = new TreeNodeDto(winner);
    root.branches[0] = new TreeNodeDto(finalist1);
    root.branches[1] = new TreeNodeDto(finalist2);

    root.branches[0].branches[0] = new TreeNodeDto(halfl1);
    root.branches[0].branches[1] = new TreeNodeDto(halfl2);
    root.branches[1].branches[0] = new TreeNodeDto(halfl3);
    root.branches[1].branches[1] = new TreeNodeDto(halfl4);

    root.branches[0].branches[0].branches[0] = new TreeNodeDto(quarter1);
    root.branches[0].branches[0].branches[1] = new TreeNodeDto(quarter2);
    root.branches[0].branches[1].branches[0] = new TreeNodeDto(quarter3);
    root.branches[0].branches[1].branches[1] = new TreeNodeDto(quarter4);
    root.branches[1].branches[0].branches[0] = new TreeNodeDto(quarter5);
    root.branches[1].branches[0].branches[1] = new TreeNodeDto(quarter6);
    root.branches[1].branches[1].branches[0] = new TreeNodeDto(quarter7);
    root.branches[1].branches[1].branches[1] = new TreeNodeDto(quarter8);

  }



  @Override
  public String toString() {
    return toString(this.root, "", true);
  }

  private String toString(TreeNodeDto node, String prefix, boolean isTail) {
    StringBuilder builder = new StringBuilder();

    if(node.branches[1]!=null) {
      builder.append(toString(node.branches[1], prefix + (isTail ? "│   " : "    "), false));
    }

    builder.append(prefix + (isTail ? "└── " : "┌── ") + node.thisParticipant.name() + "\n");

    if(node.branches[0]!=null) {
      builder.append(toString(node.branches[0], prefix + (isTail ? "    " : "│   "), true));
    }

    return builder.toString();
  }
}
