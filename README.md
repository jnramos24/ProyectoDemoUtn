# Proyecto Clase 05 — API REST de clientes

**Diplomatura en Desarrollo de Software FinTech: IA y Microservicios — UTN / Codeki**

Una API REST hecha con Spring Boot: permite listar, buscar, crear, actualizar y eliminar
clientes de un banco. Los datos se guardan en una base H2 que corre en memoria, así que
**no hay que instalar ninguna base de datos**.

---

## 1. Qué necesitás antes de empezar

- **JDK 21** instalado.
- **IntelliJ IDEA** (Community alcanza).
- **Git Bash**, que viene con Git. Se usa para probar los endpoints (sección 4).
- **Conexión a internet la primera vez.** Al abrir el proyecto, Maven descarga Spring Boot,
  Hibernate y H2. Tarda varios minutos y se hace una sola vez.

---

## 2. Cómo abrirlo en IntelliJ

1. Descomprimí el `.zip` en una carpeta **sin espacios ni acentos en la ruta**
   (por ejemplo `C:\dev\proyecto-clase05`). Evitá `OneDrive` y las carpetas con `ñ` o
   tildes: dan errores raros de Maven en Windows.
2. **File > Open** → elegí la carpeta `proyecto-clase05`, la que contiene el `pom.xml`.
   No abras el `pom.xml` directamente.
3. Cuando pregunte, aceptá **Trust Project**.
4. Esperá a que termine de descargar las dependencias. La barra de progreso está abajo a la
   derecha. Hasta que no termine, el código va a aparecer lleno de errores en rojo: **es
   normal**, todavía no bajó Spring.
5. Verificá el JDK en **File > Project Structure > Project > SDK**: tiene que decir **21**.

**Si el `pom.xml` aparece en gris o no hay carpeta `External Libraries`:** clic derecho
sobre `pom.xml` → **Add as Maven Project**.

---

## 3. Cómo correrlo

Abrí `src/main/java/ar/edu/utn/diplomatura/clase05/Clase05Application.java` y hacé clic en
la flecha verde que está al lado de `public static void main`.

En la consola vas a ver, en este orden:

```
=== Comparacion: crear la dependencia vs recibirla ===
   Alta de cliente (sin inyeccion): ana@banco.com
   [EMAIL] para ana@banco.com: Bienvenido al banco.
   Alta de cliente (con inyeccion): bruno@banco.com
   [EMAIL] para bruno@banco.com: Bienvenido al banco.

=== 5 clientes cargados en H2 ===

...

Tomcat started on port 8080
```

Esa última línea es la que importa: quiere decir que el servidor está andando.

**Para probar que funciona**, abrí en el navegador:

http://localhost:8080/clientes

Tenés que ver los 5 clientes en formato JSON.

> **Para frenar la aplicación**, usá el botón rojo de IntelliJ. Si la dejás corriendo y
> volvés a arrancarla, el puerto 8080 va a estar ocupado y no va a levantar.

---

## 4. Cómo probar los endpoints

### Endpoints disponibles

| Método | Ruta | Qué hace |
|--------|------|----------|
| `GET` | `/clientes` | Lista todos |
| `GET` | `/clientes?apellido=gomez` | Filtra por apellido |
| `GET` | `/clientes/{id}` | Trae uno |
| `POST` | `/clientes` | Crea uno nuevo |
| `PUT` | `/clientes/{id}` | Actualiza nombre, apellido y email |
| `DELETE` | `/clientes/{id}` | Elimina |
| `POST` | `/clientes/{id}/acreditaciones?importe=N` | Suma un importe al saldo |

Cuando algo no se puede hacer (el id no existe, el importe es cero o negativo), la
respuesta llega con el **cuerpo vacío**.

### Cómo se prueban: con `curl`, desde **Git Bash**

**Abrí Git Bash** (viene con Git; en Windows, botón derecho en cualquier carpeta →
*Git Bash Here*, o buscalo en el menú Inicio). Dejá la aplicación corriendo en IntelliJ y
copiá y pegá los comandos de abajo, uno por vez.

> **Tiene que ser Git Bash, no PowerShell ni CMD.** En PowerShell, `curl` es en realidad
> otro comando disfrazado y esta sintaxis no le funciona.

> **Desde el navegador solo se puede probar `GET`.** La barra de direcciones no sabe hacer
> `POST`, `PUT` ni `DELETE`. Por eso usamos `curl`.

Cada comando va en **una sola línea**: copialo entero y pegalo.

**Listar todos los clientes**

```bash
curl -i http://localhost:8080/clientes
```

**Traer el cliente con id 1**

```bash
curl -i http://localhost:8080/clientes/1
```

**Traer un id que no existe** (la respuesta llega con el cuerpo vacío)

```bash
curl -i http://localhost:8080/clientes/999
```

