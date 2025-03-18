# Laboratorio 2 - Parte 4: Implementación de un Ransomware Simulado
## Cifrado de Información
### José Daniel Gómez Cabrera 21429

En el presente fragmento del laboratorio, se implemento un proceso de 'Ransomware' con propositos educativos, encriptamos ciertos archivos de una carpeta 'directory' en este caso, luego los reemplazamos con archivos encriptados para que la 'victima' no pueda volver a acceder a ellos, y posteriormente le dejamos un archivo de 'RESCATE' para que pueda leer las instrucciones y condiciones de rescate. Esto para simular el comportamiento de un secuestro real.

### Procedimiento
1. Todos los archivos de texto de la carpeta 'directory' son encriptados con AES-256.
2. Los archivos originales son reemplazados por los archivos encriptados.
3. Se crea un archivo de 'RESCATE' con las instrucciones para recuperar los archivos.
4. Posteriormente, cuando la 'victima' realiza el rescate, la desencriptacion de los archivos se realiza por medio de la clave.
5. Los archivos originales son recuperados y los archivos encriptados son eliminados.


### Preguntas para reflexión
1. ¿Cómo podríamos evitar ataques de ransomware?
   - Creo que para evitar ataques de ransomware es fundamental implementar una estrategia de seguridad en múltiples capas. Primero, es esencial mantener todos los sistemas y software actualizados con los últimos parches de seguridad, ya que los atacantes frecuentemente explotan vulnerabilidades conocidas. Segundo, implementar soluciones de seguridad robustas como antivirus avanzados, filtros de correo electrónico y sistemas de detección de intrusiones. Tercero, realizar copias de seguridad regulares y almacenarlas desconectadas de la red principal (offline) para garantizar la recuperación en caso de un ataque exitoso. Cuarto, capacitar constantemente a los usuarios sobre prácticas seguras, como no abrir adjuntos sospechosos, no hacer clic en enlaces desconocidos y verificar la legitimidad de los remitentes. Quinto, implementar políticas de acceso basadas en el principio de mínimo privilegio, limitando los permisos de los usuarios solo a lo estrictamente necesario. Finalmente, contar con un plan de respuesta a incidentes bien documentado y probado regularmente para actuar rápidamente ante cualquier indicio de ataque.


2. ¿Qué tan importante es almacenar claves de manera segura?
   - Estoy convencido que el almacenamiento seguro de claves es absolutamente crítico en la seguridad informática moderna. Las claves criptográficas representan el fundamento de toda la estructura de seguridad, y su compromiso puede tener consecuencias catastróficas. En el contexto del ransomware, los atacantes dependen de la gestión eficaz de claves para mantener los datos como rehenes hasta recibir el pago. Para las organizaciones, almacenar claves inadecuadamente puede resultar en la pérdida permanente de datos críticos, incluso si se paga el rescate. Más allá del ransomware, las claves comprometen la integridad de todos los sistemas de autenticación, cifrado de datos en reposo y en tránsito, y la firma digital de transacciones. Las mejores prácticas incluyen el uso de módulos de seguridad de hardware (HSM), implementación de políticas estrictas de rotación y generación de claves, separación de responsabilidades para acceder a claves críticas, y auditorías regulares de los sistemas de gestión de claves. La seguridad de las claves no es simplemente un detalle técnico, sino un componente fundamental que determina la efectividad de toda la estrategia de ciberseguridad de una organización.

