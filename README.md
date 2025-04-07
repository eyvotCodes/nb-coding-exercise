# Nexu Coding Exercise

Script para producir output SQL partiendo de `./models.json` del repositorio de la prueba.

~~~python
import json
import sys
import os

if len(sys.argv) < 2:
    print("Uso: python3 <script>.py models.json", file=sys.stderr)
    sys.exit(1)
json_file = sys.argv[1]
if not os.path.exists(json_file):
    print(f"Archivo no encontrado: {json_file}", file=sys.stderr)
    sys.exit(1)

# Extraer marcas de coche únicas y asignarles un ID incremental
with open(json_file, "r") as f:
    data = json.load(f)
brand_map = {}
brand_id = 1
for item in data:
    brand = item["brand_name"].strip()
    if brand not in brand_map:
        brand_map[brand] = brand_id
        brand_id += 1

# Imprimir INSERTs para tabla brands
print("INSERT INTO brands (id, name) VALUES")
brand_values = [f"({id_}, '{name.replace('\'', '\'\'')}')" for name, id_ in brand_map.items()]
print(",\n".join(sorted(brand_values, key=lambda x: int(x.split(",")[0][1:]))) + ";\n")

# Imprimir INSERTs para tabla car_models
print("INSERT INTO car_models (id, name, average_price, brand_id) VALUES")
model_values = []
for item in data:
    id_ = item["id"]
    name = item["name"].replace("'", "''")
    avg = "0.00" if not item.get("average_price") else f"{item['average_price']:.2f}"
    brand = item["brand_name"].strip()
    brand_fk = brand_map[brand]
    model_values.append(f"({id_}, '{name}', {avg}, {brand_fk})")

print(",\n".join(model_values) + ";")
~~~

> Si bien, solo se observa una colección "models" (modelos de coches), se infiere que, 
> además de una entidad "Model" también debería existir una "Brand" para las marcas, ya
> que existe un endpoint para registrar nuevas marcas de coche.
