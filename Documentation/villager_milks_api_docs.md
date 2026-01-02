# Villager Milks API Documentation

## Overview

Villager Milks is a comprehensive subscription-based milk delivery platform that provides REST APIs for managing users, products, subscriptions, deliveries, and orders. The platform supports both subscription-based recurring deliveries and one-time purchase orders.

**Base URL:** `{{baseUrl}}/api/v1`

**Current Version:** v1

**Authentication:** JWT (JSON Web Token) with OTP verification

---

## Architecture Overview

### System Architecture

The Villager Milks platform follows a microservices architecture pattern with the following key components:

### High-Level Architecture
![High Level Architecture](Documentation\images\High-Level Architecture.png)

- **Frontend Layer:** Mobile App / Web App
- **API Gateway:** Spring Boot Backend with REST APIs
- **Service Layer:**
  - Auth Service (JWT + OTP)
  - User Service
  - Product Service
  - Subscription Service
  - Order Service
  - Delivery Service
  - Admin & Analytics Service
  - Cron/Scheduler Service
- **Infrastructure:**
  - MySQL Database (Primary data store)
  - Redis/In-Memory Cache (Session & performance optimization)
  - SMS Gateway (Notifications)

### Subscription State Machine

Subscriptions in the system follow a well-defined state machine:


 ### Subscription Lifecycle
![Subscription Lifecycle](Documentation/images/Subscription Lifecycle.png)


**States:**
- **CREATED:** Initial state when subscription is created
- **ACTIVE:** Subscription is active and generating daily deliveries
- **PAUSED:** Temporarily suspended by user
- **SKIPPED:** Single delivery skipped
- **CANCELLED:** Permanently terminated
- **COMPLETED:** Plan duration ended naturally

---

## Authentication Flow

### Authentication Flow
![Authentication Flow](Documentation\images\Auth flow diagram.png)

### Registration & Login Process

The authentication system uses JWT tokens with OTP verification for secure access:

1. **User Registration:** User provides phone and password → Account created
2. **Login:** User authenticates → Receives access token and refresh token
3. **OTP Verification:** For sensitive operations, OTP is sent via SMS
4. **Token Refresh:** Use refresh token to get new access token when expired
5. **Logout:** Invalidate refresh token

---

## API Endpoints

### 1. Authentication Endpoints

All authentication endpoints are public and don't require authorization headers.

#### 1.1 Register User

Creates a new user account in the system.

**Endpoint:** `POST /api/v1/auth/register`

**Access:** Public

**Request Body:**
```json
{
  "phoneNumber": "1234567890",
  "password": "SecurePass123!",
  "name": "John Doe",
  "email": "john@example.com"
}
```

**Response:** `201 Created`
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "userId": "a879a5a0-0ca4-4543-9451-4a037cdb5f55",
    "phoneNumber": "1234567890",
    "name": "John Doe",
    "role": "USER"
  }
}
```

**Error Responses:**
- `400 Bad Request` - Invalid input data
- `409 Conflict` - Phone number already exists

---

#### 1.2 Login

Authenticates user and returns JWT tokens.

**Endpoint:** `POST /api/v1/auth/login`

**Access:** Public

**Request Body:**
```json
{
  "phoneNumber": "1234567890",
  "password": "SecurePass123!"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "userId": "a879a5a0-0ca4-4543-9451-4a037cdb5f55",
      "phoneNumber": "1234567890",
      "name": "John Doe",
      "role": "USER"
    }
  }
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid credentials
- `404 Not Found` - User not found

---

#### 1.3 Request OTP

Sends OTP to user's registered phone number.

**Endpoint:** `POST /api/v1/auth/otp/request`

**Access:** Public

**Request Body:**
```json
{
  "phoneNumber": "1234567890",
  "purpose": "LOGIN"  
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "OTP sent successfully",
  "data": {
    "otpId": "otp_123456",
    "expiresIn": 300,
    "phoneNumber": "******7890"
  }
}
```

---

#### 1.4 Verify OTP

Verifies the OTP sent to user's phone.

**Endpoint:** `POST /api/v1/auth/otp/verify`

**Access:** Public

**Request Body:**
```json
{
  "phoneNumber": "1234567890",
  "otp": "123456",
  "otpId": "otp_123456"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "OTP verified successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "verified": true
  }
}
```

**Error Responses:**
- `400 Bad Request` - Invalid or expired OTP
- `429 Too Many Requests` - Too many failed attempts

---

#### 1.5 Refresh Token

Generates new access token using refresh token.

**Endpoint:** `POST /api/v1/auth/refresh`

**Access:** Public

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600
  }
}
```

---

#### 1.6 Logout

Invalidates user's refresh token.

**Endpoint:** `POST /api/v1/auth/logout`

**Access:** Public

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Logged out successfully"
}
```

---

### 2. Products Endpoints (Public)

#### 2.1 Get Products (Paginated)

Retrieves paginated list of available products.

**Endpoint:** `GET /api/v1/products`

**Access:** Public

**Query Parameters:**
- `page` (integer, optional, default: 0) - Page number
- `size` (integer, optional, default: 10) - Items per page

**Example Request:**
```
GET /api/v1/products?page=0&size=4
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "productId": "2815e89c-a5ed-4093-91a4-98df2e94b651",
        "name": "Fresh Cow Milk",
        "description": "100% pure cow milk delivered fresh daily",
        "category": "DAIRY",
        "pricePerUnit": 50.00,
        "unit": "LITER",
        "availableQuantity": 1000,
        "imageUrl": "https://cdn.example.com/cow-milk.jpg",
        "isActive": true,
        "nutritionalInfo": {
          "protein": "3.2g",
          "fat": "3.5g",
          "calcium": "120mg"
        }
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 4,
      "sort": {
        "sorted": false,
        "unsorted": true
      }
    },
    "totalPages": 3,
    "totalElements": 12,
    "last": false,
    "first": true
  }
}
```

---

#### 2.2 Get Product by ID

