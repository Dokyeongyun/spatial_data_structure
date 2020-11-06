package Spatial_Data_Structure;

import java.util.ArrayList;
import java.util.List;

import static Spatial_Data_Structure.FSM.*;

public class QuadTree {

    public static int width = 32; // 입력된 Binary 지도파일의 크기 => 이 값도 파일에서 읽어오도록 해야 함
    public static int height = 32;

    public static int[][] picture = new int[height][width];
    public static ArrayList<QuadTree> passableQuadTreeList = new ArrayList<>();
    public static ArrayList<QuadTree> obstacleQuadTreeList = new ArrayList<>();
    public static int c = 0;
    public static QuadTree startQuad = new QuadTree();
    public static QuadTree endQuad = new QuadTree();
    public static List<QuadTree> openList = new ArrayList<>();

    int level = 0;
    Boundary boundary;
    QuadTree NW = null, NE = null, SW = null, SE = null;
    Node center;
    String name, locationCode = "";
    double g = 0, h = 0, f = 0;
    boolean opened, closed;
    double parent_x, parent_y;

    QuadTree() {

    }

    QuadTree(int level, Boundary boundary, String name) {
        this.name = name;
        this.level = level;
        this.boundary = boundary;
        this.center = new Node((this.boundary.xMin + (this.boundary.xMax - this.boundary.xMin) / 2), (this.boundary.yMin + (this.boundary.yMax - this.boundary.yMin) / 2), 1);
    }

    // Boundary 내의 값이 모두 같으면 false 와 그 값을 리턴 => 값이 0이면 ObstacleQuad, 1이면 PassableQuad
    // Boundary 내의 값이 모두 같지 않으면 true 를 리턴 => 해당 Boundary 를 Split()
    checkValue checkBoundary(QuadTree checkBlank) {
        double xStart = checkBlank.boundary.xMin;
        double xEnd = checkBlank.boundary.xMax;
        double yStart = checkBlank.boundary.yMin;
        double yEnd = checkBlank.boundary.yMax;

        int first = picture[(int) yStart][(int) xStart];
        for (double i = yStart; i < yEnd; i++) {
            for (double j = xStart; j < xEnd; j++) {
                if (picture[(int) i][(int) j] != first) {
                    return new checkValue(true, 0);
                }
            }
        }
        return new checkValue(false, first);
    }

    void check(QuadTree checkBlank) {
        checkValue cv = checkBoundary(checkBlank);
        if (cv.check) {
            checkBlank.split();
        } else {
            if (cv.value == 0) {
                obstacleQuadTreeList.add(checkBlank);
            } else {
                passableQuadTreeList.add(checkBlank);
            }
        }
    }

