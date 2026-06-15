package modele;

import java.util.LinkedList;
import java.util.List;

import fr.iut.annotations.Responsable;

/**
 * la classe Fromage représente un fromage qui est défini par sa désignation (sa
 * clé), sa description, son type et la liste de ses articles associés
 */
public class Fromage implements Comparable<Fromage> {

    private String désignation;
    private String nomImage;
    private String description;
    private TypeLait typeFromage;
    private List<Article> articles;

    /**
     * construit un fromage
     * 
     * @param désignation clé du fromage
     * @param nomImage    image le représentant lors de l'affichage
     */
    public Fromage(String désignation, String nomImage) {
        this.désignation = désignation;
        this.nomImage = nomImage;
        this.articles = new LinkedList<Article>();
    }

    /**
     * @return la désignation ou la clé du fromage
     */
    public String getDésignation() {
        return this.désignation;
    }

    /**
     * @return met à jour le nom de l'image représentant le fromage
     */
    public String getNomImage() {
        return this.nomImage;
    }

    /**
     * @return la description longue d'un fromage
     */
    public String getDescription() {
        assert !(this.description == null);
        if (this.description == null) {
            return "Pas de description de ce fromage pour le moment. Nous en sommes désolés";
        }
        return this.description;
    }

    /**
     * ajoute ou remplace une description longue au fromage
     * 
     * @param description la description à ajouter
     */
    public void addDescription(String description) {
        this.description = description;
    }

    /**
     * modifie le type de lait d'un fromage
     * 
     * @param type le type de lait modifié
     */
    public void updateTypeFromage(TypeLait type) {
        this.typeFromage = type;
    }

    /**
     * @return le type de lait du fromage (chèvre, brebis, vache)
     */
    public TypeLait getTypeFromage() {
        return this.typeFromage;
    }

    @Override
    public String toString() {
        return this.getDésignation() + ", Fromage au lait de "
                + this.typeFromage.getTypeDeLait();
    }

    /**
     * ajoute un nouvel article au fromage, une nouvelle façon de vendre le
     * fromage
     * 
     * @param clé      la clé de l'article
     * @param prixTTC  la prix TTC de l'article
     * @param quantité la quantité en stock disponible
     */
    public void addArticle(String clé, float prixTTC, int quantité) {
        this.articles.add(new Article(this, clé, prixTTC, quantité));

    }

    /**
     * @return la liste des articles d'un fromage
     */
    public List<Article> getArticles() {
        return this.articles;
    }

    @Override
    public int compareTo(Fromage f) {
        return this.désignation.compareTo(f.désignation);
    }

    @Override
    public boolean equals(Object obj) {
        Fromage other = (Fromage) obj;
        return this.désignation.equals(other.désignation);
    }

}