Retrieves detailed information about a specific product.

**Endpoint:** `GET /api/v1/products`

**Access:** Public

**Query Parameters:**
- `id` (string, required) - Product ID

**Example Request:**
```
GET /api/v1/products?id=2815e89c-a5ed-4093-91a4-98df2e94b651
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "productId": "2815e89c-a5ed-4093-91a4-98df2e94b651",
    "name": "Fresh Cow Milk",
    "description": "100% pure cow milk delivered fresh daily",
    "category": "DAIRY",
    "pricePerUnit": 50.00,
    "unit": "LITER",
    "availableQuantity": 1000,
    "imageUrl": "https://cdn.example.com/cow-milk.jpg",
    "isActive": true,
    "nutritionalInfo": {
      "protein": "3.2g",
      "fat": "3.5g",
      "calcium": "120mg"
    },
    "reviews": [
      {
        "rating": 4.5,
        "comment": "Fresh and high quality",
        "userName": "Jane Doe"
      }
    ]
  }
}
```

**Error Responses:**
- `404 Not Found` - Product not found

---

### 3. Subscription Plans Endpoints

#### 3.1 Get Subscription Plans

Retrieves all available subscription plans.

**Endpoint:** `GET /api/v1/subscription-plans`

**Access:** Public

**Response:** `200 OK`
```json
{
  "success": true,
  "data": [
    {
      "planId": "da92fa20-591d-4916-9648-6a35f942e6e2",
      "name": "Monthly Plan",
      "duration": 30,
      "durationType": "DAYS",
      "discountPercentage": 10.0,
      "description": "Subscribe for 30 days and get 10% discount",
      "isActive": true,
      "minimumQuantity": 1,
      "features": [
        "Daily delivery",
        "Flexible pausing",
        "Skip deliveries"
      ]
    },
    {
      "planId": "f2b8c1a0-7d3e-4e19-b9f2-1a4d6c8e0f5a",
      "name": "Quarterly Plan",
      "duration": 90,
      "durationType": "DAYS",
      "discountPercentage": 15.0,
      "description": "Subscribe for 90 days and get 15% discount",
      "isActive": true,
      "minimumQuantity": 1,
      "features": [
        "Daily delivery",
        "Flexible pausing",
        "Skip deliveries",
        "Priority support"
      ]
    }
  ]
}
```

---

#### 3.2 Create Plan (ADMIN)

Creates a new subscription plan.

**Endpoint:** `POST /api/v1/subscription-plans`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "name": "Weekly Plan",
  "duration": 7,
  "durationType": "DAYS",
  "discountPercentage": 5.0,
  "description": "Subscribe for 7 days and get 5% discount",
  "isActive": true,
  "minimumQuantity": 1,
  "features": ["Daily delivery", "Skip deliveries"]
}
```

**Response:** `201 Created`
```json
{
  "success": true,
  "message": "Plan created successfully",
  "data": {
    "planId": "b3c7d5e2-9f1a-4b6c-8d2e-5f9a1c3e7d4b",
    "name": "Weekly Plan",
    "duration": 7,
    "durationType": "DAYS",
    "discountPercentage": 5.0,
    "createdAt": "2025-01-02T10:30:00Z"
  }
}
```

---

#### 3.3 Update Plan (ADMIN)

Updates an existing subscription plan.

**Endpoint:** `PUT /api/v1/subscription-plans/{planId}`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "name": "Weekly Plan - Updated",
  "discountPercentage": 7.0,
  "isActive": true
}
```

**Response:** `200 OK`

---

#### 3.4 Delete Plan (ADMIN)

Deletes a subscription plan.

**Endpoint:** `DELETE /api/v1/subscription-plans/{planId}`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `204 No Content`

---

### 4. Subscriptions (User)

#### 4.1 Create Subscription

Creates a new subscription for the authenticated user.

**Endpoint:** `POST /api/v1/subscriptions`

**Access:** Authenticated users

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "planId": "da92fa20-591d-4916-9648-6a35f942e6e2",
  "productId": "2815e89c-a5ed-4093-91a4-98df2e94b651",
  "quantity": 2,
  "startDate": "2025-01-05",
  "deliveryTimeSlot": "MORNING",
  "addressId": "addr_123456",
  "specialInstructions": "Please ring the doorbell"
}
```

**Response:** `201 Created`
```json
{
  "success": true,
  "message": "Subscription created successfully",
  "data": {
    "subscriptionId": "ca634b16-2f05-4764-ab17-0dfaa3e89a58",
    "status": "CREATED",
    "startDate": "2025-01-05",
    "endDate": "2025-02-04",
    "totalAmount": 2700.00,
    "discountedAmount": 2430.00,
    "nextDeliveryDate": "2025-01-05",
    "product": {
      "productId": "2815e89c-a5ed-4093-91a4-98df2e94b651",
      "name": "Fresh Cow Milk",
      "pricePerUnit": 50.00
    },
    "plan": {
      "planId": "da92fa20-591d-4916-9648-6a35f942e6e2",
      "name": "Monthly Plan",
      "duration": 30,
      "discountPercentage": 10.0
    }
  }
}
```

---

#### 4.2 My Subscriptions

Retrieves all subscriptions for the authenticated user.

**Endpoint:** `GET /api/v1/subscriptions/my`

**Access:** Authenticated users

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": [
    {
      "subscriptionId": "ca634b16-2f05-4764-ab17-0dfaa3e89a58",
      "status": "ACTIVE",
      "startDate": "2025-01-05",
      "endDate": "2025-02-04",
      "nextDeliveryDate": "2025-01-03",
      "quantity": 2,
      "totalAmount": 2700.00,
      "discountedAmount": 2430.00,
      "product": {
        "productId": "2815e89c-a5ed-4093-91a4-98df2e94b651",
        "name": "Fresh Cow Milk",
        "imageUrl": "https://cdn.example.com/cow-milk.jpg"
      },
      "deliveryStats": {
        "completed": 15,
        "skipped": 2,
        "remaining": 13
      }
    }
  ]
}
```

