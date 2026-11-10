# biblioteca-backend-base (proyecto para completar con los alumnos)

Esqueleto con **arquitectura MVC en capas** que ya trae la base del proyecto hecha. Los alumnos **continúan desde los services**
(Paso 4, Archivo 4.4 de la guía) y completan **service, config y controller**.

Sigue la guía: [`../docs/GUIA-COMPLETA-PASO-A-PASO.md`](../docs/GUIA-COMPLETA-PASO-A-PASO.md)
y compara con el proyecto terminado: [`../biblioteca-backend`](../biblioteca-backend).

## Qué ya viene hecho y qué falta

```
cl.ipss.biblioteca
├── enums         ✔ hecho   (EstadoPrestamo)
├── entity        ✔ hecho   (Libro, Socio, Prestamo)
├── repository    ✔ hecho   (LibroRepository, SocioRepository, PrestamoRepository)
├── dto           ✔ hecho   (requests y responses)
├── exception     ✔ hecho   (excepciones, ApiError y GlobalExceptionHandler)
├── mapper        ✔ hecho   (LibroMapper, SocioMapper, PrestamoMapper)
├── service       ✎ FALTA   <- CONTINUAR DESDE AQUI (Paso 4: interfaces, archivos 4.4 a 4.6)
├── service.impl  ✎ FALTA   <- Paso 4: implementaciones, archivos 4.7 a 4.9 (reglas de negocio)
├── controller.web ✎ FALTA  <- Paso 4B (opcional): controllers Thymeleaf, para ver el sistema en el navegador
├── config        ✎ FALTA   <- Paso 5: SwaggerConfig y CorsConfig
└── controller    ✎ FALTA   <- Paso 6: Controllers REST
```

También trae el `pom.xml` completo (web, validation, **thymeleaf**, data-jpa, postgresql, flyway, springdoc y test), `application.yml`,
las migraciones de Flyway y `crear-bd.sql`. Los tests (`src/test`) se crean en el Paso 7.

### Thymeleaf (vista opcional)

El base trae las **plantillas HTML** (`src/main/resources/templates/`) y `static/web.css` ya hechas. Lo que se completa con los
alumnos son los controllers del paquete `controller.web` (Paso 4B de la guía), que usan los mismos services que la API REST.
Una vez hecho, el sistema se ve en <http://localhost:8084/web/libros> sin necesidad de Swagger ni Angular.
Para usar solo Angular más adelante basta con `WEB_THYMELEAF=false` (no hay que borrar ni comentar nada).

Los paquetes que faltan tienen un `package-info.java` con un `TODO` que indica qué crear.

## Qué pasa si lo levantas tal cual

El proyecto **compila y arranca sin errores** aunque no existan services, config ni controllers: nada del código existente
depende de ellos. Swagger abre vacío (sin endpoints) hasta completar el Paso 6.

## Base de datos (una sola vez)

**Con `psql`:** ejecuta [`crear-bd.sql`](crear-bd.sql) como superusuario `postgres`. **Con pgAdmin:** crea la base `biblioteca_db`, abre el Query Tool sobre ella y ejecuta [`crear-bd-pgadmin.sql`](crear-bd-pgadmin.sql) (`crear-bd.sql` usa comandos de `psql` y falla en pgAdmin).

```powershell
psql -U postgres -h localhost -p 5432 -f crear-bd.sql
```

## Ejecutar

Se necesita Java 21 (`JAVA_HOME` apuntando a un JDK 21).

```powershell
.\mvnw.cmd spring-boot:run
```

Variables: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `SERVER_PORT` (8084 por defecto), `CORS_ORIGINS`, `LOG_LEVEL_APP`, `WEB_THYMELEAF` (`true` por defecto).
Swagger: http://localhost:8084/swagger-ui.html (vacío hasta que completes el paso 6).
