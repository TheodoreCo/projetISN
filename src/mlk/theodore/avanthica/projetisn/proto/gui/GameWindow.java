	package mlk.theodore.avanthica.projetisn.proto.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mlk.theodore.avanthica.projetisn.proto.db.Db;
import mlk.theodore.avanthica.projetisn.proto.middle.Choix;
import mlk.theodore.avanthica.projetisn.proto.middle.PlaySound;
import java.awt.Color;

public class GameWindow {

	private JFrame frame;
	private JList<Choix> listeDecision;
	private JTextArea taEtat;
	private JButton playMusicBtn;
	private JButton stopMusicBtn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				} catch (Exception e) {
				}
				try {
					GameWindow window = new GameWindow();
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
	public GameWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBackground(Color.WHITE);
		frame.setTitle(" TITRE");
		frame.getContentPane().setLayout(new GridBagLayout());
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension tailleEcran = toolkit.getScreenSize();

		frame.setBounds(tailleEcran.width * 25 / 100, tailleEcran.height * 35 / 100, 768, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		taEtat = new JTextArea();
		taEtat.setBackground(new Color(0, 0, 0));
		taEtat.setForeground(new Color(255, 255, 255));
		taEtat.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
		taEtat.setMargin(new Insets(10, 10, 10, 10));
		taEtat.setWrapStyleWord(true);
		taEtat.setLineWrap(true);
		taEtat.setEditable(false);
		scrollPane.setViewportView(taEtat);
		frame.getContentPane().add(scrollPane, gbc);

		listeDecision = new JList<>();
		listeDecision.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listeDecision.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
//		listeDecision.addMouseMotionListener(new MouseMotionAdapter() {
//			@Override
//			public void mouseMoved(MouseEvent e) {
//				JList jl = (JList) e.getSource();
//				int row = jl.locationToIndex(new Point(e.getX(), e.getY()));
//				jl.setSelectedIndex(row);
//			}
//		});

		listeDecision.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		listeDecision.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					System.out.println("Element de la liste cliqué: " + listeDecision.getSelectedValue());
				}

			}
		});
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.weighty = 1;
		gbc2.fill = GridBagConstraints.BOTH;
		frame.getContentPane().add(listeDecision, gbc2);

		JButton btnNewButton = new JButton("OK");
		btnNewButton.setBackground(new Color(255, 255, 204));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (listeDecision.getSelectedIndex() == -1) {
					// System.out.println("Pas de sélection !");
					JOptionPane.showMessageDialog(null, "Vous avez oublié de sélectionner un choix. Veuillez réessayer.",
							"Oups. Vous n'avez rien sélectionné.", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// interroger la base de données pour la position correspondant
				// à l'élément sélectionné
				Choix choix = getListeDecision().getSelectedValue();
				updateState(choix.getId());
			}
		});
		
		/*
		JButton Recommencer = new JButton("Recommencer");
		btnNewButton.setBackground(new Color(255, 255, 204));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
			
			Choix choix = getListeDecision().getSelectedValue();
			updateState(choix.1());
			
			
			
			
		});
		*/
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setForeground(new Color(0, 0, 0));
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.gridx = 0;
		gbc3.gridy = 2;
		frame.getContentPane().add(panel, gbc3);
		panel.add(btnNewButton);

		playMusicBtn = new JButton("Play music");
		playMusicBtn.setBackground(new Color(255, 255, 204));
		playMusicBtn.setIcon(new ImageIcon(getClass().getResource("/resources/images/start.gif")));
		playMusicBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PlaySound.startPlaying("/resources/A.wav");
			}
		});
		panel.add(playMusicBtn);
		
		stopMusicBtn = new JButton("Stop music");
		stopMusicBtn.setBackground(new Color(255, 255, 204));
		stopMusicBtn.setIcon(new ImageIcon(getClass().getResource("/resources/images/stop.gif")));
		stopMusicBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				PlaySound.stopPlaying();
			}
		});
		panel.add(stopMusicBtn);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				Db.disconnect();
			}
		});
		fillInitialScreen();
	}

	/**
	 * Connexion à la base et remplissage avec l'état initial
	 */
	private void fillInitialScreen() {
		System.out.println("Je me connecte à la couche controller pour validation du choix");
		try {
			Db.connect();
			updateState(1);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	protected JList<Choix> getListeDecision() {
		return listeDecision;
	}

	protected JTextArea getTaEtat() {
		return taEtat;
	}

	private void updateState(int position) {
		taEtat.setText(Db.getDescription(position));

		List<Choix> listeChoix = Db.getChoices(position);
		DefaultListModel<Choix> model = new DefaultListModel<>();

		for (Choix choix : listeChoix) {
			model.addElement(choix);
		}

		getListeDecision().setModel(model);
	}
}
