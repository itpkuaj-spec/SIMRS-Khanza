import sys

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
        '                    </Events>\n                  </Component>\n                <Component class="widget.Label" name="jLabel16">',
        '                    </Events>\n                  </Component>\n' + xml_dokter + '\n                <Component class="widget.Label" name="jLabel16">'
    )
    with open(form_file, 'w', encoding='utf-8') as f:
        f.write(form_content)
    print("Injected Dokter Filter")
else:
    print("Already in form")
