package it.com.torneo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

public class WinCalcio extends javax.swing.JFrame {

    List<Squadra> listaSquadra = new ArrayList<>();
    List<Partita> listaPartita = new ArrayList<>();

    public WinCalcio() {
        initComponents();
        
        caricaListaElencoSquadra();
        refreshLista();

        caricaListaElencoPartita();
        refreshTablePartita();
    }

    //prende path 
    String path() throws IOException {
        return new File("").getCanonicalPath() + "/";
    }

    public void salvaSquadra() {
        String nome = txNome.getText();
        String sede = txtSede.getText();
        Squadra newsquadra = new Squadra(nome, sede);

//        if (lstElencoSquadra.getSelectedIndex() == -1) {   //nessuna selezione,non sono in modifica
        listaSquadra.add(newsquadra);
        lblMsg.setText("Creata Nuova Squadra!");

//        } else {//lista selezionata  sono in modifica dell iesimo elemento
//            listaSquadra.set(lstElencoSquadra.getSelectedIndex(), newsquadra);
//            lblMsg.setText("Squadra aggiornato!");
//        }
        refreshLista();
        salvaSquadreCSV();
//        validaCorso();

    }

    private void refreshLista() {
        DefaultListModel model = new DefaultListModel();

        for (int i = 0; i < listaSquadra.size(); i++) {
            String nc = listaSquadra.get(i).getNome() + " - " + listaSquadra.get(i).getSede();
            model.addElement(nc);
        }

        lstElencoSquadra.setModel(model);
    }

    private void salvaSquadreCSV() {
        String testoDisplay = "nome;sede\n";
        for (int i = 0; i < listaSquadra.size(); i++) {
            testoDisplay += listaSquadra.get(i).getCSV();
        }
        try {
            FileWriter myWriter = new FileWriter(path() + "squadre.csv");
            myWriter.write(testoDisplay);
            myWriter.close();

        } catch (IOException ex) {
            lblMsg.setText("errore di scrittura file squadre.csv");
        }

    }

    void selezionaSquadra() {
        int index = lstElencoSquadra.getSelectedIndex();

//        btnUpdateCorso.setEnabled(true);
        Squadra squadra = listaSquadra.get(index);
        //lblMsg.setText("corso creato correttamente!");

        txNome.setText(squadra.getNome());
        txtSede.setText(squadra.getSede());
    }

    private void caricaListaElencoSquadra() {
        try {

            File filecsv = new File(path() + "squadre.csv");
            Scanner lettore = new Scanner(filecsv);
            int n = 1;
            while (lettore.hasNextLine()) {
                String riga = lettore.nextLine();
                if (n > 1) {
                    String[] dati = riga.split(";");

                    Squadra squadra = new Squadra(dati[0], dati[1]);
                    //il corso è pronto e lo aggiungiamo alla lista
                    listaSquadra.add(squadra);
                }
                n++;
            }
        } catch (Exception e) {
            System.out.println("ERRORE al caricari squadra!!!" + e.getMessage());
        }

    }

    private boolean validaSquadra() {
        return !txNome.getText().isBlank() || !txtSede.getText().isBlank();
    }

    public void salvaPartita() {
        int giornata = Integer.parseInt(txGiornata.getText());
        String Squadra1 = txSquadra1.getText();
        int golSquadra1 = Integer.parseInt(txtGolSquadra1.getText());
        String Squadra2 = txSquadra2.getText();
        int golSquadra2 = Integer.parseInt(txGolSquadra2.getText());

        Partita newpartita = new Partita(giornata, Squadra1, Squadra2, golSquadra1, golSquadra2);

        listaPartita.add(newpartita);

        refreshTablePartita();
        salvaPartiteCSV();

        lblMsg.setText("Inserita Nuova Partita!");

    }

    public void refreshTablePartita() {
        DefaultTableModel model = (DefaultTableModel) tbElencoPartita.getModel();
        model.setRowCount(0);//clean table
        int ncol = model.getColumnCount();
        Object[] rowData = new Object[ncol];

        for (Partita p : listaPartita) {
            rowData[0] = p.getGiornata();
            rowData[1] = p.getSquadra1();
            rowData[2] = p.getGolSquadra1();
            rowData[3] = p.getGolSquadra2();
            rowData[4] = p.getSquadra2();
            model.addRow(rowData);
        }

    }

    private void salvaPartiteCSV() {
        String testoDisplay = "giornata;squadra1,golsquadra1,golsquadra2,squadra2\n";
        for (int i = 0; i < listaPartita.size(); i++) {
            testoDisplay += listaPartita.get(i).getCSV();
        }
        try {
            FileWriter myWriter = new FileWriter(path() + "partite.csv");
            myWriter.write(testoDisplay);
            myWriter.close();

        } catch (IOException ex) {
            lblMsg.setText("ERRORE di scrittura file partite.csv");
        }

    }

