package Spatial_Data_Structure;

import javax.swing.*;
import java.awt.*;

import static Spatial_Data_Structure.QuadTree.obstacleQuadTreeList;
import static Spatial_Data_Structure.QuadTree.passableQuadTreeList;
import static Spatial_Data_Structure.QuadTree.openList;

// 경로 생성 시각화
public class DrawPanel extends JPanel {
    public void paint(Graphics g) {
        super.paint(g);
        int size = 20;
        for (QuadTree quadTree : obstacleQuadTreeList) {
            g.fillRect((int) quadTree.boundary.xMin * size, (int) quadTree.boundary.yMin * size,
                    ((int) (quadTree.boundary.xMax - quadTree.boundary.xMin)) * size,
                    ((int) (quadTree.boundary.yMax - quadTree.boundary.yMin)) * size); // x, y, width, height
        }
        for (QuadTree quadTree : passableQuadTreeList) {
            g.drawRect((int) quadTree.boundary.xMin * size, (int) quadTree.boundary.yMin * size,
                    ((int) (quadTree.boundary.xMax - quadTree.boundary.xMin)) * size,
                    ((int) (quadTree.boundary.yMax - quadTree.boundary.yMin)) * size); // x, y, width, height
        }

        g.setColor(Color.RED);
        g.fillRect(2 * size, 8 * size, size, size);
        g.fillRect(17 * size, 7 * size, size, size);
        g.fillRect(30 * size, 14 * size, size, size);
        g.fillRect(1 * size, 20 * size, size, size);
        g.fillRect(30 * size, 30 * size, size, size);
        for (int i = 0; i < openList.size(); i++) {
            if (i != openList.size() - 1) {
                g.drawLine((int) (openList.get(i).center.x * size), (int) (openList.get(i).center.y * size), (int) (openList.get(i + 1).center.x * size), (int) (openList.get(i + 1).center.y * size));
            }
        }
    }
}