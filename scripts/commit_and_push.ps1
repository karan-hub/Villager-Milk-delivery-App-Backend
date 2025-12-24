# Move to project root (parent of scripts folder)
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ProjectRoot = Resolve-Path "$ScriptDir\.."
Set-Location $ProjectRoot

Write-Host "Working directory set to project root:"
Write-Host $ProjectRoot
Write-Host ""

Write-Host "Starting commit-by-commit push to GitHub..."

function CommitPush {
    param (
        [string[]]$Files,
        [string]$Message
    )

    Write-Host ""
    Write-Host "----------------------------------------"
    Write-Host "Commit: $Message"
    Write-Host "----------------------------------------"

    # Clear staging
    git reset --quiet

    # Add files
    git add $Files
    if ($LASTEXITCODE -ne 0) {
        Write-Host "ERROR: git add failed. Check file paths."
        exit 1
    }

    # Commit
    git commit -m "$Message"
    if ($LASTEXITCODE -ne 0) {
        Write-Host "ERROR: git commit failed."
        exit 1
    }

    # Push immediately
    git push origin main
    if ($LASTEXITCODE -ne 0) {
        Write-Host "ERROR: git push failed. Stopping script."
        exit 1
    }

    Write-Host "SUCCESS: Commit pushed to GitHub."
}

# ---------------- COMMIT 1 ----------------
CommitPush @(
    "src/main/java/com/karan/village_milk_app/Controller/AuthController.java",
    "src/main/java/com/karan/village_milk_app/Request/LoginRequest.java",
    "src/main/java/com/karan/village_milk_app/model/OtpCode.java",
    "src/main/java/com/karan/village_milk_app/model/RefreshToken.java"
) "feat(auth): improve authentication and login flow"

# ---------------- COMMIT 2 ----------------
CommitPush @(
    "src/main/java/com/karan/village_milk_app/Dto/UserDTO.java",
    "src/main/java/com/karan/village_milk_app/Dto/AddressDTO.java",
    "src/main/java/com/karan/village_milk_app/model/User.java",
    "src/main/java/com/karan/village_milk_app/model/Address.java",
    "src/main/java/com/karan/village_milk_app/Service/UserService.java",
    "src/main/java/com/karan/village_milk_app/Service/Impl/UserServiceImpl.java",
    "src/main/java/com/karan/village_milk_app/Repositories/UserRepository.java"
) "feat(user): update user and address domain logic"

# ---------------- COMMIT 3 ----------------
CommitPush @(
    "src/main/java/com/karan/village_milk_app/Controller/SubscriptionController.java",
    "src/main/java/com/karan/village_milk_app/Service/Impl/SubscriptionServiceImpl.java",
    "src/main/java/com/karan/village_milk_app/Repositories/SubscriptionsRepository.java",
    "src/main/java/com/karan/village_milk_app/Repositories/SubscriptionEventsRepository.java"
) "feat(subscription): enhance subscription core functionality"

# ---------------- COMMIT 4 ----------------
CommitPush @(
    "src/main/java/com/karan/village_milk_app/Controller/SubscriptionPlanController.java",
    "src/main/java/com/karan/village_milk_app/Request/CreatePlanRequest.java",
    "src/main/java/com/karan/village_milk_app/Service/Impl/SubscriptionPlanServiceImpl.java",
    "src/main/java/com/karan/village_milk_app/Repositories/SubscriptionPlanRepository.java"
) "feat(subscription-plan): add and update subscription plans"

# ---------------- COMMIT 5 ----------------
CommitPush @(
    "src/main/java/com/karan/village_milk_app/Dto/CreateSubscriptionRequest.java",
    "src/main/java/com/karan/village_milk_app/Request/CreateSubscriptionRequest.java",
    "src/main/java/com/karan/village_milk_app/Response/SubscriptionDto.java",
    "src/main/java/com/karan/village_milk_app/model/DeliveryDto.java"
) "refactor(subscription): align subscription DTOs and requests"

# ---------------- COMMIT 6 ----------------
CommitPush @(
    "src/main/java/com/karan/village_milk_app/Request/CreateOrderRequest.java",
    "src/main/java/com/karan/village_milk_app/Request/CreateOrderItemRequest.java",
    "src/main/java/com/karan/village_milk_app/Repositories/OrderRepository.java",
    "src/main/java/com/karan/village_milk_app/Service/Impl/OrderServiceImpl.java"
) "feat(order): implement order creation and processing"

# ---------------- COMMIT 7 ----------------
CommitPush @(
    "src/main/java/com/karan/village_milk_app/Service/Impl/SubscriptionOrderScheduler.java"
) "feat(scheduler): automate order generation for subscriptions"

# ---------------- COMMIT 8 ----------------
CommitPush @(
    "src/main/java/com/karan/village_milk_app/Service/AdminSubscriptionService.java",
    "src/main/java/com/karan/village_milk_app/Service/Impl/AdminSubscriptionServiceImpl.java"
) "feat(admin): manage subscriptions from admin panel"

# ---------------- COMMIT 9 ----------------
CommitPush @(
    "src/main/java/com/karan/village_milk_app/Service/AdminService.java",
    "src/main/java/com/karan/village_milk_app/Service/Impl/AdminServiceImpl.java"
) "feat(admin): add core admin service operations"

# ---------------- COMMIT 10 ----------------
CommitPush @(
    "src/main/java/com/karan/village_milk_app/model/PaymentDto.java"
) "chore(model): clean up payment and supporting models"

Write-Host ""
Write-Host "ALL COMMITS PUSHED SEPARATELY TO GITHUB SUCCESSFULLY."