    private boolean validaPartita() {
        boolean ris = false;
        ris = !txGiornata.getText().isBlank()
                || !txSquadra1.getText().isBlank()
                || !txSquadra2.getText().isBlank()
                || !txtGolSquadra1.getText().isBlank()
                || !txGolSquadra2.getText().isBlank();

        boolean okSquadra1 = false;
        boolean okSquadra2 = false;
        for (Squadra squadra : listaSquadra) {
            if (squadra.getNome().equals(txSquadra1.getText())) {
                okSquadra1 = true;
            }

            if (squadra.getNome().equals(txSquadra2.getText())) {
                okSquadra2 = true;
            }

            if(okSquadra1 && okSquadra2){
                return true;
            }
            
            if (!okSquadra1 && !okSquadra2) {
                lblMsg.setText("Squadra NON valida!!");
                return false;
            }
        }

        return ris;
    }

    private void caricaListaElencoPartita() {
        try {

            File filecsv = new File(path() + "partite.csv");
            Scanner lettore = new Scanner(filecsv);
            int n = 1;
            while (lettore.hasNextLine()) {
                String riga = lettore.nextLine();
                if (n > 1) {
                    String[] dati = riga.split(";");

                    Partita p = new Partita(Integer.parseInt(dati[0]), dati[1], dati[2], Integer.parseInt(dati[3]), Integer.parseInt(dati[4]));
                    //il corso è pronto e lo aggiungiamo alla lista
                    listaPartita.add(p);
                }
                n++;
            }
        } catch (Exception e) {
            System.out.println("ERRORE al caricari squadra!!!" + e.getMessage());
        }

    }

//    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        String formatterDate = datainserimento.format(myFormatObj);
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblMsg = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txNome = new javax.swing.JTextField();
        txtSede = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnNew = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstElencoSquadra = new javax.swing.JList<>();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txSquadra1 = new javax.swing.JTextField();
        txSquadra2 = new javax.swing.JTextField();
        txGolSquadra2 = new javax.swing.JTextField();
        txGiornata = new javax.swing.JTextField();
        txtGolSquadra1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbElencoPartita = new javax.swing.JTable();
        btnNewPartita = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Cantarell", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Gestione Torneo Calcio");

        lblMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMsg.setText("MSG");

        jLabel2.setText("Nome:");

        jLabel3.setText("Sede:");

        txNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txNomeActionPerformed(evt);
            }
        });

        txtSede.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSedeActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel4.setText("SQUADRA                                                            ELENCO SQUADRA");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        btnNew.setText("New");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        jButton2.setText("Update");

        lstElencoSquadra.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lstElencoSquadra.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstElencoSquadraValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstElencoSquadra);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel5.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel5.setText("partita");

        jLabel6.setText("giornata:");

        jLabel7.setText("Squadra1:                      Gol:");

        jLabel8.setText("Squadra2:                      Gol:");

        txSquadra1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txSquadra1ActionPerformed(evt);
            }
        });

        tbElencoPartita.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "g", "in casa", "gol", "gol", "trasferta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbElencoPartita);
        if (tbElencoPartita.getColumnModel().getColumnCount() > 0) {
            tbElencoPartita.getColumnModel().getColumn(0).setPreferredWidth(30);
            tbElencoPartita.getColumnModel().getColumn(1).setPreferredWidth(100);
            tbElencoPartita.getColumnModel().getColumn(2).setPreferredWidth(30);
            tbElencoPartita.getColumnModel().getColumn(3).setPreferredWidth(30);
            tbElencoPartita.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        btnNewPartita.setText("New");
        btnNewPartita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewPartitaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txNome, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtSede, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(6, 6, 6)
                                .addComponent(txGiornata, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel7)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNewPartita, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txSquadra2, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                    .addComponent(txSquadra1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txGolSquadra2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtGolSquadra1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 20, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMsg)
                .addGap(30, 30, 30)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(txNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3)
                                .addComponent(txtSede, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                        .addComponent(jSeparator1)
                        .addComponent(jSeparator2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txGiornata, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txSquadra1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGolSquadra1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txGolSquadra2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txSquadra2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNewPartita, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(122, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txNomeActionPerformed

    private void txtSedeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSedeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSedeActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        if (validaSquadra())
            salvaSquadra();
        else
            lblMsg.setText("Per favore, inserisci Nome e Sede!");
    }//GEN-LAST:event_btnNewActionPerformed

    private void lstElencoSquadraValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstElencoSquadraValueChanged
        selezionaSquadra();
    }//GEN-LAST:event_lstElencoSquadraValueChanged

    private void txSquadra1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txSquadra1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txSquadra1ActionPerformed

    private void btnNewPartitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewPartitaActionPerformed
        if (validaPartita())
            salvaPartita();
    }//GEN-LAST:event_btnNewPartitaActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WinCalcio.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WinCalcio.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WinCalcio.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WinCalcio.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WinCalcio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnNewPartita;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblMsg;
    private javax.swing.JList<String> lstElencoSquadra;
    private javax.swing.JTable tbElencoPartita;
    private javax.swing.JTextField txGiornata;
    private javax.swing.JTextField txGolSquadra2;
    private javax.swing.JTextField txNome;
    private javax.swing.JTextField txSquadra1;
    private javax.swing.JTextField txSquadra2;
    private javax.swing.JTextField txtGolSquadra1;
    private javax.swing.JTextField txtSede;
    // End of variables declaration//GEN-END:variables

}
