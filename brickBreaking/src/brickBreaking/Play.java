package brickBreaking;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.Random;

import javax.swing.Timer;
import javax.swing.JPanel;

public class Play extends JPanel implements KeyListener, ActionListener{
	private boolean playing = false;
	private int score = 0;
	private boolean justStarted = true;
	
	private int totalBricksStart = 28; //For display purposes
	private int totalBricks = totalBricksStart; //For iteration purposes
	
	private Timer timer; 
	private int delay = 7; 
	
	private int playerX = 350;
	
	Random rand = new Random();
	
	//Set beginning ball position and direction
	private int ballposX = rand.nextInt(770);
	private int ballposY = 400;
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private int paddleSpeed = 30;
	private int paddleSize = 100;
	
	private MapGen map;
	
	public Play() {
		map = new MapGen(4, 7); 
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		//background
		g.setColor(Color.darkGray);
		g.fillRect(1, 1, 797, 642); 
		
		//draw map
		map.draw((Graphics2D) g);
		
		//draw borders
		g.setColor(Color.green);
		g.fillRect(0, 0, 3, 642); 
		g.fillRect(0, 0, 797, 3); 
		g.fillRect(797, 0, 3, 642); 
		
		//draw score
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 25));
		g.drawString(score+"/"+totalBricksStart, 730, 620);
		
		//draw paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 590, paddleSize, 10); 
		
		//draw ball
		g.setColor(Color.magenta);
		g.fillOval(ballposX, ballposY, 20, 20);
		
		//Set messages for the starting and ending of a game
		if(justStarted) {
			g.setColor(Color.green);
			g.setFont(new Font("comic sans", Font.BOLD, 20));
			g.drawString("Press 1 for Easy Difficulty", 250, 280); 
			
			g.setColor(Color.yellow);
			g.setFont(new Font("comic sans", Font.BOLD, 20));
			g.drawString("Press 2 for Medium Difficulty", 250, 310);
			
			g.setColor(Color.red);
			g.setFont(new Font("comic sans", Font.BOLD, 20));
			g.drawString("Press 3 for Hard Difficulty", 250, 340);
		}
		
		if(totalBricks <= 0) {
			playing = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.green);
			g.setFont(new Font("arial", Font.BOLD, 30));
			g.drawString("You Won!", 340, 350);
			
			g.setFont(new Font("comic sans", Font.BOLD, 15));
			g.drawString("Press 1 for Easy, 2 for Medium, or 3 for Hard Difficulty", 190, 400);
		}
		
		if(ballposY > 620) {
			playing = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("arial", Font.BOLD, 30));
			g.drawString("GAME OVER", 310, 350);
			
			g.setFont(new Font("arial", Font.BOLD, 30));
			g.drawString("Score: "+score+"/"+totalBricksStart, 310, 375);
			
			g.setFont(new Font("comic sans", Font.BOLD, 15));
			g.drawString("Press 1 for Easy, 2 for Medium, or 3 for Hard Difficulty", 190, 425);
		}
		
		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		//Check when the ball hits brick/paddle and change the ball's direction
		if(playing) {
			Rectangle ball = new Rectangle(ballposX, ballposY, 20, 20);
			Rectangle paddle = new Rectangle(playerX, 590, paddleSize, 10);
			if(ball.intersects(paddle)) {
				ballYdir = -ballYdir;
			}
			A: for(int i = 0; i < map.bricks.length; i++) {
				for(int j = 0; j < map.bricks[0].length; j++) {
					if(map.bricks[i][j] == true) {
						
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect)) {
							map.setBrickValue(false, i, j);
							totalBricks--;
							score += 1;
							
							if(ballposX + 19 <= brickRect.x || 
									ballposX + 1 >= brickRect.x + brickRect.width){
								ballXdir = -ballXdir;
							} else {
								ballYdir = -ballYdir;
							}
							break A;
						}
					}
				}
			}
			//Move the ball, check if it hits the edges of the window
			ballposX += ballXdir;
			ballposY += ballYdir;
			if(ballposX < 0) {
				ballXdir = -ballXdir;
			}
			if(ballposY < 0) {
				ballYdir = -ballYdir;
			}
			if(ballposX > 780) {
				ballXdir = -ballXdir;
			}
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		//Ensure the user's paddle stops at the right edge of the window
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerX >= 797 - paddleSize) { 
				playerX = 797 - paddleSize; 
			} else {
				moveRight();
			}
		}
		//Ensure the user's paddle stops at the left edge of the window
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(playerX <= 3) {
				playerX = 3; 
			} else {
				moveLeft();
			}
		}
		//For Easy Mode:
		if(e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
			if(!playing) {
				if (justStarted) {
					justStarted = false;
					playing = true;
				}
				else {
					playing = true;
					ballposX = rand.nextInt(770);
					ballposY = 400;
					ballXdir = -1;
					ballYdir = -2;
					playerX = 310;
					paddleSize = 100;
					score = 0;
					totalBricksStart = 28;
					totalBricks = 28;
					paddleSize = 100;
					map = new MapGen(4, 7);
				
					repaint();
				}
			}
		}
		
		//For Medium Mode (ball is faster):
		if(e.getKeyCode() == KeyEvent.VK_2 || e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
			if(!playing) {
				if (justStarted) {
					justStarted = false;
					playing = true;
					ballYdir = -3;
					paddleSize = 100;		
				}
				else {
					playing = true;
					ballposX = rand.nextInt(770);
					ballposY = 400;
					ballXdir = -1;
					ballYdir = -3;
					playerX = 310;
					paddleSize = 100;
					score = 0;
					totalBricksStart = 28;
					totalBricks = 28;
					map = new MapGen(4, 7);
				
					repaint();
				}
			}
		}
		
		//For Hard Mode (ball faster, paddle smaller, more bricks):
		if(e.getKeyCode() == KeyEvent.VK_3 || e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
			if(!playing) {
				if (justStarted) {
					justStarted = false;
					playing = true;
					ballYdir = -3;
					paddleSize = 70;	
					totalBricksStart = 35;
					totalBricks = 35;
					map = new MapGen(5, 7);
				}
				else {
					playing = true;
					ballposX = rand.nextInt(770);
					ballposY = 400;
					ballXdir = -1;
					ballYdir = -3;
					playerX = 310;
					paddleSize = 70;
					score = 0;
					totalBricksStart = 35;
					totalBricks = 35;
					map = new MapGen(5, 7);
				
					repaint();
				}
			}
		}
		
	}
	 public void moveRight() {
		 playerX += paddleSpeed;
	 }
	 public void moveLeft() {
		 playerX -= paddleSpeed;
	 }


}
