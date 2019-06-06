package fr.aylox;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Morpion {

	public static class MorpionJeu{
		int[][] grille;

		public MorpionJeu() {
			grille = new int[3][3];
			for(int i = 0; i < grille.length; ++i) {
				for(int j = 0; j < grille[i].length; ++j) {
					grille[i][j] = -1;
				}	
			}
		}

		public int autreJoueur(int joueur) {
			return joueur == 0 ? 1 : 0;
		}

		public String nomJoueur(int joueur) {
			return joueur == 0 ? "Joueur 1" : "Joueur 2";
		}

		public void enregistrerCoupJoueur(int i , int j, int joueur) {
			if(0 <= i && i < grille.length && 0 <= j && j < grille[i].length) {
				grille[i][j] = joueur;
			}
		}

		public boolean grilleEstRemplie() {
			for(int i = 0; i < grille.length; ++i) {
				for(int j = 0; j < grille[i].length; ++j) {
					if(grille[i][j] == -1)
						return false;
				}	
			}		
			return true;
		}

		public boolean aGagne(int joueur) {
			//Horizontal
			for(int i = 0; i < grille.length; ++i) {
				boolean all = true;
				for(int j = 0; j < grille[i].length; ++j) {
					if(grille[i][j] != joueur) {
						all = false;
						break;
					}
				}
				if(all)
					return true;
			}

			//Vertical
			if(grille.length > 0)
				for(int j = 0; j < grille[0].length; ++j) {
					boolean all = true;
					for(int i = 0; i < grille.length; ++i) {
						if(grille[i][j] != joueur) {
							all = false;
							break;
						}
					}
					if(all)
						return true;
				}

			//Diagonal
			/*
			 * 0 => [0, 1, 2]
			 * 1 => [0, 1, 2]
			 * 2 => [0, 1, 2]
			 * 
			 */
			if(grille[0][0] == joueur && grille[1][1] == joueur && grille[2][2] == joueur)
				return true;

			if(grille[0][2] == joueur && grille[1][1] == joueur && grille[2][0] == joueur)
				return true;


			return false;
		}

	}

	public static class MorpionIHM extends Application{
		public static void main(String[] args) {
			Application.launch(args);
		}
		
		private Image[] icones = new Image[2];
		private MorpionJeu jeu;
		private Label barreEtat;
		public int joueurCourant;
		private GridPane pane;

		private void chargerIcone() {
			icones[0] = new Image("croix.jpg"); 
			icones[1] = new Image("rond.jpg"); 
		}

		@Override
		public void start(Stage stage) throws Exception {
			stage.setTitle("Jeu de Morpion");
			Scene scene = new Scene(creerGrille());
			chargerIcone();
			jeu = new MorpionJeu();
			stage.setScene(scene);
			stage.show();
		}

		private GridPane creerGrille() {
			pane  = new GridPane();
			for(int i = 0; i < 3; ++i) {
				for(int j = 0; j < 3; ++j) {
					BoutonJeu jeu = new BoutonJeu(i, j);
					jeu.setOnAction(auditeurJeu);
					jeu.setMinSize(80, 80);
					jeu.setPrefSize(80, 80);
					jeu.setMaxSize(80, 80);
					pane.add(jeu, i, j);
				}
			}
			barreEtat = new Label();
			pane.add(barreEtat, 0, 3, 3, 1);
			return pane;
		}
		
		
		private final EventHandler<ActionEvent> auditeurJeu = event -> {
			if(event.getSource() instanceof BoutonJeu) {
				BoutonJeu btn = (BoutonJeu) event.getSource();
				btn.setGraphic(new ImageView(icones[joueurCourant]));
				btn.setDisable(true);
				jeu.enregistrerCoupJoueur(btn.getLigne(), btn.getColonne(), joueurCourant);
				if(jeu.aGagne(joueurCourant)) {
					barreEtat.setText(jeu.nomJoueur(joueurCourant) + " a gagné");
					for(Node o : pane.getChildren()) {
						if(o instanceof BoutonJeu) {
							((BoutonJeu)o).setDisable(true);
						}
					}
					return;
				}
				if(jeu.grilleEstRemplie()) {
					barreEtat.setText("Egalité");
				}
				joueurCourant = jeu.autreJoueur(joueurCourant);
			}
		};

		public class BoutonJeu extends Button{
			private int ligne;
			private int colonne;

			BoutonJeu(int ligne, int colonne){
				this.ligne = ligne;
				this.colonne = colonne;
			}

			public int getLigne() {
				return ligne;
			}

			public int getColonne() {
				return colonne;
			}
		}
		
		
	}
	
	

	
}
