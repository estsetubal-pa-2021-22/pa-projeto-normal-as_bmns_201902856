package pt.pa.graph;

import java.util.*;

public class GraphAdjacencyList<V,E> implements Graph<V, E> {

    private Map<V, Vertex<V>> vertices;

    public GraphAdjacencyList() {
        this.vertices = new HashMap<>();
    }

    @Override
    public boolean areAdjacent(Vertex<V> u, Vertex<V> v) throws InvalidVertexException {
        MyVertex myU = checkVertex(u);
        MyVertex myV = checkVertex(v);

        //is there a common edge between myU.incidentEdges and myV.incidentEdges ?

        Set<Edge<E,V>> intersection = new HashSet<>(myU.incidentEdges);
        intersection.retainAll(myV.incidentEdges);

        return !intersection.isEmpty();
    }

    @Override
    public int numVertices() {
        return vertices.size();
    }

    @Override
    public int numEdges() {
        return edges().size();
    }

    @Override
    public Collection<Vertex<V>> vertices() {
        return vertices.values();
    }

    @Override
    public Collection<Edge<E, V>> edges() {
        Set<Edge<E,V>> intersection = new HashSet<>();
        for (Vertex<V> ve:vertices()) {
                intersection.addAll(incidentEdges(ve));
        }
        return intersection;
    }

    @Override
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> v) throws InvalidVertexException {
        MyVertex myVertex = checkVertex(v);
        return myVertex.incidentEdges;
    }

    @Override
    public Vertex<V> opposite(Vertex<V> v, Edge<E, V> e) throws InvalidVertexException, InvalidEdgeException {
        MyVertex myVertex = checkVertex(v);
        MyEdge myEdge = checkEdge(e);
        if(myVertex.incidentEdges.contains(myEdge)){
            for (Vertex<V> ve: myEdge.vertices()) {
                if(myVertex != (MyVertex) ve){
                    return ve;
                }
            }
            if (myEdge.vertices()[1] == myEdge.vertices()[0]){
                return myEdge.vertices()[0];
            }
        }
        return null;
    }

    @Override
    public Vertex<V> insertVertex(V vElement) throws InvalidVertexException {
        if(vertices.containsKey(vElement)){
            throw new InvalidVertexException("Vertex "+ vElement + " already exists");
        }

        MyVertex v = new MyVertex(vElement);
        vertices.put(vElement,v);
        return v;
    }

    @Override
    public Edge<E, V> insertEdge(Vertex<V> u, Vertex<V> v, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        MyVertex myU =checkVertex(u);
        MyVertex myV =checkVertex(v);
        //TODO: Fix the checks for throwing the exceptions
        if(myU.incidentEdges.contains(edgeElement)){
            throw new InvalidEdgeException("Edge already exists");
        }

        if(myV.incidentEdges.contains(edgeElement)){
            throw new InvalidEdgeException("Edge already exists");
        }

        MyEdge e = new MyEdge(edgeElement);
        myU.incidentEdges.add(e);
        myV.incidentEdges.add(e);
        return e;
    }

    @Override
    public Edge<E, V> insertEdge(V vElement1, V vElement2, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        if(!vertices.containsKey(vElement1)){
            throw new InvalidVertexException("Vertex "+ vElement1 + " doesn't exists");
        }
        if(!vertices.containsKey(vElement2)){
            throw new InvalidVertexException("Vertex "+ vElement2 + " doesn't exists");
        }
        Vertex<V> v1 = vertices.get(vElement1);
        Vertex<V> v2 = vertices.get(vElement2);
        return insertEdge(v1, v2,edgeElement);
    }

    @Override
    public V removeVertex(Vertex<V> v) throws InvalidVertexException {
        MyVertex myV = checkVertex(v);
        for (Edge<E,V> ed:myV.incidentEdges) {
            removeEdge(ed);
        }
        vertices.remove(myV);
        return myV.element;
    }

    @Override
    public E removeEdge(Edge<E, V> e) throws InvalidEdgeException {
        MyEdge myE = checkEdge(e);
        for (Vertex<V> ve:myE.vertices()) {
            MyVertex vert = (MyVertex) ve;
            vert.incidentEdges.remove(myE);
        }
        return myE.element;
    }

    @Override
    public V replace(Vertex<V> v, V newElement) throws InvalidVertexException {
        if (vertices.containsKey(newElement)){
            throw new InvalidVertexException("Already Exists an Vertex with this element");
        }
        MyVertex vertex = checkVertex(v);
        V oldElement = vertex.element();
        vertex.element = newElement;
        return oldElement;
    }
    //TODO - because of For
    @Override
    public E replace(Edge<E, V> e, E newElement) throws InvalidEdgeException {
        MyEdge edges = checkEdge(e);
        for (Edge<E, V> edge : edges()) {
            if (edge.element().equals(newElement)){
                throw new InvalidVertexException("Already Exists an Edge with this element");
            }
        }
        E oldElement = edges.element;
        edges.element = newElement;
        return oldElement;
    }

    private class MyVertex implements Vertex<V> {
        private V element;
        private List<Edge<E,V>> incidentEdges;

        public MyVertex(V element) {
            this.element = element;
            this.incidentEdges = new ArrayList<>();
        }

        @Override
        public V element() {
            return element;
        }

        @Override
        public String toString() {
            return "Vertex{" + element + '}' + " --> " + incidentEdges.toString();
        }
    }

    private class MyEdge implements Edge<E, V> {
        private E element;

        public MyEdge(E element) {
            this.element = element;
        }

        @Override
        public E element() {
            return element;
        }

        @Override
        public Vertex<V>[] vertices() {
            //if the edge exists, then two existing vertices have the edge
            //in their incidentEdges lists
            List<Vertex<V>> adjacentVertices = new ArrayList<>();

            for(Vertex<V> v : GraphAdjacencyList.this.vertices.values()) {
                MyVertex myV = (MyVertex) v;

                if( myV.incidentEdges.contains(this)) {
                    adjacentVertices.add(v);
                }
            }

            if(adjacentVertices.isEmpty()) {
                return new Vertex[]{null, null}; //edge was removed meanwhile
            } else {
                if(adjacentVertices.size() == 1){
                    return new Vertex[]{adjacentVertices.get(0), adjacentVertices.get(0)};
                }else{
                    return new Vertex[]{adjacentVertices.get(0), adjacentVertices.get(1)};}
            }
        }

        @Override
        public String toString() {
            return "Edge{" + element + "}";
        }
    }

    private MyVertex checkVertex(Vertex<V> v) throws InvalidVertexException {
        if(v == null) throw new InvalidVertexException("Null vertex.");

        MyVertex vertex;
        try {
            vertex = (MyVertex) v;
        } catch (ClassCastException e) {
            throw new InvalidVertexException("Not a vertex.");
        }

        if (!vertices.containsValue(v)) {
            throw new InvalidVertexException("Vertex does not belong to this graph.");
        }

        return vertex;
    }

    private MyEdge checkEdge(Edge<E, V> e) throws InvalidEdgeException {
        if(e == null) throw new InvalidEdgeException("Null edge.");

        MyEdge edge;
        try {
            edge = (MyEdge) e;
        } catch (ClassCastException ex) {
            throw new InvalidVertexException("Not an edge.");
        }

        if (!edges().contains(edge)) {
            throw new InvalidEdgeException("Edge does not belong to this graph.");
        }

        return edge;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph | Adjacency List : \n");

        for(Vertex<V> v : vertices.values()) {
            sb.append( String.format("%s", v) );
            sb.append("\n");
        }

        return sb.toString();
    }
}