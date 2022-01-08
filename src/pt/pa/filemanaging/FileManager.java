package pt.pa.filemanaging;

import pt.pa.graph.*;
import pt.pa.model.Hub;
import pt.pa.model.Matrix;
import pt.pa.model.MatrixHashMap;
import pt.pa.model.Route;

import java.io.*;
import java.util.ArrayList;
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

    public static void graphToFile( Graph<Hub,Route> graph, String routesFile) {
        PrintWriter pw = writeTo(routesFile);

        List<Vertex<Hub>> vertexList = new ArrayList<>(graph.vertices());

        for(Vertex<Hub> vertex:vertexList){
            List<Edge<Route,Hub>> edgeList = new ArrayList<>(graph.incidentEdges(vertex));

            int[] distances = new int[vertexList.size()];
            for(Edge<Route,Hub> incidentEdge:edgeList){
                    Vertex<Hub> oppositeVertex = graph.opposite(vertex, incidentEdge);
                    int indexOppsite = vertexList.indexOf(oppositeVertex);
                    distances[indexOppsite] = (int)incidentEdge.element().getDistance();
            }

            StringBuilder builder = new StringBuilder();
            for (int j=0;j<distances.length;j++){
                builder.append(String.format(" %d",distances[j]));
            }
            pw.println(builder.substring(1));

        }

        pw.flush();
        pw.close();
    }



        public static PrintWriter writeTo(String filename){
        File ficheiro = new File(System.getProperty("user.dir") + File.separator + filename);
        System.out.println(System.getProperty("user.dir") + File.separator + filename);
        try {
            FileWriter fileWriter = new FileWriter(ficheiro);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            return new PrintWriter(bufferedWriter);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
