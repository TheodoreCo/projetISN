package mlk.theodore.avanthica.projetisn.proto.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlsDialog extends JDialog {

	public ControlsDialog() {
		setTitle("Raccourcis");
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
			JLabel lblNewLabel = new JLabel(
					"<html>\r\n<br/>\r\n<h2>Raccourcis:</h2>\r\n<hr/>\r\n<ul>\r\n\t<li>Editer le texte: <b>CTRL+ clic</b></li>\r\n\t<li>Sauvegarder les changements: <b>SHIFT + clic</b></li>\r\n</ul>\r\n<br/>\r\n</html>");
			getContentPane().add(lblNewLabel, BorderLayout.CENTER);
		}
	}
}