---

#### 4.3 Skip Delivery

Skips a specific delivery event.

**Endpoint:** `PUT /api/v1/subscriptions/events/{eventId}/skip`

**Access:** Authenticated users

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Delivery skipped successfully",
  "data": {
    "eventId": "evt_123456",
    "status": "SKIPPED",
    "deliveryDate": "2025-01-03",
    "nextDeliveryDate": "2025-01-04"
  }
}
```

---

#### 4.4 Pause Subscription

Temporarily pauses an active subscription.

**Endpoint:** `PUT /api/v1/subscriptions/{subscriptionId}/pause`

**Access:** Authenticated users

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "reason": "Going on vacation",
  "resumeDate": "2025-01-15"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Subscription paused successfully",
  "data": {
    "subscriptionId": "ca634b16-2f05-4764-ab17-0dfaa3e89a58",
    "status": "PAUSED",
    "pausedDate": "2025-01-03",
    "resumeDate": "2025-01-15"
  }
}
```

---

#### 4.5 Resume Subscription

Resumes a paused subscription.

**Endpoint:** `PUT /api/v1/subscriptions/{subscriptionId}/resume`

**Access:** Authenticated users

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Subscription resumed successfully",
  "data": {
    "subscriptionId": "ca634b16-2f05-4764-ab17-0dfaa3e89a58",
    "status": "ACTIVE",
    "resumedDate": "2025-01-15",
    "nextDeliveryDate": "2025-01-16"
  }
}
```

---

#### 4.6 Cancel Subscription

Permanently cancels a subscription.

**Endpoint:** `PUT /api/v1/subscriptions/{subscriptionId}/cancel`

**Access:** Authenticated users

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "reason": "Not satisfied with quality",
  "feedback": "The milk quality was inconsistent"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Subscription cancelled successfully",
  "data": {
    "subscriptionId": "ca634b16-2f05-4764-ab17-0dfaa3e89a58",
    "status": "CANCELLED",
    "cancelledDate": "2025-01-03",
    "refundAmount": 810.00,
    "refundStatus": "PROCESSING"
  }
}
```

---

### 5. Orders (Buy Once)

#### 5.1 Place Order

Creates a one-time purchase order.

**Endpoint:** `POST /api/v1/orders`

**Access:** Authenticated users

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "items": [
    {
      "productId": "2815e89c-a5ed-4093-91a4-98df2e94b651",
      "quantity": 3
    }
  ],
  "deliveryDate": "2025-01-04",
  "deliveryTimeSlot": "EVENING",
  "addressId": "addr_123456",
  "paymentMethod": "COD",
  "specialInstructions": "Please call before delivery"
}
```

**Response:** `201 Created`
```json
{
  "success": true,
  "message": "Order placed successfully",
  "data": {
    "orderId": "7d24cb7d-82cc-4856-a5d5-2ccf4751f257",
    "orderNumber": "ORD-2025-00123",
    "status": "CONFIRMED",
    "totalAmount": 150.00,
    "deliveryDate": "2025-01-04",
    "deliveryTimeSlot": "EVENING",
    "items": [
      {
        "productId": "2815e89c-a5ed-4093-91a4-98df2e94b651",
        "productName": "Fresh Cow Milk",
        "quantity": 3,
        "pricePerUnit": 50.00,
        "totalPrice": 150.00
      }
    ],
    "paymentStatus": "PENDING",
    "createdAt": "2025-01-02T14:30:00Z"
  }
}
```

---

#### 5.2 My Orders

Retrieves all orders for the authenticated user.

**Endpoint:** `GET /api/v1/orders/my`

**Access:** Authenticated users

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": [
    {
      "orderId": "7d24cb7d-82cc-4856-a5d5-2ccf4751f257",
      "orderNumber": "ORD-2025-00123",
      "status": "DELIVERED",
      "totalAmount": 150.00,
      "deliveryDate": "2025-01-04",
      "items": [
        {
          "productName": "Fresh Cow Milk",
          "quantity": 3,
          "totalPrice": 150.00
        }
      ],
      "createdAt": "2025-01-02T14:30:00Z",
      "deliveredAt": "2025-01-04T07:15:00Z"
    }
  ]
}
```

---

#### 5.3 Get Order by ID

Retrieves detailed information about a specific order.

**Endpoint:** `GET /api/v1/orders/{orderId}`

**Access:** Authenticated users

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "orderId": "7d24cb7d-82cc-4856-a5d5-2ccf4751f257",
    "orderNumber": "ORD-2025-00123",
    "status": "DELIVERED",
    "totalAmount": 150.00,
    "deliveryDate": "2025-01-04",
    "deliveryTimeSlot": "EVENING",
    "items": [
      {
        "productId": "2815e89c-a5ed-4093-91a4-98df2e94b651",
        "productName": "Fresh Cow Milk",
        "quantity": 3,
        "pricePerUnit": 50.00,
        "totalPrice": 150.00
      }
    ],
    "deliveryAddress": {
      "addressLine1": "123 Main Street",
      "city": "Mumbai",
      "state": "Maharashtra",
      "pincode": "400001"
    },
    "paymentMethod": "COD",
    "paymentStatus": "PAID",
    "statusHistory": [
      {
        "status": "CONFIRMED",
        "timestamp": "2025-01-02T14:30:00Z"
      },
      {
        "status": "OUT_FOR_DELIVERY",
        "timestamp": "2025-01-04T06:00:00Z"
      },
      {
        "status": "DELIVERED",
        "timestamp": "2025-01-04T07:15:00Z"
      }
    ]
  }
}
```

---

### 6. User Profile

#### 6.1 Get Profile

Retrieves user profile information.

**Endpoint:** `GET /api/v1/user/{userId}`

**Access:** Authenticated users (own profile)

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "userId": "293aa8e5-784e-4dc5-8caa-db3d13aa377b",
    "phoneNumber": "1234567890",
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER",
    "addresses": [
      {
        "addressId": "addr_123456",
        "addressLine1": "123 Main Street",
        "addressLine2": "Apt 4B",
        "city": "Mumbai",
        "state": "Maharashtra",
        "pincode": "400001",
        "isDefault": true
      }
    ],
    "createdAt": "2024-12-15T10:00:00Z",
    "lastLoginAt": "2025-01-02T09:30:00Z"
  }
}
```

