# Laboratorio 5: Simulación de Protocolo QKD BB84

### José Daniel Gómez Cabrera 21429

Este proyecto es una simulación interactiva del protocolo de distribución de claves cuánticas (QKD) BB84. Permite a los usuarios visualizar cómo Alice y Bob pueden establecer una clave secreta compartida, y cómo la presencia de una espía (Eve) puede ser detectada.

La simulación está alojada y se puede ejecutar en:

[https://lab5-qkd-jdgomez.netlify.app/](https://lab5-qkd-jdgomez.netlify.app/)

## Funcionalidades Principales

- **Establecer Número de Qubits:** Los usuarios pueden definir cuántos qubits se utilizarán en la simulación.
- **Simulación de Alice y Bob:**
  - Alice genera una secuencia aleatoria de bits y elige aleatoriamente una base (rectilínea '↕' o diagonal '↗') para codificar cada bit en un qubit.
  - Bob elige aleatoriamente una base para medir cada qubit recibido.
- **Inclusión Opcional de Eve (Espía):**
  - Si Eve está presente, intercepta los qubits enviados por Alice.
  - Eve elige aleatoriamente una base para medir cada qubit.
  - Luego, Eve reenvía un nuevo qubit a Bob, codificado con el bit que midió y en la base que utilizó.
- **Proceso de Sifting (Cribado):**
  - Alice y Bob comparan públicamente las bases que utilizaron para cada qubit.
  - Solo conservan los bits para los cuales utilizaron la misma base. Estos bits forman su clave cribada.
- **Detección de Eve:**
  - Si Eve interceptó los qubits, sus mediciones (si usó una base diferente a la de Alice) alterarán el estado de los qubits.
  - Cuando Alice y Bob comparan un subconjunto de su clave cribada, pueden detectar discrepancias.
  - La simulación calcula la Tasa de Error Cuántico de Bits (QBER). Un QBER significativamente mayor que cero indica la presencia de Eve.
- **Visualización Detallada:**
  - Se muestra una tabla con cada paso de la simulación:
    - Bit y base de Alice.
    - Base de Eve y su acción (si está presente).
    - Base de Bob y el bit que midió.
    - Si las bases de Alice y Bob coincidieron.
    - Si el bit fue conservado.
    - Si se detectó un error (posible interferencia de Eve).
  - Se presenta un sumario con:
    - Bits y bases originales de Alice.
    - Bases de Bob.
    - Bases de Eve (si está presente).
    - Bits medidos por Bob.
    - Clave sincronizada final para Alice y Bob.
    - Indicación de si las claves coinciden.
    - QBER si Eve está presente.

## 🚀 Estructura del Proyecto (Ejemplo de Astro)

Si bien la lógica principal de la simulación BB84 está contenida en componentes de React (específicamente `src/components/qkd-card.tsx`), este proyecto puede estar basado en Astro. La estructura típica de un proyecto Astro es:

```text
/
├── public/ (Activos estáticos)
├── src/
│   ├── components/ (Componentes de Astro/React/Vue/Svelte/Preact, como qkd-card.tsx)
│   └── pages/ (Archivos .astro o .md que definen las rutas)
│       └── index.astro (Página principal que probablemente usa QkdCard)
└── package.json
```

Astro busca archivos `.astro` o `.md` en el directorio `src/pages/`. Cada página se expone como una ruta basada en su nombre de archivo.

Cualquier activo estático, como imágenes, se puede colocar en el directorio `public/`.

## 🧞 Comandos (Ejemplo de Astro con npm)

Todos los comandos se ejecutan desde la raíz del proyecto, desde una terminal:

| Comando                   | Acción                                                     |
| :------------------------ | :--------------------------------------------------------- |
| `npm install`             | Instala dependencias                                       |
| `npm run dev`             | Inicia el servidor de desarrollo local en `localhost:4321` |
| `npm run build`           | Compila el sitio de producción en `./dist/`                |
| `npm run preview`         | Previsualiza la compilación localmente, antes de desplegar |
| `npm run astro ...`       | Ejecuta comandos CLI como `astro add`, `astro check`       |
| `npm run astro -- --help` | Obtiene ayuda usando el CLI de Astro                       |

## 👀 ¿Quieres aprender más?

Si estás interesado en Astro, puedes consultar [su documentación](https://docs.astro.build) o unirte a su [servidor de Discord](https://astro.build/chat).
