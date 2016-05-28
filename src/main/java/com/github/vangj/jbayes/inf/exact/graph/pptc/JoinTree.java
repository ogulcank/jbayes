package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.util.NodeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JoinTree {
  private Map<String, Clique> cliques;
  private Map<String, Set<Clique>> neighbors;
  private Set<Edge> edges;

  public JoinTree() {
    this(new ArrayList<>());
  }

  public JoinTree(List<Clique> cliques) {
    this.cliques = new HashMap<>();
    neighbors = new HashMap<>();
    edges = new LinkedHashSet<>();

    for(Clique clique : cliques) {
      addClique(clique);
    }
  }

  public Set<Clique> neighbors(Clique clique) {
    return neighbors.get(clique.id());
  }

  public Set<Clique> neighbors(Node... nodes) {
    final String id = NodeUtil.id(Arrays.asList(nodes));
    return neighbors.get(id);
  }

  public List<Clique> cliques() {
    return cliques.values().stream().collect(Collectors.toList());
  }

  public List<Edge> edges() {
    return edges.stream().collect(Collectors.toList());
  }

  public JoinTree addClique(Clique clique) {
    final String id = clique.id();
    if(cliques.containsKey(id)) {
      cliques.put(id, clique);
    }
    return this;
  }

  public JoinTree addEdge(Edge edge) {
    addClique(edge.left).addClique(edge.right);
    if(!edges.contains(edge)) {
      edges.add(edge);

      final String id1 = edge.left.id();
      final String id2 = edge.right.id();

      Set<Clique> ne1 = neighbors.get(id1);
      Set<Clique> ne2 = neighbors.get(id2);

      if(null == ne1) {
        ne1 = new LinkedHashSet<>();
        neighbors.put(id1, ne1);
      }

      if(null == ne2) {
        ne2 = new LinkedHashSet<>();
        neighbors.put(id2, ne2);
      }

      if(!ne1.contains(edge.right)) {
        ne1.add(edge.right);
      }

      if(!ne2.contains(edge.left)) {
        ne2.add(edge.left);
      }
    }
    return this;
  }

  @Override
  public String toString() {
    return (new StringBuilder())
        .append(String.join(
            System.lineSeparator(),
            cliques().stream().map(Clique::toString).collect(Collectors.toList())))
        .append(System.lineSeparator())
        .append(String.join(System.lineSeparator(),
            edges().stream().map(Edge::toString).collect(Collectors.toList())))
        .toString();
  }
}
