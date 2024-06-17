package fr.umontpellier.iut.trainsJavaFX.vues;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 *
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 * <p>
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, ses cartes en main, son score, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueOuverture extends VBox {

    @FXML
    private VBox conteneurChoix;
    @FXML
    private TextField nbJoueursChoix;
    @FXML
    private Button nbJoueursChoisi;
    @FXML
    private Label erreur;
    @FXML
    private Button commencer;

    private BooleanProperty partieInitialiser = new SimpleBooleanProperty(false);

    private VBox ecritureNomJoueurs = new VBox();

    public VueOuverture() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/ouverture.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void creerBindings(){

    }

    public void effacerAncienBouton(){
        commencer.setDisable(true);
        ecritureNomJoueurs.getChildren().clear();
    }

    private void toutLesNomsRemplis(){
        TextField text;
        boolean nomRemplis = true;
        for (Node node : ecritureNomJoueurs.getChildren()){
            if (node instanceof TextField){
                text = (TextField) node;
                if (text.getText().isEmpty()) {
                    nomRemplis = false;
                    erreur.setText("Donnez un nom à Chaque Joueurs");
                }
            }
        }
        partieInitialiser.set(nomRemplis);
    }

    public BooleanProperty partiePrete(){
        return partieInitialiser;
    }

    public String[] getNomJoueurs(){
        List<String> nomJoueurs = new ArrayList<>();
        TextField text;
        String miniText;
        int i=0;
        for (Node node : ecritureNomJoueurs.getChildren()){
            if (node instanceof TextField){
                text = (TextField) node;
                miniText = text.getText();
                nomJoueurs.add(miniText);
            }
        }
        String[] donne = nomJoueurs.toArray(new String[nomJoueurs.size()]);
        return donne;
    }

    @FXML
    private void initialize(){
        commencer.setOnMouseClicked(debuterPartie);
        ecritureNomJoueurs.setMaxWidth(150);
        nbJoueursChoisi.setOnMouseClicked(event ->{
            try{
                if (Integer.parseInt(nbJoueursChoix.getText())<=4 && Integer.parseInt(nbJoueursChoix.getText())>1) {
                    effacerAncienBouton();
                    int nb = Integer.parseInt(nbJoueursChoix.getText());
                    erreur.setText("");
                    nbJoueursChoix.clear();
                    if (conteneurChoix.getChildren().get(conteneurChoix.getChildren().size()-1) instanceof VBox)
                        conteneurChoix.getChildren().remove(conteneurChoix.getChildren().size()-1);
                    for (int i = 1; i <= nb; i++) {
                        ecritureNomJoueurs.getChildren().addAll(new Label("Nom du joueur n°" + i), new TextField());
                    }
                    conteneurChoix.getChildren().add(ecritureNomJoueurs);
                    commencer.setDisable(false);
                }
                else erreur.setText("Veuillez donné un chiffre valide entre 2 et 4");
            } catch (Exception e){
                erreur.setText("Veuillez donné un chiffre valide entre 2 et 4");
            }
        });
    }

    EventHandler<MouseEvent> debuterPartie = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            toutLesNomsRemplis();
        }
    };
}
