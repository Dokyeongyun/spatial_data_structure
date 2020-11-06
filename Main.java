package Spatial_Data_Structure;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

import static Spatial_Data_Structure.QuadTree.width;
import static Spatial_Data_Structure.QuadTree.height;
import static Spatial_Data_Structure.QuadTree.picture;
import static Spatial_Data_Structure.QuadTree.obstacleQuadTreeList;
import static Spatial_Data_Structure.QuadTree.passableQuadTreeList;
import static Spatial_Data_Structure.QuadTree.c;

public class Main {
    public static void main(String[] args) {
        System.out.println("width: " + width);
        System.out.println("height: " + height);
        int[] pixels = new int[width * height];
        try {
            FileInputStream fis = new FileInputStream(new File("C:\\Users\\Admin\\Documents\\MATLAB\\SDS_map.txt"));
            BufferedReader bur = new BufferedReader(new InputStreamReader(fis));

            Scanner input = new Scanner(fis);
            for (int i = 0; i < width * height; i++) {
                pixels[i] = Integer.parseInt(input.next());
            }
            bur.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < pixels.length; i++) {
            picture[i / width][i % width] = pixels[i];
        }
        int count = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                count++;
                if (picture[i][j] == 1) {
                    picture[i][j] = 1;  // 통행 가능 지역
                } else {
                    picture[i][j] = 0; // 통행 불가 지역
                }
            }
        }

        System.out.println("count = " + count);


        QuadTree exampleRegion = new QuadTree(1, new Boundary(0, 0, width, height), "Initial");
        exampleRegion.check(exampleRegion);


        System.out.println("======================");
        System.out.println("size of obstacleQuadTreeList : " + obstacleQuadTreeList.size());
        System.out.println("size of passableQuadTreeList : " + passableQuadTreeList.size());
        System.out.println("split 횟수: " + c + "\n");

        System.out.println("==============test================");

        QuadTree SQ = new QuadTree(5, new Boundary(30,30,31,31), "start");
        SQ.locationCode = "33330";

        QuadTree EQ = new QuadTree(5, new Boundary(17,7,18,8), "end");
        EQ.locationCode = "10223";
        exampleRegion.AStarAlgorithm(SQ, EQ);

        Dimension dim = new Dimension(1000, 1000);
        JFrame frame = new JFrame("QuadTree");
        frame.setLocation(200, 0);
        frame.setPreferredSize(dim);
        JPanel panel = new JPanel();
        DrawPanel drawPanel = new DrawPanel();
        JScrollPane scroll = new JScrollPane(drawPanel);
        scroll.createVerticalScrollBar();
        drawPanel.setPreferredSize(dim);
        scroll.setAutoscrolls(true);
        scroll.getViewport().add(panel);
        panel.add(drawPanel);
        panel.setVisible(true);
        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
