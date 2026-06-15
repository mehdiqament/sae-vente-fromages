package modele;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.iut.annotations.Responsable;

public class TestMiseEnPlaceBDD {

    private static Fromages mesArticles;

    @Responsable("Hervé LEBLANC")
    @BeforeClass
    public static void setuP() {
        mesArticles = OutilsBaseDonneesFromages.générationBaseDeFromages(
                OutilsBaseDonneesFromages.CHEMIN_FICHIER);
    }

    @Responsable("Hervé LEBLANC")
    @Test
    public void pasDeFromageSansDésignation() {
        List<Fromage> fromages = mesArticles.getFromages();
        for (var f : fromages) {
            assertNotNull(f.getDésignation());
            assertNotEquals("", f.getDésignation());
        }
    }

    @Responsable("Hervé LEBLANC")
    @Test
    public void AuMoinsUnArticleParFromage() {
        List<Fromage> fromages = mesArticles.getFromages();
        for (var f : fromages) {
            assertTrue(f.getArticles().size() > 0);
        }
    }

    @Responsable("Hervé LEBLANC")
    @Test
    public void chaqueCléArticleEstNonNull() {
        List<Fromage> fromages = mesArticles.getFromages();
        for (var f : fromages) {
            List<Article> articles = f.getArticles();
            for (var a : articles) {
                assertNotNull(a.getClé());
            }
        }
    }

    @Responsable("Hervé LEBLANC")
    @Test
    public void chaqueArticleRéfèreSonFRomage() {
        List<Fromage> fromages = mesArticles.getFromages();
        for (var f : fromages) {
            List<Article> articles = f.getArticles();
            for (var a : articles) {
                assertSame(f, a.getFromage());
            }
        }
    }
    
    @Responsable("Mehdi BOUIN")
    @Test
    public void testGetDesignation() {
        Fromage f = new Fromage("Camembert", "camembert");
        assertEquals("Camembert", f.getDésignation());
    }

    @Responsable("Mehdi BOUIN")
    @Test
    public void testGetNomImage() {
        Fromage f = new Fromage("Camembert", "camembert");
        assertEquals("camembert", f.getNomImage());
    }

    @Responsable("Mehdi BOUIN")
    @Test
    public void testAddDescription() {
        Fromage f = new Fromage("Camembert", "camembert");
        f.addDescription("Un fromage normand");
        assertEquals("Un fromage normand", f.getDescription());
    }
    
    @Responsable("Mehdi BOUIN")
    @Test
    public void testAddArticle() {
        Fromage f = new Fromage("Camembert", "camembert");
        f.addArticle("250g", 5.50f, 10);
        assertEquals(1, f.getArticles().size());
    }

    @Responsable("Mehdi BOUIN")
    @Test
    public void testAddPlusieursArticles() {
        Fromage f = new Fromage("Camembert", "camembert");
        f.addArticle("250g", 5.50f, 10);
        f.addArticle("500g", 9.90f, 5);
        assertEquals(2, f.getArticles().size());
    }

    @Responsable("Mehdi BOUIN")
    @Test
    public void testUpdateTypeFromage() {
        Fromage f = new Fromage("Camembert", "camembert");
        f.updateTypeFromage(TypeLait.VACHE);
        assertEquals(TypeLait.VACHE, f.getTypeFromage());
    }

    @Responsable("Mehdi BOUIN")
    @Test
    public void testCompareTo() {
        Fromage f1 = new Fromage("Camembert", "camembert");
        Fromage f2 = new Fromage("Roquefort", "roquefort");
        assertTrue(f1.compareTo(f2) < 0);
    }

    @Responsable("Mehdi BOUIN")
    @Test
    public void testEquals() {
        Fromage f1 = new Fromage("Camembert", "camembert");
        Fromage f2 = new Fromage("Camembert", "camembert");
        assertTrue(f1.equals(f2));
    }
    
    @Responsable("Mehdi BOUIN")
    @Test
    public void testGetPrixTTC() {
        Fromage f = new Fromage("Camembert", "camembert");
        f.addArticle("250g", 5.50f, 10);
        Article a = f.getArticles().get(0);
        assertEquals(5.50f, a.getPrixTTC(), 0.01);
    }

    @Responsable("Mehdi BOUIN")
    @Test
    public void testGetQuantiteEnStock() {
        Fromage f = new Fromage("Camembert", "camembert");
        f.addArticle("250g", 5.50f, 10);
        Article a = f.getArticles().get(0);
        assertEquals(10, a.getQuantitéEnStock());
    }

    @Responsable("Mehdi BOUIN")
    @Test
    public void testGetCle() {
        Fromage f = new Fromage("Camembert", "camembert");
        f.addArticle("250g", 5.50f, 10);
        Article a = f.getArticles().get(0);
        assertEquals("250g", a.getClé());
    }
    
}
