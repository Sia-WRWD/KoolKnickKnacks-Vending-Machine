/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainFunctions;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author ir4un
 */
public class InsertMenu extends javax.swing.JFrame {

    /**
     * Creates new form startMenu
     */
    int mousepX;
    int mousepY;

    public InsertMenu() throws IOException {

        initComponents();
        lbltemp.setVisible(false);

        //scaleImage();
    }

    public void insertData() throws IOException {

        String source = System.getProperty("user.dir") + "\\src\\txtdatabase\\itemlist.txt"; //Retrieving Directory of itemlist.txt File.
        File file = new File(source);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String record;

        while ((record = br.readLine()) != null) { //Retrieving All Data itemlist.txt File.
            String[] fields = record.split("::");
            String product_id = fields[0];
            String product_name = fields[1];
            String product_price = fields[2];
            String product_quantity = fields[3];
            String product_thumbslot = fields[4];

            String lastNum = fields[0];

            if ("*".equals(product_name)) { //Checks if Data are empty, then Data will be inserted.
                br.close();
                deleteData(source, product_id, 1, "::"); //Removes Line of Empty Data.
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bf = new BufferedWriter(fw);
                String newproduct_name = txtfproduct_name.getText(); //Assign Variables Values Based on Input of Textboxes.
                String newproduct_price = txtfproduct_price.getText();
                String newproduct_quantity = txtfproduct_quantity.getText();
                String datalist = product_id + "::" + newproduct_name + "::" + newproduct_price + "::" + newproduct_quantity + "::" + "thumbslot" + product_id + ".png" + "\n";
                bf.write(datalist); //Writes the Values From The Variables to the itemlist.txt File.
                bf.close();
                br.close();
                sortData(source, source); //Rearrange the Lines in itemlist.txt Alphabetically.
                String oldFileLocation = lbltemp.getText(); //Assigns Location of Selected Image to a Label to Send to insertImage() Method.
                insertImage(product_id, oldFileLocation); //Calls insertImage Method with values of product_id and oldFileLocation.

            } else if ((lastNum.equals("10")) && (!"*".equals(product_name))) { //Checks if the Last Line is Empty, Then it Means That All Lines are full and itemlist.txt is full and No Data Will Be Inserted.
                br.close();
                JFrame parent = new JFrame(); //Display Error Message.
                JOptionPane.showMessageDialog(parent, "Max Amount of Slots Reached!" + "\n" + "Please Remove an Item from a Slot.");
            }

        }

    }

    public void retrieveImage() {

        String source = System.getProperty("user.dir");
        String destination = source + "\\src\\productthumbnail\\"; //Sets the Directory Folder Containing Thumbnail Image Files.

        try {

            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("4 Extensions Supported", "Jpg", "png", "jpeg", "gif");
            fileChooser.setFileFilter(filter);
            int selected = fileChooser.showOpenDialog(null);
            if (selected == JFileChooser.APPROVE_OPTION) { //Opens Window To Select Image File To Upload As Thumbnail Image.

                File file = fileChooser.getSelectedFile(); //Store Image as File Object.
                String getselectedImage = file.getAbsolutePath(); //Assigns Variable Containing Directory Where It Was Selected.

                ImageIcon imIco = new ImageIcon(getselectedImage); //Creates Image Icon Object From The Provided Directory.
                Image imFit = imIco.getImage(); //Creates Image Object From The Image Icon Object.
                lblproduct_thumb.setIcon(new ImageIcon(imFit)); //Sets Icon Image on Label As A Preview.
                lbltemp.setText(getselectedImage); //Sets Directory of Selected Image Which Will Be Sent To the insertData() Method.

            }
        } catch (Exception e) {

            JFrame parent = new JFrame(); //Display Error Message.
            JOptionPane.showMessageDialog(parent, "Error Occured While Trying To Retrieve Image");
        }

    }

