package mlk.theodore.avanthica.projetisn.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JeuDontVousEtesLeHero {

	private Color initialColor = null;
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JeuDontVousEtesLeHero window = new JeuDontVousEtesLeHero();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public JeuDontVousEtesLeHero() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Le bouton vient d'être cliqué");
			}
		});
		
		btnNewButton.addMouseListener(new MouseAdapter() {		
			@Override
			public void mouseExited(MouseEvent e) {
				btnNewButton.setBackground(initialColor);
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if (initialColor == null) {
					initialColor = btnNewButton.getBackground();
				}
				
				btnNewButton.setBackground(Color.ORANGE);
				
			}
		});
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Add  button");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.add(new JButton("" + new Date()));
			}
		});
		panel.add(btnNewButton_1);
	}

}
