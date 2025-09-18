import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator {
    public int map[][];
    public int brickWidth;
    public int brickHeight;
    public int brickYOffset = 0;  // offset to move bricks down

    // Gap size between bricks
    public final int gap = 10;

    public MapGenerator(int row, int col) {
        map = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                map[i][j] = 1;
            }
        }
        brickWidth = 500 / col;
        brickHeight = 250 / row;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    int brickX = j * (brickWidth + gap) + 80;
                    int brickY = i * (brickHeight + gap) + 50 + brickYOffset;

                    g.setColor(Color.white);
                    g.fillRect(brickX, brickY, brickWidth, brickHeight);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(brickX, brickY, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}
