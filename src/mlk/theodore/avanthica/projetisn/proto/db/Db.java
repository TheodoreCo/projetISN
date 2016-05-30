package mlk.theodore.avanthica.projetisn.proto.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mlk.theodore.avanthica.projetisn.proto.middle.Choix;
import mlk.theodore.avanthica.projetisn.proto.middle.Etat;

/**
 * Classe pour assurer l'interface avec la base de donn�es.
 * 
 * @author theo
 *
 */
public class Db {
	// variable de classe indispensable pour travailler avec une base de donn�es
	private static Connection con;
	
	/**
	 * M�thode qui initialise la connexion
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void connect() throws ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbcDriver");
		con = DriverManager.getConnection("jdbc:hsqldb:file:db/DB", "SA", "");
		System.out.println("Connect� � la base de donn�es");
	}

	/**
	 * D�connexion de la base de donn�es. A appeler � la fin du programme
	 * (l'�v�nement windowClosing est un bon candidat).
	 */
	public static void disconnect() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException ignored) {
			}
		}
		System.out.println("Connexion ferm�e");
	}

	/**
	 * Retourne la description associ�e � l'�tat avec id = position.
	 * 
	 * @param position
	 * @return
	 */
	public static Etat getEtat(int position) {
		String sql = "SELECT description, fichier_musique FROM Etat WHERE id=?";
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement(sql);
			st.setInt(1, position);
			rs = st.executeQuery();
			if (rs.next()) {
				Etat etat = new Etat(rs.getString("description"), rs.getString("fichier_musique"));
				return etat;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {try {rs.close();} catch (Exception ignored) {}}
			if (st != null) {try {st.close();} catch (Exception ignored) {}}
		}
		
		return null;
	}

	/**
	 * Retourne la liste des Choix depuis l'�tat avec id = position.
	 * 
	 * @param position
	 * @return
	 */
	public static List<Choix> getChoices(int position) {
		List<Choix> listeChoix = new ArrayList<>();

		String sql = "SELECT id_cible, point_entree" +
				" FROM Etat, Transition" +
				" WHERE id_source = ?" +
				" AND id = id_cible";

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement(sql);
			st.setInt(1, position);
			rs = st.executeQuery();
			while (rs.next()) {
				Choix choix = new Choix(rs.getInt("id_cible"), rs.getString("point_entree"));
				listeChoix.add(choix);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {try {rs.close();} catch (Exception ignored) {}}
			if (st != null) {try {st.close();} catch (Exception ignored) {}}
		}
		return listeChoix;
	}
}
