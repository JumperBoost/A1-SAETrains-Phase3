package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.util.Objects;

/**
 * Cette classe représente la vue d'une carte.
 * <p>
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarte extends StackPane {

    private final ICarte carte;
    private ImageView imageCarte;

    public VueCarte(ICarte carte) {
        this.carte = carte;
        ImageView iv = new ImageView("images/cartes/" + carte.getNom().replace(" ", "_").toLowerCase() + ".jpg");
        iv.setFitWidth(iv.getImage().getWidth() / 4);
        iv.setFitHeight(iv.getImage().getHeight() / 4);
        //set(iv);
        setStyle("-fx-background-color: transparent");

    }

    public void setCarteChoisieListener(EventHandler<MouseEvent> quandCarteEstChoisie) {
        setOnMouseClicked(quandCarteEstChoisie);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VueCarte vueCarte = (VueCarte) o;
        return Objects.equals(carte, vueCarte.carte);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carte);
    }

/*    public boolean estEgal(Carte carte) {
        if(carte.equals(carte))
            return true;
        return false;
    }*/

}
