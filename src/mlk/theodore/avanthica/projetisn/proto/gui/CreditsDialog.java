package mlk.theodore.avanthica.projetisn.proto.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CreditsDialog extends JDialog {


	/**
	 * Create the dialog.
	 */
	public CreditsDialog() {
		setTitle("Cr\u00E9dits");
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(null);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				
			}
		}
		{
			JLabel lblNewLabel = new JLabel("<html><br/>Interface homme-machine et insertion de donn\u00E9es dans la base de donn\u00E9es: <b>Avanthika Nandakumaran</b><br/>\r\n\r\nCr\u00E9ation et connection \u00E0 la base de donn\u00E9es, musique et menus: <b>Th\u00E9odore Coman</b><br/><br/><hr/>\r\n\r\nMerci \u00E0 notre professeur <b>M. CUESTA</b> pour cette belle ann\u00E9e en ISN.<br/>\r\nEt un grand merci \u00E0 toute l'\u00E9quipe de <b>MAO</b> du conservatoire de Bussy-Saint-Georges.</html>");
			getContentPane().add(lblNewLabel, BorderLayout.CENTER);
		}
	}

}
