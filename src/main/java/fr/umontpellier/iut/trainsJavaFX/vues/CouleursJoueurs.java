package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.mecanique.CouleurJoueur;

import java.util.Map;

public class CouleursJoueurs {
    public static Map<CouleurJoueur, String> couleursBackgroundJoueur = Map.of(
            CouleurJoueur.JAUNE, "#FED440",
            CouleurJoueur.ROUGE, "#795593",
            CouleurJoueur.BLEU, "#4093B6",
            CouleurJoueur.VERT, "#2CCDB4"
    );

    public static Map<CouleurJoueur, String> nomCouleursCubeJoueur = Map.of(
            CouleurJoueur.JAUNE, "cube_yellow.png",
            CouleurJoueur.ROUGE, "cube_red.png",
            CouleurJoueur.BLEU, "cube_blue.png",
            CouleurJoueur.VERT, "cube_green.png"
    );

}