---

#### 6.2 Update Profile

Updates user profile information.

**Endpoint:** `PUT /api/v1/user/{userId}`

**Access:** Authenticated users (own profile)

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "name": "John Doe Updated",
  "email": "john.updated@example.com",
  "addresses": [
    {
      "addressLine1": "456 New Street",
      "city": "Mumbai",
      "state": "Maharashtra",
      "pincode": "400002",
      "isDefault": true
    }
  ]
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Profile updated successfully",
  "data": {
    "userId": "293aa8e5-784e-4dc5-8caa-db3d13aa377b",
    "name": "John Doe Updated",
    "email": "john.updated@example.com",
    "updatedAt": "2025-01-02T15:45:00Z"
  }
}
```

---

### 7. Admin - User Management

#### 7.1 Get All Users

Retrieves paginated list of all users.

**Endpoint:** `GET /api/v1/admin/users`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Query Parameters:**
- `page` (integer, optional, default: 0)
- `size` (integer, optional, default: 20)

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "productId": "2815e89c-a5ed-4093-91a4-98df2e94b651",
        "name": "Fresh Cow Milk",
        "category": "DAIRY",
        "pricePerUnit": 50.00,
        "availableQuantity": 1000,
        "isActive": true,
        "createdAt": "2024-11-01T00:00:00Z"
      }
    ],
    "totalPages": 2,
    "totalElements": 25
  }
}
```

---

#### 8.2 Create Product

Creates a new product in the system.

**Endpoint:** `POST /api/v1/admin/products`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Buffalo Milk",
  "description": "Rich and creamy buffalo milk",
  "category": "DAIRY",
  "pricePerUnit": 65.00,
  "unit": "LITER",
  "availableQuantity": 500,
  "imageUrl": "https://cdn.example.com/buffalo-milk.jpg",
  "isActive": true,
  "nutritionalInfo": {
    "protein": "4.5g",
    "fat": "7.0g",
    "calcium": "180mg"
  }
}
```

**Response:** `201 Created`
```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "productId": "3f8d9c2e-7b4a-4d3f-a1e5-6c9b8d2f1a3e",
    "name": "Buffalo Milk",
    "pricePerUnit": 65.00,
    "createdAt": "2025-01-02T16:30:00Z"
  }
}
```

---

#### 8.3 Update Product

Updates an existing product.

**Endpoint:** `PUT /api/v1/admin/products/{productId}`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Premium Buffalo Milk",
  "pricePerUnit": 70.00,
  "availableQuantity": 600,
  "isActive": true
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Product updated successfully",
  "data": {
    "productId": "2815e89c-a5ed-4093-91a4-98df2e94b651",
    "name": "Premium Buffalo Milk",
    "pricePerUnit": 70.00,
    "updatedAt": "2025-01-02T17:00:00Z"
  }
}
```

---

#### 8.4 Delete Product

Deletes a product from the system.

**Endpoint:** `DELETE /api/v1/admin/products/{productId}`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `204 No Content`

**Error Responses:**
- `400 Bad Request` - Product has active subscriptions
- `404 Not Found` - Product not found

---

### 9. Admin - Subscription Management

#### 9.1 Get All Subscriptions

Retrieves all subscriptions with optional filtering.

**Endpoint:** `GET /api/v1/admin/subscriptions`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Query Parameters:**
- `status` (string, optional) - Filter by status: ACTIVE, PAUSED, CANCELLED, COMPLETED
- `page` (integer, optional, default: 0)
- `size` (integer, optional, default: 20)

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "subscriptionId": "ca634b16-2f05-4764-ab17-0dfaa3e89a58",
        "userId": "293aa8e5-784e-4dc5-8caa-db3d13aa377b",
        "userName": "John Doe",
        "phoneNumber": "1234567890",
        "status": "ACTIVE",
        "productName": "Fresh Cow Milk",
        "quantity": 2,
        "startDate": "2025-01-05",
        "endDate": "2025-02-04",
        "nextDeliveryDate": "2025-01-03",
        "totalAmount": 2430.00
      }
    ],
    "totalPages": 10,
    "totalElements": 200
  }
}
```

---

#### 9.2 Get Active Subscriptions

Retrieves only active subscriptions.

**Endpoint:** `GET /api/v1/admin/subscriptions?status=ACTIVE`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** Same format as Get All Subscriptions, filtered to ACTIVE status only.

---

#### 9.3 Get Paused Subscriptions

Retrieves only paused subscriptions.

**Endpoint:** `GET /api/v1/admin/subscriptions?status=PAUSED`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** Same format as Get All Subscriptions, filtered to PAUSED status only.

---

### 10. Admin - Delivery Management

#### 10.1 Get Today's Deliveries

Retrieves all deliveries scheduled for today.

**Endpoint:** `GET /api/v1/admin/deliveries`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "date": "2025-01-02",
    "totalDeliveries": 45,
    "byStatus": {
      "SCHEDULED": 30,
      "DELIVERED": 10,
      "MISSED": 5
    },
    "deliveries": [
      {
        "eventId": "evt_789012",
        "subscriptionId": "ca634b16-2f05-4764-ab17-0dfaa3e89a58",
        "userId": "293aa8e5-784e-4dc5-8caa-db3d13aa377b",
        "userName": "John Doe",
        "phoneNumber": "1234567890",
        "productName": "Fresh Cow Milk",
        "quantity": 2,
        "deliveryDate": "2025-01-02",
        "timeSlot": "MORNING",
        "status": "SCHEDULED",
        "address": {
          "addressLine1": "123 Main Street",
          "city": "Mumbai",
          "pincode": "400001"
        }
      }
    ]
  }
}
```