    void split() {
        if (this.boundary.getxMax() - this.boundary.getxMin() < 2) {
            if (this.boundary.getyMax() - this.boundary.getyMin() < 2) {
                return;
            }
        }
        c++; // 분할 횟수 측정

        double xOffset = this.boundary.getxMin() + (this.boundary.getxMax() - this.boundary.getxMin()) / 2;
        double yOffset = this.boundary.getyMin() + (this.boundary.getyMax() - this.boundary.getyMin()) / 2;

        // 위 과정을 통해 생성된 4개의 사각형을 정의한다.
        NW = new QuadTree(this.level + 1, new Boundary(this.boundary.getxMin(), this.boundary.getyMin(), xOffset, yOffset), this.name + (this.level + 1) + "'s NW");
        NE = new QuadTree(this.level + 1, new Boundary(xOffset, this.boundary.getyMin(), this.boundary.getxMax(), yOffset), this.name + (this.level + 1) + "'s NE");
        SW = new QuadTree(this.level + 1, new Boundary(this.boundary.getxMin(), yOffset, xOffset, this.boundary.getyMax()), this.name + (this.level + 1) + "'s SW");
        SE = new QuadTree(this.level + 1, new Boundary(xOffset, yOffset, this.boundary.getxMax(), this.boundary.getyMax()), this.name + (this.level + 1) + "'s SE");

        NW.locationCode = this.locationCode + "0";
        NE.locationCode = this.locationCode + "1";
        SW.locationCode = this.locationCode + "2";
        SE.locationCode = this.locationCode + "3";

        System.out.println(name + "<NW> level:" + NW.level + " [" + NW.boundary.xMin + ", " + NW.boundary.xMax + "]" + "[" + NW.boundary.yMin + ", " + NW.boundary.yMax + "] LocationCode: " + NW.locationCode);
        System.out.println(name + "<NE> level:" + NE.level + " [" + NE.boundary.xMin + ", " + NE.boundary.xMax + "]" + "[" + NE.boundary.yMin + ", " + NE.boundary.yMax + "] LocationCode: " + NE.locationCode);
        System.out.println(name + "<SW> level:" + SW.level + " [" + SW.boundary.xMin + ", " + SW.boundary.xMax + "]" + "[" + SW.boundary.yMin + ", " + SW.boundary.yMax + "] LocationCode: " + SW.locationCode);
        System.out.println(name + "<SE> level:" + SE.level + " [" + SE.boundary.xMin + ", " + SE.boundary.xMax + "]" + "[" + SE.boundary.yMin + ", " + SE.boundary.yMax + "] LocationCode: " + SE.locationCode);

        check(NW);
        check(NE);
        check(SW);
        check(SE);
    }

