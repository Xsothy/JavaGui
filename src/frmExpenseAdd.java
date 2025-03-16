import Support.DB;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Admin
 */
public class frmExpenseAdd extends javax.swing.JFrame {
      private int expenseId;
    private String expenseDate;
    private String expenseDescription;
    private double expenseAmount;
    private String staffId;
    private String imagePath;
    private frmExpenseView parentForm;

    public frmExpenseAdd(frmExpenseView parent, int id, String date, String description, double amount, String sId, String imagePath) {
        initComponents();
        this.parentForm = parent;
        this.expenseId = id;
        this.expenseDate = date;
        this.expenseDescription = description;
        this.expenseAmount = amount;
        this.staffId = sId;
        this.imagePath = imagePath;

        txtDate.setText(expenseDate);
        txtDesc.setText(expenseDescription);
        txtAmount.setText(String.valueOf(expenseAmount));
        populateStaffComboBox();
        cbStaffID.setSelectedItem(staffId);

        // Load and resize image with fixed size (200x200)
        loadExpenseImage();
    }
    private void loadExpenseImage() {
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                try {
                    ImageIcon icon = new ImageIcon(imagePath);
                    Image img = icon.getImage();Image scaledImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    lblImage.setIcon(new ImageIcon(scaledImg));
                } catch (Exception e) {
                    System.out.println("Error loading image: " + e.getMessage());
                    lblImage.setIcon(null);
                }
            } else {
                System.out.println("Image file not found: " + imagePath);
                lblImage.setIcon(null);
            }
        } else {
            lblImage.setIcon(null);
        }
    }
 
    private void setScaledImage(Image img) {
        int labelWidth = lblImage.getWidth();
        int labelHeight = lblImage.getHeight();

        if (labelWidth > 0 && labelHeight > 0) {
            Image scaledImg = img.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaledImg));
        } else {
            lblImage.setIcon(new ImageIcon(img));
        }
    }

    public frmExpenseAdd() {
        initComponents();
        populateStaffComboBox();
        clickEnter();
        addHoverEffect(btnImage);
        addHoverEffect(btnClose);
        addHoverEffect(btnSave);
    }

    private void populateStaffComboBox() {
//        try {
//            Connection con = DB.getInstance().getConnection().getData();
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT name FROM staff");
//            cbStaffID.removeAllItems();
//            while (rs.next()) {
//                String staffName = rs.getString("name");
//                cbStaffID.addItem(staffName);
//            }
//
//            rs.close();
//            stmt.close();
//            con.close();
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Error loading staff data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
    }
    private void clickEnter(){
        txtDate.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnSaveActionPerformed(null);
                }
            }
        });
       
        cbStaffID.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnSaveActionPerformed(null);
                }
            }
        });
        btnImage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnSaveActionPerformed(null);
                }
            }
        });
        
    }
     
    private JButton focusedButton = null;

    private void addHoverEffect(JButton button) {
        Color originalColor = button.getBackground(); 

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button != focusedButton) {
                    button.setBackground(new Color(73, 71, 79)); 
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button != focusedButton) {
                    button.setBackground(originalColor); 
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        txtDesc = new javax.swing.JTextField();
        txtDate = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        Customer = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cbStaffID = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        lblImage = new javax.swing.JLabel();
        btnImage = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(55, 58, 89));
        setFocusTraversalPolicyProvider(true);
        setFocusable(false);
        setForeground(new java.awt.Color(51, 102, 255));
        setLocation(new java.awt.Point(600, 200));
        setUndecorated(true);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("Bundle"); // NOI18N
        jLabel2.setText(bundle.getString("frmExpenseAdd.jLabel2.text")); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText(bundle.getString("frmExpenseAdd.jLabel3.text")); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText(bundle.getString("frmExpenseAdd.jLabel4.text")); // NOI18N

        txtAmount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtAmount.setText(bundle.getString("frmExpenseAdd.txtAmount.text")); // NOI18N

        txtDesc.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtDesc.setText(bundle.getString("frmExpenseAdd.txtDesc.text")); // NOI18N

        txtDate.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtDate.setText(bundle.getString("frmExpenseAdd.txtDate.text")); // NOI18N

        btnSave.setBackground(new java.awt.Color(51, 51, 255));
        btnSave.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText(bundle.getString("frmExpenseAdd.btnSave.text")); // NOI18N
        btnSave.setBorder(null);
        btnSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnClose.setBackground(new java.awt.Color(255, 51, 51));
        btnClose.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setText(bundle.getString("frmExpenseAdd.btnClose.text")); // NOI18N
        btnClose.setBorder(null);
        btnClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(55, 58, 89));

        Customer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-product-65.png"))); // NOI18N
        Customer.setText(bundle.getString("frmExpenseAdd.Customer.text")); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText(bundle.getString("frmExpenseAdd.jLabel1.text")); // NOI18N
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(Customer)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Customer)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        cbStaffID.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbStaffID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStaffIDActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel6.setText(bundle.getString("frmExpenseAdd.jLabel6.text")); // NOI18N

        lblImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add-to-basket.png"))); // NOI18N
        lblImage.setText(bundle.getString("frmExpenseAdd.lblImage.text")); // NOI18N

        btnImage.setBackground(new java.awt.Color(51, 51, 255));
        btnImage.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnImage.setForeground(new java.awt.Color(255, 255, 255));
        btnImage.setIcon(new javax.swing.ImageIcon("D:\\Y3S2\\javaII\\TestMainfrm_1\\src\\img\\icons8-upload-26 (1).png")); // NOI18N
        btnImage.setText(bundle.getString("frmExpenseAdd.btnImage.text")); // NOI18N
        btnImage.setActionCommand(bundle.getString("frmExpenseAdd.btnImage.actionCommand")); // NOI18N
        btnImage.setBorder(null);
        btnImage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImage.setFocusCycleRoot(true);
        btnImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(18, 18, 18)
                            .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(cbStaffID, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(lblImage)
                                        .addGap(12, 12, 12))
                                    .addComponent(btnImage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(17, 17, 17))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbStaffID, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(38, 38, 38)
                        .addComponent(lblImage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnImage, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose(); 
    }//GEN-LAST:event_btnCloseActionPerformed
    private String selectedImagePath = "";
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
          try {

            String createdAt = txtDate.getText().trim();
            String descText = txtDesc.getText().trim();
            String amountText = txtAmount.getText().trim();
            String staffName = cbStaffID.getSelectedItem() != null ? cbStaffID.getSelectedItem().toString().trim() : "";


            if (createdAt.isEmpty() || descText.isEmpty() || amountText.isEmpty() || staffName.isEmpty()) {
                showMessage("Please fill all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                showMessage("Invalid amount format.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int staffId = getStaffIdByName(staffName);
            if (staffId == 0) {
                showMessage("Staff not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (expenseId == 0) {
                saveExpenseToDatabase(staffId, amount, createdAt, descText, selectedImagePath);
                JOptionPane.showMessageDialog(this, "Expense added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                if (parentForm != null) {
                    parentForm.renderExpenseTable();
                }
                dispose(); 
                resetForm();
            } else {
                updateExpenseInDatabase(expenseId, staffId, amount, createdAt, descText, selectedImagePath);
                JOptionPane.showMessageDialog(this, "Expense updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                if (parentForm != null) {
                    parentForm.renderExpenseTable();
                    this.dispose();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmExpenseAdd.class.getName()).log(Level.SEVERE, "Database error", ex);
            showMessage("Error occurred while saving the expense. Please try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(frmExpenseAdd.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            showMessage("An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void showMessage(String message, String title, int messageType) {
        this.toFront();
        this.requestFocus();
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

    private void cbStaffIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStaffIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbStaffIDActionPerformed

    private void btnImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImageActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();

            ImageIcon imageIcon = new ImageIcon(selectedImagePath);
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaledImage));
        }
    }//GEN-LAST:event_btnImageActionPerformed
    private void saveExpenseToDatabase(int staffId, double amount, String createdAt, String descText, String selectedImagePath) throws SQLException, IOException {
        //FIXME: fix
        //        String uploadDir = "D:/Y3S2/javaII/Testing_Java/src/Expenses/";
//        String imageName = null;
//
//        File directory = new File(uploadDir);
//        if (!directory.exists()) {
//            directory.mkdirs(); 
//        }
//
//        if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
//            File sourceFile = new File(selectedImagePath);
//            if (sourceFile.exists()) {  
//                String extension = selectedImagePath.substring(selectedImagePath.lastIndexOf("."));
//                imageName = UUID.randomUUID().toString() + extension;
//                File destinationFile = new File(uploadDir + imageName);
//
//                Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//            } else {
//                System.out.println("Error: Selected image file does not exist!");
//            }
//        }
//
//        String query = "INSERT INTO expense (date, `desc`, amount, picture, sid) VALUES (?, ?, ?, ?, ?)";
//        try (Connection con = DB.getInstance().getConnection().getData();
//             PreparedStatement stmt = con.prepareStatement(query)) {
//
//            stmt.setString(1, createdAt);
//            stmt.setString(2, descText);
//            stmt.setDouble(3, amount);
//
//            if (imageName != null) {
//                stmt.setString(4, imageName); 
//            } else {
//                stmt.setNull(4, java.sql.Types.VARCHAR);
//            }
//
//            stmt.setInt(5, staffId);
//
//            int rowsInserted = stmt.executeUpdate();
//            if (rowsInserted == 0) {
//                throw new SQLException("Failed to insert the expense.");
//            }
//        }
    }

    private void updateExpenseInDatabase(int expenseId, int staffId, double amount, String createdAt, String descText, String selectedImagePath) throws SQLException {
//      String targetFolder = "D:/Y3S2/javaII/Testing_Java/src/Expenses/";
//      String imageFileName = null;
//
//      if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
//          File sourceFile = new File(selectedImagePath);
//          imageFileName = sourceFile.getName();
//          File targetFile = new File(targetFolder + imageFileName);
//
//          try {
//              Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//          } catch (IOException e) {
//              throw new SQLException("Error saving image: " + e.getMessage());
//          }
//      } else {
//          String query = "SELECT picture FROM expense WHERE id = ?";
//          try (Connection con = DB.getInstance().getConnection().getData();
//               PreparedStatement stmt = con.prepareStatement(query)) {
//              stmt.setInt(1, expenseId);
//              try (ResultSet rs = stmt.executeQuery()) {
//                  if (rs.next()) {
//                      imageFileName = rs.getString("picture");
//                  }
//              }
//          } catch (SQLException e) {
//              throw new SQLException("Error retrieving current image: " + e.getMessage());
//          }
//      }
//
//      String updateQuery = "UPDATE expense SET date = ?, `desc` = ?, amount = ?, picture = ?, sid = ? WHERE id = ?";
//      try (Connection con = DB.getInstance().getConnection().getData();
//           PreparedStatement stmt = con.prepareStatement(updateQuery)) {
//          stmt.setString(1, createdAt);
//          stmt.setString(2, descText);
//          stmt.setDouble(3, amount);
//
//          if (imageFileName != null) {
//              stmt.setString(4, imageFileName);
//          } else {
//              stmt.setNull(4, java.sql.Types.VARCHAR);
//          }
//
//          stmt.setInt(5, staffId);
//          stmt.setInt(6, expenseId);
//
//          int rowsUpdated = stmt.executeUpdate();
//          if (rowsUpdated == 0) {
//              throw new SQLException("No rows updated, please check the expense ID.");
//          }
//      }
  }

    private int getStaffIdByName(String staffName) throws SQLException {
        String query = "SELECT id FROM staff WHERE name = ?";
        try (Connection con = (Connection) DB.getInstance().getConnection().getData();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, staffName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int staffId = rs.getInt("id");
                return staffId;
            } else {
                return 0;
            }
        }
    }

    private void resetForm() {
        txtAmount.setText("");
        txtDesc.setText("");
        cbStaffID.setSelectedIndex(0);
        txtDate.setText(""); 
        lblImage.setIcon(new ImageIcon("img/add-to-basket.png"));
    }
    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(frmExpenseAdd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmExpenseAdd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmExpenseAdd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmExpenseAdd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
              //new frmExpenseAdd().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Customer;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnImage;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbStaffID;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblImage;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtDesc;
    // End of variables declaration//GEN-END:variables

}
