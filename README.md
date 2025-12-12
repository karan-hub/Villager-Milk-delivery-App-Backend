# Village Milk App

A comprehensive milk delivery application backend built with Spring Boot, providing subscription-based milk delivery services for villagers and urban customers.

## Overview

This application consists of a Spring Boot backend API that supports:
- User authentication via phone number and OTP
- Subscription management for milk delivery
- Product catalog management
- Payment integration with Razorpay
- Admin panel for managing users, subscriptions, and deliveries

## Features

### User Features
- **Phone Number + OTP Authentication**: Secure login using mobile verification
- **Profile Management**: Update personal details and delivery addresses
- **Product Catalog**: Browse available milk products (Cow Milk, Buffalo Milk)
- **Subscription Plans**: Choose from 1 week, 15 days, or 1 month plans
- **Flexible Delivery**: Select delivery days (Mon-Sun) and time slots (Morning/Evening)
- **One-time Orders**: Place individual orders alongside subscriptions
- **Online Payments**: Secure payments through Razorpay integration
- **Order History**: View past orders and subscription details

### Admin Features
- **User Management**: View and manage all registered users
- **Subscription Oversight**: Monitor and manage all active subscriptions
- **Delivery Management**: Generate and track daily delivery schedules
- **Product Management**: Update milk prices and product information
- **Payment Tracking**: View payment history and transaction details

## Technology Stack

- **Backend**: Spring Boot 3.5.7
- **Database**: MySQL 8.0
- **Security**: Spring Security with JWT authentication
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven
- **Java Version**: 17
- **Payment Gateway**: Razorpay
- **Other Libraries**:
  - Lombok (code generation)
  - ModelMapper (object mapping)
  - JJWT (JSON Web Tokens)

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6+
- Git

## Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd village-milk-app
   ```

2. **Set up MySQL Database**
   ```sql
   CREATE DATABASE VilgoMilks;
   ```

3. **Configure Environment Variables**
   
   Create environment variables or update `application-dev.yml`:
   ```yaml
   MYSQL_HOST=localhost
   JWT_SECRET=your-secret-key-here
   JWT_ISSUER=api.villagemilk.com
   ```

4. **Build the project**
   ```bash
   mvn clean install
   ```

## Configuration

The application uses Spring profiles for different environments:

- **dev**: Development profile (default)
- **prod**: Production profile

### Application Properties

Key configuration files:
- `application.yml`: Common configuration
- `application-dev.yml`: Development-specific settings
- `application-prod.yml`: Production settings

### Database Configuration

Update the datasource URL in `application-dev.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/VilgoMilks?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: your-username
    password: your-password
```

### JWT Configuration

Configure JWT settings:
```yaml
security:
  jwt:
    secret: your-jwt-secret
    issuer: api.villagemilk.com
    access-ttl-seconds: 3600
    refresh-ttl-seconds: 2592000
```

## Running the Application

### Development Mode
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8083` (dev profile).

### Production Mode
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## API Endpoints

### Authentication
- `POST /api/v1/auth/login` - User login with OTP
- `POST /api/v1/auth/signup` - User registration
- `POST /api/v1/auth/refresh` - Refresh access token

### User Management
- `GET /api/v1/users/{userId}` - Get user profile
- `PUT /api/v1/users/{userId}` - Update user profile
- `GET /api/v1/users/phone/{phone}` - Get user by phone

### Products
- `GET /api/v1/products` - List all products
- `GET /api/v1/products/{id}` - Get product details

### Subscriptions
- `POST /api/v1/subscriptions` - Create subscription
- `GET /api/v1/subscriptions/user/{userId}` - Get user subscriptions
- `PUT /api/v1/subscriptions/{id}` - Update subscription
- `DELETE /api/v1/subscriptions/{id}` - Cancel subscription

### Orders
- `POST /api/v1/orders` - Place one-time order
- `GET /api/v1/orders/user/{userId}` - Get user orders

### Admin Endpoints
- `GET /api/v1/admin/users` - List all users
- `GET /api/v1/admin/subscriptions` - List all subscriptions
- `PUT /api/v1/admin/products/{id}/price` - Update product price

## Database Schema

### ERD Diagram
![Entity Relationship Diagram](Vilgo%20ERD.png)

### Main Tables
- **users**: User accounts and profiles
- **products**: Milk products catalog
- **subscriptions**: User subscription plans
- **subscription_events**: Individual delivery events
- **orders**: One-time orders
- **payments**: Payment transactions
- **addresses**: User delivery addresses
- **otp_codes**: OTP verification codes
- **refresh_tokens**: JWT refresh tokens

## Testing

Run tests with:
```bash
mvn test
```

## Deployment

### Docker (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/village-milk-app-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Production Checklist
- [ ] Update `application-prod.yml` with production database
- [ ] Set `JWT_COOKIE_SECURE=true` for HTTPS
- [ ] Configure proper logging levels
- [ ] Set up database backups
- [ ] Configure monitoring and alerts

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contact

For questions or support, please contact the development team.

---

**Note**: This backend is designed to work with a React Native mobile app and a web-based admin panel. The mobile app and admin panel are separate components not included in this repository.</content>
<filePath="d:\village-milk-app\README.md