---

#### 10.2 Get Scheduled Deliveries

Retrieves deliveries with SCHEDULED status.

**Endpoint:** `GET /api/v1/admin/deliveries?status=SCHEDULED`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** Same format as Get Today's Deliveries, filtered to SCHEDULED status.

---

#### 10.3 Get Deliveries by Date

Retrieves deliveries for a specific date.

**Endpoint:** `GET /api/v1/admin/deliveries?date={date}`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Query Parameters:**
- `date` (string, required) - Format: YYYY-MM-DD (e.g., 2025-01-13)

**Example:**
```
GET /api/v1/admin/deliveries?date=2025-01-13
```

**Response:** Same format as Get Today's Deliveries, filtered by specified date.

---

#### 10.4 Get Deliveries by Date and Status

Retrieves deliveries for a specific date and status.

**Endpoint:** `GET /api/v1/admin/deliveries?date={date}&status={status}`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Query Parameters:**
- `date` (string, required) - Format: YYYY-MM-DD
- `status` (string, required) - SCHEDULED, DELIVERED, MISSED

**Example:**
```
GET /api/v1/admin/deliveries?date=2025-01-12&status=DELIVERED
```

**Response:** Same format as Get Today's Deliveries, filtered by date and status.

---

#### 10.5 Mark Delivered

Marks a delivery as delivered.

**Endpoint:** `PUT /api/v1/admin/deliveries/{eventId}/deliver`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "deliveredAt": "2025-01-02T07:30:00Z",
  "notes": "Delivered successfully to customer"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Delivery marked as delivered",
  "data": {
    "eventId": "evt_789012",
    "status": "DELIVERED",
    "deliveredAt": "2025-01-02T07:30:00Z"
  }
}
```

---

#### 10.6 Mark Missed

Marks a delivery as missed.

**Endpoint:** `PUT /api/v1/admin/deliveries/{eventId}/missed`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "reason": "Customer not available",
  "notes": "Customer did not answer the door"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Delivery marked as missed",
  "data": {
    "eventId": "evt_789012",
    "status": "MISSED",
    "missedAt": "2025-01-02T08:00:00Z",
    "reason": "Customer not available"
  }
}
```

---

### 11. Admin Analytics

#### 11.1 Dashboard Analytics

Retrieves overall dashboard statistics.

**Endpoint:** `GET /api/v1/admin/analytics/dashboard`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "overview": {
      "totalUsers": 1250,
      "activeSubscriptions": 456,
      "totalRevenue": 125000.00,
      "todayDeliveries": 45
    },
    "recentGrowth": {
      "userGrowth": 8.5,
      "subscriptionGrowth": 12.3,
      "revenueGrowth": 15.7
    },
    "topProducts": [
      {
        "productName": "Fresh Cow Milk",
        "subscriptionCount": 320,
        "revenue": 48000.00
      }
    ]
  }
}
```

---

#### 11.2 User Analytics

Retrieves user-related analytics.

**Endpoint:** `GET /api/v1/admin/analytics/users`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "totalUsers": 1250,
    "newUsersThisMonth": 87,
    "activeUsers": 890,
    "usersByRole": {
      "USER": 1245,
      "ADMIN": 5
    },
    "userGrowthTrend": [
      {
        "month": "2024-11",
        "count": 1050
      },
      {
        "month": "2024-12",
        "count": 1163
      },
      {
        "month": "2025-01",
        "count": 1250
      }
    ]
  }
}
```

---

#### 11.3 Subscription Analytics

Retrieves subscription-related analytics.

**Endpoint:** `GET /api/v1/admin/analytics/subscriptions`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "totalSubscriptions": 678,
    "activeSubscriptions": 456,
    "pausedSubscriptions": 32,
    "cancelledSubscriptions": 145,
    "completedSubscriptions": 45,
    "subscriptionsByPlan": [
      {
        "planName": "Monthly Plan",
        "count": 320,
        "percentage": 70.2
      },
      {
        "planName": "Quarterly Plan",
        "count": 136,
        "percentage": 29.8
      }
    ],
    "churnRate": 4.5,
    "averageSubscriptionValue": 2430.00
  }
}
```

---

#### 11.4 Delivery Analytics

Retrieves delivery-related analytics.

**Endpoint:** `GET /api/v1/admin/analytics/deliveries`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "totalDeliveries": 12450,
    "successfulDeliveries": 11890,
    "missedDeliveries": 340,
    "skippedDeliveries": 220,
    "successRate": 95.5,
    "todayStats": {
      "scheduled": 45,
      "delivered": 38,
      "pending": 7
    },
    "deliveryTrend": [
      {
        "date": "2025-01-01",
        "delivered": 42,
        "missed": 3
      },
      {
        "date": "2025-01-02",
        "delivered": 38,
        "missed": 2
      }
    ]
  }
}
```

---

#### 11.5 Revenue Analytics

Retrieves revenue-related analytics.