**Filtrar por apellido**

```bash
curl -i "http://localhost:8080/clientes?apellido=gomez"
```

**Crear un cliente.** El `id` no se manda: lo genera la base. Fijate que el CUIT vuelve
sin guiones.

```bash
curl -i -X POST http://localhost:8080/clientes -H "Content-Type: application/json" -d '{"nombre":"Federico","apellido":"Roldan","cuit":"20-33445566-7","email":"fede.roldan@mail.com","saldo":25000}'
```

**Actualizar nombre, apellido y email del cliente 1**

```bash
curl -i -X PUT http://localhost:8080/clientes/1 -H "Content-Type: application/json" -d '{"nombre":"Ana Maria","apellido":"Gomez","email":"anamaria.gomez@mail.com"}'
```

**Acreditar 50000 a Carla** (id 3, que arranca con saldo 0)

```bash
curl -i -X POST "http://localhost:8080/clientes/3/acreditaciones?importe=50000"
```

**Acreditar un importe negativo** (la respuesta llega vacía y el saldo no cambia)

```bash
curl -i -X POST "http://localhost:8080/clientes/3/acreditaciones?importe=-100"
```

**Verificar que el saldo de Carla quedó en 50000**

```bash
curl -i http://localhost:8080/clientes/3
```

**Eliminar el cliente 5**

```bash
curl -i -X DELETE http://localhost:8080/clientes/5
```

**Confirmar que quedaron menos clientes**

```bash
curl -i http://localhost:8080/clientes
```

> **Qué significa el `-i`:** muestra también los encabezados de la respuesta, incluido el
> código de estado (`HTTP/1.1 200`). Sin `-i` verías solo el JSON.

---

## 5. Cómo ver la base de datos

Con la aplicación corriendo, abrí http://localhost:8080/h2-console

Completá así:

| Campo | Valor |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:clase05` |
| User Name | `sa` |
| Password | *(vacío)* |

> El campo **JDBC URL** viene con otro valor por defecto. Hay que corregirlo o no conecta.

Una vez adentro, probá:

```sql
SELECT * FROM CLIENTES;
```

Esa tabla no la creó nadie a mano: la generó Hibernate mirando la clase `Cliente`.

**Los datos se pierden al cerrar la aplicación.** La base está en memoria: cada vez que
arrancás, volvés a tener los mismos 5 clientes del principio.

---

## 6. Cómo está organizado el código

Una carpeta por capa. Un pedido HTTP entra por arriba y baja hasta la base:

```
   Navegador / Git Bash (curl)
        |
        v
   controladores/   ClienteController      recibe la peticion, devuelve JSON
        |
        v
   servicios/       ClienteService         reglas del negocio
        |
        v
   repositorios/    ClienteRepository      habla con la base de datos
        |
        v
   [ Base H2 ]
```

```
src/main/java/ar/edu/utn/diplomatura/clase05/
  Clase05Application.java          arranca la aplicacion
  entidades/       Cliente.java              la tabla clientes, como clase Java
  repositorios/    ClienteRepository.java    acceso a la base
  servicios/       ClienteService.java       reglas del negocio
  controladores/   ClienteController.java    los endpoints HTTP
  componentes/     ValidadorDeCuit.java      herramienta para trabajar con CUIT
  ioc/                                       demo: dos formas de obtener una dependencia
  config/          CargaDatosIniciales.java  carga los 5 clientes de ejemplo

src/main/resources/
  application.properties           configuracion de la base y del servidor
```

La carpeta `ioc/` es solo una demostración: imprime en consola al arrancar y no forma
parte de la API.

---

## 7. Si algo no funciona

| Problema | Qué hacer |
|----------|-----------|
| Todo el código aparece en rojo | Maven todavía está descargando. Esperá a que termine la barra de abajo a la derecha. |
| `pom.xml` en gris, no reconoce Spring | Clic derecho en `pom.xml` → **Add as Maven Project** |
| `Port 8080 was already in use` | Ya hay otra instancia corriendo. Frenala con el botón rojo, o agregá `server.port=8081` en `application.properties`. |
| `Invalid source release: 21` | El JDK del proyecto no es el 21. **File > Project Structure > Project > SDK**. |
| La consola H2 no conecta | El campo *JDBC URL* tiene que decir exactamente `jdbc:h2:mem:clase05`. |
| Se borraron los clientes que creé | Es lo esperado: la base está en memoria y se vacía al cerrar la aplicación. |
| El `POST` desde el navegador no anda | El navegador solo hace `GET`. Probá `POST`, `PUT` y `DELETE` con `curl` desde Git Bash. |

---

## Datos de la aplicación

| Ítem | Valor |
|------|-------|
| Java | 21 |
| Spring Boot | 3.5.5 |
| Build | Maven |
| Base de datos | H2 en memoria |
| Puerto | 8080 |
