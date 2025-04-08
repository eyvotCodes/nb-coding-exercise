# Nexu Coding Exercise

### Database

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

_Spring Boot_ busca por defecto los archivos `schema.sql` y `data.sql` en el
directorio `./src/mian/resources` para configurar la base de datos, así que así se encuentran
en el proyecto. El archivo `schema.sql` se hizo de forma manual, mientras que el `data.sql` se
generó redireccionando el output del script de migración para escribir un nuevo archivo:

~~~
$ chmod +x models2sql.py # agregar permisos de ejecución al script
$ python3 ./models2sql.py > data.sql
~~~

> Se puede acceder a un _playground_ de H2 en el `localhost:8080/h2-console` para interactuar
> con la base de datos (en la práctica se usó para validar el setup de la misma).

### Contratos

Contrato para el __API REST__ (métodos de los endpoints a implementar).

~~~java
List<Brand> listBrands(); // GET /brands
List<Model> listModelsForBrand(int brandId); // GET /brands/:id/models
Brand registerNewBrand(String name); // POST /brands
Model registerNewModelForBrand(int brandId, String modelName, int modelAveragePrice); // POST /brands/:id/models
Model editModel(int id, int averagePrice); // PUT /models/:id
List<Model> listModelsForRangeOfPrices(int greater, int lower); // GET /models?greater=&lower=
~~~

Contrato para el repositorio de datos de marcas de coche.

~~~java
List<Brand> findAllBrandsCalculatingAverage();
Brand addNewBrand(String name);
~~~

Contrato para el repositorio de datos de modelos de coche.

~~~java
List<Model> findAllModelsForBrand(int brandId);
Model addNewModelForBrand(int brandId, String modelName, int modelAveragePrice);
Model updateModel(int id, int averagePrice);
List<Model> findModelsBetweenPriceRange(int minAveragePrice, int maxAveragePrice);
~~~

### Testing

Se pueden hacer __testing manual__ de los __endpoints__ y consultar su __documentación__
en `localhost:8080/swagger-ui/index.html` o desde cualquier cliente http, por ejemplo:

~~~
$ curl -X GET http://localhost:8080/brands
~~~
