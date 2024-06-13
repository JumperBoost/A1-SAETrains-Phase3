package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

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
    private Label nomJoueur;

    public VueDuJeu(IJeu jeu) {
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

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();
        joueurCourant.creerBindings();
        autresJoueurs.creerBinding();
        instruction.textProperty().bind(jeu.instructionProperty());
        minHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(bottomProperty(), leftProperty(), topProperty(), centerProperty(), rightProperty());
            }
            @Override
            protected double computeValue() {
                double taille = joueurCourant.heightProperty().get() + reserve.heightProperty().get() + nomJoueur.heightProperty().get();
                return taille;
            }
        });
        minWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(bottomProperty(), leftProperty(), topProperty(), centerProperty(), rightProperty());
            }
            @Override
            protected double computeValue() {
                double taille = joueurCourant.widthProperty().get() + reserve.widthProperty().get() + nomJoueur.widthProperty().get();
                return taille;
            }
        });
        nomJoueur.textProperty().bind(new StringBinding() {
            {
                super.bind(jeu.joueurCourantProperty());
            }

            @Override
            protected String computeValue() {
                return jeu.joueurCourantProperty().getValue().getNom();
            }
        });
    }

    public IJeu getJeu() {
        return jeu;
    }
}
