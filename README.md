Base URL (padrão): `http://localhost:8080`

## Swagger / OpenAPI
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`


## Endpoints (controllers)

- UserController (`/user`) — arquivo: `src/main/java/com/comparador/ComparadorTI/user/controller/UserController.java`
  - GET `/user`  
    - Retorna todos os usuários. Resposta: `200 OK` com JSON array.
  - GET `/user/{id}`  
    - Retorna usuário por id. Resposta: `200 OK` ou `404` se não existir.
  - POST `/user/create`  
    - Cria usuário via `@RequestParam`. Parâmetros: `name` (string), `email` (string).  
    - Exemplo (PowerShell / curl):
      ```
      curl -X POST "http://localhost:8080/user/create" -H "Content-Type: application/x-www-form-urlencoded" --data "name=John Doe&email=john.doe@example.com"
      ```

- MessagesController (`/messages`) — arquivo: `src/main/java/com/comparador/ComparadorTI/messages/controller/MessagesController.java`
  - GET `/messages/{id}`  
    - Retorna mensagem por id. Resposta: `200 OK` ou `404`.
  - POST `/messages/create`  
    - Cria mensagem via `@RequestBody` JSON. Body validado com `@Valid`.  
    - Exemplo:
      ```
      curl -X POST "http://localhost:8080/messages/create" -H "Content-Type: application/json" --data '{"content":"Olá","author":"Jane"}'
      ```

- EventController (mapeamento raiz) — arquivo: `src/main/java/com/comparador/ComparadorTI/events/controller/EventController.java`
  - GET `/all`  
    - Retorna todos os eventos.
  - POST `/create`  
    - Cria evento via `@RequestParam` `type` e `source`. `type` deve corresponder ao enum `EventType` (`PUBLISH`, `DELETE`, `UPDATE`).  
    - Exemplo:
      ```
      curl -X POST "http://localhost:8080/create" -H "Content-Type: application/x-www-form-urlencoded" --data "type=PUBLISH&source=systemA"
      ```
  - GET `/{id}`  
    - Retorna evento por id.
  - PATCH `/status/{id}`  
    - Atualiza/alternar status do evento.

## Verificação de criação
- `Create` endpoints retornam o objeto criado.
- Chame o endpoint de GET correspondente com o `id` retornado para verificar.

## Erros comuns
- Objeto não encontrado, conflito caso o objeto já exista no repositório e bad request para status de enum não existente.
