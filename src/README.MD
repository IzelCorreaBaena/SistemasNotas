# Sistema de Notas por Usuario — Java

Aplicación de consola en Java para gestionar notas personales por usuario, con persistencia en ficheros y contraseñas encriptadas.

## Estructura del proyecto

```
proyecto/
├── src/
│   ├── Main.java       → Punto de entrada, menú principal
│   ├── Usuario.java    → Registro, login, encriptación SHA-256
│   └── Notas.java      → CRUD completo de notas por usuario
└── data/
    └── usuarios/
        └── <usuario>/
            ├── pass.txt          → Contraseña encriptada en SHA-256
            ├── 1-titulo.txt      → Nota 1
            └── 2-titulo.txt      → Nota 2
```

## Funcionalidades

- **Registro de usuario** con validación de nombre y contraseña mínima de 4 caracteres
- **Login** con verificación de contraseña encriptada en SHA-256
- **Crear** notas con título y contenido
- **Listar** todas las notas del usuario
- **Ver** el contenido de una nota concreta
- **Editar** el contenido de una nota existente
- **Borrar** nota con renumeración automática del resto
- Contraseñas nunca almacenadas en texto plano

## Compilación y ejecución

```bash
# Compilar desde la raíz del proyecto
javac src/*.java

# Ejecutar
java src.Main
```

## Requisitos

- Java 8 o superior
- No requiere librerías externas