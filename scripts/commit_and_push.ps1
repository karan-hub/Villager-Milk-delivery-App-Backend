# ================================
# CONFIG
# ================================
$branch = "main"
$remote = "origin"

# Start date (edit if needed)
$baseDate = Get-Date "2025-12-20 10:00:00"

function Commit-With-Date($message, $daysOffset) {
    $commitDate = $baseDate.AddDays($daysOffset).ToString("yyyy-MM-dd HH:mm:ss")
    git commit -m "$message" --date="$commitDate"
}

# ================================
# COMMIT 1 – Address Entity + Repo
# ================================
git add `
src/main/java/com/karan/village_milk_app/model/Address.java `
src/main/java/com/karan/village_milk_app/Repositories/AddressRepository.java

Commit-With-Date "fix(address): correct user relation and repository design" 0

# ================================
# COMMIT 2 – Address DTOs
# ================================
git add `
src/main/java/com/karan/village_milk_app/Request/AddressRequest.java `
src/main/java/com/karan/village_milk_app/Response/AddressResponse.java `
src/main/java/com/karan/village_milk_app/Dto/AddressDTO.java

Commit-With-Date "feat(address): add request and response DTOs" 1

# ================================
# COMMIT 3 – Address Service Interface
# ================================
git add `
src/main/java/com/karan/village_milk_app/Service/AddressService.java

Commit-With-Date "feat(address): define address service contract" 2

# ================================
# COMMIT 4 – Address Service Impl
# ================================
git add `
src/main/java/com/karan/village_milk_app/Service/Impl/AddressServiceImpl.java

Commit-With-Date "feat(address): implement address service with ownership checks" 3

# ================================
# COMMIT 5 – Address Controller
# ================================
git add `
src/main/java/com/karan/village_milk_app/Controller/AddressController.java

Commit-With-Date "feat(address): expose REST APIs for address management" 4

# ================================
# COMMIT 6 – Payment DTOs
# ================================
git add `
src/main/java/com/karan/village_milk_app/Request/InitiatePaymentRequest.java `
src/main/java/com/karan/village_milk_app/Request/ConfirmPaymentRequest.java `
src/main/java/com/karan/village_milk_app/Response/PaymentResponse.java

Commit-With-Date "feat(payment): add payment request and response models" 5

# ================================
# COMMIT 7 – Payment Service Interface
# ================================
git add `
src/main/java/com/karan/village_milk_app/Service/PaymentService.java

Commit-With-Date "feat(payment): define payment service contract" 6

# ================================
# COMMIT 8 – Payment Service Impl
# ================================
git add `
src/main/java/com/karan/village_milk_app/Service/Impl/PaymentServiceImpl.java

Commit-With-Date "feat(payment): implement payment workflow" 7

# ================================
# COMMIT 9 – Payment Controller
# ================================
git add `
src/main/java/com/karan/village_milk_app/Controller/PaymentController.java

Commit-With-Date "feat(payment): add payment REST controller" 8

# ================================
# COMMIT 10 – Exception Classes
# ================================
git add `
src/main/java/com/karan/village_milk_app/Exceptions/BadRequestException.java `
src/main/java/com/karan/village_milk_app/Exceptions/UnauthorizedException.java `
src/main/java/com/karan/village_milk_app/Exceptions/ResourceNotFoundException.java

Commit-With-Date "feat(exception): add custom domain exceptions" 9

# ================================
# COMMIT 11 – Global Exception Handler
# ================================
git add `
src/main/java/com/karan/village_milk_app/Exceptions/GlobalExceptionHandler.java

Commit-With-Date "feat(exception): centralize API error handling" 10

# ================================
# COMMIT 12 – User Helper
# ================================
git add `
src/main/java/com/karan/village_milk_app/healpers/UserHelper.java

Commit-With-Date "refactor(auth): harden current user resolution logic" 11

# ================================
# COMMIT 13 – Security Filter
# ================================
git add `
src/main/java/com/karan/village_milk_app/Security/JwtAuthenticationFilter.java

Commit-With-Date "feat(security): improve JWT authentication filter" 12

# ================================
# COMMIT 14 – Security Config
# ================================
git add `
src/main/java/com/karan/village_milk_app/Config/SecurityConfig.java `
src/main/java/com/karan/village_milk_app/Config/SecurityEndpoints.java

Commit-With-Date "config(security): update endpoint authorization rules" 13

# ================================
# COMMIT 15 – Admin Controllers
# ================================
git add `
src/main/java/com/karan/village_milk_app/Controller/AdminController.java `
src/main/java/com/karan/village_milk_app/Controller/AdminProductController.java

Commit-With-Date "feat(admin): enhance admin management endpoints" 14

# ================================
# COMMIT 16 – Subscription Plan Fixes
# ================================
git add `
src/main/java/com/karan/village_milk_app/Controller/SubscriptionPlanController.java `
src/main/java/com/karan/village_milk_app/Repositories/SubscriptionPlanRepository.java `
src/main/java/com/karan/village_milk_app/Service/Impl/SubscriptionPlanServiceImpl.java

Commit-With-Date "fix(subscription): correct plan retrieval and service logic" 15

# ================================
# COMMIT 17 – User Service
# ================================
git add `
src/main/java/com/karan/village_milk_app/Service/Impl/UserServiceImpl.java `
src/main/java/com/karan/village_milk_app/Service/UserService.java `
src/main/java/com/karan/village_milk_app/Controller/UserController.java

Commit-With-Date "feat(user): improve user service and controller flows" 16

# ================================
# COMMIT 18 – Product Service
# ================================
git add `
src/main/java/com/karan/village_milk_app/Service/ProductService.java `
src/main/java/com/karan/village_milk_app/Service/Impl/ProductServiceImpl.java `
src/main/java/com/karan/village_milk_app/Controller/productController.java

Commit-With-Date "feat(product): stabilize product CRUD operations" 17

# ================================
# COMMIT 19 – Config Files
# ================================
git add `
src/main/java/com/karan/village_milk_app/Config/ProjectConfig.java `
src/main/resources/application-dev.yml

Commit-With-Date "config(app): update application and environment settings" 18

# ================================
# COMMIT 20 – Final Cleanup
# ================================
git add .

Commit-With-Date "chore: final cleanup and consistency fixes" 19

# ================================
# PUSH
# ================================
git push $remote $branch
