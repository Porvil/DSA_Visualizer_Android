package com.iiitd.dsavisualizer.datastructures.graphs;

public class Edge {

    public int src;
    public int des;
    public double weight;

    public Edge(int src, int des) {
        this.src = src;
        this.des = des;
        this.weight = 1;
    }

    public Edge(int src, int des, double weight) {
        this.src = src;
        this.des = des;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "src=" + src +
                ", des=" + des +
                ", weight=" + weight +
                '}';
    }
}