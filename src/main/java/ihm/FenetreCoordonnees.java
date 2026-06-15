package ihm;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;

import fr.iut.annotations.Responsable;

public class FenetreCoordonnees extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField champNom;
    private JTextField champPrenom;
    private JTextField champAdresse1;
    private JTextField champAdresse2;
    private JTextField champCodePostal;
    private JTextField champVille;
    private JTextField champTelephone;
    private JTextField champMail;

    private JRadioButton rbCb;
    private JRadioButton rbPaypal;
    private JRadioButton rbCheque;
    private JRadioButton rbOui;
    private JRadioButton rbNon;

    private IHM parent;
    private String transporteurNom;
    private String fraisTexte;

    
    public FenetreCoordonnees(IHM parent, String transporteurNom, String fraisTexte) {
        super(parent, "BlancJus - Vos coordonnées", true);
        this.parent         = parent;
        this.transporteurNom = transporteurNom;
        this.fraisTexte      = fraisTexte;
        setSize(550, 520);
        setLocationRelativeTo(parent);

        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);
        JLabel lblTitre = new JLabel("Vos coordonnées", SwingConstants.CENTER);
        panel.add(lblTitre);

        JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.CENTER);
        panel_1.setLayout(new GridLayout(8, 2, 5, 8));

        panel_1.add(new JLabel("Nom", SwingConstants.CENTER));
        champNom = new JTextField(); panel_1.add(champNom);

        panel_1.add(new JLabel("Prénom", SwingConstants.CENTER));
        champPrenom = new JTextField(); panel_1.add(champPrenom);

        panel_1.add(new JLabel("Adresse 1", SwingConstants.CENTER));
        champAdresse1 = new JTextField(); panel_1.add(champAdresse1);

        panel_1.add(new JLabel("Adresse 2", SwingConstants.CENTER));
        champAdresse2 = new JTextField(); panel_1.add(champAdresse2);

        panel_1.add(new JLabel("Code postal", SwingConstants.CENTER));
        champCodePostal = new JTextField(); panel_1.add(champCodePostal);

        panel_1.add(new JLabel("Ville", SwingConstants.CENTER));
        champVille = new JTextField(); panel_1.add(champVille);

        panel_1.add(new JLabel("Téléphone", SwingConstants.CENTER));
        champTelephone = new JTextField(); panel_1.add(champTelephone);

        panel_1.add(new JLabel("Mail", SwingConstants.CENTER));
        champMail = new JTextField(); panel_1.add(champMail);

        JPanel panel_2 = new JPanel();
        getContentPane().add(panel_2, BorderLayout.SOUTH);
        panel_2.setLayout(new GridLayout(3, 1, 0, 5)); 
        
        JPanel panPaiement = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panPaiement.add(new JLabel("Mode de paiement :"));
        rbCb     = new JRadioButton("Carte bancaire", true);
        rbPaypal = new JRadioButton("PayPal");
        rbCheque = new JRadioButton("Chèque");
        ButtonGroup bgPay = new ButtonGroup();
        bgPay.add(rbCb); bgPay.add(rbPaypal); bgPay.add(rbCheque);
        panPaiement.add(rbCb); panPaiement.add(rbPaypal); panPaiement.add(rbCheque);
        panel_2.add(panPaiement);

        JPanel panNewsletter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panNewsletter.add(new JLabel("S'inscrire à la newsletter :"));
        rbOui = new JRadioButton("Oui", true);
        rbNon = new JRadioButton("Non");
        ButtonGroup bgNews = new ButtonGroup();
        bgNews.add(rbOui); bgNews.add(rbNon);
        panNewsletter.add(rbOui); panNewsletter.add(rbNon);
        panel_2.add(panNewsletter);

        JPanel panBoutonsAction = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnOk = new JButton("Confirmer");
        JButton btnAnnuler = new JButton("Annuler");
        panBoutonsAction.add(btnOk);
        panBoutonsAction.add(btnAnnuler);
        panel_2.add(panBoutonsAction);

        btnAnnuler.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String paiement = "Carte bancaire";
                if (rbPaypal.isSelected()) {
                	paiement = "PayPal";
                }
                if (rbCheque.isSelected()) {
                	paiement = "Chèque";
                }
                boolean abonneNewsletter = rbOui.isSelected();
                dispose();
                new FenetreFacture(
                    parent, paiement, transporteurNom, fraisTexte, abonneNewsletter,
                    champNom.getText().trim(),
                    champPrenom.getText().trim(),
                    champAdresse1.getText().trim(),
                    champAdresse2.getText().trim(),
                    champCodePostal.getText().trim(),
                    champVille.getText().trim(),
                    champTelephone.getText().trim(),
                    champMail.getText().trim()
                ).setVisible(true);
            }
        });
    }
}