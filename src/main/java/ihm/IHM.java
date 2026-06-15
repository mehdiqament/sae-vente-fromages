package ihm;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.iut.annotations.Responsable;

import java.awt.event.*;

import modele.*;

public class IHM extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String IMAGES40   = "/images/fromages/hauteur40/";
    private static final String CHEMIN_JSON = "src/main/resources/data/fromages.json";

    private Fromages base;

    private JLabel lblIconeTitreHaut;
    private JLabel lblIconeFromage;
    private JLabel lblMontantPanier;
    private JComboBox<String> cbFiltres;
    private DefaultListModel<Fromage> modèleListe;
    private JList<Fromage> listFromages;

    private List<LignePanier> lignesPanier = new ArrayList<>();
    private double montantTotal = 0.0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new IHM().setVisible(true);
            }
        });
    }

    public IHM() {
        base = OutilsBaseDonneesFromages.générationBaseDeFromages(CHEMIN_JSON);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 600);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panBandeau = new JPanel(new BorderLayout(0, 0));
        contentPane.add(panBandeau, BorderLayout.NORTH);

        JPanel panTitre = new JPanel();
        panBandeau.add(panTitre);
        panTitre.add(new JLabel("Nos fromages"));

        JPanel panPanier = new JPanel();

        lblIconeTitreHaut = new JLabel();
        mettreAjourIconeHaut("Tous");
        panTitre.add(lblIconeTitreHaut);

        JLabel lblCaddie = new JLabel();
        lblCaddie.setIcon(chargerIcone(IMAGES40 + "cadi.png", 35, 30));
        panPanier.add(lblCaddie);

        panBandeau.add(panPanier, BorderLayout.EAST);
        lblMontantPanier = new JLabel("0,00 €");
        panPanier.add(lblMontantPanier);
        panPanier.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (montantTotal == 0.0) {
                    JOptionPane.showMessageDialog(IHM.this, "Panier vide !");
                } else {
                    new FenetrePanier(IHM.this).setVisible(true);
                }
            }
        });

        modèleListe = new DefaultListModel<>();
        listFromages = new JList<>(modèleListe);
        listFromages.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Fromage) {
                    setText(((Fromage) value).getDésignation());
                }
                return this;
            }
        });
        listFromages.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Fromage f = listFromages.getSelectedValue();
                    if (f != null) {
                        new FenetreDetailFromage(IHM.this, f).setVisible(true);
                        listFromages.clearSelection();
                    }
                }
            }
        });
        contentPane.add(new JScrollPane(listFromages), BorderLayout.CENTER);

        JPanel panZoneBasse = new JPanel(new BorderLayout(0, 0));
        contentPane.add(panZoneBasse, BorderLayout.SOUTH);

        JPanel panFiltre = new JPanel();
        panZoneBasse.add(panFiltre, BorderLayout.CENTER);

        lblIconeFromage = new JLabel();
        mettreAjourIconeFiltre("Tous");
        panFiltre.add(lblIconeFromage);

        String[] optionsFiltre = {"Tous", "Vache", "Chèvre", "Brebis"};
        cbFiltres = new JComboBox<>(optionsFiltre);
        cbFiltres.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String choix = (String) cbFiltres.getSelectedItem();
                mettreAjourIconeFiltre(choix);
                mettreAjourIconeHaut(choix);
                if ("Vache".equals(choix)) {
                    chargerListe(base.fromagesAuLaitDe(TypeLait.VACHE));
                } else if ("Chèvre".equals(choix)) {
                    chargerListe(base.fromagesAuLaitDe(TypeLait.CHEVRE));
                } else if ("Brebis".equals(choix)) {
                    chargerListe(base.fromagesAuLaitDe(TypeLait.BREBIS));
                } else {
                    chargerListe(base.getFromages());
                }
            }
        });
        panFiltre.add(cbFiltres);

        JPanel panBouton = new JPanel();
        panZoneBasse.add(panBouton, BorderLayout.EAST);
        JButton btnQuitter = new JButton("Quitter");
        btnQuitter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panBouton.add(btnQuitter);
        chargerListe(base.getFromages());
    }

    public void ajouterAuPanier(Fromage fromage, Article article, int qte) {
        double total = article.getPrixTTC() * qte;
        ImageIcon miniature = chargerIcone("/images/fromages/hauteur40/" + fromage.getNomImage() + ".jpg", 35, 30);
        lignesPanier.add(new LignePanier(
            fromage.getDésignation() + " - " + article.getClé(),
            article.getPrixTTC(),
            qte,
            total,
            miniature,
            article
        ));
        montantTotal += total;
        lblMontantPanier.setText(String.format("%.2f €", montantTotal));
    }

    public List<LignePanier> getLignesPanier() {
        return lignesPanier;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montant) {
        montantTotal = montant;
    }

    public JLabel getLblMontantPanier() {
        return lblMontantPanier;
    }

    ImageIcon chargerIcone(String chemin, int largeur, int hauteur) {
        try {
            URL url = getClass().getResource(chemin);
            if (url != null) {
                Image img = new ImageIcon(url).getImage()
                        .getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void chargerListe(List<Fromage> liste) {
        modèleListe.clear();
        for (Fromage f : liste) {
            modèleListe.addElement(f);
        }
    }

    private void mettreAjourIconeFiltre(String typeLait) {
        ImageIcon icone = chargerIcone(IMAGES40 + "fromage.png", 55, 40);
        lblIconeFromage.setIcon(icone);
    }

    private void mettreAjourIconeHaut(String typeLait) {
        String fichier = nomFichierIcone(typeLait);
        ImageIcon icone = chargerIcone(IMAGES40 + fichier, 50, 40);
        if (icone != null && lblIconeTitreHaut != null) {
            lblIconeTitreHaut.setIcon(icone);
            if (lblIconeTitreHaut.getParent() != null) {
                lblIconeTitreHaut.getParent().revalidate();
            }
        }
    }

    private String nomFichierIcone(String typeLait) {
        if ("Vache".equals(typeLait))  return "vache.png";
        if ("Chèvre".equals(typeLait)) return "chevre.png";
        if ("Brebis".equals(typeLait)) return "brebis.png";
        return "tous.png";
    }
}