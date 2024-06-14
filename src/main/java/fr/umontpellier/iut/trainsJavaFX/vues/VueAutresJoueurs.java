package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.CouleurJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.Joueur;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import java.lang.classfile.Label;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends VBox {

    private VueDuJeu vueDuJeu;

    private List<IJoueur> joueurs = new ArrayList<>();

    public VueAutresJoueurs() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/autresJoueurs.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ajoutDesJoueurs(){
        IJeu jeu = vueDuJeu.getJeu();
        getChildren().clear();

        VBox joueur;
        Label nomJoueur;
        ImageView scoreI;
        ImageView railI;

        Label score;
        Label rail;


        HBox donnee;
        for (IJoueur j : jeu.getJoueurs()){
            if (j != jeu.joueurCourantProperty().get()) {
                joueurs.add(j);
                nomJoueur = new Label(j.getNom());
                score = new Label(String.valueOf(j.scoreProperty().get()));
                rail = new Label(String.valueOf(j.nbJetonsRailsProperty().getValue()));

                scoreI = new ImageView(new Image("images/icons/score.png"));
                scoreI.fitHeightProperty().set(30);
                scoreI.fitWidthProperty().set(30);
                railI = new ImageView(new Image("images/icons/" + CouleursJoueurs.nomCouleursCubeJoueur.get(j.getCouleur())));
                railI.fitHeightProperty().set(30);
                railI.fitWidthProperty().set(30);

                donnee = new HBox(scoreI, score, railI, rail);
                donnee.setSpacing(10);
                joueur = new VBox();
                joueur.getChildren().addAll(nomJoueur, donnee);

                getChildren().add(joueur);
            }
        }
    }

    public void creerBinding(){
        vueDuJeu = (VueDuJeu) getParent().getParent();
        setAlignment(Pos.TOP_CENTER);
        setSpacing(20);
        setPadding(new Insets(20));
        ajoutDesJoueurs();
        vueDuJeu.getJeu().joueurCourantProperty().addListener(new ChangeListener<IJoueur>() {
            @Override
            public void changed(ObservableValue<? extends IJoueur> observableValue, IJoueur ancien, IJoueur nouveau) {
                if (nouveau!=ancien){
                    ajoutDesJoueurs();
                }
            }
        });
        setMinSize(getMinHeight(), getMinWidth());
        for (Node j : getChildren()){
            VBox joueur = (VBox) j;
            joueur.minHeightProperty().bind(new DoubleBinding() {
                {
                    super.bind(getParent().getScene().heightProperty());
                }
                @Override
                protected double computeValue() {
                    return heightProperty().getValue() / getChildren().size();
                }
            });
            joueur.minWidthProperty().bind(new DoubleBinding() {
                {
                    super.bind(getParent().getScene().widthProperty());
                }
                @Override
                protected double computeValue() {
                    return widthProperty().getValue() / getChildren().size();
                }
            });
        }

    }
}
