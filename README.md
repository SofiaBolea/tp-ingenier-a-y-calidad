# Trabajo Práctico Integrador – Sistemas de Gestión de la Configuración.

## Universidad Tecnológica Nacional – Facultad Regional Santa Fe

### Autores
Bianculli Pablo, Bolea Sofía, Radolovic Victoria & Yeroncich Francisco.

### Comisión
A – 2025.

---

#### Asignatura: Ingeniería y Calidad de Software
#### Fecha de entrega: 26 de septiembre de 2025.

---

## Descripción del Proyecto
Este repositorio contiene un proyecto de ejemplo desarrollado en el lenguaje **Java**, versión 21.0.1, acerca de un **Sistema de reserva y gestión de aulas para la facultad**. El motivo del presente trabajo está orientado a aplicar conceptos de **Gestión de la Configuración de Software** utilizando **GitHub** como repositorio remoto y el flujo de trabajo **Gitflow** para la administración de ramas.

El código consiste en varias clases (entidades, DTOs, gestores, etc.) que fueron siendo modificadas levemente, con tal de reflejar cambios, por más mínimos que sean, en concepto de *Features* (funcionalidades) y *Hotfixes* (arreglos rápidos o arreglos en caliente); todo esto se fue versionando a lo largo de distintas ramas, comenzando con `main` y luego otras que fueron creadas sobre la marcha (`Develop`,`Feature-1`, `Release-1/1.0.0`, `Release-2/1.1.0`, `hotfix/1.0.1`), siguiendo las consignas del trabajo práctico.

---

## Documentación del Proyecto

### ¿Qué documentamos en un README?
En este archivo README incluiríamos:
- **Título y descripción general del proyecto** (qué hace, tecnologías utilizadas y sus versiones).
- **Instrucciones de instalación** (cómo clonar, compilar y ejecutar).
- **Instrucciones de uso** (ejemplos de comandos y capturas de pantalla sobre cómo probar la aplicación).
- **Flujo de trabajo Gitflow aplicado** (qué ramas se utilizan y para qué).
- **Tipo de versionado** (Versionado Semántico, en este caso).
- **Historial de versiones** (releases etiquetadas con tags en Git).
- **Autores y colaboradores**.

De esta manera, el README se convierte en un anexo que documenta información importante acerca del trabajo y su versionado, evolucionando junto con el software.

---

## Gestión de Cambios (Pull Requests)

Cuando un **externo** realiza una modificación, es importante que entendamos el cambio antes de aceptarlo en la rama principal. Por eso, al crear un **Pull Request (PR)** en GitHub le solicitamos al contribuidor que complete la siguiente información:

### Datos solicitados en un Pull Request (PR)
1. **Descripción del cambio**: explicar claramente qué se modificó.
2. **Justificación**: por qué fue necesario el cambio (razón).
3. **Impacto**: cómo afecta al sistema (funcionalidad, rendimiento, mantenimiento).
4. **Pruebas realizadas**: evidencias de que el cambio funciona (logs, capturas, casos de prueba).
5. **Problema relacionado** (si corresponde): vincular el cambio a una tarea o bug reportado.

### ¿Qué nos ofrece GitHub para ayudarnos?
- **Plantillas de PR (*Pull Request Templates*)**: se pueden definir en el repositorio para estandarizar qué información debe completar cada colaborador. Son archivos (*.github/pull_request_template.md*) que podemos agregar al repositorio, y GitHub los despliega automáticamente cada vez que alguien crea un PR.

- **Revisiones de código (*Code Review*)**: los mantenedores pueden comentar, solicitar cambios o aprobar los PRs antes de fusionarlos. Para ello, GitHub tiene integrado el sistema de “Reviewers”, en donde se pueden asignar revisores, pedir cambios, aprobar o rechazar PRs.

- **Checks automáticos (CI/CD)**: se pueden configurar workflows que ejecuten pruebas automáticas cuando alguien hace un PR. Para ello, con GitHub Actions, podemos configurar flujos que corran tests o análisis de calidad al levantar un PR. GitHub los muestra como “checks” verdes o rojos.

- **Historial y trazabilidad**: cada PR queda registrado con autor, fecha, commits incluidos y discusión asociada, en la pestaña “Pull requests” del repositorio.

Con estas herramientas, garantizamos que el proceso de integración de cambios externos mantenga la calidad del software y la coherencia del proyecto.

### Buenas prácticas adicionales
- Todo PR debe ser revisado y aprobado por al menos un miembro del equipo antes de fusionarse.  
- Recomendamos usar un formato estandarizado (plantilla de PR) para que las descripciones sean consistentes.  
- Siempre considerar la Documentación oficial: [GitHub Pull Requests](https://docs.github.com/es/pull-requests).