**Endpoint:** `GET /api/v1/admin/analytics/revenue`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "totalRevenue": 125000.00,
    "thisMonthRevenue": 45600.00,
    "lastMonthRevenue": 39500.00,
    "growthRate": 15.4,
    "revenueBySource": {
      "subscriptions": 98000.00,
      "oneTimeOrders": 27000.00
    },
    "revenueByProduct": [
      {
        "productName": "Fresh Cow Milk",
        "revenue": 78000.00,
        "percentage": 62.4
      },
      {
        "productName": "Buffalo Milk",
        "revenue": 47000.00,
        "percentage": 37.6
      }
    ],
    "monthlyTrend": [
      {
        "month": "2024-11",
        "revenue": 35000.00
      },
      {
        "month": "2024-12",
        "revenue": 39500.00
      },
      {
        "month": "2025-01",
        "revenue": 45600.00
      }
    ]
  }
}
```

---

## Error Handling

### Standard Error Response Format

All API errors follow a consistent format:

```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Human-readable error message",
    "details": [
      {
        "field": "fieldName",
        "message": "Specific field error message"
      }
    ],
    "timestamp": "2025-01-02T10:30:00Z",
    "path": "/api/v1/endpoint"
  }
}
```

### Common HTTP Status Codes

| Status Code | Description | Usage |
|------------|-------------|-------|
| 200 | OK | Successful GET, PUT requests |
| 201 | Created | Successful POST request creating resource |
| 204 | No Content | Successful DELETE request |
| 400 | Bad Request | Invalid input data or business logic error |
| 401 | Unauthorized | Missing or invalid authentication token |
| 403 | Forbidden | Authenticated but lacking permissions |
| 404 | Not Found | Resource does not exist |
| 409 | Conflict | Resource already exists or state conflict |
| 422 | Unprocessable Entity | Validation errors |
| 429 | Too Many Requests | Rate limit exceeded |
| 500 | Internal Server Error | Unexpected server error |

### Common Error Codes

| Error Code | Description |
|-----------|-------------|
| `AUTH_001` | Invalid credentials |
| `AUTH_002` | Token expired |
| `AUTH_003` | Invalid OTP |
| `AUTH_004` | OTP expired |
| `USER_001` | User not found |
| `USER_002` | Phone number already exists |
| `PRODUCT_001` | Product not found |
| `PRODUCT_002` | Insufficient quantity |
| `SUBSCRIPTION_001` | Subscription not found |
| `SUBSCRIPTION_002` | Cannot modify completed subscription |
| `DELIVERY_001` | Delivery event not found |
| `ORDER_001` | Order not found |
| `PAYMENT_001` | Payment failed |

---

## Rate Limiting

API requests are rate-limited to ensure fair usage and system stability:

- **Public endpoints:** 100 requests per hour per IP
- **Authenticated endpoints:** 1000 requests per hour per user
- **Admin endpoints:** 5000 requests per hour per admin

Rate limit headers are included in all responses:

```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1641132000
```

---

## Pagination

All list endpoints support pagination with the following parameters:

**Query Parameters:**
- `page` (integer, default: 0) - Zero-based page index
- `size` (integer, default: 20, max: 100) - Number of items per page
- `sort` (string, optional) - Sort field and direction (e.g., `createdAt,desc`)

**Response Structure:**
```json
{
  "content": [],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {}
  },
  "totalPages": 5,
  "totalElements": 100,
  "first": true,
  "last": false
}
```

---

## Webhook Events

Villager Milks can send webhook notifications for important events:

### Supported Events

| Event Type | Description |
|-----------|-------------|
| `subscription.created` | New subscription created |
| `subscription.activated` | Subscription became active |
| `subscription.paused` | Subscription paused by user |
| `subscription.resumed` | Subscription resumed |
| `subscription.cancelled` | Subscription cancelled |
| `subscription.completed` | Subscription plan duration ended |
| `delivery.scheduled` | Delivery scheduled |
| `delivery.delivered` | Delivery completed successfully |
| `delivery.missed` | Delivery missed |
| `order.placed` | New order created |
| `order.delivered` | Order delivered |
| `payment.success` | Payment successful |
| `payment.failed` | Payment failed |

### Webhook Payload Format

```json
{
  "event": "subscription.created",
  "timestamp": "2025-01-02T10:30:00Z",
  "data": {
    "subscriptionId": "ca634b16-2f05-4764-ab17-0dfaa3e89a58",
    "userId": "293aa8e5-784e-4dc5-8caa-db3d13aa377b",
    "status": "CREATED"
  },
  "signature": "sha256_hash_of_payload"
}
```

---

## Best Practices

### Authentication
1. Store JWT tokens securely (not in localStorage for web apps)
2. Refresh access tokens before expiry
3. Always use HTTPS in production
4. Implement proper token cleanup on logout

### API Usage
1. Use pagination for list endpoints
2. Cache responses when appropriate
3. Handle rate limits gracefully with exponential backoff
4. Validate data client-side before sending requests
5. Use appropriate HTTP methods (GET for reads, POST for creates, PUT for updates, DELETE for deletes)

### Error Handling
1. Always check `success` field in responses
2. Display user-friendly error messages
3. Log detailed errors for debugging
4. Implement retry logic for transient failures
5. Handle network timeouts appropriately

### Security
1. Never expose API keys or tokens in client code
2. Validate all user input
3. Use HTTPS for all API calls
4. Implement CSRF protection for web applications
5. Regularly rotate credentials

---

## Environment Configuration

### Development
```
BASE_URL=http://localhost:8080
API_VERSION=v1
TIMEOUT=30000
```

### Production
```
BASE_URL=https://api.villagermilks.com
API_VERSION=v1
TIMEOUT=30000
```

---

## SDKs and Client Libraries

### JavaScript/TypeScript
```javascript
import { VillagerMilksClient } from '@villagermilks/sdk';

const client = new VillagerMilksClient({
  baseUrl: 'https://api.villagermilks.com',
  apiVersion: 'v1'
});

// Authenticate
const { accessToken } = await client.auth.login({
  phoneNumber: '1234567890',
  password: 'password'
});

// Get products
const products = await client.products.list({ page: 0, size: 10 });

// Create subscription
const subscription = await client.subscriptions.create({
  planId: 'plan-id',
  productId: 'product-id',
  quantity: 2
});
```

---

## Support and Resources

- **API Documentation:** https://docs.villagermilks.com
- **Developer Portal:** https://developers.villagermilks.com
- **Support Email:** support@villagermilks.com
- **Status Page:** https://status.villagermilks.com

---

## Changelog

### Version 1.0.0 (2025-01-02)
- Initial API release
- Authentication with JWT and OTP
- User management
- Product catalog
- Subscription management
- Order management
- Delivery tracking
- Admin analytics

---

## License

This API documentation is proprietary to Villager Milks. Unauthorized use, reproduction, or distribution is prohibited.

---

**Last Updated:** January 2, 2025

**API Version:** 1.0.0

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "userId": "293aa8e5-784e-4dc5-8caa-db3d13aa377b",
        "phoneNumber": "1234567890",
        "name": "John Doe",
        "email": "john@example.com",
        "role": "USER",
        "status": "ACTIVE",
        "subscriptionCount": 2,
        "totalOrderValue": 5000.00,
        "createdAt": "2024-12-15T10:00:00Z"
      }
    ],
    "totalPages": 5,
    "totalElements": 100
  }
}
```

