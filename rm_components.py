import re

form_path = 'src/tambahan_it/PKUDlgListKlaim.form'
with open(form_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Remove MnPilihCeklisRanap block
content = re.sub(r'^[ \t]*<Menu class=\"javax\.swing\.JMenu\" name=\"MnPilihCeklisRanap\">.*?</Menu>[\r\n]*', '', content, flags=re.MULTILINE | re.DOTALL)

# Remove BtnKetWarna block
content = re.sub(r'^[ \t]*<Component class=\"widget\.Button\" name=\"BtnKetWarna\">.*?</Component>[\r\n]*', '', content, flags=re.MULTILINE | re.DOTALL)

with open(form_path, 'w', encoding='utf-8') as f:
    f.write(content)
