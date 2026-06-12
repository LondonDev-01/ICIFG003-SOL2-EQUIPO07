# Apunte MER — Sistema de Reservas de Salas

## Entidades

### CARRERA
| Atributo | Tipo | Restricciones |
|---|---|---|
| id | Integer | PK, auto-incremento |
| nombre_carrera | String(100) | NOT NULL |
| facultad | String(100) | NOT NULL |

### EDIFICIO
| Atributo | Tipo | Restricciones |
|---|---|---|
| id | Integer | PK, auto-incremento |
| nombre_edificio | String(100) | NOT NULL |
| direccion | String(200) | NOT NULL |

### ESTUDIANTE
| Atributo | Tipo | Restricciones |
|---|---|---|
| id | Integer | PK, auto-incremento |
| rut | String(12) | UNIQUE |
| nombre | String(100) | NOT NULL |
| apellido | String(100) | NOT NULL |
| correo | String(150) | UNIQUE |
| telefono | String(20) | — |
| fecha_registro | LocalDate | NOT NULL |
| id_carrera | Integer | FK → CARRERA, NOT NULL |

### SALA
| Atributo | Tipo | Restricciones |
|---|---|---|
| id | Integer | PK, auto-incremento |
| codigo_sala | String(20) | UNIQUE |
| nombre_sala | String(100) | NOT NULL |
| capacidad | Integer | NOT NULL |
| piso | Integer | NOT NULL |
| descripcion | String(255) | NOT NULL |
| estado | String(30) | NOT NULL |
| id_edificio | Integer | FK → EDIFICIO, NOT NULL |

### HORARIO_DISPONIBLE
| Atributo | Tipo | Restricciones |
|---|---|---|
| id | Integer | PK, auto-incremento |
| hora_inicio | LocalTime | NOT NULL |
| hora_termino | LocalTime | NOT NULL |
| id_sala | Integer | FK → SALA, NOT NULL |

### ESTADO_RESERVA
| Atributo | Tipo | Restricciones |
|---|---|---|
| id_estado | Integer | PK, auto-incremento |
| nombre_estado | String(30) | NOT NULL |

### RESERVA
| Atributo | Tipo | Restricciones |
|---|---|---|
| id | Integer | PK, auto-incremento |
| fecha_reserva | LocalDate | NOT NULL |
| observacion | String(255) | NOT NULL |
| fecha_creacion | LocalDateTime | NOT NULL |
| id_estudiante | Integer | FK → ESTUDIANTE, NOT NULL |
| id_sala | Integer | FK → SALA, NOT NULL |
| id_horario | Integer | FK → HORARIO_DISPONIBLE, NOT NULL |
| id_estado | Integer | FK → ESTADO_RESERVA, NOT NULL |

---

## Relaciones

| # | Relación | Padre (1) | Hijo (N) | FK en Hijo | Cardinalidad | Descripción |
|---|---|---|---|---|---|---|
| 1 | pertenece_a | CARRERA | ESTUDIANTE | id_carrera | 1:N | Un estudiante pertenece a una carrera; una carrera tiene muchos estudiantes |
| 2 | contiene | EDIFICIO | SALA | id_edificio | 1:N | Una sala pertenece a un edificio; un edificio contiene muchas salas |
| 3 | tiene | SALA | HORARIO_DISPONIBLE | id_sala | 1:N | Un horario pertenece a una sala; una sala tiene muchos horarios disponibles |
| 4 | realiza | ESTUDIANTE | RESERVA | id_estudiante | 1:N | Una reserva la hace un estudiante; un estudiante puede tener muchas reservas |
| 5 | se_reserva | SALA | RESERVA | id_sala | 1:N | Una reserva ocupa una sala; una sala puede tener muchas reservas |
| 6 | ocupa | HORARIO_DISPONIBLE | RESERVA | id_horario | 1:N | Una reserva usa un horario; un horario puede estar en muchas reservas |
| 7 | tiene_estado | ESTADO_RESERVA | RESERVA | id_estado | 1:N | Una reserva tiene un estado; un estado aplica a muchas reservas |

---

## Diagrama ER

