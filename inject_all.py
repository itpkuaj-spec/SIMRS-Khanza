import sys

# 1. Update PKUDlgListKlaim.java
java_file = 'd:\\Project\\Source SIMRS ORI\\SIMRS PKU Aisyiyah Jepara\\SIMRS-Khanza\\src\\tambahan_it\\PKUDlgListKlaim.java'
with open(java_file, 'r', encoding='utf-8') as f:
    java_content = f.read()

# Add BtnSeekDokter declaration if missing
if 'private widget.Button BtnSeekDokter;' not in java_content:
    java_content = java_content.replace('    // Variables declaration - do not modify//GEN-BEGIN:variables',
                                      '    private widget.Button BtnSeekDokter;\n    // Variables declaration - do not modify//GEN-BEGIN:variables')

# Add jLabelDokter declaration if missing
if 'private widget.Label jLabelDokter;' not in java_content:
    java_content = java_content.replace('    // Variables declaration - do not modify//GEN-BEGIN:variables',
                                      '    private widget.Label jLabelDokter;\n    // Variables declaration - do not modify//GEN-BEGIN:variables')

# Add BtnSeekDokter event handlers if missing
if 'BtnSeekDokterActionPerformed' not in java_content:
    handlers = '''
    private void BtnSeekDokterActionPerformed(java.awt.event.ActionEvent evt) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DokterLayanan.setLocationRelativeTo(internalFrame1);
        DokterLayanan.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }
'''
    java_content = java_content.replace('    // Variables declaration - do not modify//GEN-BEGIN:variables',
                                      handlers + '\n    // Variables declaration - do not modify//GEN-BEGIN:variables')

# Inject initialization into initComponents()
init_code_dokter = '''
        jLabelDokter = new widget.Label();
        BtnSeekDokter = new widget.Button();
        
        jLabelDokter.setText("Dokter :");
        jLabelDokter.setName("jLabelDokter"); // NOI18N
        jLabelDokter.setPreferredSize(new java.awt.Dimension(50, 23));
        panelisi6.add(jLabelDokter);

        kdDokterView.setName("kdDokterView"); // NOI18N
        kdDokterView.setPreferredSize(new java.awt.Dimension(70, 23));
        panelisi6.add(kdDokterView);

        nmDokterView.setEditable(false);
        nmDokterView.setName("nmDokterView"); // NOI18N
        nmDokterView.setPreferredSize(new java.awt.Dimension(200, 23));
        panelisi6.add(nmDokterView);

        BtnSeekDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeekDokter.setMnemonic('4');
        BtnSeekDokter.setToolTipText("ALt+4");
        BtnSeekDokter.setName("BtnSeekDokter"); // NOI18N
        BtnSeekDokter.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnSeekDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeekDokterActionPerformed(evt);
            }
        });
        panelisi6.add(BtnSeekDokter);
'''

# Find the spot to inject filter dokter: right before jLabel16 in panelisi6 (or just add it to panelisi6)
if 'BtnSeekDokter = new widget.Button();' not in java_content:
    java_content = java_content.replace('        panelisi6.add(jLabel16);', init_code_dokter + '\n        panelisi6.add(jLabel16);')

init_code_simpan = '''
        BtnSimpanCatatan = new widget.Button();
        BtnSimpanCatatan.setBackground(new java.awt.Color(255, 51, 0));
        BtnSimpanCatatan.setForeground(new java.awt.Color(255, 255, 255));
        BtnSimpanCatatan.setMnemonic('S');
        BtnSimpanCatatan.setText("Simpan");
        BtnSimpanCatatan.setToolTipText("Alt+S");
        BtnSimpanCatatan.setName("BtnSimpanCatatan"); // NOI18N
        BtnSimpanCatatan.setOpaque(true);
        BtnSimpanCatatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanCatatanActionPerformed(evt);
            }
        });
        BtnSimpanCatatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanCatatanKeyPressed(evt);
            }
        });
        panelBiasa10.add(BtnSimpanCatatan);
'''
if 'BtnSimpanCatatan = new widget.Button();' not in java_content:
    java_content = java_content.replace('        panelBiasa10.add(BtnCloseInpindah4);', init_code_simpan + '\n        panelBiasa10.add(BtnCloseInpindah4);')

with open(java_file, 'w', encoding='utf-8') as f:
    f.write(java_content)


# 2. Update PKUDlgListKlaim.form
form_file = 'd:\\Project\\Source SIMRS ORI\\SIMRS PKU Aisyiyah Jepara\\SIMRS-Khanza\\src\\tambahan_it\\PKUDlgListKlaim.form'
with open(form_file, 'r', encoding='utf-8') as f:
    form_content = f.read()

xml_dokter = '''                <Component class="widget.Label" name="jLabelDokter">
                  <Properties>
                    <Property name="text" type="java.lang.String" value="Dokter :"/>
                    <Property name="name" type="java.lang.String" value="jLabelDokter" noResource="true"/>
                    <Property name="preferredSize" type="java.awt.Dimension" editor="org.netbeans.beaninfo.editors.DimensionEditor">
                      <Dimension value="[50, 23]"/>
                    </Property>
                  </Properties>
                </Component>
                <Component class="widget.TextBox" name="kdDokterView">
                  <Properties>
                    <Property name="name" type="java.lang.String" value="kdDokterView" noResource="true"/>
                    <Property name="preferredSize" type="java.awt.Dimension" editor="org.netbeans.beaninfo.editors.DimensionEditor">
                      <Dimension value="[70, 23]"/>
                    </Property>
                  </Properties>
                </Component>
                <Component class="widget.TextBox" name="nmDokterView">
                  <Properties>
                    <Property name="editable" type="boolean" value="false"/>
                    <Property name="name" type="java.lang.String" value="nmDokterView" noResource="true"/>
                    <Property name="preferredSize" type="java.awt.Dimension" editor="org.netbeans.beaninfo.editors.DimensionEditor">
                      <Dimension value="[200, 23]"/>
                    </Property>
                  </Properties>
                </Component>
                <Component class="widget.Button" name="BtnSeekDokter">
                  <Properties>
                    <Property name="icon" type="javax.swing.Icon" noResource="true" editor="org.netbeans.modules.form.editors2.IconEditor">
                      <Image iconType="3" name="/picture/190.png"/>
                    </Property>
                    <Property name="mnemonic" type="int" value="52"/>
                    <Property name="toolTipText" type="java.lang.String" value="ALt+4"/>
                    <Property name="name" type="java.lang.String" value="BtnSeekDokter" noResource="true"/>
                    <Property name="preferredSize" type="java.awt.Dimension" editor="org.netbeans.beaninfo.editors.DimensionEditor">
                      <Dimension value="[28, 23]"/>
                    </Property>
                  </Properties>
                  <Events>
                    <EventHandler event="actionPerformed" listener="java.awt.event.ActionListener" parameters="java.awt.event.ActionEvent" handler="BtnSeekDokterActionPerformed"/>
                  </Events>
                </Component>'''

if 'name="BtnSeekDokter"' not in form_content:
    form_content = form_content.replace(
        '                  </Properties>\n                </Component>\n                <Component class="widget.Label" name="jLabel16">',
        '                  </Properties>\n                </Component>\n' + xml_dokter + '\n                <Component class="widget.Label" name="jLabel16">'
    )

with open(form_file, 'w', encoding='utf-8') as f:
    f.write(form_content)

print("Done")
