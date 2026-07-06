import sys

with open('d:\\Project\\Source SIMRS ORI\\SIMRS PKU Aisyiyah Jepara\\SIMRS-Khanza\\src\\tambahan_it\\PKUDlgListKlaim.form', 'r', encoding='utf-8') as f:
    content = f.read()

xml_btn_simpan = '''                <Component class="widget.Button" name="BtnSimpanCatatan">
                  <Properties>
                    <Property name="background" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
                      <Color blue="0" green="33" red="ff" type="rgb"/>
                    </Property>
                    <Property name="foreground" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
                      <Color blue="ff" green="ff" red="ff" type="rgb"/>
                    </Property>
                    <Property name="mnemonic" type="int" value="83"/>
                    <Property name="text" type="java.lang.String" value="Simpan"/>
                    <Property name="toolTipText" type="java.lang.String" value="Alt+S"/>
                    <Property name="name" type="java.lang.String" value="BtnSimpanCatatan" noResource="true"/>
                    <Property name="opaque" type="boolean" value="true"/>
                  </Properties>
                  <Events>
                    <EventHandler event="actionPerformed" listener="java.awt.event.ActionListener" parameters="java.awt.event.ActionEvent" handler="BtnSimpanCatatanActionPerformed"/>
                    <EventHandler event="keyPressed" listener="java.awt.event.KeyListener" parameters="java.awt.event.KeyEvent" handler="BtnSimpanCatatanKeyPressed"/>
                  </Events>
                </Component>'''

content = content.replace(
    '              <SubComponents>\n                <Component class="widget.Button" name="BtnCloseInpindah4">',
    '              <SubComponents>\n' + xml_btn_simpan + '\n                <Component class="widget.Button" name="BtnCloseInpindah4">'
)

xml_cmb_hlm = '''                <Component class="widget.Label" name="jLabel11">
                  <Properties>
                    <Property name="text" type="java.lang.String" value="Limit Data :"/>
                    <Property name="name" type="java.lang.String" value="jLabel11" noResource="true"/>
                    <Property name="preferredSize" type="java.awt.Dimension" editor="org.netbeans.beaninfo.editors.DimensionEditor">
                      <Dimension value="[70, 23]"/>
                    </Property>
                  </Properties>
                </Component>
                <Component class="widget.ComboBox" name="cmbHlm">
                  <Properties>
                    <Property name="model" type="javax.swing.ComboBoxModel" editor="org.netbeans.modules.form.editors2.ComboBoxModelEditor">
                      <StringArray count="8">
                        <StringItem index="0" value="50"/>
                        <StringItem index="1" value="100"/>
                        <StringItem index="2" value="200"/>
                        <StringItem index="3" value="300"/>
                        <StringItem index="4" value="400"/>
                        <StringItem index="5" value="500"/>
                        <StringItem index="6" value="1000"/>
                        <StringItem index="7" value="Semua"/>
                      </StringArray>
                    </Property>
                    <Property name="name" type="java.lang.String" value="cmbHlm" noResource="true"/>
                    <Property name="preferredSize" type="java.awt.Dimension" editor="org.netbeans.beaninfo.editors.DimensionEditor">
                      <Dimension value="[75, 23]"/>
                    </Property>
                  </Properties>
                </Component>'''

content = content.replace(
    '                  <Events>\n                    <EventHandler event="keyPressed" listener="java.awt.event.KeyListener" parameters="java.awt.event.KeyEvent" handler="DTPTglAkhirKeyPressed"/>\n                  </Events>\n                </Component>\n                <Component class="widget.CekBox" name="chkAutoRefresh">',
    '                  <Events>\n                    <EventHandler event="keyPressed" listener="java.awt.event.KeyListener" parameters="java.awt.event.KeyEvent" handler="DTPTglAkhirKeyPressed"/>\n                  </Events>\n                </Component>\n' + xml_cmb_hlm + '\n                <Component class="widget.CekBox" name="chkAutoRefresh">'
)

xml_btn_warna = '''                <Component class="widget.Button" name="BtnKetWarna">
                  <Properties>
                    <Property name="icon" type="javax.swing.Icon" editor="org.netbeans.modules.form.editors2.IconEditor">
                      <Image iconType="3" name="/picture/satuan.png"/>
                    </Property>
                    <Property name="mnemonic" type="int" value="72"/>
                    <Property name="text" type="java.lang.String" value="Keterangan Warna"/>
                    <Property name="toolTipText" type="java.lang.String" value="Alt+H"/>
                    <Property name="name" type="java.lang.String" value="BtnKetWarna" noResource="true"/>
                    <Property name="preferredSize" type="java.awt.Dimension" editor="org.netbeans.beaninfo.editors.DimensionEditor">
                      <Dimension value="[170, 30]"/>
                    </Property>
                  </Properties>
                  <Events>
                    <EventHandler event="actionPerformed" listener="java.awt.event.ActionListener" parameters="java.awt.event.ActionEvent" handler="BtnKetWarnaActionPerformed"/>
                    <EventHandler event="keyPressed" listener="java.awt.event.KeyListener" parameters="java.awt.event.KeyEvent" handler="BtnKetWarnaKeyPressed"/>
                  </Events>
                </Component>'''

content = content.replace(
    '                  <Properties>\n                    <Property name="horizontalAlignment" type="int" value="2"/>\n                    <Property name="text" type="java.lang.String" value="-"/>\n                    <Property name="horizontalTextPosition" type="int" value="4"/>\n                    <Property name="name" type="java.lang.String" value="timeslaps" noResource="true"/>\n                    <Property name="preferredSize" type="java.awt.Dimension" editor="org.netbeans.beaninfo.editors.DimensionEditor">\n                      <Dimension value="[100, 23]"/>\n                    </Property>\n                  </Properties>\n                </Component>\n                <Component class="widget.Button" name="BtnAll">',
    '                  <Properties>\n                    <Property name="horizontalAlignment" type="int" value="2"/>\n                    <Property name="text" type="java.lang.String" value="-"/>\n                    <Property name="horizontalTextPosition" type="int" value="4"/>\n                    <Property name="name" type="java.lang.String" value="timeslaps" noResource="true"/>\n                    <Property name="preferredSize" type="java.awt.Dimension" editor="org.netbeans.beaninfo.editors.DimensionEditor">\n                      <Dimension value="[100, 23]"/>\n                    </Property>\n                  </Properties>\n                </Component>\n' + xml_btn_warna + '\n                <Component class="widget.Button" name="BtnAll">'
)

with open('d:\\Project\\Source SIMRS ORI\\SIMRS PKU Aisyiyah Jepara\\SIMRS-Khanza\\src\\tambahan_it\\PKUDlgListKlaim.form', 'w', encoding='utf-8') as f:
    f.write(content)
