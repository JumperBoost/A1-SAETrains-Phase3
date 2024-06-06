package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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
public class VueDuJeu extends VBox {

    private final IJeu jeu;
    @FXML
    private HBox cartesEnMain;
    @FXML
    private VuePlateau plateau;
    @FXML
    private Label instruction;
    @FXML
    private Label nomJoueur;
    @FXML
    private Button passer;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        plateau = new VuePlateau();
        instruction = new Label();
        nomJoueur = new Label();
        passer = new Button("Passer");
        cartesEnMain = new HBox();
        getChildren().addAll(plateau, nomJoueur, instruction, passer, cartesEnMain);
        setAlignment(Pos.CENTER);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/jeu.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private VueCarte trouverBoutonCarte(Carte carte) {
        for(Node buttonNode : cartesEnMain.getChildren()) {
            VueCarte button = (VueCarte) buttonNode;
            if(button.equals(carte))
                return button;
        }
        return null;
    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();
        instruction.textProperty().bind(jeu.instructionProperty());
        nomJoueur.textProperty().bind(new StringBinding() {
            {
                super.bind(jeu.joueurCourantProperty());
            }

            @Override
            protected String computeValue() {
                return jeu.joueurCourantProperty().getValue().getNom();
            }
        });
        passer.setOnMouseClicked(actionPasserParDefaut);
        jeu.joueurCourantProperty().addListener((observableValue, oldValue, newValue) -> {
            cartesEnMain.getChildren().clear();
            VueCarte vueCarte;
            ImageView iv;
            for(Carte carte : jeu.joueurCourantProperty().getValue().mainProperty()) {
                vueCarte = new VueCarte(carte);
                EventHandler<MouseEvent> changement = (mouseEvent -> {
                    VueCarte laCarte = (VueCarte) mouseEvent.getSource();
                    System.out.println("Choix de la carte " + carte.getNom() + ".");
                    jeu.joueurCourantProperty().getValue().uneCarteDeLaMainAEteChoisie(carte.getNom());
                });
                vueCarte.setCarteChoisieListener(changement);
                cartesEnMain.getChildren().add(vueCarte);
            }
        });

        for(IJoueur joueur : jeu.getJoueurs()) {
            joueur.mainProperty().addListener(new ListChangeListener<Carte>() {
                @Override
                public void onChanged(Change<? extends Carte> change) {
                    while (change.next()) {
                        if (change.wasRemoved())
                            cartesEnMain.getChildren().remove(trouverBoutonCarte(change.getRemoved().get(0)));
                    }
                }
            });
        }
    }

    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent ->  {
        System.out.println("Vous avez choisi Passer");
        getJeu().passerAEteChoisi();
    });

}
