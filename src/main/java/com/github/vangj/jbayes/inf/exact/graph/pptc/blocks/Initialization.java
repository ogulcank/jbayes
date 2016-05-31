package com.github.vangj.jbayes.inf.exact.graph.pptc.blocks;

import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;
import com.github.vangj.jbayes.inf.exact.graph.pptc.Clique;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;

import static com.github.vangj.jbayes.inf.exact.graph.util.PotentialUtil.getPotential;
import static com.github.vangj.jbayes.inf.exact.graph.util.PotentialUtil.multiply;

/**
 * Step 4. Assigns initial join-tree potentials using the conditional probabilities
 * from the belief network.
 */
public class Initialization {
  private Initialization() {

  }

  public static void initialization(JoinTree joinTree) {
    joinTree.allCliques().forEach(clique -> {
      Potential potential = getPotential(clique.nodes());
      joinTree.addPotential(clique, potential);
    });

    joinTree.nodes().forEach(node -> {
      Clique clique = (null == node.getMetadata("parent.clique"))
          ? joinTree.cliquesContainingNodeAndParents(node).get(0)
          : (Clique)node.getMetadata("parent.clique");

      node.addMetadata("parent.clique", clique);

      Potential p1 = joinTree.getPotential(clique);
      Potential p2 = node.getPotential();

      multiply(p1, p2);
    });

    //set likelihood to 1
    joinTree.nodes().forEach(node -> {
      node.getValues().forEach(value -> {
        joinTree.setEvidence(node, value, 1.0d);
      });
    });

    //observation entry
    joinTree.nodes().forEach(node -> {
      node.getValues().forEach(value -> {
        Clique clique = (Clique)node.getMetadata("parent.clique");
        Potential cliquePotential = joinTree.getPotential(clique);
        Potential nodePotential = joinTree.getEvidence(node, value);
        multiply(cliquePotential, nodePotential);
      });
    });
  }
}
