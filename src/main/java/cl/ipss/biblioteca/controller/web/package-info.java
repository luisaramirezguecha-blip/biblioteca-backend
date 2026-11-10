/**
 * PASO 4B (opcional) - Controllers WEB con Thymeleaf.
 *
 * Las plantillas HTML (src/main/resources/templates) y el CSS (static/web.css) YA vienen hechos en el base.
 *
 * TODO: crea aqui LibroWebController y PrestamoWebController (@Controller, @Hidden, @ConditionalOnProperty
 * app.web.enabled). Usan los MISMOS services que la API REST y devuelven el nombre de una plantilla
 * ("libros", "disponibilidad", "prestamos") o una redireccion. Guia: Archivos 4B.1 y 4B.2.
 *
 * Luego: compilar, LEVANTAR y abrir http://localhost:8084/web/libros
 *
 * Para usar solo Angular mas adelante: WEB_THYMELEAF=false (no hace falta borrar nada).
 *
 * Codigo de referencia: biblioteca-backend/src/main/java/cl/ipss/biblioteca/controller/web
 */
package cl.ipss.biblioteca.controller.web;
