package ihm;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.SwingConstants;

import fr.iut.annotations.Responsable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FenetreFacture extends JDialog {

    private static final long serialVersionUID = 1L;

    private JEditorPane zoneHtml;
    private IHM parent;

    public FenetreFacture(IHM parent, String paiement, String transporteurNom, String fraisTexte,
            boolean abonneNewsletter,
            String nom, String prenom, String adresse1, String adresse2,
            String codePostal, String ville, String telephone, String mail) {

        super(parent, "BlancJus - Votre facture", true);
        this.parent = parent;
        setSize(600, 570);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JLabel lblTitre = new JLabel("Votre facture", SwingConstants.CENTER);
        getContentPane().add(lblTitre, BorderLayout.NORTH);

        java.time.LocalDateTime maintenant = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter
            .ofPattern("EEEE d MMMM yyyy, HH:mm:ss", java.util.Locale.FRENCH);
        String dateHeure = maintenant.format(fmt);
        String heureSuffix = " heure d'été d'Europe centrale";

        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family:sans-serif; margin:10px;'>");

        // EN-TÊTE
        html.append("<p style='color:#cc6600; font-size:15px; font-weight:bold;'>")
            .append("Fromagerie BlancJus pour vous servir et resservir en fromages</p>");
        html.append("<p><i>Commande du ").append(dateHeure).append(heureSuffix).append("</i></p>");
        html.append("<p><b>").append(prenom).append(" ").append(nom.toUpperCase()).append("</b><br>");
        if (!adresse1.isEmpty())
            html.append("Adresse : ").append(adresse1);
        if (!adresse2.isEmpty())
            html.append(", ").append(adresse2);
        if (!adresse1.isEmpty() || !adresse2.isEmpty())
            html.append(" ").append(codePostal).append(" ").append(ville).append("<br>");
        if (!telephone.isEmpty())
            html.append("Téléphone : ").append(telephone).append("<br>");
        if (!mail.isEmpty())
            html.append("Mèl : ").append(mail).append("<br>");
        html.append("</p><hr>");

        html.append("<div style='font-size:14px; padding:5px; margin-bottom:15px;'>Merci pour votre commande !</div>");

        html.append("<table cellspacing='0' cellpadding='6' style='width:100%; border-collapse:collapse; border: 1px solid black; text-align:center;'>");
        html.append("<tr><th style='border: 1px solid black;'>Produit</th>"
                + "<th style='border: 1px solid black;'>Prix unitaire</th>"
                + "<th style='border: 1px solid black;'>Quantité</th>"
                + "<th style='border: 1px solid black;'>Prix TTC</th></tr>");

        for (LignePanier ligne : parent.getLignesPanier()) {
            html.append("<tr>")
                .append("<td style='text-align:left; border: 1px solid black;'>").append(ligne.designation).append("</td>")
                .append("<td style='border: 1px solid black;'>").append(String.format("%.2f €", ligne.prixUnitaire)).append("</td>")
                .append("<td style='border: 1px solid black;'>").append(ligne.quantite).append("</td>")
                .append("<td style='border: 1px solid black;'>").append(String.format("%.2f €", ligne.total)).append("</td>")
                .append("</tr>");
        }
        html.append("</table><br>");

        double frais = 0.0;
        try {
            frais = Double.parseDouble(fraisTexte.replace(" €", "").replace(",", ".").trim());
        } catch (Exception ex) {
            frais = 0.0;
        }

        double totalFinal = parent.getMontantTotal() + frais;

        html.append("<p><b>Total commande : ")
            .append(String.format("%.2f €", parent.getMontantTotal()))
            .append(" - Paiement par ").append(paiement).append("</b></p>");
        html.append("<p><b>Frais de transport : ")
            .append(fraisTexte).append(" - ").append(transporteurNom).append("</b></p>");
        html.append("<p style='font-size:14px;'><b>Total TTC : ")
            .append(String.format("%.2f €", totalFinal)).append("</b></p>");
        html.append("<p style='font-size:12px; color:#555555;'>Inscription à la newsletter : ")
            .append(abonneNewsletter ? "Oui" : "Non").append("</p>");
        html.append("</body></html>");

        zoneHtml = new JEditorPane("text/html", html.toString());
        zoneHtml.setEditable(false);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(zoneHtml);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // SOUTH
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.SOUTH);
        JButton btnImprimer = new JButton("Imprimer");
        JButton btnQuitter  = new JButton("Quitter");
        panel.add(btnImprimer);
        panel.add(btnQuitter);

        btnImprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    zoneHtml.print();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(FenetreFacture.this,
                        "Impression impossible : " + ex.getMessage());
                }
            }
        });

        btnQuitter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.getLignesPanier().clear();
                parent.setMontantTotal(0.0);
                parent.getLblMontantPanier().setText("0,00 €");
                dispose();
            }
        });
    }
}