---

#### 7.2 Get User By Phone

Searches for a user by phone number.

**Endpoint:** `GET /api/v1/admin/users/phone/{phoneNumber}`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "userId": "293aa8e5-784e-4dc5-8caa-db3d13aa377b",
    "phoneNumber": "1020304050",
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER",
    "subscriptions": [
      {
        "subscriptionId": "ca634b16-2f05-4764-ab17-0dfaa3e89a58",
        "status": "ACTIVE",
        "productName": "Fresh Cow Milk"
      }
    ]
  }
}
```

---

#### 7.3 Create User

Creates a new user (admin function).

**Endpoint:** `POST /api/v1/admin/users`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "phoneNumber": "9876543210",
  "password": "TempPass123!",
  "name": "Jane Smith",
  "email": "jane@example.com",
  "role": "USER"
}
```

**Response:** `201 Created`

---

#### 7.4 Make Admin

Promotes a user to admin role.

**Endpoint:** `PUT /api/v1/admin/users/{userId}/make-admin`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "User promoted to admin successfully",
  "data": {
    "userId": "293aa8e5-784e-4dc5-8caa-db3d13aa377b",
    "role": "ADMIN",
    "updatedAt": "2025-01-02T16:00:00Z"
  }
}
```

---

#### 7.5 Delete User

Deletes a user from the system.

**Endpoint:** `DELETE /api/v1/admin/users/{userId}`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `204 No Content`

---

### 8. Admin - Product Management

#### 8.1 Get Products

Retrieves all products with pagination.

**Endpoint:** `GET /api/v1/admin/products`

**Access:** Admin only

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Query Parameters:**
- `page` (integer, optional, default: 0)
- `size` (integer, optional, default: 20)

```md
### 8. Admin – Product Management

APIs for managing milk products (Cow / Buffalo) used in subscriptions and one-time orders.  
All endpoints are **restricted to ADMIN users only**.

---

#### 8.1 Get Products

Retrieves all products with pagination.

**Endpoint:** `GET /api/v1/admin/products`

**Access:** Admin only

**Headers:**
```

Authorization: Bearer {accessToken}



**Query Parameters:**
- `page` (integer, optional, default: 0)
- `size` (integer, optional, default: 20)

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Products fetched successfully",
  "data": {
    "content": [
      {
        "id": "2815e89c-a5ed-4093-91a4-98df2e94b651",
        "name": "Buffalo Milk",
        "price": 65,
        "unit": "LITER",
        "type": "BUFFALO",
        "inStock": true
      }
    ],
    "page": 0,
    "size": 20,
    "totalElements": 5,
    "totalPages": 1
  }
}
````

---

#### 8.2 Create Product

Creates a new milk product.

**Endpoint:** `POST /api/v1/admin/products`

**Access:** Admin only

**Headers:**

```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**Request Body:**

```json
{
  "name": "Cow Milk",
  "price": 55,
  "unit": "LITER",
  "type": "COW",
  "inStock": true
}
```

**Response (201 Created):**

```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "id": "89f83869-50c8-4777-86c7-57932fff817a"
  }
}
```

---

#### 8.3 Update Product

Updates an existing product.

**Endpoint:** `PUT /api/v1/admin/products/{productId}`

**Access:** Admin only

**Headers:**

```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**Path Parameters:**

* `productId` (UUID, required)

**Request Body:**

```json
{
  "name": "Buffalo Milk Premium",
  "price": 70,
  "unit": "LITER",
  "type": "BUFFALO",
  "inStock": true
}
```

**Response (200 OK):**

```json
{
  "success": true,
  "message": "Product updated successfully"
}
```

---

#### 8.4 Delete Product

Deletes a product permanently.

**Endpoint:** `DELETE /api/v1/admin/products/{productId}`

**Access:** Admin only

**Headers:**

```
Authorization: Bearer {accessToken}
```

**Path Parameters:**

* `productId` (UUID, required)

**Response (204 No Content):**

```json
{
  "success": true,
  "message": "Product deleted successfully"
}
```
Understood. Below is a **clean, properly structured, industry-standard Markdown section**, consistent with what you shared earlier (numbering, headings, flow, wording).
You can **copy–paste this directly into your README.md**.

---


### 9. Admin – Subscription Management

APIs for monitoring and managing user subscriptions across the system.  
All endpoints are **restricted to ADMIN users only**.

---

#### 9.1 Get All Subscriptions

Retrieves all subscriptions in the system. Supports filtering by status.

**Endpoint:** `GET /api/v1/admin/subscriptions`

**Access:** Admin only

**Headers:**
```

Authorization: Bearer {accessToken}

````

**Query Parameters (optional):**
- `status` (string, optional)  
  Allowed values: `ACTIVE`, `PAUSED`, `CANCELLED`

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Subscriptions fetched successfully",
  "data": [
    {
      "subscriptionId": "ca634b16-2f05-4764-ab17-0dfaa3e89a58",
      "userId": "293aa8e5-784e-4dc5-8caa-db3d13aa377b",
      "status": "ACTIVE",
      "startDate": "2025-01-01",
      "deliverySlot": "MORNING"
    }
  ]
}
````

---

### 10. Admin – Delivery Management

APIs for managing and tracking daily milk deliveries.

---

#### 10.1 Get Deliveries

Retrieves deliveries for today or a specific date, optionally filtered by status.

**Endpoint:** `GET /api/v1/admin/deliveries`

**Access:** Admin only

