# Finance Dashboard Backend

A robust REST API backend for a Finance Dashboard system built with Java, Spring Boot, and MySQL.
It supports role-based access control, financial record management, and dashboard analytics.

---

## Tech Stack

| Layer          | Technology              |
|----------------|-------------------------|
| Language       | Java 17                 |
| Framework      | Spring Boot 3.4.4       |
| Database       | MySQL 8                 |
| ORM            | Spring Data JPA         |
| Security       | Spring Security + JWT   |
| Build Tool     | Maven                   |
| Testing        | Postman                 |

---

## Project Structure

```text
src/main/java/org/abhinesh/fynex/
├── config/         -> Security configuration
├── controller/     -> REST API endpoints
├── dto/            -> Request and Response objects
├── entity/         -> Database models
├── enums/          -> Role and TransactionType enums
├── exception/      -> Custom exceptions and global handler
├── repository/     -> JPA database queries
├── security/       -> JWT filter, UserDetails
└── services/       -> Business logic
```

---

## Local Setup

### Prerequisites
- Java 17+
- MySQL 8+
- Maven

### Steps

**1. Clone the repository**
```bash
git clone <repository_url>
cd fynex
```

**2. Create MySQL database**
```sql
CREATE DATABASE finance_dashboard;
```

**3. Configure application.properties**

Update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/finance_dashboard
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
jwt.secret=YOUR_LONG_64_CHAR_JWT_SECRET_KEY
jwt.expiration=86400000
```

**4. Run the application**
```bash
mvn spring-boot:run
```

**5. Server runs at**
```
http://localhost:8080
```

---

## Roles and Permissions

| Action                        | VIEWER | ANALYST | ADMIN |
|-------------------------------|--------|---------|-------|
| Register / Login              | Yes    | Yes     | Yes   |
| View own profile              | Yes    | Yes     | Yes   |
| View financial records        | Yes    | Yes     | Yes   |
| View recent transactions      | Yes    | Yes     | Yes   |
| View dashboard summary        | No     | Yes     | Yes   |
| View income / expenses        | No     | Yes     | Yes   |
| View category totals          | No     | Yes     | Yes   |
| View monthly trends           | No     | Yes     | Yes   |
| Create financial records      | No     | No      | Yes   |
| Update financial records      | No     | No      | Yes   |
| Delete financial records      | No     | No      | Yes   |
| Manage users                  | No     | No      | Yes   |

---

## Authentication

The API uses JWT (JSON Web Tokens) for authentication.

**How it works:**
1. Register or login to receive a JWT token
2. Include the token in every subsequent request header
3. Token expires after 24 hours (configurable via `jwt.expiration`)

**Header format:**
```
Authorization: Bearer YOUR_JWT_TOKEN
```

---

## API Reference

### Auth APIs

| Method | Endpoint              | Access | Description         |
|--------|-----------------------|--------|---------------------|
| POST   | /api/auth/register    | Public | Register a new user |
| POST   | /api/auth/login       | Public | Login and get token |

---

### User APIs

| Method | Endpoint                    | Access | Description            |
|--------|-----------------------------|--------|------------------------|
| GET    | /api/users/me               | All    | Get own profile        |
| GET    | /api/users                  | ADMIN  | Get all users          |
| GET    | /api/users/{id}             | ADMIN  | Get user by ID         |
| PUT    | /api/users/{id}             | ADMIN  | Update role and status |
| PATCH  | /api/users/{id}/activate    | ADMIN  | Activate user          |
| PATCH  | /api/users/{id}/deactivate  | ADMIN  | Deactivate user        |
| DELETE | /api/users/{id}             | ADMIN  | Delete user            |

---

### Financial Record APIs

| Method | Endpoint           | Access | Description         |
|--------|--------------------|--------|---------------------|
| POST   | /api/records       | ADMIN  | Create new record   |
| GET    | /api/records       | ALL    | Get all records     |
| GET    | /api/records/{id}  | ALL    | Get record by ID    |
| PUT    | /api/records/{id}  | ADMIN  | Update record       |
| DELETE | /api/records/{id}  | ADMIN  | Soft delete record  |

**Supported query filters for GET /api/records:**
```
?type=INCOME
?type=EXPENSE
?category=Salary
?startDate=2024-01-01&endDate=2024-12-31
?type=INCOME&startDate=2024-01-01&endDate=2024-12-31
?type=INCOME&category=Salary
?category=Salary&startDate=2024-01-01&endDate=2024-12-31
?type=INCOME&category=Salary&startDate=2024-01-01&endDate=2024-12-31
```

---

### Dashboard APIs

| Method | Endpoint                   | Access         | Description            |
|--------|----------------------------|----------------|------------------------|
| GET    | /api/dashboard/summary     | ADMIN, ANALYST | Full dashboard summary |
| GET    | /api/dashboard/income      | ADMIN, ANALYST | Total income           |
| GET    | /api/dashboard/expenses    | ADMIN, ANALYST | Total expenses         |
| GET    | /api/dashboard/balance     | ADMIN, ANALYST | Net balance            |
| GET    | /api/dashboard/categories  | ADMIN, ANALYST | Category wise totals   |
| GET    | /api/dashboard/recent      | ALL            | Recent 5 transactions  |
| GET    | /api/dashboard/trends      | ADMIN, ANALYST | Monthly trends         |

---

## Sample API Requests

### Register
```json
POST /api/auth/register
{
  "username": "adminuser",
  "email": "admin@test.com",
  "password": "admin123",
  "role": "ADMIN"
}
```

### Login
```json
POST /api/auth/login
{
  "email": "admin@test.com",
  "password": "admin123"
}
```

### Create Financial Record
```json
POST /api/records
Authorization: Bearer YOUR_TOKEN

