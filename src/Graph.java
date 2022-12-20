import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Graph {
    private int towns;
    private int district;
    private int roads;
    private int [][] roadsArr;
    private String [] temp;
    public int [][] distancesArr;
    private int [] roadBelongings;
    private int[] belongings;



    public Graph() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] header = bufferedReader.readLine().split(" ");

        this.towns = Integer.parseInt(header[0]);
        this.district = Integer.parseInt(header[1]);
        this.roads = Integer.parseInt(header[2]);
        this.roadsArr = new int[roads][3];
        this.distancesArr = new int[district][towns];

        for (int i=0; i<roads; i++){
            temp = bufferedReader.readLine().split(" ");
            roadsArr[i][0] = Integer.parseInt(temp[0])-1;
            roadsArr[i][1] = Integer.parseInt(temp[1])-1;
            roadsArr[i][2] = Integer.parseInt(temp[2]);
        }
        this.roadBelongings = new int[roads];
        Arrays.fill(roadBelongings, -1);
        this.getDistancesArr();
        belongings = this.districtBelongings();

    }

    public int[] getDistances(int district) {
        ArrayList<Integer> queue = new ArrayList<>();
        ArrayList<Integer> visited = new ArrayList<>();
        int [] distances = new int[towns];
        Arrays.fill(distances, Integer.MAX_VALUE);
        queue.add(district);
        distances[district] = 0;
        int vertex = 0;
        while (queue.size() != 0){
            vertex = queue.get(0);
            queue.remove(0);
            for (int i=0; i<roads; i++){
                if (!visited.contains(vertex)){
                    if (vertex == roadsArr[i][0]){
                        queue.add(roadsArr[i][1]);
                        if (distances[roadsArr[i][1]] > distances[roadsArr[i][0]] + roadsArr[i][2]){
                            distances[roadsArr[i][1]] = distances[roadsArr[i][0]] + roadsArr[i][2];
                        }
                    }
                    if (vertex == roadsArr[i][1]){
                        queue.add(roadsArr[i][0]);
                        if (distances[roadsArr[i][0]] > distances[roadsArr[i][1]] + roadsArr[i][2]){
                            distances[roadsArr[i][0]] = distances[roadsArr[i][1]] + roadsArr[i][2];
                        }
                    }
                }
            }

            visited.add(vertex);
        }
        return distances;
    }

    public void getDistancesArr(){
        for (int i=0; i<district; i++){
            distancesArr[i] = getDistances(i);
        }
    }

    public int[] districtBelongings(){
        int [] townBelongings = new int[towns];
        Arrays.fill(townBelongings, 0);
        for (int i=0; i<towns; i++){
            int temp = distancesArr[0][i];
            for (int j=0; j<district; j++){
                if (temp > distancesArr[j][i]){
                    temp = distancesArr[j][i];
                    townBelongings[i] = j;
                }
            }
        }

        for(int i=0; i<roads; i++){
            if(townBelongings[roadsArr[i][0]]==townBelongings[roadsArr[i][1]]){
                roadBelongings[i] = townBelongings[roadsArr[i][0]];
            }
            else {
                roadBelongings[i] = -1;
            }
        }
        return townBelongings;
    }

    public int computePrize(){
        int sum =0;
        ArrayList<Integer> vertexes = new ArrayList<>();
        for (int d=0; d<district; d++){
            vertexes.clear();
            vertexes.add(d);
            int position = 1;

            while (position >= 0){
                int value = Integer.MAX_VALUE;
                int tmp = 0;
                position = -1;
                for (int i=0; i<roads; i++){
                    /*System.out.println(i + ", " +d);
                    System.out.println(vertexes.contains(roadsArr[i][0]) + ", " + roadsArr[i][0]);
                    System.out.println(!vertexes.contains(roadsArr[i][1]));
                    System.out.println(roadsArr[i][2]<value);
                    System.out.println(belongings[roadsArr[i][0]] == d);
                    System.out.println(belongings[roadsArr[i][1]] == d);*/
                    if (vertexes.contains(roadsArr[i][0]) && !vertexes.contains(roadsArr[i][1]) && roadsArr[i][2]<value && belongings[roadsArr[i][0]] == d && belongings[roadsArr[i][1]] == d){
                        position = 1;
                        value = roadsArr[i][2];
                        tmp = roadsArr[i][1];
                    }
                    if (vertexes.contains(roadsArr[i][1]) && !vertexes.contains(roadsArr[i][0]) && roadsArr[i][2]<value && belongings[roadsArr[i][0]] == d && belongings[roadsArr[i][1]] == d){
                        position = 0;
                        value = roadsArr[i][2];
                        tmp = roadsArr[i][0];
                    }
                }

                if (position >= 0){
                    sum += value;
                    vertexes.add(tmp);

                }
            }
        }
        int[][] connected = new int[district][district];
        for (int i=0; i<district; i++){
            for (int j=0; j< district; j++){
                connected[i][j] = Integer.MAX_VALUE;
            }
        }

        for (int i=0; i<roadBelongings.length; i++) {
            if (roadBelongings[i] == -1) {
                if (connected[belongings[roadsArr[i][0]]][belongings[roadsArr[i][1]]] > roadsArr[i][2]){
                    connected[belongings[roadsArr[i][0]]][belongings[roadsArr[i][1]]] = roadsArr[i][2];
                    connected[belongings[roadsArr[i][1]]][belongings[roadsArr[i][0]]] = roadsArr[i][2];
                }
            }
        }

        ArrayList<Integer> que = new ArrayList<>();
        que.add(0);
        int im =0;
        int jm =0;

        while (true){
            int min = Integer.MAX_VALUE;
            int position = -1;
            for (int i=0; i<district; i++){
                for (int j=0; j<district; j++){
                    if ((que.contains(i) || que.contains(j)) && !(que.contains(i) && que.contains(j)) && connected[i][j]<min){
                        position = 1;
                        min = connected[i][j];
                        im = i;
                        jm = j;
                    }

                }
            }
            if (que.contains(im)){
                que.add(jm);
            }else{
                que.add(im);
            }
            if (position >= 0){
                sum += min;
            }
            if (position == -1)break;
        }

        return sum;
    }




}


