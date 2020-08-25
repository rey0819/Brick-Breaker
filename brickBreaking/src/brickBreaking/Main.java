package brickBreaking;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Play game = new Play();
		frame.setBounds(10, 10, 800, 650); 
		frame.setTitle("Break the Bricks");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(game);
		frame.setVisible(true);
	}

}
