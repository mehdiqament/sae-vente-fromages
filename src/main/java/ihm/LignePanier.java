package ihm;

import javax.swing.ImageIcon;

import fr.iut.annotations.Responsable;
import modele.Article; 

public class LignePanier {
    public String designation;
    public double prixUnitaire;
    public int quantite; 
    public double total;
    public ImageIcon miniature; 
    public Article articleAssocie;

    public LignePanier(String designation, double prixUnitaire, int quantite, double total, ImageIcon miniature, Article articleAssocie) {
        this.designation  = designation;
        this.prixUnitaire = prixUnitaire; 
        this.quantite     = quantite;
        this.total        = total; 
        this.miniature    = miniature; 
        this.articleAssocie = articleAssocie; 
    }
}