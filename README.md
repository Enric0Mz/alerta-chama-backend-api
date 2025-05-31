# 🏠🔥 Alerta Chama: Sistema Inteligente de Detecção e Prevenção de Risco de Incêndio

## Índice
1.  [Visão Geral do Projeto](#1-visão-geral-do-projeto)
2.  [Problema Abordado](#2-problema-abordado)
3.  [Objetivo da Aplicação](#3-objetivo-da-aplicação)
4.  [Arquitetura e Camadas](#4-arquitetura-e-camadas)
5.  [Funcionalidades e Endpoints da API](#5-funcionalidades-e-endpoints-da-api)
    * [5.1. Gerenciamento de Usuários](#51-gerenciamento-de-usuários)
    * [5.2. Gerenciamento de Locais](#52-gerenciamento-de-locais)
    * [5.3. Recebimento e Análise de Dados de Sensores](#53-recebimento-e-análise-de-dados-de-sensores)
    * [5.4. Integração com API Externa (OpenWeatherMap)](#54-integração-com-api-externa-openweathermap)
    * [5.5. Criptografia de Senhas](#55-criptografia-de-senhas)
6.  [Tecnologias Utilizadas](#6-tecnologias-utilizadas)
7.  [Como Configurar e Executar o Projeto](#7-como-configurar-e-executar-o-projeto)
8.  [Como Testar a API (Usando Postman/Insomnia)](#8-como-testar-a-api-usando-postmaninsomnia)
10. [Integrantes do Projeto](#9-integrantes-do-projeto)

---

## 1. Visão Geral do Projeto

O "Alerta Chama" é um sistema backend desenvolvido em Java com Spring Boot, projetado para detectar e alertar sobre riscos de incêndio. O foco principal é a prevenção de incêndios causados por falhas elétricas e a identificação de comportamentos/situações de risco secundários durante interrupções no fornecimento de energia. A aplicação serve como o hub central que recebe dados de sensores, realiza análises de risco, e gerencia informações de usuários e locais.

---

## 2. Problema Abordado

Incêndios causados por falhas elétricas são uma ameaça constante em ambientes residenciais e comerciais. Fatores como fiação antiga, sobrecargas, curtos-circuitos e uso inadequado de equipamentos elétricos podem levar a situações perigosas. Além disso, em cenários de falta de energia (apagões), o uso de fontes alternativas como velas, lampiões e geradores pode introduzir novos vetores de risco de incêndio e acidentes. A falta de um sistema proativo de monitoramento e alerta agrava o problema.

---

## 3. Objetivo da Aplicação

O objetivo principal do "Alerta Chama" é mitigar os riscos de incêndio através de:
* **Detecção Precoce:** Monitorar parâmetros elétricos e ambientais para identificar padrões anormais indicativos de risco de falha elétrica.
* **Análise Contextual:** Integrar dados de sensores com informações externas (ex: clima) para uma avaliação de risco mais precisa.
* **Alertas Inteligentes:** Notificar usuários e autoridades (futuramente) sobre situações de perigo iminente.
* **Gestão de Recursos:** Oferecer uma plataforma para gerenciar locais monitorados e seus usuários.
* **Prevenção Comportamental (Módulo Futuro IoT/Python):** Fornecer uma base para um módulo que identifique e alerte sobre comportamentos humanos de risco durante apagões.

---

## 4. Arquitetura e Camadas

O backend do "Alerta Chama" segue uma **arquitetura em camadas (N-Tier)** clara e modular, utilizando princípios de Arquitetura Orientada a Serviço (SOA).

* **Camada de Controle (API Controllers):**
    * **Responsabilidade:** Receber requisições HTTP, validar dados de entrada e delegar a lógica de negócio para a camada de Serviço. Atua como a interface RESTful da aplicação.
    * **Tecnologias:** Spring Web (`@RestController`).

* **Camada de Serviço (Business Logic Services):**
    * **Responsabilidade:** Contém a lógica de negócio principal, coordena operações, aplica regras de validação complexas e interage com a camada de Dados.
    * **Tecnologias:** Spring (`@Service`), Injeção de Dependência.

* **Camada de Dados (Repositories & Entities):**
    * **Responsabilidade:** Gerenciar a persistência e recuperação de dados do banco de dados. Mapeia objetos Java para tabelas de banco de dados.
    * **Tecnologias:** Spring Data JPA (`JpaRepository`), Hibernate, Entidades (`@Entity`), H2 Database (para desenvolvimento).

* **Camada de Acesso a APIs Externas (`infra.external_apis`):**
    * **Responsabilidade:** Encapsular a lógica de comunicação com serviços de terceiros (ex: OpenWeatherMap).
    * **Tecnologias:** Spring WebFlux (`WebClient`).

* **DTOs (Data Transfer Objects):**
    * **Responsabilidade:** Objetos usados para transferir dados entre as camadas da API e os clientes, separando a representação da API da entidade de domínio interna.
    * **Tecnologias:** Lombok (`@Data`).

---

## 5. Funcionalidades e Endpoints da API

A API é o ponto central para todas as interações. Abaixo, detalhamos os módulos já implementados e seus respectivos endpoints.

### 5.1. Gerenciamento de Usuários

Permite o cadastro e consulta de usuários do sistema. As senhas são criptografadas para segurança.

* **Recurso:** `/api/users`
* **Endpoints:**
    * **`POST /api/users/register`**
        * **Objetivo:** Registrar um novo usuário no sistema. A senha é criptografada automaticamente.
        * **Request Body (JSON):**
            ```json
            {
                "username": "novo_usuario",
                "email": "email@example.com",
                "password": "SenhaSegura123"
            }
            ```
        * **Response Body (JSON - Status 201 CREATED):**
            ```json
            {
                "id": 1,
                "username": "novo_usuario",
                "email": "email@example.com",
                "role": "USER",
                "password": "$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            }
            ```
          *(Nota: O campo `password` foi incluido apenas para fins de acadêmicos. Com o projeto em produção, ele será removido.)*
        * **Status Codes:** `201 CREATED`, `400 BAD REQUEST` (validação), `409 CONFLICT` (username/email já existe).

    * **`GET /api/users/{username}`**
        * **Objetivo:** Buscar um usuário pelo nome de usuário.
        * **Parâmetro de Path:** `username` (string)
        * **Response Body (JSON - Status 200 OK):** (Mesmo formato de `POST /register` Response Body)
        * **Status Codes:** `200 OK`, `404 NOT FOUND`.

    * **`GET /api/users`**
        * **Objetivo:** Listar todos os usuários registrados no sistema.
        * **Response Body (JSON - Status 200 OK):** Uma lista de objetos `UserResponse`.
        * **Status Codes:** `200 OK`.

### 5.2. Gerenciamento de Locais

Permite registrar e consultar locais físicos (casas, escritórios) que serão monitorados, associando-os a um usuário proprietário.

* **Recurso:** `/api/locations`
* **Endpoints:**
    * **`POST /api/locations`**
        * **Objetivo:** Criar um novo local monitorado.
        * **Request Body (JSON):**
            ```json
            {
                "name": "Minha Casa",
                "address": "Rua Principal, 100",
                "description": "Residencia familiar, 3 quartos.",
                "ownerUserId": 1  // ID do usuário proprietário (obtido no registro do usuário)
            }
            ```
        * **Response Body (JSON - Status 201 CREATED):**
            ```json
            {
                "id": 1,
                "name": "Minha Casa",
                "address": "Rua Principal, 100",
                "description": "Residencia familiar, 3 quartos.",
                "ownerUser": { /* UserResponse do proprietário */ }
            }
            ```
        * **Status Codes:** `201 CREATED`, `400 BAD REQUEST` (validação), `404 NOT FOUND` (ownerUserId não existe).

    * **`GET /api/locations/{id}`**
        * **Objetivo:** Buscar um local específico pelo seu ID.
        * **Parâmetro de Path:** `id` (long)
        * **Response Body (JSON - Status 200 OK):** (Mesmo formato de `POST /locations` Response Body)
        * **Status Codes:** `200 OK`, `404 NOT FOUND`.

    * **`GET /api/locations`**
        * **Objetivo:** Listar todos os locais registrados no sistema.
        * **Response Body (JSON - Status 200 OK):** Uma lista de objetos `LocationResponse`.
        * **Status Codes:** `200 OK`.

    * **`GET /api/locations/user/{userId}`**
        * **Objetivo:** Listar todos os locais associados a um usuário específico.
        * **Parâmetro de Path:** `userId` (long)
        * **Response Body (JSON - Status 200 OK):** Uma lista de objetos `LocationResponse`.
        * **Status Codes:** `200 OK`, `404 NOT FOUND` (usuário não encontrado).

### 5.3. Recebimento e Análise de Dados de Sensores

Este é o core da detecção de risco. Recebe leituras de sensores e as processa, integrando dados climáticos para uma análise de risco mais apurada.

* **Recurso:** `/api/sensor-data`
* **Endpoints:**
    * **`POST /api/sensor-data`**
        * **Objetivo:** Receber dados de um sensor IoT. O backend processa esses dados, consulta a API externa de clima, executa a lógica de análise de risco e salva o status resultante.
        * **Request Body (JSON):**
            ```json
            {
                "sensorId": "SENSOR_ABC_001",
                "location": "Sao Paulo",  // A cidade é usada para consultar a API de clima
                "temperature": 75.0,     // Ex: Temperatura em Celsius (de painel, fio, ambiente)
                "current": 25.5          // Ex: Corrente em Ampere (de circuito)
            }
            ```
        * **Response Body (JSON - Status 201 CREATED):**
            ```json
            {
                "id": 1,
                "sensorId": "SENSOR_ABC_001",
                "location": "Sao Paulo",
                "temperature": 75.0,
                "current": 25.5,
                "status": "CRITICO - Temperatura do equipamento muito alta!", // Status calculado pela lógica de risco
                "timestamp": "2025-05-31T18:00:00"
            }
            ```
        * **Status Codes:** `201 CREATED`, `400 BAD REQUEST` (validação), `500 INTERNAL SERVER ERROR` (problemas na integração com API externa ou lógica interna).

    * **`GET /api/sensor-data/{sensorId}`**
        * **Objetivo:** Buscar as últimas leituras de um sensor específico pelo seu ID.
        * **Parâmetro de Path:** `sensorId` (string)
        * **Response Body (JSON - Status 200 OK):** Uma lista de objetos `SensorData`.
        * **Status Codes:** `200 OK`, `404 NOT FOUND` (sensor não encontrado).

    * **`GET /api/sensor-data`**
        * **Objetivo:** Listar todas as leituras de sensores registradas.
        * **Response Body (JSON - Status 200 OK):** Uma lista de objetos `SensorData`.
        * **Status Codes:** `200 OK`.

### 5.4. Integração com API Externa (OpenWeatherMap)

O sistema consome a API OpenWeatherMap para obter dados climáticos (temperatura ambiente, umidade, condições climáticas) que são usados na análise de risco.

* **Consumo:** A chamada é feita internamente pelo `SensorDataService` ao receber novos dados de sensor, utilizando o `WebClient` do Spring WebFlux.
* **Finalidade:** Enriquecer a lógica de `analyzeRisk`, permitindo que o sistema identifique riscos potenciais em condições climáticas extremas (ex: risco de sobreaquecimento em dias muito quentes, risco de surto elétrico durante tempestades).

### 5.5. Criptografia de Senhas

* **Implementação:** Todas as senhas de usuários são criptografadas utilizando `BCryptPasswordEncoder` (parte do Spring Security) antes de serem armazenadas no banco de dados.
* **Segurança:** Garante que, mesmo em caso de vazamento do banco de dados, as senhas originais dos usuários não sejam diretamente expostas.

---

## 6. Tecnologias Utilizadas

* **Linguagem de Programação:** Java 17+
* **Framework Principal:** Spring Boot (versão estável mais recente)
* **Web Framework:** Spring Web
* **Acesso a Dados:** Spring Data JPA, Hibernate
* **Banco de Dados:** H2 Database (em memória para desenvolvimento, facilmente substituível por PostgreSQL/MySQL em produção)
* **Cliente HTTP:** Spring WebFlux (`WebClient`) para consumo de APIs externas
* **Validação:** Jakarta Bean Validation (`spring-boot-starter-validation`)
* **Segurança:** Spring Security (`BCryptPasswordEncoder` para criptografia de senha)
* **Utilitários:** Lombok (redução de boilerplate code)
* **Gerenciador de Dependências:** Apache Maven
* **Documentação da API:** Springdoc OpenAPI / Swagger UI (A ser implementado como próxima etapa para a documentação interativa da API)

---

## 7. Como Configurar e Executar o Projeto

### Pré-requisitos
* Java Development Kit (JDK) 17 ou superior
* Apache Maven
* Uma IDE (IntelliJ IDEA, VS Code com extensões Java, Eclipse)
* Chave de API da OpenWeatherMap (gratuita, pode ser obtida em [openweathermap.org](https://openweathermap.org/))

### Passos de Configuração
1.  **Clonar o Repositório:**
    ```bash
    git clone https://github.com/Enric0Mz/alerta-chama-backend-api
    cd alerta-chama-backend
    ```
2.  **Configurar a Chave de API:**
    * Abra o arquivo `src/main/resources/application.properties`.
    * Substitua `SUA_VERDADEIRA_API_KEY_AQUI` pela sua chave de API real da OpenWeatherMap, ou use a key localizada em `application.properties`
    * Verifique se `openweathermap.api.url` está definida como `https://api.openweathermap.org/data/2.5`.

    ```properties
    # Configurações da OpenWeatherMap API
    openweathermap.api.url=https://api.openweathermap.org/data/2.5
    openweathermap.api.key=SUA_VERDADEIRA_API_KEY_AQUI
    ```
3.  **Construir o Projeto (Maven):**
    ```bash
    mvn clean install
    ```
4.  **Executar a Aplicação:**
    * A partir da linha de comando na raiz do projeto:
        ```bash
        mvn spring-boot:run
        ```
    * Ou, diretamente pela sua IDE: Execute a classe principal `AlertaChamaBackendApplication.java`.

A aplicação estará disponível em `http://localhost:8080`.

---

## 8. Como Testar a API (Usando Postman/Insomnia)

Com a aplicação rodando, você pode usar ferramentas como Postman ou Insomnia para interagir com a API.

* **Console H2:** Para verificar os dados no banco de dados, acesse `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:alertachama_db`, User: `sa`, Password: `password`).

**Fluxo de Teste Sugerido:**

1.  **Registrar Usuário:**
    * **`POST`** `http://localhost:8080/api/users/register`
    * **Body:** `{"username": "testuser", "email": "test@example.com", "password": "password123"}`
    * *Anote o `id` retornado.*

2.  **Criar Local:**
    * **`POST`** `http://localhost:8080/api/locations`
    * **Body:** `{"name": "Casa Teste", "address": "Rua X, 1", "description": "Local de teste", "ownerUserId": [ID DO USUÁRIO CRIADO]}`

3.  **Enviar Dados de Sensor (Aciona Integração com Clima e Análise de Risco):**
    * **`POST`** `http://localhost:8080/api/sensor-data`
    * **Body:** `{"sensorId": "SENSOR_001_COZINHA", "location": "Sao Paulo", "temperature": 80.0, "current": 30.0}`
    * *Verifique os logs da aplicação para ver a chamada à OpenWeatherMap e o resultado da análise de risco.*
    * *Observe o campo `status` na resposta JSON.*

4.  **Verificar Dados de Sensor:**
    * **`GET`** `http://localhost:8080/api/sensor-data/SENSOR_001_COZINHA`

---

## 9. Integrantes do Projeto

* Enrico Marquez - RM99325
* Breno Rubio - RM97864