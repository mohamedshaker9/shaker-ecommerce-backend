# ShakerECommerce
ShakerECommerce is a Spring Boot–based backend application designed for a modern e-commerce platform.
It provides robust APIs for managing products, categories, and orders, with support for product's images uploads.

The system also integrates with Stripe to enable secure and seamless checkout and payment processing.

## Features
*   **Product and category management:** Complete CRUD for products and categories with admin-only access for creation and updates.
*   **product image uploads:** Integrated file service for managing product assets with dedicated storage paths.
*   **Order creation and processing:** Transactional order placement logic that handles cart clearing and status tracking.
*   **Stripe payment integration:** Checkout flow support using Stripe for secure transaction processing.
*   **RESTful APIs:** Clean, versioned endpoints (`/api/v1`) specifically designed for frontend consumption.
*   **Containerized setup:** Multi-stage Docker environment for quick and consistent deployment across any system.


## Technical Stack
*   **Java 17** & **Spring Boot 3**
*   **PostgreSQL** for the database.
*   **Lombok** to keep the boilerplate down.
*   **ModelMapper** for DTO <-> Entity conversions.
*   **Docker** for easy environment setup.


## Running the project

### Local Setup
1.  **Configure Environment:** Update `src/main/resources/application.properties` with your PostgreSQL connection details and your Stripe API keys.
2.  **Build & Run:** You can run it directly from your IDE or use the Maven wrapper:
    ```bash
    ./mvnw spring-boot:run
    ```

### Docker (Recommended)
The project includes a multi-stage Dockerfile and a `docker-compose` setup to spin up both the app and the database automatically.
1.  **Build and Launch:**
    ```bash
    docker-compose up --build
    ```
2.  **Environment Variables:** If you're running via Docker, make sure to pass your Stripe keys through the `docker-compose.yml` environment section or an `.env` file.


## 📂 Project Structure

```text
src/main/java/com/shaker/shakerecommerce/
├── config/      # Application & Security configurations
├── controller/  # REST API Endpoints
├── service/     # Core Business Logic
├── repo/        # Data Access Layer (JPA)
├── model/       # Database Entities
├── dto/         # Request/Response Objects
└── exceptions/  # Error Handling logic
```