    public void insertImage(String insertedProductID, String imageFileSelected) {

        File file = new File(imageFileSelected); //Create A File Object With The Directory of the Selected Image.

        String source = System.getProperty("user.dir"); //Retrieving Directory of The Source Files.
        String destination = source + "\\src\\productthumbnail\\"; //Retrieving Directory of The Folder Containing Thumbnail Image Files.
        String extensionName = FilenameUtils.getExtension(imageFileSelected); // Retrieve File Extension of Selected File.
        String newFileName = destination + "thumbslot" + insertedProductID + "." + extensionName; // Assigning New Directory and New Image Filename.

        File newFile = new File(newFileName);  // Create File Objects of the Image File That Will Be Transfered To The Source Folder.
        File oriFileName = new File(destination);

        try {
            if (newFile.exists()) { //Checks If An Image With The Same Name Already Exist.
                throw new java.io.IOException("file exists");
            }
            Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING); //Copies File To The Thumbnail Image Folder.
            boolean success = oriFileName.renameTo(newFile); //After Copying, The Image Will Be Renamed.

            if (!success) {
                // File was not successfully renamed
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(String filepath, String removeterm, int positionOfTerm, String delimiter) throws IOException {

        int position = positionOfTerm - 1; // Integer Value Assigned To Choose Which Column in a Line To Be Selected for Deletion.
        String tempFile = System.getProperty("user.dir") + "\\src\\txtdatabase\\temp.txt"; //Creates A Temporary File Which Will Store the Updated itemlist.txt File.
        File oldFile = new File(filepath);
        File newFile = new File(tempFile);
        String readData;
        String data[];

        try {

            FileWriter fw = new FileWriter(tempFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);

            while ((readData = br.readLine()) != null) { //Data in itemlist.txt Will Be Inserted into data Array.
                data = readData.split("::");
                if (!(data[position].equalsIgnoreCase(removeterm))) { //Identify Which Line Contains the removeterm Value.
                    pw.println(readData); //Writes to the temp.txt File Without the Data of the Identified Line with the removeterm Value.
                }
            }

            pw.flush();
            pw.close();
            fr.close();
            br.close();
            bw.close();
            fw.close();

            oldFile.delete(); //Deletes The Original itemlist.txt File.
            File dump = new File(filepath);
            newFile.renameTo(dump); //Renames The temp.txt File to itemlist.txt.

        } catch (Exception e) {

            JFrame parent = new JFrame(); //Display Error Message.
            JOptionPane.showMessageDialog(parent, "Error Occured While Trying To Delete Data");
        }

    }

    public void sortData(String filepath, String tofileName) {

        try { //What in the sauerkraut is going on here?.

            Path path = Paths.get(filepath);
            Charset charset = Charset.forName("UTF-8");

            List<String> lines = Files.readAllLines(path, charset);
            Collections.sort(lines, new IgnoreCase());

            Path toPath = Paths.get(tofileName);
            Files.write(toPath, lines, charset);

        } catch (Exception e) {

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        windowsControl = new javax.swing.JPanel();
        windowsLogo = new javax.swing.JPanel();
        panellogo = new javax.swing.JPanel();
        lbllogo = new javax.swing.JLabel();
        windowsButtons1 = new javax.swing.JPanel();
        btnminmax = new javax.swing.JPanel();
        lblminmax = new javax.swing.JLabel();
        btnclose = new javax.swing.JPanel();
        lblclose = new javax.swing.JLabel();
        btnplay = new javax.swing.JPanel();
        lblplay = new javax.swing.JLabel();
        btnmute = new javax.swing.JPanel();
        lblmute = new javax.swing.JLabel();
        windowsButtons2 = new javax.swing.JPanel();
        paneltitle = new javax.swing.JPanel();
        lbltitle = new javax.swing.JLabel();
        mainFrame = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnSubmit = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtfproduct_name = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtfproduct_price = new javax.swing.JTextField();
        lblproduct_thumb = new javax.swing.JLabel();
        txtfproduct_quantity = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnSelectImg = new javax.swing.JButton();
        lbltemp = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        windowsControl.setBackground(new java.awt.Color(51, 51, 51));
        windowsControl.setPreferredSize(new java.awt.Dimension(100, 50));
        windowsControl.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                windowsControlMouseDragged(evt);
            }
        });
        windowsControl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                windowsControlMousePressed(evt);
            }
        });
        windowsControl.setLayout(new java.awt.BorderLayout());

        windowsLogo.setBackground(new java.awt.Color(51, 51, 51));
        windowsLogo.setPreferredSize(new java.awt.Dimension(75, 50));

        panellogo.setBackground(new java.awt.Color(51, 51, 51));
        panellogo.setPreferredSize(new java.awt.Dimension(75, 50));
        panellogo.setLayout(new java.awt.BorderLayout());

        lbllogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbllogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/kkklogo.png"))); // NOI18N
        lbllogo.setMaximumSize(new java.awt.Dimension(75, 50));
        lbllogo.setMinimumSize(new java.awt.Dimension(50, 50));
        lbllogo.setPreferredSize(new java.awt.Dimension(75, 50));
        lbllogo.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                lbllogoMouseDragged(evt);
            }
        });
        lbllogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbllogoMousePressed(evt);
            }
        });
        panellogo.add(lbllogo, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout windowsLogoLayout = new javax.swing.GroupLayout(windowsLogo);
        windowsLogo.setLayout(windowsLogoLayout);
        windowsLogoLayout.setHorizontalGroup(
            windowsLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(windowsLogoLayout.createSequentialGroup()
                .addComponent(panellogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        windowsLogoLayout.setVerticalGroup(
            windowsLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, windowsLogoLayout.createSequentialGroup()
                .addComponent(panellogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        windowsControl.add(windowsLogo, java.awt.BorderLayout.LINE_START);

        windowsButtons1.setBackground(new java.awt.Color(51, 51, 51));
        windowsButtons1.setPreferredSize(new java.awt.Dimension(200, 50));

        btnminmax.setBackground(new java.awt.Color(51, 51, 51));
        btnminmax.setLayout(new java.awt.BorderLayout());

        lblminmax.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblminmax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/minmax.png"))); // NOI18N
        lblminmax.setMaximumSize(new java.awt.Dimension(50, 50));
        lblminmax.setMinimumSize(new java.awt.Dimension(50, 50));
        lblminmax.setPreferredSize(new java.awt.Dimension(50, 50));
        lblminmax.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblminmaxMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblminmaxMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblminmaxMouseExited(evt);
            }
        });
        btnminmax.add(lblminmax, java.awt.BorderLayout.CENTER);

        btnclose.setBackground(new java.awt.Color(51, 51, 51));
        btnclose.setLayout(new java.awt.BorderLayout());

        lblclose.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblclose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/X.png"))); // NOI18N
        lblclose.setMaximumSize(new java.awt.Dimension(50, 50));
        lblclose.setMinimumSize(new java.awt.Dimension(50, 50));
        lblclose.setPreferredSize(new java.awt.Dimension(50, 50));
        lblclose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblcloseMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblcloseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblcloseMouseExited(evt);
            }
        });
        btnclose.add(lblclose, java.awt.BorderLayout.CENTER);

        btnplay.setBackground(new java.awt.Color(51, 51, 51));
        btnplay.setLayout(new java.awt.BorderLayout());

        lblplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblplay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/speakerPlay.png"))); // NOI18N
        lblplay.setMaximumSize(new java.awt.Dimension(50, 50));
        lblplay.setMinimumSize(new java.awt.Dimension(50, 50));
        lblplay.setPreferredSize(new java.awt.Dimension(50, 50));
        lblplay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblplayMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblplayMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblplayMouseExited(evt);
            }
        });
        btnplay.add(lblplay, java.awt.BorderLayout.CENTER);

        btnmute.setBackground(new java.awt.Color(51, 51, 51));
        btnmute.setLayout(new java.awt.BorderLayout());

        lblmute.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblmute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/speakerMute.png"))); // NOI18N
        lblmute.setMaximumSize(new java.awt.Dimension(50, 50));
        lblmute.setMinimumSize(new java.awt.Dimension(50, 50));
        lblmute.setPreferredSize(new java.awt.Dimension(50, 50));
        lblmute.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmuteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblmuteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblmuteMouseExited(evt);
            }
        });
        btnmute.add(lblmute, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout windowsButtons1Layout = new javax.swing.GroupLayout(windowsButtons1);
        windowsButtons1.setLayout(windowsButtons1Layout);
        windowsButtons1Layout.setHorizontalGroup(
            windowsButtons1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(windowsButtons1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnmute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnminmax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnclose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        windowsButtons1Layout.setVerticalGroup(
            windowsButtons1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, windowsButtons1Layout.createSequentialGroup()
                .addGroup(windowsButtons1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnplay, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnminmax, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnclose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnmute, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        windowsControl.add(windowsButtons1, java.awt.BorderLayout.LINE_END);

        windowsButtons2.setBackground(new java.awt.Color(51, 51, 51));

        paneltitle.setBackground(new java.awt.Color(51, 51, 51));
        paneltitle.setLayout(new java.awt.BorderLayout());

        lbltitle.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbltitle.setForeground(new java.awt.Color(255, 255, 255));
        lbltitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbltitle.setText("Insert Product Menu");
        lbltitle.setMaximumSize(new java.awt.Dimension(500, 50));
        lbltitle.setMinimumSize(new java.awt.Dimension(50, 50));
        lbltitle.setPreferredSize(new java.awt.Dimension(500, 50));

        javax.swing.GroupLayout windowsButtons2Layout = new javax.swing.GroupLayout(windowsButtons2);
        windowsButtons2.setLayout(windowsButtons2Layout);
        windowsButtons2Layout.setHorizontalGroup(
            windowsButtons2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, windowsButtons2Layout.createSequentialGroup()
                .addContainerGap(171, Short.MAX_VALUE)
                .addComponent(paneltitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbltitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72))
        );
        windowsButtons2Layout.setVerticalGroup(
            windowsButtons2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paneltitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(windowsButtons2Layout.createSequentialGroup()
                .addComponent(lbltitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        windowsControl.add(windowsButtons2, java.awt.BorderLayout.CENTER);

        mainFrame.setBackground(new java.awt.Color(72, 105, 193));

        jLabel1.setText("Welcome to KoolKnickKnacks!");

        jLabel2.setText("Bringing the fun, to you....");

        btnSubmit.setBackground(new java.awt.Color(51, 51, 51));
        btnSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmit.setText("Submit Product");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Product Name:");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Product Price:");

        lblproduct_thumb.setBackground(new java.awt.Color(255, 255, 255));

        txtfproduct_quantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtfproduct_quantityActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Product Quantity:");

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Product Thumbnail:");

        btnSelectImg.setBackground(new java.awt.Color(51, 51, 51));
        btnSelectImg.setForeground(new java.awt.Color(255, 255, 255));
        btnSelectImg.setText("Select Image");
        btnSelectImg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectImgActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(426, 426, 426)
                        .addComponent(jLabel7))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtfproduct_price, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(374, 374, 374)
                                .addComponent(txtfproduct_name, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(436, 436, 436)
                                .addComponent(jLabel4)
                                .addGap(61, 61, 61)))
                        .addComponent(txtfproduct_quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(386, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(439, 439, 439))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(444, 444, 444))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnSelectImg, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblproduct_thumb, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(192, 192, 192)
                        .addComponent(lbltemp, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel3)
                .addGap(5, 5, 5)
                .addComponent(txtfproduct_name, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtfproduct_price, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtfproduct_quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblproduct_thumb, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbltemp, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnSelectImg, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 33, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainFrameLayout = new javax.swing.GroupLayout(mainFrame);
        mainFrame.setLayout(mainFrameLayout);
        mainFrameLayout.setHorizontalGroup(
            mainFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainFrameLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(384, 384, 384))
            .addGroup(mainFrameLayout.createSequentialGroup()
                .addGroup(mainFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainFrameLayout.createSequentialGroup()
                        .addGap(366, 366, 366)
                        .addComponent(jLabel1))
                    .addGroup(mainFrameLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
            .addComponent(btnSubmit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mainFrameLayout.setVerticalGroup(
            mainFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainFrameLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(windowsControl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mainFrame, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(windowsControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(mainFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setSize(new java.awt.Dimension(1024, 768));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void changecolor(JPanel hover, Color rand) {
        hover.setBackground(rand);

    }
    private void windowsControlMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_windowsControlMouseDragged
        int kordinatX = evt.getXOnScreen();
        int kordinatY = evt.getYOnScreen();

        this.setLocation(kordinatX - mousepX, kordinatY - mousepY);
    }//GEN-LAST:event_windowsControlMouseDragged

    private void windowsControlMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_windowsControlMousePressed
        mousepX = evt.getX();
        mousepY = evt.getY();
    }//GEN-LAST:event_windowsControlMousePressed

    private void lblcloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblcloseMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lblcloseMouseClicked

    private void lblcloseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblcloseMouseEntered
        changecolor(btnclose, new Color(93, 93, 93));
        lblclose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));    }//GEN-LAST:event_lblcloseMouseEntered

    private void lblcloseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblcloseMouseExited
        changecolor(btnclose, new Color(51, 51, 51));
        lblclose.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));    }//GEN-LAST:event_lblcloseMouseExited

    private void lblplayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblplayMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblplayMouseClicked

    private void lblplayMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblplayMouseEntered
        changecolor(btnplay, new Color(93, 93, 93));
        lblplay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));    }//GEN-LAST:event_lblplayMouseEntered

    private void lblplayMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblplayMouseExited
        changecolor(btnplay, new Color(51, 51, 51));
        lblplay.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));    }//GEN-LAST:event_lblplayMouseExited

    private void lblmuteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmuteMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblmuteMouseClicked

    private void lblmuteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmuteMouseEntered
        changecolor(btnmute, new Color(93, 93, 93));
        lblmute.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));    }//GEN-LAST:event_lblmuteMouseEntered

    private void lblmuteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmuteMouseExited
        changecolor(btnmute, new Color(51, 51, 51));
        lblmute.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));    }//GEN-LAST:event_lblmuteMouseExited

    private void lbllogoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbllogoMousePressed
        mousepX = evt.getX();
        mousepY = evt.getY();    }//GEN-LAST:event_lbllogoMousePressed

    private void lbllogoMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbllogoMouseDragged
        int kordinatX = evt.getXOnScreen();
        int kordinatY = evt.getYOnScreen();

        this.setLocation(kordinatX - mousepX, kordinatY - mousepY);    }//GEN-LAST:event_lbllogoMouseDragged

    private void lblminmaxMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblminmaxMouseEntered
        changecolor(btnminmax, new Color(93, 93, 93));
        lblminmax.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));    }//GEN-LAST:event_lblminmaxMouseEntered

    private void lblminmaxMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblminmaxMouseExited
        changecolor(btnminmax, new Color(51, 51, 51));
        lblminmax.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));    }//GEN-LAST:event_lblminmaxMouseExited

    private void lblminmaxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblminmaxMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblminmaxMouseClicked

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        try {
            insertData();

        } catch (IOException ex) {
            Logger.getLogger(InsertMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnSelectImgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectImgActionPerformed
        retrieveImage();
    }//GEN-LAST:event_btnSelectImgActionPerformed

    private void txtfproduct_quantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtfproduct_quantityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtfproduct_quantityActionPerformed

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
            java.util.logging.Logger.getLogger(InsertMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InsertMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InsertMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InsertMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new InsertMenu().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(InsertMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSelectImg;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JPanel btnclose;
    private javax.swing.JPanel btnminmax;
    private javax.swing.JPanel btnmute;
    private javax.swing.JPanel btnplay;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblclose;
    private javax.swing.JLabel lbllogo;
    private javax.swing.JLabel lblminmax;
    private javax.swing.JLabel lblmute;
    private javax.swing.JLabel lblplay;
    private javax.swing.JLabel lblproduct_thumb;
    private javax.swing.JLabel lbltemp;
    private javax.swing.JLabel lbltitle;
    private javax.swing.JPanel mainFrame;
    private javax.swing.JPanel panellogo;
    private javax.swing.JPanel paneltitle;
    private javax.swing.JTextField txtfproduct_name;
    private javax.swing.JTextField txtfproduct_price;
    private javax.swing.JTextField txtfproduct_quantity;
    private javax.swing.JPanel windowsButtons1;
    private javax.swing.JPanel windowsButtons2;
    private javax.swing.JPanel windowsControl;
    private javax.swing.JPanel windowsLogo;
    // End of variables declaration//GEN-END:variables
}
