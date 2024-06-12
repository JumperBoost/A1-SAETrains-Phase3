package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {
    @FXML
    private HBox cartesEnMain;
    @FXML
    private Button passer;
    @FXML
    private Label pioche;
    @FXML
    private Label defausse;
    @FXML
    private Label rails;
    @FXML
    private ImageView jetons_img;
    @FXML
    private Label jetons;
    @FXML
    private Label argent;
    @FXML
    private Label scoreTotal;
    @FXML
    private HBox cartesEnJeu;
    @FXML
    private HBox cartesRecues;

    private VueDuJeu vueDuJeu;

    public VueJoueurCourant() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/joueurCourant.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestion graphique pile de cartes
     */

    private String getPaneName(HBox box, Carte carte) {
        return box.getId() + "_" + carte.getNom().replaceAll(" ", "_").toLowerCase();
    }

    private void retirerCarte(HBox box, Carte carte) {
        Pane pane = (Pane) box.getChildren().stream().filter(p -> p.getId().equals(getPaneName(box, carte))).findFirst().orElse(null);
        if(pane != null) {
            ImageView iv = ((VueCarte) pane.getChildren().get(0)).getImageView();
            pane.getChildren().remove(pane.getChildren().size()-1);
            pane.setPrefWidth(iv.getFitWidth() + 10 * (pane.getChildren().size()-1));
            if(pane.getChildren().isEmpty())
                box.getChildren().remove(pane);
        }
    }

    private void mettreAJourPositionPane(Pane pane, VueCarte vueCarte) {
        pane.setPrefWidth(vueCarte.getImageView().getFitWidth() + 10 * (pane.getChildren().size()-1));
        int layoutX = 0;
        for(Node children : pane.getChildren()) {
            VueCarte carteChildren = (VueCarte) children;
            carteChildren.setLayoutX(layoutX);
            layoutX += 10;
        }
    }

    private void ajouterCarte(HBox box, Carte carte) {
        Pane pane = (Pane) box.getChildren().stream().filter(p -> p.getId().equals(getPaneName(box, carte))).findFirst().orElse(null);
        if(pane == null) {
            pane = new Pane();
            pane.setId(getPaneName(box, carte));
            box.getChildren().add(pane);
        }
        VueCarte vueCarte = new VueCarte(carte);
        vueCarte.setCarteChoisieListener(actionChoisirCarte);
        pane.getChildren().add(vueCarte);
        mettreAJourPositionPane(pane, vueCarte);
    }

    private void reinitialiserPaquetCartes(HBox box, ListeDeCartes listeDeCartes) {
        box.getChildren().clear();
        List<Pane> panes = new ArrayList<>();
        for(Carte carte : listeDeCartes) {
            VueCarte vueCarte = new VueCarte(carte);
            vueCarte.setCarteChoisieListener(actionChoisirCarte);
            Pane pane = panes.stream().filter(p -> p.getId() != null && p.getId().equals(getPaneName(box, carte))).findFirst().orElse(null);
            if(pane == null) {
                pane = new Pane();
                pane.setId(getPaneName(box, carte));
                panes.add(pane);
            }
            pane.getChildren().add(vueCarte);
        }
        for(Pane pane : panes) {
            VueCarte vueCarte = (VueCarte) pane.getChildren().get(0);
            mettreAJourPositionPane(pane, vueCarte);
        }
        box.getChildren().addAll(panes);
    }

    private void ajouterListeners(ListeDeCartes listeDeCartes, HBox box) {
        listeDeCartes.addListener((ListChangeListener<Carte>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    for (Carte c : change.getRemoved())
                        retirerCarte(box, c);
                } else if (change.wasAdded()) {
                    for (Carte c : change.getAddedSubList())
                        ajouterCarte(box, c);
                }

            }
        });
    }

    public void creerBindings() {
        vueDuJeu = (VueDuJeu) getParent().getParent();

        passer.setOnMouseClicked(actionPasserParDefaut);
        // Affichage des cartes
        vueDuJeu.getJeu().joueurCourantProperty().addListener((observableValue, oldValue, newValue) -> {
            IJoueur joueur = newValue;
            // Changement du joueur courant
            reinitialiserPaquetCartes(cartesEnMain, joueur.mainProperty());
            reinitialiserPaquetCartes(cartesEnJeu, joueur.cartesEnJeuProperty());
            reinitialiserPaquetCartes(cartesRecues, joueur.cartesRecuesProperty());

            // Affichages des données
            pioche.textProperty().bind(Bindings.convert(joueur.piocheProperty().sizeProperty()));
            defausse.textProperty().bind(Bindings.convert(joueur.defausseProperty().sizeProperty()));
            rails.textProperty().bind(Bindings.convert(joueur.pointsRailsProperty()));
            jetons.textProperty().bind(Bindings.convert(joueur.nbJetonsRailsProperty()));
            argent.textProperty().bind(Bindings.convert(joueur.argentProperty()));
            scoreTotal.textProperty().bind(Bindings.convert(joueur.scoreProperty()));

            // Affichage des logos personnalisés
            jetons_img.setImage(new Image(String.valueOf(getClass().getClassLoader().getResource("images/icons/" + CouleursJoueurs.nomCouleursCubeJoueur.get(joueur.getCouleur())))));
        });

        for(IJoueur joueur : vueDuJeu.getJeu().getJoueurs()) {
            ajouterListeners(joueur.mainProperty(), cartesEnMain);
            ajouterListeners(joueur.cartesEnJeuProperty(), cartesEnJeu);
            ajouterListeners(joueur.cartesRecuesProperty(), cartesRecues);
        }
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent ->  {
        System.out.println("Vous avez choisi Passer");
        vueDuJeu.getJeu().passerAEteChoisi();
    });

    EventHandler<MouseEvent> actionChoisirCarte = (mouseEvent -> {
        VueCarte vueCarte = (VueCarte) mouseEvent.getSource();
        if(vueCarte.getParent().getId().startsWith(cartesEnMain.getId() + "_"))
            vueDuJeu.getJeu().joueurCourantProperty().getValue().uneCarteDeLaMainAEteChoisie(vueCarte.getCarte().getNom());
    });
}