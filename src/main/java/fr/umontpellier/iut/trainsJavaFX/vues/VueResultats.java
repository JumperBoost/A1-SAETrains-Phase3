package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.TrainsIHM;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;

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
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", new ButtonType[]{ new ButtonType("Oui"), new ButtonType("Non") });
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
        alert.setHeaderText(texte);
        alert.setContentText("Voulez-vous relancer une partie ?");
        TrainsIHM.arreterMusique("train_aventure");
        TrainsIHM.lancerMusique("applaudissement");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get().getText().equals("Oui")) {
            ihm.debuterJeu();
        } else Platform.exit();
    }

    ChangeListener<Boolean> fini = new ChangeListener<Boolean>(){
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
            if (newValue){
                afficherResultat();
            }
        }
    };

}
