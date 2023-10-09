package at.ac.tuwien.sepr.assignment.individual.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
class TreeNode implements Serializable {

  TournamentDetailParticipantDto thisParticipant;
  TreeNode[] branches;

  public TreeNode(TournamentDetailParticipantDto thisParticipant) {
    this.thisParticipant = thisParticipant;
    this.branches = new TreeNode[2];
  }
}

public class TournamentStandingsTreeDto implements Serializable {
  @JsonUnwrapped
  TreeNode root;

  public TournamentStandingsTreeDto() {
    root = null;
  }

  // Insert a participant into the binary tree
  public void insert(TournamentDetailParticipantDto thisParticipant) {
    root = insertRec(root, thisParticipant);
  }

  // Helper method to recursively insert a participant into the tree
  private TreeNode insertRec(TreeNode root, TournamentDetailParticipantDto thisParticipant) {
    if (root == null) {
      root = new TreeNode(thisParticipant);
      return root;
    }
    Long left = thisParticipant.entryNumber();
    Long right = root.thisParticipant.entryNumber();
    int comparisonResult = left.compareTo(right);

    if (comparisonResult < 0) {
      root.branches[0] = insertRec(root.branches[0], thisParticipant);
    } else if (comparisonResult > 0) {
      root.branches[1] = insertRec(root.branches[1], thisParticipant);
    }

    return root;
  }


  // Inorder traversal of the binary tree (prints the participants in order)
  public void inorderTraversal() {
    inorderTraversalRec(root);
  }

  private void inorderTraversalRec(TreeNode root) {
    if (root != null) {
      inorderTraversalRec(root.branches[0]);
      System.out.print(root.thisParticipant + " ");
      inorderTraversalRec(root.branches[1]);
    }
  }
}