{
  "amount": 50000,
  "type": "INCOME",
  "category": "Salary",
  "date": "2024-04-01",
  "notes": "Monthly salary"
}
```

### Dashboard Summary Response
```json
GET /api/dashboard/summary
Authorization: Bearer YOUR_TOKEN

{
  "success": true,
  "message": "Dashboard summary fetched",
  "data": {
    "totalIncome": 70000.00,
    "totalExpenses": 14500.00,
    "netBalance": 55500.00,
    "categoryWiseTotals": [
      { "category": "Salary",    "total": 50000.00 },
      { "category": "Freelance", "total": 20000.00 },
      { "category": "Rent",      "total": 8000.00  },
      { "category": "Food",      "total": 4500.00  },
      { "category": "Travel",    "total": 2000.00  }
    ],
    "recentTransactions": [...]
  },
  "timestamp": "2024-04-01T10:00:00"
}
```

---

## Standard API Response Format

Every API response follows this structure:
```json
{
  "success": true,
  "message": "Descriptive message",
  "data": { },
  "timestamp": "2024-04-01T10:00:00"
}
```

Error response:
```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "timestamp": "2024-04-01T10:00:00"
}
```

---

## Error Handling

| Scenario                  | HTTP Status | Message                                 |
|---------------------------|-------------|-----------------------------------------|
| Validation failed         | 400         | Field level error messages              |
| Invalid credentials       | 401         | Invalid email or password               |
| Insufficient permissions  | 403         | You do not have permission              |
| Resource not found        | 404         | Resource not found with id: X           |
| Duplicate resource        | 409         | Email already registered                |
| Server error              | 500         | Something went wrong. Please try again  |

---

## Assumptions Made

- A user can only have one role at a time
- Role is assigned at registration time by the registering client
- Deleted financial records are soft deleted (marked as deleted, not removed from DB)
- JWT tokens expire after 24 hours
- An admin cannot deactivate or delete their own account
- All monetary values use BigDecimal for precision
- Dates follow ISO format: YYYY-MM-DD