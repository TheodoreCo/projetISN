package fr.theocoman.essais;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Cette classe permet de faire une démo de connexion à une base de données
 * HSQLDB, de création d'une table, d'écriture des valeurs, de lecture des
 * valeurs et de fermeture de connexion
 * 
 * @author theo
 *
 */
public class DatabaseDemo {

	public static void main(String[] args) {
		Connection connection = null;
		ResultSet resultSet = null;
		Statement statement = null;
		
		try {
			// Première étape: obligatoire pour tout programme: connexion à la base de données.
			// On se connecte une seule fois et on prend soin de fermer la connexion à la fin - d'où le finally.
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection("jdbc:hsqldb:file:db/DB", "SA", "");
			System.out.println("Connecté à la base de données");

			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT decision FROM choix WHERE etat='Endormi'");
			
			 while (resultSet.next()) {
				 System.out.println("Décision : " + resultSet.getString("decision"));
			 }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// @formatter:off
			if (resultSet != null) { try {resultSet.close();} catch(Exception ignored) {}}
			if (statement != null) { try {statement.close();} catch(Exception ignored) {}}
			if (connection != null) { try {connection.close();} catch(Exception ignored) {}}
			System.out.println("Connexion fermée");
			// @formatter:on
		}
	}

	/*
	 * Création de la DB dans l'interface HSQLDB:
	 * 
	 * create table if not exists choix (position integer, etat varchar(100) not null, decision
	 * varchar(100) not null);
	 * 
	 * insert into choix(1, etat, decision) values ('Endormi', 'Reveille. Porte G
	 * ?'); insert into choix(1, etat, decision) values ('Endormi', 'Reveille.
	 * Porte D ?');
	 * 
	 * commit;
	 */

}
