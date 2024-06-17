package fr.umontpellier.iut.trainsJavaFX;

import fr.umontpellier.iut.trainsJavaFX.mecanique.Jeu;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.FabriqueListeDeCartes;
import fr.umontpellier.iut.trainsJavaFX.mecanique.plateau.Plateau;
import fr.umontpellier.iut.trainsJavaFX.vues.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javafx.beans.property.*;
import javafx.scene.media.*;
import javafx.util.Duration;

public class TrainsIHM extends Application {
    private VueChoixJoueurs vueChoixJoueurs;
    private Stage primaryStage;
    private Jeu jeu;

    private final boolean avecVueChoixJoueurs = false;
    private BooleanProperty commencer = new SimpleBooleanProperty(false);

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        ouvrir();
    }

    private void debuterJeu() {
        if (avecVueChoixJoueurs) {
            vueChoixJoueurs = new VueChoixJoueurs();
            vueChoixJoueurs.setNomsDesJoueursDefinisListener(quandLesNomsJoueursSontDefinis);
            vueChoixJoueurs.show();
        } else {
            demarrerPartie();
        }
    }

    public void ouvrir(){
        String[] nomsJoueurs;
        Plateau plateau = Plateau.OSAKA;
        VueOuverture ouverture = new VueOuverture();
        ouverture.creerBindings();
        Scene scene = new Scene(ouverture, Screen.getPrimary().getBounds().getWidth() * DonneesGraphiques.pourcentageEcran, Screen.getPrimary().getBounds().getHeight() * DonneesGraphiques.pourcentageEcran);
        primaryStage.setMinWidth(1160);
        primaryStage.setMinHeight(755);
        primaryStage.setMaxWidth(Screen.getPrimary().getBounds().getWidth());
        primaryStage.setMaxHeight(Screen.getPrimary().getBounds().getHeight());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Trains");
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(event -> {
            this.arreterJeu();
            event.consume();
        });
        primaryStage.show();
        if (avecVueChoixJoueurs) {
            nomsJoueurs = vueChoixJoueurs.getNomsJoueurs().toArray(new String[0]);
            plateau = vueChoixJoueurs.getPlateau();
        } else {
            commencer.bind(ouverture.partiePrete());
            commencer.addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    if (t1) {
                        String[] nomsJoueurs = ouverture.getNomJoueurs();
                        List<String> cartesPreparation = new ArrayList<>(FabriqueListeDeCartes.getNomsCartesPreparation());
                        Collections.shuffle(cartesPreparation);
                        String[] nomsCartes = cartesPreparation.subList(0, 8).toArray(new String[0]);
                        jeu = new Jeu(nomsJoueurs, nomsCartes, plateau);
                        demarrerPartie();
                    }
                }
            });

        }
        // Tirer aléatoirement 8 cartes préparation

    }

    public static void chargerMusiques() {
        MediaPlayer train_aventure = new MediaPlayer(new Media(TrainsIHM.class.getClassLoader().getResource("musique/train_aventure.mp3").toExternalForm()));
        train_aventure.setVolume(0.2);
        train_aventure.setCycleCount(MediaPlayer.INDEFINITE);
        MediaView train_aventure_view = new MediaView(train_aventure);
        train_aventure_view.setId("sound_train_aventure");

        MediaPlayer argent = new MediaPlayer(new Media(TrainsIHM.class.getClassLoader().getResource("musique/argent.mp3").toExternalForm()));
        argent.setVolume(0.25);
        MediaView argent_view = new MediaView(argent);
        argent_view.setId("sound_argent");

        MediaPlayer applaudissement = new MediaPlayer(new Media(TrainsIHM.class.getClassLoader().getResource("musique/applaudissement.mp3").toExternalForm()));
        applaudissement.setVolume(0.2);
        MediaView applaudissement_view = new MediaView(applaudissement);
        applaudissement_view.setId("sound_applaudissement");

        List<MediaView> mediaViewList = new ArrayList<>(List.of(train_aventure_view, argent_view, applaudissement_view));
        mediaViewList.forEach(mediaView -> mediaView.getMediaPlayer().setOnEndOfMedia(() -> {
            mediaView.getMediaPlayer().stop();
            mediaView.getMediaPlayer().seek(Duration.ZERO);
        }));
        GestionJeu.getVueDuJeu().getChildren().addAll(mediaViewList);
    }

    public static void lancerMusique(String nom) {
        MediaView mediaView = (MediaView) GestionJeu.getVueDuJeu().getChildren().stream().filter(c -> c instanceof MediaView && c.getId().equals("sound_" + nom)).findFirst().orElse(null);
        if(mediaView != null)
            mediaView.getMediaPlayer().play();
    }

    public static void arreterMusique(String nom) {
        MediaView mediaView = (MediaView) GestionJeu.getVueDuJeu().getChildren().stream().filter(c -> c instanceof MediaView && c.getId().equals("sound_" + nom)).findFirst().orElse(null);
        if(mediaView != null) {
            mediaView.getMediaPlayer().stop();
            mediaView.getMediaPlayer().seek(Duration.ZERO);
        }
    }

    public void demarrerPartie() {

        GestionJeu.setJeu(jeu);
        VueDuJeu vueDuJeu = new VueDuJeu(jeu);

        Scene scene = new Scene(vueDuJeu, Screen.getPrimary().getBounds().getWidth() * DonneesGraphiques.pourcentageEcran, Screen.getPrimary().getBounds().getHeight() * DonneesGraphiques.pourcentageEcran); // la scene doit être créée avant de mettre en place les bindings
        vueDuJeu.creerBindings();
        jeu.run(); // le jeu doit être démarré après que les bindings ont été mis en place

        VueResultats vueResultats = new VueResultats(this);
        vueResultats.creerBinding();
        primaryStage.setMinWidth(1160);
        primaryStage.setMinHeight(755);
        primaryStage.setMaxWidth(Screen.getPrimary().getBounds().getWidth());
        primaryStage.setMaxHeight(Screen.getPrimary().getBounds().getHeight());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Trains");
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(event -> {
            this.arreterJeu();
            event.consume();
        });
    }

    private final ListChangeListener<String> quandLesNomsJoueursSontDefinis = change -> {
        if (!vueChoixJoueurs.getNomsJoueurs().isEmpty())
            demarrerPartie();
    };

    public void arreterJeu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("On arrête de jouer ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    public Jeu getJeu() {
        return jeu;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}