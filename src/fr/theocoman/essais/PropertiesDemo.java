package fr.theocoman.essais;

import java.io.FileReader;
import java.util.Properties;

public class PropertiesDemo {
	public static void main(String[] args) {
		// Properties p = new Properties();
		//
		// p.put("root", "Vous dormez et un bruit vous réveille. Que faites-vous
		// ?");
		// p.put("choice1", "Je prends la porte à gauche");
		// p.put("choice2", "Je prends la porte à droite");
		// p.put("choice3", "Je me rendors");
		
		// p.put(new Date().toString(), "5.0");

		// On simule un long calcul: le système "dort" pendant 5000 ms. On doit
		// catcher une exception
		// try {
		// Thread.sleep(5000);
		// } catch (InterruptedException ignored) {}
		//
		// Interrogation d'un objet Properties par rapport à une key non
		// existante ==> prints "null"
		// System.out.println(p.get(new Date()));

		// Affichage par défaut: Java affiche l'objet Properties avec des
		// virgules entre chaque paire <key value>
		// System.out.println(p);

		// Interrogation d'un objet Properties par rapport à une key existante
		// System.out.println(p.get("choice1"));

		// Interrogation d'un objet Properties par rapport à une key non
		// existante ==> prints "null"
		// System.out.println(p.get("choice1000"));

		// Sauvegarde de l'objet
		// try (FileWriter writer = new
		// FileWriter("projetISN-sauvegardeDeProperties.props");){
		// p.store(writer, "Ceci est un commentaire optionnel");
		// System.out.println("L'objet p a été correctement sauvegardé");
		// } catch (Exception e) {
		// System.out.println("L'objet p n'a pu être sauvegardé");
		// e.printStackTrace();
		// }

		// Lecture de l'objet sauvegardé sur disque
		Properties newP = new Properties();
		try (FileReader reader = new FileReader("projetISN-sauvegardeDeProperties.props");) {
			newP.load(reader);
			System.out.println("L'objet newP a été correctement lu: " + newP);
		} catch (Exception e) {
			System.out.println("L'objet newP n'a pu être lu");
			e.printStackTrace();
		}
	}
}
