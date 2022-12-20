package pal;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class GraphNew {

    private int towns;
    private int district;
    private int roads;
    private int [][] roadsArr;
    private String [] temp;
    public int [][] distancesArr;
    private int[] roadBelonging;
    private int[] townsBelongings;


    public GraphNew() throws IOException {
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
        this.townsBelongings = this.getTownsBelongings();
        this.roadBelonging = new int[roads];
        this.getRoadBelonging();
    }

    public int[] getNumberOfRoadsFromDistrict(int district){
        boolean again = true;
        int [] dist = new int[towns];
        Arrays.fill(dist, 0);
        ArrayList<Integer> que = new ArrayList<>();
        que.add(district);
        ArrayList<Integer> toAdd = new ArrayList<>();
        while (again){
            again = false;
            for (int i=0; i<roads;i++){
                if (que.contains(roadsArr[i][0]) && !que.contains(roadsArr[i][1])){
                    dist[roadsArr[i][1]] = dist[roadsArr[i][0]] + 1;
                    toAdd.add(roadsArr[i][1]);
                    again = true;
                }
                if (que.contains(roadsArr[i][1]) && !que.contains(roadsArr[i][0])){
                    dist[roadsArr[i][0]] = dist[roadsArr[i][1]] + 1;
                    toAdd.add(roadsArr[i][0]);
                    again = true;
                }
            }
            for (int i=0; i<toAdd.size(); i++){
                que.add(toAdd.get(i));
            }
        }
        return dist;
    }
    public int[] getTownsBelongings(){
        int[][] districts = new int[district][towns];
        int [] res = new int[towns];
        for (int i =0; i<district; i++){
            districts[i] = getNumberOfRoadsFromDistrict(i);
        }
        for (int i=0; i<towns; i++){
            int temp = districts[0][i];
            int index = 0;
            for (int j=0; j<district; j++){
                if(districts[j][i]<temp){
                    temp = districts[j][i];
                    index = j;
                }
            }
            res[i] = index;
        }
        return res;
    }
    public void getRoadBelonging(){
        for (int r=0; r<roads;r++){
            int first = -2;
            int second = -2;
            for (int n=0; n<towns; n++){
                if (roadsArr[r][0] == n){
                    first = townsBelongings[n];
                }
                if (roadsArr[r][1] == n){
                    second = townsBelongings[n];
                }
            }
            if (first == second){
                roadBelonging[r] = first;
            }else {
                roadBelonging[r] = -1;
            }
        }
    }
    public int getSumOfDistrict(int district){
        ArrayList<Integer> vertexes = new ArrayList<>();
        vertexes.add(district);
        int position = 1;
        int sum = 0;

        while (position>=0){
            int value = Integer.MAX_VALUE;
            int tmp = 0;
            position = -1;
            for (int i=0; i<roads; i++){
                /*System.out.println("d = "+district+", i = "+ i);
                System.out.println(vertexes.contains(roadsArr[i][0]));
                System.out.println(!vertexes.contains(roadsArr[i][1]));
                System.out.println(roadsArr[i][2] +", "+value);
                System.out.println(roadsArr[i][0] == district);
                System.out.println(roadsArr[i][1] == district);*/
                if (vertexes.contains(roadsArr[i][0]) && !vertexes.contains(roadsArr[i][1]) && roadsArr[i][2]<value && roadBelonging[i] == district){
                    position = 1;
                    value = roadsArr[i][2];
                    tmp = roadsArr[i][1];
                }
                if (vertexes.contains(roadsArr[i][1]) && !vertexes.contains(roadsArr[i][0]) && roadsArr[i][2]<value &&roadBelonging[i] == district){
                    position = 0;
                    value = roadsArr[i][2];
                    tmp = roadsArr[i][0];
                }
            }
            if (position>=0){
                vertexes.add(tmp);
                sum += value;
            }
        }
        return sum;
    }
    public int getSumOfEverything(){
        int sum = 0;
        for (int i=0; i<district; i++){
            sum += getSumOfDistrict(i);
        }
        System.out.println(sum);

        /*for(int i=0; i<roads; i++){
            if(roadBelonging[i] == -1){
                System.out.println(townsBelongings[roadsArr[i][0]] + ";"+ townsBelongings[roadsArr[i][1]]+";"+roadsArr[i][2]);
            }
        }*/
        ArrayList<Integer> increments = new ArrayList<>();
        ArrayList<Integer> dist = new ArrayList<>();
        dist.add(0);
        for (int i = 0; i<district-1; i++){
            int value = Integer.MAX_VALUE;
            boolean again = false;
            int temp = 0;
            for (int r=0; r<roads; r++){
                if (roadBelonging[r] == -1){
                    if(roadsArr[r][2]<value){
                        if (dist.contains(townsBelongings[roadsArr[r][0]]) && !dist.contains(townsBelongings[roadsArr[r][1]])){
                            temp = townsBelongings[roadsArr[r][1]];
                            value = roadsArr[r][2];
                            again = true;
                        }
                        if (dist.contains(townsBelongings[roadsArr[r][1]]) && !dist.contains(townsBelongings[roadsArr[r][0]])){
                            temp = townsBelongings[roadsArr[r][0]];
                            value = roadsArr[r][2];
                            again = true;
                        }
                    }
                }
            }
            if (again){
                dist.add(temp);
                sum += value;
                increments.add(value);
                //System.out.println(sum);
            }
        }
        System.out.println(Arrays.toString(increments.toArray()));
        return sum;
    }





}
