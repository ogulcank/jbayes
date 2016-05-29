package com.github.vangj.jbayes.inf.exact.graph.pptc.blocks;

import static com.github.vangj.jbayes.inf.exact.graph.util.PotentialUtil.*;

import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;
import com.github.vangj.jbayes.inf.exact.graph.pptc.Clique;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;

import java.util.List;

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
      List<Clique> cliques = joinTree.getCliquesContaining(node);
      if(cliques.size() > 0) {
        Clique clique = cliques.get(0);
        Potential p1 = joinTree.getPotential(clique);
        Potential p2 = node.getPotential();

        System.out.println("assigned " + node.getId() + " to " + clique.id());

        if(null != p1 && null != p2) {
          multiply(p1, p2);
          System.out.println(p1);
        } else {
          //hrmm.. why wasn't couldn't we get the potentials?
        }
      } else {
        //hrmmm.. why couldn't we find a clique containing the node?
      }
    });
  }


}
