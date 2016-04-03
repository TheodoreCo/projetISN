package fr.theocoman.essais;

import javax.swing.JFrame;

public class DemoFrame {
	public static void main(String[] args) {
		JFrame f = new JFrame("ceci est une frame");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 300);
		f.setVisible(true);
	}
}
