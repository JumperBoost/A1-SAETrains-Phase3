package fr.umontpellier.iut.trainsJavaFX;

import fr.umontpellier.iut.trainsJavaFX.vues.VueDuJeu;

public class GestionJeu {

    private static IJeu jeu;
    private static VueDuJeu vueDuJeu;

    public static void setJeu(IJeu jeu) {
        if (jeu != null)
            GestionJeu.jeu = jeu;
    }

    public static VueDuJeu getVueDuJeu() {
        return vueDuJeu;
    }

    public static void setVueDuJeu(VueDuJeu vueDuJeu) {
        GestionJeu.vueDuJeu = vueDuJeu;
    }

    public static IJeu getJeu() {
        return jeu;
    }
}
