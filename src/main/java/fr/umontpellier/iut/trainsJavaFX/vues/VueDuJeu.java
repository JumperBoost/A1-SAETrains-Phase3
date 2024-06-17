package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.TrainsIHM;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 *
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 * <p>
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, ses cartes en main, son score, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends BorderPane {

    private final IJeu jeu;
    @FXML
    private VuePlateau plateau;
    @FXML
    private VueJoueurCourant joueurCourant;
    @FXML
    private VueAutresJoueurs autresJoueurs;
    @FXML
    private VueReserve reserve;
    @FXML
    private Label instruction;
    @FXML
    private HBox topBox;
    @FXML
    private Label nomJoueur;
    @FXML
    private VBox conteneurChoix;
    @FXML
    private FlowPane boxChoix;
    @FXML
    private Pane zoomCarte;

    public VueDuJeu(IJeu jeu) {
        GestionJeu.setVueDuJeu(this);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/jeu.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.jeu = jeu;
    }

    @FXML
    private void initialize() {
        TrainsIHM.chargerMusiques();
        TrainsIHM.lancerMusique("train_aventure");
    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();
        joueurCourant.creerBindings();
        autresJoueurs.creerBindings();
        instruction.textProperty().bind(jeu.instructionProperty());
        nomJoueur.textProperty().bind(new StringBinding() {
            {
                super.bind(jeu.joueurCourantProperty());
            }

            @Override
            protected String computeValue() {
                topBox.styleProperty().setValue("-fx-background-color: " + CouleursJoueurs.couleursBackgroundJoueur.get(jeu.joueurCourantProperty().getValue().getCouleur()));
                return jeu.joueurCourantProperty().getValue().getNom();
            }
        });
    }

    public void setZoomCarte(ICarte carte) {
        zoomCarte.getChildren().clear();
        zoomCarte.setVisible(carte != null);
        if(carte != null)
            zoomCarte.getChildren().add(new VueCarte(carte, 0.5));
    }

    public VBox getConteneurChoix() {
        return conteneurChoix;
    }

    public FlowPane getBoxChoix() {
        return boxChoix;
    }

    public IJeu getJeu() {
        return jeu;
    }
}
