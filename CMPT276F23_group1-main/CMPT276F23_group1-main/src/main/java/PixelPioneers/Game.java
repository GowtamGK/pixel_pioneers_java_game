package PixelPioneers;

import javax.swing.JFrame;

public class Game extends JFrame{

	public Game() { //constructor
		//create new game
		add(new Model());
	}
	
	public static void main(String[] args) {
		//set game window
		Game window = new Game();
		window.setVisible(true);
		window.setTitle("PixelPioneers");
		window.setSize(568,620);
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
	}
}