package fr.theocoman.essais;

import java.io.FileReader;
import java.util.Properties;

public class PropertiesDemo {
	public static void main(String[] args) {
		// Properties p = new Properties();
		//
		// p.put("root", "Vous dormez et un bruit vous r�veille. Que faites-vous
		// ?");
		// p.put("choice1", "Je prends la porte � gauche");
		// p.put("choice2", "Je prends la porte � droite");
		// p.put("choice3", "Je me rendors");
		
		// p.put(new Date().toString(), "5.0");

		// On simule un long calcul: le syst�me "dort" pendant 5000 ms. On doit
		// catcher une exception
		// try {
		// Thread.sleep(5000);
		// } catch (InterruptedException ignored) {}
		//
		// Interrogation d'un objet Properties par rapport � une key non
		// existante ==> prints "null"
		// System.out.println(p.get(new Date()));

		// Affichage par d�faut: Java affiche l'objet Properties avec des
		// virgules entre chaque paire <key value>
		// System.out.println(p);

		// Interrogation d'un objet Properties par rapport � une key existante
		// System.out.println(p.get("choice1"));

		// Interrogation d'un objet Properties par rapport � une key non
		// existante ==> prints "null"
		// System.out.println(p.get("choice1000"));

		// Sauvegarde de l'objet
		// try (FileWriter writer = new
		// FileWriter("projetISN-sauvegardeDeProperties.props");){
		// p.store(writer, "Ceci est un commentaire optionnel");
		// System.out.println("L'objet p a �t� correctement sauvegard�");
		// } catch (Exception e) {
		// System.out.println("L'objet p n'a pu �tre sauvegard�");
		// e.printStackTrace();
		// }

		// Lecture de l'objet sauvegard� sur disque
		Properties newP = new Properties();
		try (FileReader reader = new FileReader("projetISN-sauvegardeDeProperties.props");) {
			newP.load(reader);
			System.out.println("L'objet newP a �t� correctement lu: " + newP);
		} catch (Exception e) {
			System.out.println("L'objet newP n'a pu �tre lu");
			e.printStackTrace();
		}
	}
}
