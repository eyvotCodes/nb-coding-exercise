# Nexu Coding Exercise

> Este documento no busca seguir un formato típico de README.md como el que se esperaría en un
> proyecto de software colaborativo. Si bien contiene algunos paralelismos, su enfoque es más
> narrativo, describiendo cómo se abordó la prueba técnica y los razonamientos detrás de cada
> decisión, tal como se indica en el enunciado: *"with some notes on your thoughts"*. Esta parte
> puede ser más valiosa para los revisores que simplemente hacer ~~"code first, think later"~~.


## Tooling & Tech Stack Assessment

### Consideraciones Para La Prueba

- Time-boxing de 2h *"we hope that you can spend around 2 hours working through this exercise"*.
- Creación de endpoints de una API REST.
- Persistencia en Base de Datos.
- Cargar como inicial `./models.json` del repositorio de la prueba.
- No se espera la finalización (o correcta finalización) de la prueba dentro del time-box de 2h
  "We don't expect you to finish in 2 hours".
- User un linter de código *"your code should be linted"*.
- Implementar pruebas *"your code should include at least a couple of tests"*.

### Objetivos Como Candidato

- Proporcionar agilidad a los revisores de la prueba:
   - H2 como base de datos. Es una base en memoria muy práctica para este tipo de casos y
     ampliamente usada en el ecosistema de Spring.
   - Swagger UI. Se proporciona una URL donde los endpoints pueden probarse desde una interfaz
     gráfica.
- Dar un "vertical slice" técnico a nivel de código, por lo que cada endpoint se implementará
  respetando contratos y contará con pruebas automatizadas que validan su funcionamiento.

### Agilidad vs. Estructura

Lo primero que suele venir a la mente en este tipo de pruebas es "agilidad" (para abarcar lo más
posible dentro del time-box de 2h). Sin embargo, dado que en las instrucciones se menciona que el
código debe estar "linteado", que haya al menos algunos tests y que no se espera que el candidato
termine en ese tiempo, parece que también se valorará positivamente el uso de buenas prácticas
y estructura.

Por este motivo se eligió el lenguaje Java, ya que "se presta" para seguir buenas prácticas de
Arquitectura Limpia, como el principio SOLID de Inversión de Dependencias. Java por naturaleza
promueve orden, "tipado" fuerte y convenciones muy claras desde el propio lenguaje. Si se tratara
de un reto enfocado en agilidad, tal vez habría optado por Python/FastAPI
o Node.js/Express.

Aunque se menciona que el código debe estar “linted”, en el ecosistema de Java no es tan común
apoyarse en linters básicos como en lenguajes más permisivos (por ejemplo, JavaScript). En lugar
de un linter como tal, evaluaría la posibilidad integrar una herramienta más potente como
*SonarQube*, que también detecta problemas de seguridad, duplicidad, code smells, entre otros.


### Stack Elegido

Desarrollo

- Versión LTS más reciente de **Java** (21) compatible con cloud vendors populares.
- Versión estable más reciente de **Spring Boot** (3.4.4).
- Versión más reciente de **Maven** (project building, dependency management).
- **SDKMAN!** (package management).

Management

- **Notion** para “progress tracking” mediante una base de datos de tareas con vista de backlog
  y board.

## Project Setup

Para ejecutar el proyecto de forma local, se necesita instalar previamente lo siguiente:

~~~
# instalación de JDKMAN! (para instalar Java, Spring Boot y Maven)
$ curl -s "https://get.sdkman.io" | bash
$ source "$HOME/.sdkman/bin/sdkman-init.sh" # en caso de seguir usarndo la misma sesión del shell
$ sdk version # comprobar instalación

# instalación de maven
$ sdk install maven # última versión estable

# instalación de java
$ sdk install java 21.0.2-open # última lts compatible con cloud vendors populares

# instalación de spring boot
$ sdk install springbot # versión estable más reciente
$ echo 'export JAVA_HOME="$HOME/.sdkman/candidates/java/current"' >> ~/.zshrc # Spring CLI ocupa dicha variable, agregar dicha variable al ~/.bashrc en caso de no usar ZSH
$ source ~/.zshrc # en caso de seguir usarndo la misma sesión del shell, usar ~/.bashrc en caso de no usar ZSH
$ echo $JAVA_HOME # comprobar variable

# comprobaciones adicionales
$ java -version
$ mvn -version
$ spring --version
~~~

Una vez instalado el stack de desarrollo (o si se el equipo ya cuenta con Java y Maven) ejecutar:

~~~
$ mvn spring-boot:run
~~~

Esto producirá un output parecido al siguiente:

~~~
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.4.4)

...

2025-04-08T11:32:16.325-06:00  INFO 6335 --- [nbce] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
~~~

Como se observa, el proyecto usará el puerto 8080 para arrancar el servidor web.

### Testing

Una vez arrancado el proyecto, se puede hacer **testing manual** de los **endpoints** y consultar
su **documentación** en `localhost:8080/swagger-ui/index.html` o desde cualquier cliente http,
por ejemplo:

~~~
$ curl -X GET http://localhost:8080/brands
~~~

También se pueden ejecutar **pruebas automatizadas**, se alcanzaron a crear las siguientes:

1. **Test de Integración** para verificar el precio promedio de las marcas, ya que se calcula
   desde la base de datos.
