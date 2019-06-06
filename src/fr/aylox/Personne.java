package fr.aylox;



import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Personne {

	public static class PionJeu{

		public static PionJeu VIDE = new PionJeu("vide.png"),
				ROUGE = new PionJeu("rouge.png"),
				BLEU = new PionJeu("bleu.png"),
				FUCHSIA = new PionJeu("fuchsia.png"),
				JAUNE = new PionJeu("jaune.png"),
				MARRON = new PionJeu("marron.png"),
				ORANGE = new PionJeu("orange.png"),
				VERT = new PionJeu("vert.png"),
				VIOLET = new PionJeu("violet.png");

		public static PionJeu[] all = new PionJeu[] {ROUGE, BLEU, FUCHSIA, JAUNE, MARRON, ORANGE, VERT, VIOLET};

		private Image image;

		public PionJeu(String fileName) {
			this.image = new Image(fileName);
		}

		public Image getImage() {
			return image;
		}

		public PionJeu suivant() {
			int actual = 0;
			for(PionJeu pj : all) {
				if(pj == this)
					break;
				++actual;
			}
			int next = (actual+1)%all.length;
			return all[next];
		}
	}

	public static class CaseJeu extends Button{
		private ImageView imageView;
		private PionJeu pion;
		private EventHandler<ActionEvent> boutonPionJeuListener = (event) -> {
			if(getPion() != null)
				setPion(getPion().suivant());
		};

		public CaseJeu() {
			vider();
			setDisable(false);
		}

		public PionJeu getPion() {
			return pion;
		}

		public void setPion(PionJeu pion) {
			this.pion = pion;
			if(pion != null)
				imageView.setImage(pion.getImage());
		}

		public void vider() {
			setPion(PionJeu.VIDE);
		}

		public void setMasque(boolean masque) {
			this.setVisible(masque);
		}


	}

	public static class Combinaison{

		private int taille;
		PionJeu[] pions;

		public Combinaison(int taille) {
			this.taille = taille;
			pions = new PionJeu[taille];
			for(int i = 0; i < taille; ++i)
				pions[i] = PionJeu.VIDE;
		}

		public PionJeu getPion(int i){
			if(i < 0 || i >= taille)
				return PionJeu.VIDE;
			return pions[i];
		}

		public void setPion(int i, PionJeu pion) {
			if(i < 0 || i >= taille)
				return;
			pions[i] = pion;
		}

		public int getTaille() {
			return taille;
		}

		public boolean contient(PionJeu pion) {
			for(PionJeu p : pions)
				if(pion == p)
					return true;
			return false;
		}

		public static Combinaison genererCombinaisonAleatoire(int taille) {
			Combinaison c = new Combinaison(taille);
			for(int i = 0; i < taille; ++i) {
				c.setPion(i,PionJeu.all[(int) (Math.random() * (taille-1))]);
			}
			return c;
		}
	}
	
	public static class Rangee extends HBox{
		
		private CaseJeu[] pions;
		private int taille;
		
		public Rangee(int taille) {
			this.taille = taille;
			pions = new CaseJeu[taille];
			vider();
			getChildren().addAll(pions);
		}
		
		public void vider() {
			for(int i = 0; i < taille; ++i) {
				pions[i] = new CaseJeu();
			}
		}
		public void setMasque(boolean enabled) {
			for(int i = 0; i < taille; ++i) {
				pions[i].setMasque(enabled);
			}
		}
		
		public void setActive(boolean enabled) {
			for(int i = 0; i < taille; ++i) {
				// ??? 
			}	
		}
		
		public void setCombinaison(Combinaison c) {
			for(int i = 0; i < taille && i < c.getTaille(); ++i) {
				pions[i].setPion(c.getPion(i));
			}
		}
		
		public Combinaison getCombinaison() {
			Combinaison c = new Combinaison(taille);
			for(int i = 0; i < taille; ++i) {
				c.setPion(i, pions[i].getPion());
			}
			return c;
		}
		
	}
	
	public static class Plateau{
		private int nombrePionsParRangee;
		private int nombreRangee;
		private Rangee rangeeSecrete;
		private Combinaison combinaisonSecrete;
		private Rangee[] rangees;
		private IntegerProperty nombreDeCoupsJoues = new SimpleIntegerProperty(0);
		private BooleanProperty aGagne = new SimpleBooleanProperty();
		private BooleanProperty aPerdu = new SimpleBooleanProperty();
		private BooleanProperty estPartieTerminee = new SimpleBooleanProperty();
		
		public Plateau() {
			
		}
		
		public void createBindings() {
			aPerdu.bind(Bindings.greaterThan(nombreDeCoupsJoues, nombreRangee));
			estPartieTerminee.bind(Bindings.or(aGagne, aPerdu));
			estPartieTerminee.addListener(event -> {});
		}
	}
	
}
