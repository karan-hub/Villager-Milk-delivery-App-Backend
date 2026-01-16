# Vilgo Milk App

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

---

## 🚀 Production Deployment Guide

### Step 1: Environment Setup

Create these environment variables on your server:

```bash
# Database Configuration
DB_USERNAME=vilgo_prod_user
DB_PASSWORD=your_secure_db_password
MYSQL_HOST=localhost

# JWT Security
JWT_SECRET=your-256-bit-production-jwt-secret-key-here
JWT_ISSUER=https://yourdomain.com

# Cashfree Payment Gateway (Get from Cashfree Dashboard)
CASHFREE_APP_ID=CF_PROD_APP_ID_HERE
CASHFREE_SECRET_KEY=CF_PROD_SECRET_KEY_HERE
CASHFREE_CLIENT_SECRET=CF_PROD_CLIENT_SECRET_HERE
CASHFREE_BASE_URL=https://api.cashfree.com

# Webhook Configuration
WEBHOOK_BASE_URL=https://yourdomain.com
```

### Step 2: Server Setup (Ubuntu/Debian)

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Java 17
sudo apt install openjdk-17-jdk -y

# Install MySQL
sudo apt install mysql-server -y
sudo mysql_secure_installation

# Create database and user
sudo mysql -u root -p
```

```sql
CREATE DATABASE VilgoMilks CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'vilgo_prod'@'localhost' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON VilgoMilks.* TO 'vilgo_prod'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### Step 3: Deploy Application

```bash
# Clone your repository
git clone https://github.com/yourusername/village-milk-app.git
cd village-milk-app

# Set environment variables
sudo nano /etc/environment
# Add the environment variables from Step 1

# Build application
chmod +x mvnw
./mvnw clean package -DskipTests

# Create application directory
sudo mkdir -p /opt/village-milk-app
sudo cp target/village-milk-app-*.jar /opt/village-milk-app/
sudo cp -r src/main/resources /opt/village-milk-app/config
```

### Step 4: Create Systemd Service

```bash
sudo nano /etc/systemd/system/village-milk-app.service
```

Add this content:

```ini
[Unit]
Description=Village Milk App
After=network.target mysql.service

[Service]
Type=simple
User=ubuntu
EnvironmentFile=/etc/environment
WorkingDirectory=/opt/village-milk-app
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod village-milk-app-*.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

```bash
# Enable and start service
sudo systemctl daemon-reload
sudo systemctl enable village-milk-app
sudo systemctl start village-milk-app
sudo systemctl status village-milk-app
```

### Step 5: Configure Nginx (Reverse Proxy)

```bash
# Install Nginx
sudo apt install nginx -y

# Create site configuration
sudo nano /etc/nginx/sites-available/village-milk-app
```

Add this configuration:

```nginx
server {
    listen 80;
    server_name yourdomain.com;

    # Security headers
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-XSS-Protection "1; mode=block" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header Referrer-Policy "no-referrer-when-downgrade" always;
    add_header Content-Security-Policy "default-src 'self' http: https: data: blob: 'unsafe-inline'" always;

    # Gzip compression
    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_types text/plain text/css text/xml text/javascript application/javascript application/xml+rss application/json;

    location / {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;
        proxy_read_timeout 86400;
    }

    # Webhook endpoint - important for payments
    location /api/v1/webhooks/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        # Disable buffering for webhooks
        proxy_buffering off;
        proxy_request_buffering off;
    }
}
```

```bash
# Enable site
sudo ln -s /etc/nginx/sites-available/village-milk-app /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

### Step 6: SSL Certificate (Required for Production)

```bash
# Install certbot
sudo apt install snapd -y
sudo snap install core; sudo snap refresh core
sudo snap install --classic certbot
sudo ln -s /snap/bin/certbot /usr/bin/certbot

# Get SSL certificate
sudo certbot --nginx -d yourdomain.com

# Set up auto-renewal
sudo crontab -e
# Add: 0 12 * * * /usr/bin/certbot renew --quiet
```

### Step 7: Configure Cashfree Webhooks

1. **Login to Cashfree Dashboard**
2. **Go to Payment Gateway → Webhooks**
3. **Add webhook URL**: `https://yourdomain.com/api/v1/webhooks/cashfree`
4. **Select events**: `ORDER_STATUS_UPDATE`
5. **Save configuration**

### Step 8: Testing Production Deployment

```bash
# Check application status
sudo systemctl status village-milk-app

# Check application logs
sudo journalctl -u village-milk-app -f

# Test API endpoint
curl https://yourdomain.com/api/v1/products

# Test webhook endpoint (should return 401 without signature)
curl -X POST https://yourdomain.com/api/v1/webhooks/cashfree \
  -H "Content-Type: application/json" \
  -d '{"test": "data"}'
```

### Step 9: Monitoring Setup

```bash
# Install monitoring tools
sudo apt install htop iotop -y

# Set up log rotation
sudo nano /etc/logrotate.d/village-milk-app
```

Add:

```
/opt/village-milk-app/logs/*.log {
    daily
    missingok
    rotate 52
    compress
    delaycompress
    notifempty
    create 644 ubuntu ubuntu
}
```

### Step 10: Backup Strategy

```bash
# Create backup script
sudo nano /opt/village-milk-app/backup.sh
```

Add:

```bash
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
mysqldump -u vilgo_prod -p'your_password' VilgoMilks > /opt/backups/vilgo_db_$DATE.sql
tar -czf /opt/backups/village-milk-app_$DATE.tar.gz /opt/village-milk-app/
```

```bash
# Make executable and set up cron
sudo chmod +x /opt/village-milk-app/backup.sh
sudo crontab -e
# Add: 0 2 * * * /opt/village-milk-app/backup.sh
```

### Security Checklist ✅

- [ ] Environment variables set (no hardcoded secrets)
- [ ] SSL certificate installed
- [ ] Firewall configured (only ports 22, 80, 443 open)
- [ ] Database user has minimal privileges
- [ ] Webhook signature verification enabled
- [ ] Application runs as non-root user
- [ ] Logs are rotated and monitored
- [ ] Backups are automated
- [ ] HTTPS enforced (redirect HTTP to HTTPS)

### Troubleshooting Production Issues

1. **Application won't start**
   ```bash
   sudo journalctl -u village-milk-app -n 50
   ```

2. **Database connection issues**
   ```bash
   sudo mysql -u vilgo_prod -p
   # Check if you can connect
   ```

3. **Webhook not working**
   - Check Nginx logs: `sudo tail -f /var/log/nginx/error.log`
   - Verify webhook URL in Cashfree dashboard
   - Test with signature verification disabled temporarily

4. **SSL issues**
   ```bash
   sudo certbot certificates
   sudo nginx -t && sudo systemctl reload nginx
   ```

### Performance Optimization

1. **JVM Tuning** (add to systemd service):
   ```
   ExecStart=/usr/bin/java -Xmx2g -Xms512m -XX:+UseG1GC -jar village-milk-app-*.jar
   ```

2. **Database Optimization**:
   - Enable query caching
   - Set up connection pooling
   - Monitor slow queries

3. **Caching** (consider Redis for session/cache)

### Cost Optimization

- Use smaller EC2 instance initially
- Set up auto-scaling if needed
- Monitor resource usage
- Use CloudWatch for alerts

---

**🎉 Your application is now production-ready!**

Remember to:
- Regularly update dependencies
- Monitor application metrics
- Keep backups current
- Renew SSL certificates automatically
- Monitor Cashfree webhook delivery