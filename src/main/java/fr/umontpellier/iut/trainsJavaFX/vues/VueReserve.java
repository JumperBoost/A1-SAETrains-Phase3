package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import javafx.beans.property.IntegerProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import java.io.IOException;
import java.util.Map;

public class VueReserve extends FlowPane {
    public VueReserve() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/reserve.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getChildren().clear();
        IJeu jeu = GestionJeu.getJeu();
        for(Map.Entry<String, IntegerProperty> entry : jeu.getTaillesPilesReserveProperties().entrySet()) {
            VueCarte carte = new VueCarte(jeu.getReserve().getCarte(entry.getKey()));
            carte.setCarteChoisieListener(actionChoisirCarte);
            carte.setNbCartes(entry.getValue());
            getChildren().add(carte);
        }
    }

    EventHandler<MouseEvent> actionChoisirCarte = (mouseEvent -> {
        VueCarte vueCarte = (VueCarte) mouseEvent.getSource();
        GestionJeu.getJeu().uneCarteDeLaReserveEstAchetee(vueCarte.getCarte().getNom());
    });
}
