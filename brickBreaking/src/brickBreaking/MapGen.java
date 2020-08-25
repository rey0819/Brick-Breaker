package brickBreaking;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGen {
	// 2D array of bricks (true=brick there, false=brick was broken)
	public boolean bricks[][];
	public int brickWidth;
	public int brickHeight;
	
	// Create map of bricks, with all bricks beginning as true
	public MapGen(int row, int col) {
		bricks = new boolean[row][col];
		for(int i = 0; i < bricks.length; i++) {
			for(int j = 0; j < bricks[0].length; j++) {
				bricks[i][j] = true;
			}
		}
		// Make bricks an appropriate size depending on number of rows/columns
		brickWidth = 640/col;
		brickHeight = 200/row;
	}
	
	/* Draw the bricks one by one, with a black border in between each and
	   put them in the correct spot vertically and horizontally (centered) */ 
	public void draw(Graphics2D g) {
		for(int i = 0; i < bricks.length; i++) {
			for(int j = 0; j < bricks[0].length; j++) {
				if(bricks[i][j] == true) { //==1
					g.setColor(Color.white);
					g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
					
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.black);
					g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
				}
			}
		}
	}
	// Update value of a brick
	public void setBrickValue(boolean value, int row, int col) {
		bricks[row][col] = value;
	}

}
