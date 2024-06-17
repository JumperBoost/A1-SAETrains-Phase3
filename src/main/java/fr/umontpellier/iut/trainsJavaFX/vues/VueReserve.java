package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.TrainsIHM;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.IntegerProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.util.Map;

public class VueReserve extends VBox {
    @FXML
    private FlowPane cartesReserve;

    public VueReserve() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/reserve.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cartesReserve.getChildren().clear();
        IJeu jeu = GestionJeu.getJeu();
        for(Map.Entry<String, IntegerProperty> entry : jeu.getTaillesPilesReserveProperties().entrySet()) {
            VueCarte carte = new VueCarte(jeu.getReserve().getCarte(entry.getKey()));
            carte.setCarteChoisieListener(actionChoisirCarte);
            carte.setNbCartes(entry.getValue());
            cartesReserve.getChildren().add(carte);
        }
    }

    public void creerBindings(){
        minHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(getParent().getScene().heightProperty());
            }
            @Override
            protected double computeValue() {
                return getParent().getScene().heightProperty().getValue() / 2;
            }
        });
    }

    EventHandler<MouseEvent> actionChoisirCarte = (mouseEvent -> {
        VueCarte vueCarte = (VueCarte) mouseEvent.getSource();
        MediaPlayer argent = TrainsIHM.creerMusique("src/main/resources/musique/argent.mp3");
        argent.setAutoPlay(true);
        argent.play();
        GestionJeu.getJeu().uneCarteDeLaReserveEstAchetee(vueCarte.getCarte().getNom());
    });
}
