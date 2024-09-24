import re
input_file = r"C:\temp\dresden.xml"
output_file = r"C:\temp\dresden_transformed.xml"

with open(input_file, 'r', encoding='latin1') as f:
    content = f.read()
    content = re.sub(r'(<\?xml.*encoding=["\'])([^"\']*)(["\'].*\?>)', r'\1UTF-8\3', content)


with open(output_file, 'w', encoding='utf-8') as f:
    f.write(content)
