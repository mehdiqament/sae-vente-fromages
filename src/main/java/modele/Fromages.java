package modele;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import fr.iut.annotations.Responsable;

/**
 * La classe fromages détient l'image mémoire de la base de données json
 */
public class Fromages {

    private List<Fromage> fromages;

    /**
     * constructeur de la structure d'acceuil de la base de données fromage
     */
    public Fromages() {
        this.fromages = new LinkedList<Fromage>();
    }

    /**
     * ajoute une liste de fromages à la base
     * 
     * @param fromages la liste de fromages récupérée du fichier Json
     */
    public void addFromages(List<Fromage> fromages) {
        this.fromages.addAll(fromages);
    }

    /**
     * filtre les fromages par type de lait
     * 
     * @param lait le type de lait
     * @return la liste des fromages du type de lait @lait
     */
    public List<Fromage> fromagesAuLaitDe(TypeLait lait) {
        List<Fromage> FromageTypeLait = new LinkedList<>();
        for (Fromage f : fromages) {
            if (f.getTypeFromage().equals(lait)) {
            	FromageTypeLait.add(f);
            }
        }
        return FromageTypeLait;
    }

    /**
     * @return la liste des fromages
     */
    public List<Fromage> getFromages() {
        Collections.sort(this.fromages);
        return this.fromages;
    }

    /**
     * récupère un fromage depuis la base en mémoire à partir de sa désignation
     * 
     * @param désignationFromage la désignation du fromage
     * @return le fromage dont la clé est sa désignation, null si non trouvé
     */
    public Fromage getFromage(String désignationFromage) {
        for (Fromage f : this.fromages) {
            if (f.getDésignation().equals(désignationFromage)) {
                return f;
            }
        }
        return null;
    }

    /**
     * récupére un article depuis la base en mémoire à partir de la désignation
     * d'un fromage et de sa clef
     * 
     * @param désignationFromage la désifnation du fromage dont est issu
     *                           l'article
     * @param cléArticle         la clé de l'article
     * @return l'article s'il est trouvé, null si non trouvé
     */
    public Article getArticle(String désignationFromage, String cléArticle) {
        for (Fromage f : this.fromages) {
            if (f.getDésignation().equals(désignationFromage)) {
                for (Article article : f.getArticles()) {
                    if (article.getClé().equals(cléArticle)) {
                        return article;
                    }
                }
            }
        }
        return null;
    }
}