**Headers:**

```
Authorization: Bearer {accessToken}
```

**Query Parameters (optional):**

* `date` (string, format: YYYY-MM-DD)
* `status` (string: `SCHEDULED`, `DELIVERED`, `MISSED`)

**Response (200 OK):**

```json
{
  "success": true,
  "message": "Deliveries fetched successfully",
  "data": [
    {
      "eventId": "e12345",
      "subscriptionId": "s12345",
      "deliveryDate": "2025-01-13",
      "status": "SCHEDULED"
    }
  ]
}
```

---

#### 10.2 Mark Delivery as Delivered

Marks a scheduled delivery as delivered.

**Endpoint:** `PUT /api/v1/admin/deliveries/{eventId}/deliver`

**Access:** Admin only

**Headers:**

```
Authorization: Bearer {accessToken}
```

**Path Parameters:**

* `eventId` (UUID, required)

**Response (200 OK):**

```json
{
  "success": true,
  "message": "Delivery marked as delivered"
}
```

---

#### 10.3 Mark Delivery as Missed

Marks a scheduled delivery as missed.

**Endpoint:** `PUT /api/v1/admin/deliveries/{eventId}/missed`

**Access:** Admin only

**Headers:**

```
Authorization: Bearer {accessToken}
```

**Path Parameters:**

* `eventId` (UUID, required)

**Response (200 OK):**

```json
{
  "success": true,
  "message": "Delivery marked as missed"
}
```

---

### 11. Orders – Buy Once (USER)

APIs for placing and viewing one-time milk orders (non-subscription).

---

#### 11.1 Place Order

Places a one-time milk order.

**Endpoint:** `POST /api/v1/orders`

**Access:** User only

**Headers:**

```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**Request Body:**

```json
{
  "productId": "2815e89c-a5ed-4093-91a4-98df2e94b651",
  "quantity": 2
}
```

**Response (200 OK):**

```json
{
  "success": true,
  "message": "Order placed successfully",
  "data": {
    "orderId": "7d24cb7d-82cc-4856-a5d5-2ccf4751f257",
    "status": "PLACED"
  }
}
```

---

Below is the **properly structured, clean, industry-standard Markdown**, written **exactly in the same format** as your earlier sections (headings, spacing, consistency).
You can **paste this directly into your README.md**.

---

```md
#### 11.2 Get My Orders

Retrieves all one-time orders placed by the authenticated user.

**Endpoint:** `GET /api/v1/orders/my`

**Access:** User only

**Headers:**
```

Authorization: Bearer {accessToken}

````

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Orders fetched successfully",
  "data": [
    {
      "orderId": "7d24cb7d-82cc-4856-a5d5-2ccf4751f257",
      "status": "PLACED",
      "totalAmount": 130,
      "createdAt": "2025-01-12T09:30:00"
    }
  ]
}
````

---

#### 11.3 Get Order by ID

Retrieves details of a specific one-time order.

**Endpoint:** `GET /api/v1/orders/{orderId}`

**Access:** User only

**Headers:**

```
Authorization: Bearer {accessToken}
```

**Path Parameters:**

* `orderId` (UUID, required)

**Response (200 OK):**

```json
{
  "success": true,
  "message": "Order details fetched successfully",
  "data": {
    "orderId": "7d24cb7d-82cc-4856-a5d5-2ccf4751f257",
    "status": "PLACED",
    "totalAmount": 130,
    "items": [
      {
        "productName": "Buffalo Milk",
        "quantity": 2,
        "price": 65
      }
    ],
    "createdAt": "2025-01-12T09:30:00"
  }
}
```

---

### 12. Admin – Analytics

APIs providing system-level and business analytics.
All endpoints are **restricted to ADMIN users only**.

---

#### 12.1 Dashboard Analytics

Provides high-level metrics for the admin dashboard.

**Endpoint:** `GET /api/v1/admin/analytics/dashboard`

**Access:** Admin only

**Headers:**

```
Authorization: Bearer {accessToken}
```

**Response (200 OK):**

```json
{
  "success": true,
  "message": "Dashboard analytics fetched successfully",
  "data": {
    "totalUsers": 120,
    "activeSubscriptions": 85,
    "todayDeliveries": 60
  }
}
```

---

#### 12.2 User Analytics

Provides user-related statistics.

**Endpoint:** `GET /api/v1/admin/analytics/users`

**Access:** Admin only

**Headers:**

```
Authorization: Bearer {accessToken}
```

**Response (200 OK):**

```json
{
  "success": true,
  "message": "User analytics fetched successfully",
  "data": {
    "totalUsers": 120,
    "newUsersThisMonth": 18
  }
}
```

---

#### 12.3 Subscription Analytics

Provides subscription growth and status metrics.

**Endpoint:** `GET /api/v1/admin/analytics/subscriptions`

**Access:** Admin only

**Headers:**

```
Authorization: Bearer {accessToken}
```

**Response (200 OK):**

```json
{
  "success": true,
  "message": "Subscription analytics fetched successfully",
  "data": {
    "active": 85,
    "paused": 10,
    "cancelled": 5
  }
}
```

---

#### 12.4 Delivery Analytics

Provides delivery performance statistics.

**Endpoint:** `GET /api/v1/admin/analytics/deliveries`

**Access:** Admin only

**Headers:**

```
Authorization: Bearer {accessToken}
```

**Response (200 OK):**

```json
{
  "success": true,
  "message": "Delivery analytics fetched successfully",
  "data": {
    "delivered": 540,
    "missed": 12,
    "successRate": "97.8%"
  }
}
```
---


#### 12.5 Revenue Analytics

Provides revenue and earnings statistics.

**Endpoint:** `GET /api/v1/admin/analytics/revenue`

**Access:** Admin only

**Response (200 OK):**

```json
{
  "success": true,
  "message": "Revenue analytics fetched successfully",
  "data": {
    "totalRevenue": 125000,
    "monthlyRevenue": 42000
  }
}
 ```

 
 

 