```
  CARRERA                EDIFICIO
     │1                      │1
     │                       │
     │N                      │N
 ESTUDIANTE                 SALA
     │1                   ───┤───1
     │                    │   │
     │N                   │   │N
     │              HORARIO_DISPONIBLE
     │                    │1
     │                    │
     └────N── RESERVA ──N─┘──N── ESTADO_RESERVA
                           │
                      (entidad nexo:
                       4 FKs externas)
```

---

## Resumen de Cardinalidades

- **Todas las relaciones son 1:N** (no hay N:M ni 1:1)
- **La FK siempre va en el lado N** (el hijo)
- **`mappedBy`** va en el lado 1 (el padre), indicando que el hijo es quien tiene la FK
- **RESERVA** es la entidad nexo: conecta 4 entidades simultneamente (ESTUDIANTE, SALA, HORARIO_DISPONIBLE, ESTADO_RESERVA)

---

## Notas para el estudio

| Tema | Detalle |
|---|---|
| Convencion PK | Todas usan `id` excepto ESTADO_RESERVA que usa `id_estado` |
| Cascade ALL | En todas las `@OneToMany` — borrar un padre elimina sus hijos en cascada |
| ESTADO_RESERVA | Es una entidad con tabla propia, pero conceptualmente podria ser un enum |
| fetch = LAZY | Todas las relaciones usan carga diferida |
| @JsonIgnore | En todos los `@OneToMany` para evitar ciclos en JSON |
| nullable = false | Todas las FK son obligatorias — no se puede crear una reserva sin los 4 padres |

---

## Tabla Visual para Presentacion

| Entidad Padre | Cardinalidad | Entidad Hija | FK en Hija |
|---|---|---|---|
| CARRERA | 1 → N | ESTUDIANTE | id_carrera |
| EDIFICIO | 1 → N | SALA | id_edificio |
| SALA | 1 → N | HORARIO_DISPONIBLE | id_sala |
| ESTUDIANTE | 1 → N | RESERVA | id_estudiante |
| SALA | 1 → N | RESERVA | id_sala |
| HORARIO_DISPONIBLE | 1 → N | RESERVA | id_horario |
| ESTADO_RESERVA | 1 → N | RESERVA | id_estado |

---

## Guion de Presentacion (1 slide)

> *"Este es el modelo MER de nuestro sistema de reservas. Son **7 entidades** conectadas por **7 relaciones, todas 1 a N** — no hay N a M ni 1 a 1.*
>
> *Las dos entidades raíz son **Carrera** y **Edificio**: no dependen de nadie. De Carrera salen los Estudiantes; de Edificio salen las Salas. De cada Sala cuelgan los Horarios Disponibles.*
>
> *Toda la lógica del sistema vive en **Reserva**, que es la entidad nexo: tiene **cuatro llaves foráneas** — apunta al Estudiante que reserva, a la Sala que se reserva, al Horario que ocupa, y al Estado de la reserva.*
>
> *La regla que se repite en todo el modelo es simple: **la FK siempre va del lado de N**, y en JPA eso se traduce a que el `@JoinColumn` está en la entidad hija, mientras que el padre lleva el `@OneToMany` con `mappedBy`."*

### Diagrama simplificado para slide (version limpia)

```
                    ┌──────────┐         ┌──────────┐
                    │ CARRERA  │         │ EDIFICIO │
                    └────┬─────┘         └────┬─────┘
                       1│                   1│
                        │                    │
                       N│                    │N
                ┌───────▼──────┐    ┌────────▼─────┐
                │  ESTUDIANTE  │    │     SALA     │
                └───────┬──────┘    └────┬─────┬───┘
                       1│             1│     │1
                        │               │     │
                    ┌───▼────┐          │   ┌─▼────────────┐
                    │        │          │   │   HORARIO    │
                    │        │          │   │  DISPONIBLE  │
                    │        │          │   └──────┬───────┘
                    │        │          │        1│
                    │ RESERVA│◄─────────┤         │
                    │        │◄─────────┘         │
                    │        │◄───────────┐       │
                    │        │            │       │
                    └────┬───┘         N──┤       │
                       N1│                 │       │
                         │        ┌────────▼──────▼┐
                         │        │ ESTADO_RESERVA │
                         │        └────────────────┘
                         │
                      (todas las relaciones son 1:N)
```