2. **Test E2E** para el endpoint `GET /brands`.

Para ejecutar las pruebas automatizadas, hay que asegurarse antes de que no se esté ejecutando
ya el proyecto, y usar el siguiente comando:

~~~
$ mvn test
~~~

Se producirá un output que entre su texto incluye lo siguiente:

~~~
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.4.4)
 
...
 
Hibernate:     SELECT
        b.id,
        b.name,
        FLOOR(AVG(m.average_price)) AS average_price
    FROM brands b
    LEFT JOIN car_models m ON m.brand_id = b.id
    GROUP BY b.id, b.name
    ORDER BY b.id

✔ Should return brands with the average price of their related models
  Found 61 brands in total (showing only those in the example)
    ↳ Acura → avg: 702,109
    ↳ Audi → avg: 630,759
    ↳ Bentley → avg: 3,342,575
    ↳ BMW → avg: 858,702
    ↳ Buick → avg: 290,371
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.155 s

...

✔ GET /brands → List all brands
  Found 2943 characters in total response
  Example brands in JSON response...
{
  "id" : 1,
  "name" : "Acura",
  "average_price" : 702109
},
{
  "id" : 2,
  "name" : "Audi",
  "average_price" : 630759
},
{
  "id" : 3,
  "name" : "Bentley",
  "average_price" : 3342575
},
{
  "id" : 4,
  "name" : "BMW",
  "average_price" : 858702
},
{
  "id" : 5,
  "name" : "Buick",
  "average_price" : 290371
},
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.033 s
~~~

## Desarrollo De La Prueba

### Database

Para cumplir con el requerimiento de usar la data de `./models.json` del repositorio de la prueba,
se usó el siguiente script de Python para producir un output SQL (posterior carga en H2).

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
> además de una entidad "Model" también debería existir una entidad "Brand" para las marcas, ya
> que existe un endpoint para registrar nuevas marcas de coche.

*Spring Boot* busca por defecto los archivos `schema.sql` y `data.sql` en el
directorio `./src/mian/resources` para configurar la base de datos, así que así se encuentran
en el proyecto. El archivo `schema.sql` se hizo de forma manual, mientras que el `data.sql` se
generó redireccionando el output del script de Python para escribir un nuevo archivo:

~~~
$ chmod +x models2sql.py # agregar permisos de ejecución al script
$ python3 ./models2sql.py > data.sql
~~~

> Se puede acceder a un *playground SQL* de H2 en `localhost:8080/h2-console` para interactuar
> con la base de datos (en la práctica se usó para validar el setup de la misma).

### Contratos

> Antes de empezar a codificar como tal, se definieron contratos que cumplieran con los
> requerimientos de la prueba.

Contrato para el **API REST** (métodos de los endpoints a implementar).

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

### Implementaciones

Después de la definición de los contratos, se empezaron a implementar las funcionalidades pactadas
en forma de "vertical slice": desde el acceso a datos a la exposición en un endpoint RETS y sus
respectivas pruebas automatizadas.

### Tiempo

Respondiendo a la pregunta de la descripción de la prueba **"how you would proceed if you had
more time"**, simplemente seguiría implementando el resto
de [**tareas de este board**](https://fleyva.notion.site/1ce77f27d8f4806597abcf0ec06168b4).

| Task                                                                       | Completion |
|----------------------------------------------------------------------------|------------|
| Setup board del proyecto                                                   | ✅          |
| Setup/inicialización del proyecto                                          | ✅          |
| Script de data inicial para la Base de Datos                               | ✅          |
| Creación de repositorio remoto para alojar código fuente del proyecto      | ✅          |
| Setup de Base de Datos en memoria para el proyecto                         | ✅          |
| Creación de contratos a implementar por la capa de infraestructura         | ✅          |
| Endpoint GET /brands                                                       | ✅          |
| Setup Swagger UI, documentar GET /brands                                   | ✅          |
| Pruebas automatizadas para GET /brands                                     | ✅          |
| README.md para revisores de la prueba                                      | ✅          |
| Endpoint GET /brands/:id/models                                            | ⏳          |
| Pruebas automatizadas para GET /brands/:id/models                          | ⏳          |
| Endpoint POST /brands                                                      | ⏳          |
| Pruebas automatizadas para POST /brands                                    | ⏳          |
| Endpoint POST /brands/:id/models                                           | ⏳          |
| Pruebas automatizadas para POST /brands/:id/models                         | ⏳          |
| Endpoint PUT /models/:id                                                   | ⏳          |
| Pruebas automatizadas para PUT /models/:id                                 | ⏳          |
| Endpoint GET /models?greater=&lower=                                       | ⏳          |
| Pruebas automatizadas para GET /models?greater=&lower=                     | ⏳          |
| Evaluar/integrar herramienta de análisis de código estático tipo SonarQube | ⏳          |
| Pruebas manuales de todo el proyecto                                       | ⏳          |
| Deploy del proyecto                                                        | ⏳          |

Es decir, continuaría implementado las funcionalidades pactadas por los contratos que se definieron
y posiblemente realizar el setup de una herramienta estilo SonarQube. Dicho eso, el proyecto se puede
"complicar" a cualquier nivel más allá de los requerimientos de la prueba. Por ejemplo, llegar
a una solución *GitOps*.
