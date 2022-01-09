package pt.pa.filemanaging;

import pt.pa.graph.*;
import pt.pa.model.Hub;
import pt.pa.model.Matrix;
import pt.pa.model.MatrixHashMap;
import pt.pa.model.Route;
import pt.pa.shortcuts.StringFromIterable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FileManager {

    // TODO: Generate graph procedurally and not store the Matrix in memory (risk of OutOfMemoryError). Read by line!
    // TODO: For each city from 0 to n - 1, check routes to the previously read hubs.
    public static Graph<Hub, Route> graphFromFiles(
            String nameFile, String weightFile, String xyFile, String routesFile
    ){
        Matrix<Integer> distances = distanceMatrixFromFile(routesFile);

        List<Hub> indexOfCities = new ArrayList<>();
        Graph<Hub, Route> myGraph = new GraphAdjacencyList<>();

        BufferedReader names = readFrom(nameFile);
        BufferedReader weights = readFrom(weightFile);
        BufferedReader coordinates = readFrom(xyFile);

        String nameLine = null, weightLine = null, coordsLine = null;

        while (true) {
            try {
                if (
                        (nameLine = names.readLine()) == null ||
                        (weightLine = weights.readLine()) == null ||
                        (coordsLine = coordinates.readLine()) == null
                ) {
                    break;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            Hub city = hubFromStrings(nameLine, coordsLine, weightLine);

            indexOfCities.add(city);

            myGraph.insertVertex(city);
        }

        for (int y = 0; y < indexOfCities.size(); y++) {
            for (int x = y; x < indexOfCities.size(); x++) {
                Integer dist = distances.get(x, y);
                if (dist != null) {
                    Hub atX = indexOfCities.get(x);
                    Hub atY = indexOfCities.get(y);
                    myGraph.insertEdge(
                        atX, atY, new Route(atX, atY, dist)
                    );
                }
            }
        }

        return myGraph;
    }

public static void routesToFile(Graph<Hub, Route> g, String filename) {
    PrintWriter pw = printTo(filename);

    if (pw != null) {
        List<Vertex<Hub>> vertices = new ArrayList<>(g.vertices());

        for (Vertex<Hub> v: vertices) {
            Integer[] dist = new Integer[8];
            Arrays.fill(dist, 0);
            //for (int i = 0; i < vertices.size(); i++) dist.add(0);

            ArrayList<Edge<Route, Hub>> incident = new ArrayList<>(g.incidentEdges(v));

            for(Edge<Route, Hub> e: g.incidentEdges(v)) {
                //                   Se v é o índice 0 ?          O oposto é o 1    O oposto é o 0
                Vertex<Hub> other = (e.vertices()[0].equals(v)) ? e.vertices()[1] : e.vertices()[0];
                //dist.set(vertices.indexOf(other), (int)(e.element().getDistance()));
                dist[vertices.indexOf(other)] = (int)(e.element().getDistance());
            }

            pw.println((new StringFromIterable<Integer>()).delimit(" ", Arrays.asList(dist)));
        }

        pw.flush();
        pw.close();
    }
}

    public static Matrix<Integer> distanceMatrixFromFile(String filename) {
        BufferedReader br = readFrom(filename);

        Matrix<Integer> mat = new MatrixHashMap<>();

        String line = "";
        int index = 0;

        while(true) {
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            int[] distanceArray = distancesFromLine(line);
            for(int i = 0; i < distanceArray.length; i++) {
                if (index != i && distanceArray[i] != 0 && i >= index) {
                    mat.put(i, index, distanceArray[i]);
                }
            }
            index++;
        }

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mat;
    }

    public static int[] distancesFromLine(String routesLine) {
        String[] routesSplit = routesLine.split(" ");
        int[] distances = new int[routesSplit.length];

        for (int i = 0; i < routesSplit.length; i++)
            distances[i] = Integer.parseInt(routesSplit[i]);

        return distances;
    }

    public static Hub hubFromStrings(String name, String xy, String weight){
        String[] xySplit = xy.split(" ");
        int x = Integer.parseInt(xySplit[0]);
        int y = Integer.parseInt(xySplit[1]);

        int population = Integer.parseInt(weight);

        return new Hub(name, population, x, y);
    }

    public static BufferedReader readFrom(String filename) {
        File ficheiro = new File(System.getProperty("user.dir") + File.separator + filename);
        System.out.println(System.getProperty("user.dir") + File.separator + filename);
        try {
            FileReader fileReader = new FileReader(ficheiro);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            return bufferedReader;
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static PrintWriter printTo(String filename) {
        File ficheiro = new File(System.getProperty("user.dir") + File.separator + filename);
        System.out.println(System.getProperty("user.dir") + File.separator + filename);
        try {
            return new PrintWriter(new BufferedWriter(new FileWriter(ficheiro)));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
