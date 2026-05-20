# Reviews Service

Microservicio de reseñas de productos. Permite a los usuarios calificar y comentar productos, validando que tanto el usuario como el producto existan.

## Información general

| Campo | Valor |
|-------|-------|
| Puerto | `8084` |
| Base de datos | `db_reviews` (PostgreSQL) |
| Contexto | `/api/reviews` |

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/reviews` | Listar todas las reseñas |
| `GET` | `/api/reviews/{id}` | Obtener reseña por ID |
| `GET` | `/api/reviews/product/{productId}` | Listar reseñas de un producto |
| `GET` | `/api/reviews/user/{userId}` | Listar reseñas de un usuario |
| `POST` | `/api/reviews` | Crear reseña |
| `PUT` | `/api/reviews/{id}` | Actualizar reseña completa |
| `PATCH` | `/api/reviews/{id}` | Actualizar parcialmente (rating y/o comment) |
| `DELETE` | `/api/reviews/{id}` | Eliminar reseña |

## Ejemplo de uso

**Crear reseña:**
```bash
curl -X POST http://localhost:8084/api/reviews \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 1,
    "rating": 5,
    "comment": "Excelente producto, muy buena calidad"
  }'
```

**Respuesta:**
```json
{
  "id": 1,
  "productId": 1,
  "userId": 1,
  "rating": 5,
  "comment": "Excelente producto, muy buena calidad"
}
```

**Ver reseñas de un producto:**
```bash
curl http://localhost:8084/api/reviews/product/1
```

## Validaciones

- `rating` debe ser entre `1` y `5`
- `comment` no puede estar vacío
- El usuario y el producto deben existir (verificado vía Feign)

## Modelo de datos

```sql
CREATE TABLE reviews (
    id         BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id BIGINT       NOT NULL,
    user_id    BIGINT       NOT NULL,
    rating     INTEGER      NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment    VARCHAR(1000) NOT NULL
);
```

## Dependencias externas

| Servicio | Uso | Puerto |
|---------|-----|--------|
| **productos** | Valida que el producto exista | `8081` |
| **users** | Valida que el usuario exista | `8082` |

## Configuración (variables de entorno Docker)

| Variable | Descripción |
|----------|-------------|
| `SPRING_DATASOURCE_URL` | URL de conexión a PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de datos |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de la base de datos |
| `FEIGN_CLIENT_PRODUCT_URL` | URL del servicio de productos |
| `FEIGN_CLIENT_USER_URL` | URL del servicio de usuarios |

## Tecnologías

- Java 25 · Spring Boot 4.0.6
- Spring Data JPA · Hibernate 7
- Spring Cloud OpenFeign
- Flyway (migraciones)
- PostgreSQL 16
- Lombok · Bean Validation
