# Bank Inc - API REST para Administración de Tarjetas y Transacciones

## Tecnologías Utilizadas
- **Java 17**
- **Spring Boot**
- **PostgreSQL** (Pendiente implementación)
- **Desplegado en Azure**
- **Puerto:** 9000

## Descripción
Bank Inc ha contratado el desarrollo de un sistema para administrar tarjetas y transacciones mediante una API REST.

## Endpoints

### 1. Generar Número de Tarjeta
- **Método:** GET  
- **Recurso:** `/card/{productId}/number`  
- **Descripción:** Genera un número de tarjeta único basado en un `productId`.

### 2. Activar Tarjeta
- **Método:** POST  
- **Recurso:** `/card/enroll`  
- **Body:**
  ```json
  {
    "cardId": "1020301234567801"
  }
  ```
- **Descripción:** Activa una tarjeta específica.

### 3. Bloquear Tarjeta
- **Método:** DELETE  
- **Recurso:** `/card/{cardId}`  
- **Descripción:** Bloquea una tarjeta específica según su `cardId`.

### 4. Recargar Saldo
- **Método:** POST  
- **Recurso:** `/card/balance`  
- **Body:**
  ```json
  {
    "cardId": "1020301234567801",
    "balance": "10000"
  }
  ```
- **Descripción:** Recarga saldo en una tarjeta específica.

### 5. Consulta de Saldo
- **Método:** GET  
- **Recurso:** `/card/balance/{cardId}`  
- **Descripción:** Obtiene el saldo actual de una tarjeta.

### 6. Transacción de Compra
- **Método:** POST  
- **Recurso:** `/transaction/purchase`  
- **Body:**
  ```json
  {
    "cardId": "1020301234567801",
    "price": 100
  }
  ```
- **Descripción:** Realiza una compra con la tarjeta especificada.

### 7. Consultar Transacción
- **Método:** GET  
- **Recurso:** `/transaction/{transactionId}`  
- **Descripción:** Consulta los detalles de una transacción específica.

## Despliegue en Azure
La API ha sido desplegada en **Azure**, pero debido a restricciones de tiempo y presupuesto, la base de datos aún no ha sido implementada. A continuación, se presentan capturas de pantalla del despliegue:

![image](https://github.com/user-attachments/assets/9fd3c83a-1a3b-4990-89f4-d42423ee1441)

![image](https://github.com/user-attachments/assets/90cb7752-6c0b-4a11-835b-0e2c6f8f7a94)

## Colección de Postman
Para facilitar la prueba de la API, puedes importar la siguiente colección en Postman:

[Descargar Colección](src/main/resources/postman/api-collection.json)



## Cómo Probar la API
Cambiar estas variables en el archivo aplication.properties por sus propiedades en la base de datos. debe ser postgresql

#spring.datasource.url=${DATABASE_URL}
#spring.datasource.username=${DATABASE_USER}
#spring.datasource.password=${DATABASE_PASSWORD}

Puedes probar la API utilizando herramientas como **Postman** o **cURL**. Ejemplo de consulta de saldo:
```sh
curl -X GET [http://localhost:9000/card/balance/1020301234567801](http://localhost:9000/card/123456/number)
```

---

Este creara el numero de tarjeta y en base a la coleccion postman se probara las siguientes funciones.


