package ihm;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.SwingConstants;

import fr.iut.annotations.Responsable;

import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import modele.Article;
import modele.Fromage;

public class FenetreDetailFromage extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final String IMAGES200 = "/images/fromages/hauteur200/";

    private JLabel lblNom;
    private JLabel lblImage;
    private JTextArea txtDesc;
    private JComboBox<Article> cbArticles;
    private JSpinner spinnerQte;
    private JLabel lblStock;
    private JButton btnAjouter;
    private JButton btnAnnuler;
    
    public FenetreDetailFromage(IHM parent, Fromage fromage) {
        super(parent, "BlancJus", true);
        setBounds(100, 100, 650, 480);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(new BorderLayout(15, 15));

        lblNom = new JLabel(fromage.getDésignation(), SwingConstants.CENTER);
        getContentPane().add(lblNom, BorderLayout.NORTH);

        JPanel zoneCentrale = new JPanel();
        getContentPane().add(zoneCentrale, BorderLayout.CENTER);
        zoneCentrale.setLayout(new GridLayout(1, 2, 15, 0));

        lblImage = new JLabel();
        ImageIcon icone = parent.chargerIcone(IMAGES200 + fromage.getNomImage() + ".jpg", 280, 200);
        if (icone != null) lblImage.setIcon(icone);
        zoneCentrale.add(lblImage);

        JScrollPane scrollDesc = new JScrollPane();
        zoneCentrale.add(scrollDesc);

        txtDesc = new JTextArea(fromage.getDescription());
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        scrollDesc.setViewportView(txtDesc);

        JPanel zoneBasse = new JPanel(new FlowLayout());
        getContentPane().add(zoneBasse, BorderLayout.SOUTH);

        cbArticles = new JComboBox<Article>();
        List<Article> articles = fromage.getArticles();
        for (Article a : articles) cbArticles.addItem(a);
        cbArticles.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Article) {
                    Article a = (Article) value;
                    setText(a.getClé() + "  -  " + String.format("%.2f €", a.getPrixTTC()));
                }
                return this;
            }
        });
        zoneBasse.add(cbArticles);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 0, 999999, 1);
        spinnerQte = new JSpinner(spinnerModel);
        zoneBasse.add(spinnerQte);

        lblStock = new JLabel();
        zoneBasse.add(lblStock);

        Article premier = articles.isEmpty() ? null : articles.get(0);
        mettreAJourAffichageStock(premier, spinnerModel);

        cbArticles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Article sel = (Article) cbArticles.getSelectedItem();
                if (sel != null) {
                    mettreAJourAffichageStock(sel, spinnerModel);
                }
            }
        });

        btnAjouter = new JButton("Ajouter au panier");
        btnAnnuler = new JButton("Annuler");

        btnAnnuler.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnAjouter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Article artChoisi = (Article) cbArticles.getSelectedItem();
                
                if (artChoisi != null) {
                    int stockDispo = artChoisi.getQuantitéEnStock();
                    int qte = 0;

                    try {
                        spinnerQte.commitEdit();
                        qte = (Integer) spinnerQte.getValue();
                    } catch (Exception ex) {
                        try {
                            String texteTape = ((JSpinner.DefaultEditor) spinnerQte.getEditor()).getTextField().getText().trim();
                            qte = Integer.parseInt(texteTape);
                        } catch (NumberFormatException nfe) {
                            JOptionPane.showMessageDialog(FenetreDetailFromage.this,
                                "Veuillez saisir un nombre entier valide.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    if (qte > stockDispo) {
                        JOptionPane.showMessageDialog(
                            FenetreDetailFromage.this,
                            "Le nombre de fromages demandé (" + qte + ") n'est pas disponible.\nStock actuel : " + stockDispo,
                            "Stock insuffisant",
                            JOptionPane.PLAIN_MESSAGE 
                        );
                        return; 
                    }

                    if (qte <= 0) {
                        JOptionPane.showMessageDialog(FenetreDetailFromage.this,
                            "Veuillez choisir une quantité supérieure à 0.", 
                            "Quantité invalide", 
                            JOptionPane.PLAIN_MESSAGE);
                        return;
                    }

                    artChoisi.setQuantitéEnStock(stockDispo - qte); 
                    parent.ajouterAuPanier(fromage, artChoisi, qte);
                    dispose(); 
                }
            }
        });

        zoneBasse.add(btnAjouter);
        zoneBasse.add(btnAnnuler);
    }

    private void mettreAJourAffichageStock(Article article, SpinnerNumberModel spinnerModel) {
            int stockDispo = article.getQuantitéEnStock();
            if (stockDispo == 0) {
                lblStock.setText("En Rupture");
                lblStock.setForeground(Color.RED);
                spinnerQte.setEnabled(false);
                spinnerModel.setMinimum(0);
                spinnerModel.setMaximum(0);
                spinnerModel.setValue(0);
            } else {
                lblStock.setText("Stock disponible : " + stockDispo);
                lblStock.setForeground(Color.BLACK);
                spinnerQte.setEnabled(true);
                spinnerModel.setMinimum(1);
                spinnerModel.setMaximum(999999); 
                
                int valeurActuelle = (Integer) spinnerModel.getValue();
                if (valeurActuelle <= 0) {
                    spinnerModel.setValue(1);
                }
            }
        }
    }