    void AStarAlgorithm(QuadTree SQ, QuadTree EQ) {
        QuadTree currentQuad = new QuadTree();
        QuadTree tempQuad = new QuadTree();

        startQuad = SQ;
        endQuad = EQ;

        List<QuadTree> neighbors;
        QuadTree neighbor = new QuadTree();

        double x, y;
        double next_g;
        double f_temp;

        System.out.println("StartQuad's LocationCode : " + startQuad.locationCode);
        System.out.println("EndQuad's LocationCode : " + endQuad.locationCode);

        startQuad.opened = true;
        openList.add(startQuad);

        while (!(openList.size() == 0)) {
            currentQuad = findCheapestPath(openList);
            f_temp = 9999999;
            tempQuad = new QuadTree();

            if (currentQuad.parent_x == 999) {
                System.out.println("No Path!!");
                break;
            }
            currentQuad.closed = true;

            if (currentQuad.locationCode.equals(endQuad.locationCode)) {
                System.out.println("Path Finding Complete : Success");
                System.out.println("최단 거리: " + currentQuad.f);
                break;
            }
            System.out.println("    현재노드 (" + currentQuad.center.x + ", " + currentQuad.center.y + ") locationCode: " + currentQuad.locationCode);

            neighbors = getNeighbors(currentQuad);

            for (int i = 0; i < neighbors.size(); i++) {
                neighbor = neighbors.get(i);

                if (neighbor.closed) {
                    continue;
                }
                System.out.println("    이웃노드 (" + neighbor.center.x + ", " + neighbor.center.y + ") locationCode: " + neighbor.locationCode);

                x = neighbor.center.x;
                y = neighbor.center.y;

                // 현재 노드와 neighbor 간의 거리 계산
                next_g = (currentQuad.g + Math.sqrt(Math.pow((currentQuad.center.x - x), 2) + Math.pow((currentQuad.center.y - y), 2)));
                if (!neighbor.opened || next_g < neighbor.g) {
                    neighbor.g = next_g;
                    neighbor.h = Math.sqrt(Math.pow((x - endQuad.center.x), 2) + Math.pow((y - endQuad.center.y), 2));
                    neighbor.f = neighbor.g + neighbor.h;

                    System.out.println("     g: " + neighbor.g + " h: " + neighbor.h + " f: " + neighbor.f);

                    neighbor.parent_x = currentQuad.center.x;
                    neighbor.parent_y = currentQuad.center.y;

                    if (f_temp > neighbor.f) {
                        f_temp = neighbor.f;
                        tempQuad = neighbor;
                    }
                }
            }
            if (!tempQuad.opened) {
                tempQuad.opened = true;
                openList.add(tempQuad);
            } else {
                for (int w = 0; w < openList.size(); w++) {
                    if (openList.get(w).locationCode.equals(tempQuad.locationCode)) {
                        openList.add(tempQuad);
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < openList.size(); i++) {
            System.out.print(openList.get(i).locationCode + " ");
        }
        System.out.println();
    }

    List<QuadTree> getNeighbors(QuadTree currentQuad) {
        List<QuadTree> g_neighbors = new ArrayList<>();
        for(int i=0; i<8; i++){
            g_neighbors.add(new QuadTree());
        }
        List<String> adjacency = isExist(currentQuad.locationCode);

        for (int i = 0; i < adjacency.size(); i++) {
            if (adjacency.get(i).equals("-1")) {
                QuadTree a = new QuadTree();
                a.closed = true;
                g_neighbors.set(i, a);
            }
            // 이분탐색 등 탐색효율이 빠른 방법으로 변경 필요
            for (QuadTree quadTree : passableQuadTreeList) {
                if (quadTree.locationCode.equals(adjacency.get(i))) {
                    g_neighbors.set(i, quadTree);
                }
            }
        }
        return g_neighbors;
    }

    ArrayList<String> isExist(String locationCode) {
        int length = locationCode.length();
        int index;
        boolean halt;
        String resultCode;

        ArrayList<String> resultCodeList = new ArrayList<>();
        ArrayList<String> adjacency = new ArrayList<>();
        ArrayList<String> neighborsLocationCodeList = new ArrayList<>();
        for(int i=0; i<8; i++){
            adjacency.add("-1");
        }
        String c_Code;
        int direction;

        // 해당 LocationCode의 8방향 LocationCode를 구함
        for (int i = LU; i <= RD; i++) {
            index = 0;
            resultCode = "";
            direction = i;
            while (true) {
                c_Code = locationCode.substring((length - index) - 1, length - index);
                FSM result = fsmArr[direction-LU][Integer.parseInt(c_Code)];
                resultCode += result.code;
                direction = result.direction;
                halt = result.halt;
                if (halt) {
                    resultCodeList.add(resultCode);
                    break;
                } else {
                    index++;
                }
                if ((length - index) == 0) {
                    resultCodeList.add(resultCode);
                    break;
                }
            }
        }

        for (String s : resultCodeList) {
            int resultCodeLength = s.length();
            StringBuffer resultCodeSB = new StringBuffer(s).reverse();
            String finalLocationCode = locationCode.substring(0, length - resultCodeLength) + resultCodeSB;
            neighborsLocationCodeList.add(finalLocationCode);
        }

        for (int i = 0; i < neighborsLocationCodeList.size(); i++) {
            for (QuadTree quadTree : passableQuadTreeList) {
                if (neighborsLocationCodeList.get(i).matches(quadTree.locationCode + ".*")) {
                    neighborsLocationCodeList.set(i, quadTree.locationCode);
                    adjacency.set(i, quadTree.locationCode);
                }
            }
            for (QuadTree quadTree : obstacleQuadTreeList) {
                if (neighborsLocationCodeList.get(i).matches(quadTree.locationCode + ".*")) {
                    neighborsLocationCodeList.set(i, quadTree.locationCode);
                }
            }
        }

        System.out.println(neighborsLocationCodeList);
        System.out.println(adjacency);

        /*
         adjacency 의 값이 -1 인 것 중에서 ( locationCode가 passableQuadTreeList에 존재하지 않거나
                                         인접한 격자가 더 큰 격자가 아닐 때(더 작은 격자가 인접해 있을 때))
         neighborsLocationCodeList 의 값이 obstacleQuadTreeList에 있다면 PASS
         아니면
         neighborsLocationCodeList 에 특정 문자를 더해가며 PassableQuadTreeList 에서 다시 탐색
         격자의 크기가 한 단계 작을 때 (level -1) 방향에 따라 LU->3 | U->2,3 | RU->2 | L->1,3 | R->0,2 | LU->1 | U->0,1 | RU->0
         (level-2) ....
         같은 값이 있다면 adjacency 의 값을 해당 LocationCode + 문자로 설정
         */
        for (int i = 0; i < adjacency.size(); i++) {
            if (!adjacency.get(i).equals("-1")) {
                continue;
            }

            checkValue result = new checkValue();
            checkValue result2 = new checkValue();

            String tempLC = neighborsLocationCodeList.get(i);
            int idx = 0;
            for(int j=0; j<4; j++){
                if(fsmArr[i][j].direction == i+LU){
                    result = findSmallerNeighbor(tempLC, fsmArr[i][j].code);
                    idx = j;
                    break;
                }
            }
            for(int j=idx+1; j<4; j++){
                if(fsmArr[i][j].direction == i+LU){
                    result2 = findSmallerNeighbor(tempLC, fsmArr[i][j].code);
                    break;
                }
            }
            result = getCheckValue(result, result2);

            if (result.check) {
                adjacency.set(i, result.locationCode);
            }
        }
        System.out.println("이웃노드 List" + adjacency);
        return adjacency;
    }

    checkValue getCheckValue(checkValue result, checkValue result2) {
        if (result.check && result2.check) {
            double a = calF(result.locationCode);
            double b = calF(result2.locationCode);
            if (a > b) {
                result = result2;
            }
        } else if (result2.check) {
            result = result2;
        }
        return result;
    }

    double calF(String locationCode) {
        if (locationCode.equals("-1")) {
            return 0;
        }
        for (QuadTree quadTree : passableQuadTreeList) {
            if (quadTree.locationCode.equals(locationCode)) {
                double x = quadTree.center.x;
                double y = quadTree.center.y;
                return Math.sqrt(Math.pow((x - endQuad.center.x), 2) + Math.pow((y - endQuad.center.y), 2));
            }
        }
        System.out.println("calF Error");
        return 0;
    }

    checkValue findSmallerNeighbor(String tempLC, String addString) {
        checkValue temp = new checkValue();
        if (obstacleCheck(tempLC)) {
            return new checkValue(true, "-1");
        } else {
            temp = passableCheck(tempLC + addString);
            if (temp.check) {
                return new checkValue(true, temp.locationCode);
            } else {
                findSmallerNeighbor(tempLC + addString, addString);
            }
        }
        return new checkValue(false, temp.locationCode);
    }

    boolean obstacleCheck(String locationCode) {
        for (QuadTree quadTree : obstacleQuadTreeList) {
            if (quadTree.locationCode.equals(locationCode)) {
                return true;
            }
        }
        return false;
    }

    checkValue passableCheck(String locationCode) {
        for (QuadTree quadTree : passableQuadTreeList) {
            if (quadTree.locationCode.equals(locationCode)) {
                return new checkValue(true, locationCode);
            }
        }
        return new checkValue(false, locationCode);
    }

    QuadTree findCheapestPath(List<QuadTree> openList) {
        QuadTree cheapest = new QuadTree();
        double cheapestCost = 99999999;
        double totalCost;
        boolean check = false;

        for (QuadTree tree : openList) {
            for (QuadTree quadTree : passableQuadTreeList) {
                String temp = quadTree.locationCode;
                if (tree.locationCode.equals(temp)) {
                    if (!quadTree.closed) {
                        totalCost = tree.f;
                        check = true;
                        if (totalCost < cheapestCost) {
                            cheapest = quadTree;
                            cheapestCost = totalCost;
                        }
                    }
                }
            }
        }

        if (!check)
            cheapest.parent_x = 999;

        return cheapest;
    }
}
