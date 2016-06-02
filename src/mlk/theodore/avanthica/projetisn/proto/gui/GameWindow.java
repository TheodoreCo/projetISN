package mlk.theodore.avanthica.projetisn.proto.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;
import mlk.theodore.avanthica.projetisn.proto.db.Db;
import mlk.theodore.avanthica.projetisn.proto.middle.Choix;
import mlk.theodore.avanthica.projetisn.proto.middle.Etat;

public class GameWindow {

	private JFrame mainFrame;
	private JList<Choix> listeDecision;
	private JTextArea taEtat;
	private JButton playMusicBtn;
	private JButton stopMusicBtn;
	// Mémorise l'historique des états pour pouvoir exécuter le 'Back'
	// qui va faire un retour arrière vers l'avant-dernier élément de cette
	// liste
	private List<Integer> stateHistory = new ArrayList<>();

	// variable utilisée pour retenir l'objet Music courant (nécessaire pour la
	// bibliothèque TinyMusic)
	private Music currentPlayingMusic;

	// retient le nom du fichier de musique pour pouvoir l'utilser depuis les
	// boutons Start & Stop music
	private String currentPlayingFile;
	private JMenuBar menuBar;
	private JMenuItem mntmBack;
	private JSeparator separator;
	private JMenuItem mntmExit;
	private JMenu mnAbout;
	private JMenuItem mntmCredits;
	private JMenuItem mntmContrles;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Construit le look and feel de l'application
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						// System.out.println(info.getName());
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				} catch (Exception e) {
					// e.printStackTrace(); Pas besoin de faire quelque chose de
					// spécifique
				}

				try {
					GameWindow window = new GameWindow();
					window.mainFrame.setVisible(true);

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
		mainFrame = new JFrame();
		mainFrame.getContentPane().setBackground(Color.WHITE);
		// mainFrame.setBackground(Color.WHITE);
		mainFrame.setTitle("HAGALAB");
		mainFrame.getContentPane().setLayout(new GridBagLayout());

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension tailleEcran = toolkit.getScreenSize();

		mainFrame.setBounds(tailleEcran.width * 1 / 5, tailleEcran.height * 1 / 4, tailleEcran.width * 3 / 5,
				tailleEcran.height * 2 / 4);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 0);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		taEtat = new JTextArea();
		taEtat.setEditable(false);
		taEtat.setBackground(new Color(0, 0, 0)); // black
		taEtat.setForeground(new Color(255, 255, 255)); // white
		taEtat.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
		taEtat.setMargin(new Insets(10, 10, 10, 10)); // Laisser de l'espace
														// autour du texte
		taEtat.setLineWrap(true); // Pour passer automatiquement à la ligne
		taEtat.setWrapStyleWord(true);
		taEtat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.isControlDown()) {
					taEtat.setEditable(true);
				}

				if (e.isShiftDown()) {
					try {
						Db.updateDescription(stateHistory.get(stateHistory.size() - 1), taEtat.getText());
					} catch (SQLException ex) {
						JOptionPane.showMessageDialog(mainFrame, "Erreur d'accès à la base: " + ex.getMessage(),
								"Attention", JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
					taEtat.setEditable(false);
				}
			}
		});
		scrollPane.setViewportView(taEtat); // C'est le "add" spécifique lorsque
											// l'on veut lier un composant
											// (comme une JTextArea) à
		// un ScollPane
		mainFrame.getContentPane().add(scrollPane, gbc);

		listeDecision = new JList<>();
		listeDecision.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listeDecision.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));

		// Rajout d'un listener pour les doubles-click sur un élément de la
		// liste
		listeDecision.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Choix choix = getListeDecision().getSelectedValue();
					updateState(choix.getId());
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		// Afficher à la console l'élément de liste sélectionné
		listeDecision.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					System.out.println("Element de la liste cliqué: " + listeDecision.getSelectedValue());
				}
			}
		});

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.insets = new Insets(0, 0, 5, 0);
		gbc2.gridx = 0;
		gbc2.gridy = 2;
		gbc2.weighty = 1;
		gbc2.fill = GridBagConstraints.BOTH;
		mainFrame.getContentPane().add(listeDecision, gbc2);

		JButton okButton = new JButton("OK");
		okButton.setMnemonic('O');
		okButton.setBackground(new Color(255, 255, 204));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (listeDecision.getSelectedIndex() == -1) {
					// System.out.println("Pas de sélection !");
					JOptionPane.showMessageDialog(null,
							"Vous avez oublié de sélectionner un choix. Veuillez réessayer.",
							"Oups. Vous n'avez rien sélectionné.", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// interroger la base de données pour la position correspondant
				// à l'élément sélectionné
				Choix choix = getListeDecision().getSelectedValue();
				updateState(choix.getId());
			}
		});

		JPanel panel = new JPanel();
		panel.setBorder(null);

		panel.setBackground(Color.WHITE);
		panel.setForeground(Color.BLACK);
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.insets = new Insets(0, 0, 5, 0);
		gbc3.gridx = 0;
		gbc3.gridy = 3;
		mainFrame.getContentPane().add(panel, gbc3);
		panel.add(okButton);

		playMusicBtn = new JButton("Play music");
		playMusicBtn.setMnemonic('P');
		playMusicBtn.setBackground(new Color(255, 255, 204));
		playMusicBtn.setIcon(new ImageIcon(getClass().getResource("/resources/images/start.gif")));
		playMusicBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!TinySound.isInitialized()) {
					TinySound.init();
				}

				if (currentPlayingMusic == null && currentPlayingFile != null) {
					currentPlayingMusic = TinySound.loadMusic(currentPlayingFile);
					currentPlayingMusic.play(true);
				}
			}
		});
		panel.add(playMusicBtn);

		stopMusicBtn = new JButton("Stop music");
		stopMusicBtn.setMnemonic('S');
		stopMusicBtn.setBackground(new Color(255, 255, 204));
		stopMusicBtn.setIcon(new ImageIcon(getClass().getResource("/resources/images/stop.gif")));
		stopMusicBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentPlayingMusic != null) {
					currentPlayingMusic.stop();
					currentPlayingMusic.unload();
					currentPlayingMusic = null;
				}
			}
		});
		panel.add(stopMusicBtn);

		menuBar = new JMenuBar();
		GridBagConstraints gbc_menuBar = new GridBagConstraints();
		gbc_menuBar.anchor = GridBagConstraints.WEST;
		gbc_menuBar.gridx = 0;
		gbc_menuBar.gridy = 0;

		JMenu mnGameMenu = new JMenu("Game");

		JMenuItem mntmNewGame = new JMenuItem("New Game");
		mntmNewGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateState(1);
			}
		});

		mnGameMenu.add(mntmNewGame);

		menuBar.add(mnGameMenu);

		mntmBack = new JMenuItem("Back");
		mntmBack.setToolTipText("Mais c'est de la triche!");
		mntmBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				back();
			}
		});

		mnGameMenu.add(mntmBack);

		separator = new JSeparator();
		mnGameMenu.add(separator);

		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitApplication();
				System.exit(0);
			}
		});
		mnGameMenu.add(mntmExit);
		mainFrame.getContentPane().add(menuBar, gbc_menuBar);

		mnAbout = new JMenu("About");
		menuBar.add(mnAbout);

		mntmCredits = new JMenuItem("Credits...");
		mntmCredits.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CreditsDialog creditsDialog = new CreditsDialog();
				creditsDialog.pack(); // La taille de la fenêtre des crédits
										// s'adapte automatiquement
				creditsDialog.setLocationRelativeTo(mainFrame); // CEntre la
																// fenêtre des
																// crédits.
				creditsDialog.setVisible(true);
			}
		});
		mnAbout.add(mntmCredits);

		mntmContrles = new JMenuItem("Contr\u00F4les");
		mntmContrles.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ControlsDialog controlsDialog = new ControlsDialog();
				controlsDialog.pack();
				controlsDialog.setLocationRelativeTo(mainFrame);
				controlsDialog.setVisible(true);
			}
		});
		mnAbout.add(mntmContrles);
		JMenuItem mntmDoc = new JMenuItem("Documentation");
		mntmDoc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (Desktop.isDesktopSupported()) {
						Desktop.getDesktop().open(new File("./doc/Compte_rendu_ISN.docx"));
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		});
		mnAbout.add(mntmDoc);

		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitApplication();
			}
		});
		fillInitialScreen();
	}

	private void back() {
		if (stateHistory.size() >= 2) {
			int oldPosition = stateHistory.size() - 2;
			int oldValue = stateHistory.get(oldPosition);
			updateState(oldValue);
			// On supprime les deux derniers éléments de la statehistory
			// (updateState() rajoute un élément)
			stateHistory.remove(stateHistory.size() - 1);
			stateHistory.remove(stateHistory.size() - 1);
		}
	}

	private void exitApplication() {
		Db.disconnect();
		TinySound.shutdown();
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

	private JList<Choix> getListeDecision() {
		return listeDecision;
	}

	private void updateState(int position) {
		stateHistory.add(position);

		Etat etat = Db.getEtat(position);
		taEtat.setText(etat.getDescription());

		String nomFichierMus = etat.getNomFichierMusique();

		if (nomFichierMus != null) {
			if (!TinySound.isInitialized()) {
				TinySound.init();
			}

			if (currentPlayingMusic != null) {
				currentPlayingMusic.stop();
				currentPlayingMusic.unload();
			}

			currentPlayingFile = "/resources/sounds/" + etat.getNomFichierMusique();
			currentPlayingMusic = TinySound.loadMusic(currentPlayingFile);
			currentPlayingMusic.play(true);
		}

		List<Choix> listeChoix = Db.getChoices(position);
		DefaultListModel<Choix> model = new DefaultListModel<>();

		for (Choix choix : listeChoix) {
			model.addElement(choix);
		}

		getListeDecision().setModel(model);
	}
}
