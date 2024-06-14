package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.TrainsIHM;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;

import java.util.*;

public class VueResultats extends Pane {

    private TrainsIHM ihm;

    public VueResultats(TrainsIHM ihm) {
        this.ihm = ihm;
    }

    public void creerBinding(){
        ihm.getJeu().finDePartieProperty().addListener(fini);
    };

    public void afficherResultat(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin de partie");
        Set<IJoueur> ordreGagnant = new TreeSet<>(new Comparator<IJoueur>() {
            @Override
            public int compare(IJoueur o1, IJoueur o2) {
                if (o1.getScoreTotal()>o2.getScoreTotal())
                    return -1;
                else if (o1.getScoreTotal()<o2.getScoreTotal())
                    return 1;
                else if (!o1.equals(o2)) {
                    return 1;
                }
                return 0;
            }
        });
        for (IJoueur j : ihm.getJeu().getJoueurs()){
            ordreGagnant.add(j);
        }
        String texte = "Le gagnant est : ";
        Iterator<IJoueur> it = ordreGagnant.iterator();
        IJoueur j;
        int podium = 1;
        while (it.hasNext()){
            j = it.next();
            if (podium==1)
                texte += j.getNom() + " avec un score total de " + j.getScoreTotal() + "\n";
            texte = texte + podium + "eme " + j.getNom() + " - score : " + j.getScoreTotal() + "\n";
            podium++;
        }
        alert.setContentText(texte);
        MediaPlayer applaudissement = TrainsIHM.creerMusique("src/main/resources/musique/applaudissement.mp3");
        getChildren().add(new MediaView(applaudissement));
        applaudissement.play();
        alert.showAndWait();
        Platform.exit();
    }

    ChangeListener<Boolean> fini = new ChangeListener<Boolean>(){
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean booleanProperty, Boolean t1) {
            if (t1){
                System.out.println("FINI");
                afficherResultat();
            }
        }
    };

}
