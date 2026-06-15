package ihm;

import java.awt.BorderLayout;
import java.util.List;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import fr.iut.annotations.Responsable;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import modele.Article;

public class FenetrePanier extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTable tablePanier;
    private JButton btnRecalculer;
    private DefaultTableModel modeleTableau;
    private IHM parent;
    private JPanel panTotaux;
    private JTextField txtSousTotal;
    private JTextField txtExpedition;
    private JTextField txtTotal;
    private JComboBox<String> cbTransport;
    private JButton btnValider;
    private JButton btnVider;
    private JButton btnContinuer;

    public FenetrePanier(IHM parent) {
        super(parent, "BlancJus - Votre panier", true);
        this.parent = parent;
        setSize(680, 520);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel(new BorderLayout(0, 0));
        getContentPane().add(panel, BorderLayout.NORTH);
        panel.add(new JLabel("Votre panier", SwingConstants.LEFT), BorderLayout.WEST);
        btnRecalculer = new JButton("Mettre à jour");
        panel.add(btnRecalculer, BorderLayout.EAST);

        String[] colonnes = { "", "Produit", "Prix unitaire", "Quantité", "Total" };
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int col) { return col == 3; }
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 0 ? ImageIcon.class : super.getColumnClass(col);
            }
        };

        for (LignePanier lp : parent.getLignesPanier()) {
            modeleTableau.addRow(new Object[] {
                lp.miniature,
                lp.designation,
                String.format("%.2f €", lp.prixUnitaire),
                lp.quantite,
                String.format("%.2f €", lp.total)
            });
        }

        tablePanier = new JTable(modeleTableau);
        tablePanier.setRowHeight(35);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(tablePanier);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel panelBasPrincipal = new JPanel(new BorderLayout(0, 0));
        getContentPane().add(panelBasPrincipal, BorderLayout.SOUTH);

        JPanel panelMiddleBas = new JPanel(new BorderLayout(0, 0));
        panelBasPrincipal.add(panelMiddleBas, BorderLayout.CENTER);

        panTotaux = new JPanel();
        panTotaux.setLayout(new GridBagLayout());
        panelMiddleBas.add(panTotaux, BorderLayout.EAST);

        txtSousTotal = new JTextField(8);
        txtSousTotal.setEditable(false);
        txtExpedition = new JTextField(8);
        txtExpedition.setEditable(false);
        txtTotal = new JTextField(8);
        txtTotal.setEditable(false);

        JLabel lblSousTotal = new JLabel("Sous-Total :", SwingConstants.RIGHT);
        GridBagConstraints gbc_lblSousTotal = new GridBagConstraints();
        gbc_lblSousTotal.insets = new Insets(4, 4, 4, 4);
        gbc_lblSousTotal.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblSousTotal.gridx = 0;
        gbc_lblSousTotal.gridy = 0;
        panTotaux.add(lblSousTotal, gbc_lblSousTotal);

        GridBagConstraints gbc_txtSousTotal = new GridBagConstraints();
        gbc_txtSousTotal.insets = new Insets(4, 4, 4, 4);
        gbc_txtSousTotal.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtSousTotal.gridx = 1;
        gbc_txtSousTotal.gridy = 0;
        panTotaux.add(txtSousTotal, gbc_txtSousTotal);

        JLabel lblExpedition = new JLabel("Expédition :", SwingConstants.RIGHT);
        GridBagConstraints gbc_lblExpedition = new GridBagConstraints();
        gbc_lblExpedition.insets = new Insets(4, 4, 4, 4);
        gbc_lblExpedition.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblExpedition.gridx = 0;
        gbc_lblExpedition.gridy = 1;
        panTotaux.add(lblExpedition, gbc_lblExpedition);

        GridBagConstraints gbc_txtExpedition = new GridBagConstraints();
        gbc_txtExpedition.insets = new Insets(4, 4, 4, 4);
        gbc_txtExpedition.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtExpedition.gridx = 1;
        gbc_txtExpedition.gridy = 1;
        panTotaux.add(txtExpedition, gbc_txtExpedition);

        JLabel lblTotalFinal = new JLabel("TOTAL :", SwingConstants.RIGHT);
        GridBagConstraints gbc_lblTotal = new GridBagConstraints();
        gbc_lblTotal.insets = new Insets(4, 4, 4, 4);
        gbc_lblTotal.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblTotal.gridx = 0;
        gbc_lblTotal.gridy = 2;
        panTotaux.add(lblTotalFinal, gbc_lblTotal);

        GridBagConstraints gbc_txtTotal = new GridBagConstraints();
        gbc_txtTotal.insets = new Insets(4, 4, 4, 4);
        gbc_txtTotal.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtTotal.gridx = 1;
        gbc_txtTotal.gridy = 2;
        panTotaux.add(txtTotal, gbc_txtTotal);

        JPanel panel_2 = new JPanel();
        panelMiddleBas.add(panel_2, BorderLayout.WEST);
        cbTransport = new JComboBox<>();
        cbTransport.addItem("Colissimo");
        cbTransport.addItem("Chronorelais");
        cbTransport.addItem("Chronofresh");
        panel_2.add(new JLabel("frais de port offerts à partir de 120€"));
        panel_2.add(cbTransport);

        JPanel panBoutons = new JPanel();
        panelBasPrincipal.add(panBoutons, BorderLayout.SOUTH);
        btnValider   = new JButton("Valider le panier");
        btnVider     = new JButton("Vider le panier");
        btnContinuer = new JButton("Continuer les achats");
        panBoutons.add(btnValider);
        panBoutons.add(btnVider);
        panBoutons.add(btnContinuer);

        txtSousTotal.setText(String.format("%.2f €", parent.getMontantTotal()));
        calculerFrais();

        cbTransport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculerFrais();
            }
        });

        btnRecalculer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tablePanier.isEditing()) tablePanier.getCellEditor().stopCellEditing();

                double total = 0.0;
                List<LignePanier> anciennesLignes = new ArrayList<>(parent.getLignesPanier());
                parent.getLignesPanier().clear();

                for (int i = 0; i < tablePanier.getRowCount(); i++) {
                    try {
                        LignePanier ancienneLigne = anciennesLignes.get(i);
                        Article art = ancienneLigne.articleAssocie;
                        double px = ancienneLigne.prixUnitaire;

                        int nouvelleQte = Integer.parseInt(tablePanier.getValueAt(i, 3).toString().trim());
                        int ancienneQte = ancienneLigne.quantite;

                        int difference = nouvelleQte - ancienneQte;

                        if (art != null && difference > art.getQuantitéEnStock()) {
                            JOptionPane.showMessageDialog(FenetrePanier.this,
                                "Stock insuffisant pour " + ancienneLigne.designation + ". Rebasculé au maximum.",
                                "Avertissement", JOptionPane.PLAIN_MESSAGE);
                            nouvelleQte = ancienneQte + art.getQuantitéEnStock();
                        }

                        if (art != null) {
                            art.setQuantitéEnStock(art.getQuantitéEnStock() - (nouvelleQte - ancienneQte));
                        }

                        double tot = px * nouvelleQte;
                        tablePanier.setValueAt(String.format("%.2f €", tot), i, 4);
                        tablePanier.setValueAt(nouvelleQte, i, 3);
                        total += tot;

                        parent.getLignesPanier().add(new LignePanier(
                            ancienneLigne.designation, px, nouvelleQte, tot, ancienneLigne.miniature, art));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                parent.setMontantTotal(total);
                txtSousTotal.setText(String.format("%.2f €", total));
                parent.getLblMontantPanier().setText(String.format("%.2f €", total));
                parent.getLblMontantPanier().revalidate();
                parent.getLblMontantPanier().repaint();
                calculerFrais();
            }
        });

        btnContinuer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnVider.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int choix = JOptionPane.showConfirmDialog(
                    FenetrePanier.this,
                    "Voulez vous vraiment supprimer le panier ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE
                );
                if (choix == JOptionPane.YES_OPTION) {
                    for (LignePanier lp : parent.getLignesPanier()) {
                        if (lp.articleAssocie != null) {
                            lp.articleAssocie.setQuantitéEnStock(
                                lp.articleAssocie.getQuantitéEnStock() + lp.quantite);
                        }
                    }
                    modeleTableau.setRowCount(0);
                    parent.getLignesPanier().clear();
                    parent.setMontantTotal(0.0);
                    parent.getLblMontantPanier().setText("0,00 €");
                    parent.getLblMontantPanier().revalidate();
                    parent.getLblMontantPanier().repaint();
                    dispose();
                }
            }
        });

        btnValider.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tablePanier.isEditing()) tablePanier.getCellEditor().stopCellEditing();
                btnRecalculer.doClick();
                dispose();
                new FenetreCoordonnees(parent,
                    (String) cbTransport.getSelectedItem(),
                    txtExpedition.getText()).setVisible(true);
            }
        });
    }

    private void calculerFrais() {
        double frais = 0.0;
        if (parent.getMontantTotal() < 120.0) {
            String mode = (String) cbTransport.getSelectedItem();
            if ("Colissimo".equals(mode)) {
                frais = 14.90;
            } else if ("Chronorelais".equals(mode)) {
                frais = 10.90;
            } else if ("Chronofresh".equals(mode)) {
                frais = 9.90;
            }
        }
        txtExpedition.setText(String.format("%.2f €", frais));
        txtTotal.setText(String.format("%.2f €", parent.getMontantTotal() + frais));
    }
}