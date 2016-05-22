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
import java.awt.event.MouseListener;
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

import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;
import mlk.theodore.avanthica.projetisn.proto.db.Db;
import mlk.theodore.avanthica.projetisn.proto.middle.Choix;
import mlk.theodore.avanthica.projetisn.proto.middle.Etat;
import mlk.theodore.avanthica.projetisn.proto.middle.PlaySound;
import java.awt.Color;

public class GameWindow {

	private JFrame mainFrame;
	private JList<Choix> listeDecision;
	private JTextArea taEtat;
	private JButton playMusicBtn;
	private JButton stopMusicBtn;

	// variable utilis�e pour retenir l'objet Music courant (n�cessaire pour la biblioth�que TinyMusic)
	private Music currentPlayingMusic;
	
	// retient le nom du fichier de musique pour pouvoir l'utilser depuis les boutons Start & Stop music
	private String currentPlayingFile;

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
					// e.printStackTrace(); Pas besoin de faire quelque chose de sp�cifique
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

		mainFrame.setBounds(tailleEcran.width * 1 / 5,
				tailleEcran.height * 1 / 4,
				tailleEcran.width * 3 / 5,
				tailleEcran.height * 2 / 4);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
		taEtat.setMargin(new Insets(10, 10, 10, 10)); // Laisser de l'espace autour du texte
		taEtat.setLineWrap(true); // Pour passer automatiquement � la ligne
		taEtat.setWrapStyleWord(true); // Pour ne pas couper les mots
		taEtat.setEditable(false);
		scrollPane.setViewportView(taEtat); // C'est le "add" sp�cifique lorsque l'on veut lier un composant (comme une JTextArea) �
		// un ScollPane
		mainFrame.getContentPane().add(scrollPane, gbc);

		listeDecision = new JList<>();
		listeDecision.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listeDecision.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
		
		// Rajout d'un listener pour les doubles-click sur un �l�ment de la liste
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

		// Afficher � la console l'�l�ment de liste s�lectionn�
		listeDecision.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					System.out.println("Element de la liste cliqu�: " + listeDecision.getSelectedValue());
				}

			}
		});
		
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.weighty = 1;
		gbc2.fill = GridBagConstraints.BOTH;
		mainFrame.getContentPane().add(listeDecision, gbc2);

		JButton okButton = new JButton("OK");
		okButton.setMnemonic('O');
		okButton.setBackground(new Color(255, 255, 204));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (listeDecision.getSelectedIndex() == -1) {
					// System.out.println("Pas de s�lection !");
					JOptionPane.showMessageDialog(null,
							"Vous avez oubli� de s�lectionner un choix. Veuillez r�essayer.",
							"Oups. Vous n'avez rien s�lectionn�.", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// interroger la base de donn�es pour la position correspondant
				// � l'�l�ment s�lectionn�
				Choix choix = getListeDecision().getSelectedValue();
				updateState(choix.getId());
			}
		});

		JPanel panel = new JPanel();
		
		panel.setBackground(Color.WHITE);
		panel.setForeground(Color.BLACK);
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.gridx = 0;
		gbc3.gridy = 2;
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

		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				Db.disconnect();
				TinySound.shutdown();
			}
		});
		fillInitialScreen();
	}

	/**
	 * Connexion � la base et remplissage avec l'�tat initial
	 */
	private void fillInitialScreen() {
		System.out.println("Je me connecte � la couche controller pour validation du choix");
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
