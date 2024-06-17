package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.text.Normalizer;

/**
 * Cette classe représente la vue d'une carte.
 * <p>
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarte extends StackPane {

    private final ICarte carte;
    private final ImageView imageView;

    public VueCarte(ICarte carte, double ratioRedimensionnage) {
        this.carte = carte;
        String nomCarte = Normalizer.normalize(carte.getNom().replaceAll(" ", "_").toLowerCase(), Normalizer.Form.NFKD).replaceAll("\\p{M}", "");
        imageView = new ImageView(String.valueOf(getClass().getClassLoader()
                .getResource("images/cartes/" + nomCarte + ".jpg")));
        imageView.setFitWidth(imageView.getImage().getWidth() * ratioRedimensionnage);
        imageView.setFitHeight(imageView.getImage().getHeight() * ratioRedimensionnage);
        getChildren().add(imageView);
    }

    public VueCarte(ICarte carte) {
        this(carte, 0.2);
    }

    public void setCarteChoisieListener(EventHandler<MouseEvent> quandCarteEstChoisie) {
        setOnMouseClicked(quandCarteEstChoisie);
    }

    public void setNbCartes(IntegerProperty nbCartesProperty) {
        // Suppression de l'ancien StackPane si déjà existant
        getChildren().stream().filter(node -> node instanceof StackPane).findFirst().ifPresent(node -> getChildren().remove(node));
        StackPane conteneur = new StackPane();
        conteneur.translateXProperty().bind(Bindings.multiply((double) 25 / 75, imageView.fitWidthProperty()));
        conteneur.translateYProperty().bind(Bindings.multiply((double) 40 / 75, imageView.fitWidthProperty()));

        Circle fondNbCartes = new Circle(0, Color.WHITE);
        fondNbCartes.radiusProperty().bind(Bindings.multiply((double) 10 / 75, imageView.fitWidthProperty()));
        fondNbCartes.setStroke(Color.BLACK);
        Label nbCartes = new Label();
        nbCartes.textProperty().bind(Bindings.convert(nbCartesProperty));
        nbCartes.styleProperty().bind(Bindings.concat("-fx-font-size: ", Bindings.multiply((double) 16 / 75, imageView.fitWidthProperty())));
        conteneur.getChildren().addAll(fondNbCartes, nbCartes);
        getChildren().add(conteneur);

        // Désactivation de la carte si nbCartes est égale à 0
        disableProperty().bind(nbCartesProperty.isEqualTo(0));
    }

    public ICarte getCarte() {
        return carte;
    }

    public ImageView getImageView() {
        return imageView;
    }

}
