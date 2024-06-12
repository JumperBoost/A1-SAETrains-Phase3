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
    private ImageView imageView;

    public VueCarte(ICarte carte) {
        this.carte = carte;
        imageView = new ImageView(String.valueOf(getClass().getClassLoader()
                .getResource("images/cartes/" + carte.getNom().replaceAll(" ", "_").toLowerCase() + ".jpg")));
        imageView.setFitWidth(imageView.getImage().getWidth() / 5);
        imageView.setFitHeight(imageView.getImage().getHeight() / 5);
        getChildren().add(imageView);
    }

    public void setCarteChoisieListener(EventHandler<MouseEvent> quandCarteEstChoisie) {
        setOnMouseClicked(quandCarteEstChoisie);
    }

    public ICarte getCarte() {
        return carte;
    }

    public ImageView getImageView() {
        return imageView;
    }

}
