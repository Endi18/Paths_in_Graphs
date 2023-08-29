# Paths in Graphs


## Introduction

In this assignment, you will be given as input a directed graph (including nodes, edges and their weights) and some designated starting node. You will then compute the shortest path from this starting node to each node in the graph. However, in doing so you will have to determine whether the graph has any cycles and accordingly, use the most efficient algorithm for each case (cyclic or acyclic).

## Definition

You will be given a text representation of the graph in the input text file. The first line consists of two space-separated numbers, n and e, respectively the number of nodes and the number of edges. In the second line you will be given the names of nodes as a list of n space-separated tokens. If the content of this line is “use-indexes”, you can think of the nodes being named with their indexes, i.e. 0, 1, 2 and so on. In the third line you will be given the index of the starting node. Finally e lines will follow, one for each edge, in the format (fromIndex toIndex weight). All said indexes will range [0, 1, ..., n − 1]. The graph parameters are subject to following conditions.

- 1 ≤ n ≤ 10<sup>5</sup>
- 1 ≤ e ≤ 10<sup>4</sup>
- weights can be a <em>non-negative</em> real number!

## Example Input
Consider the input below: </br>
5 9</br>
aqua black cyan daffodil ebony</br>
0</br>
0 1 4</br>
0 2 2</br>
1 2 3</br>
2 1 1</br>
1 3 2</br>
1 4 3</br>
2 3 4</br>
2 4 5</br>
4 3 1</br>

The first lines states that there are five nodes and nine edges. The second line states that the nodes are labeled with corresponding names (0 - aqua, ..., and 4 - ebony). Line 3 states that n<sub>0</sub> is the starting node, i.e. the one labeled as aqua. Line 4 establishes the presence of edge n<sub>0</sub> → n<sub>1</sub>, and its weight as 4, and so on for the remaining 8 lines.</br>
In fact this is the text representation of the graph and it is shown in the picture below (without the color codes). You can further convince yourselves that any graph can be expressed in such a format.</br></br>
<p align="center">
<img src="https://github.com/Endi18/Paths_in_Graphs/assets/85561383/842d5eae-7eaf-4599-be55-2f0c9c19909f">
</p>

## Bonus Consideration

You are asked to compute the shortest paths from the starting node in the given graph. For bonus consideration you can try to compute also the longest paths from the starting node, defined more precisely below.

- If the graph is acyclic, the longest path is unique, compute this path
- If the graph contains cycles, we require the longest path between any two nodes which contains no cycles.
