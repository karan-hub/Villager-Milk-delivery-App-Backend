Write-Host "Starting commit & push process..." -ForegroundColor Green
Set-Location (git rev-parse --show-toplevel)
$ErrorActionPreference = "Stop"

# Helper: reset index before every commit
function Reset-Index {
    git reset | Out-Null
}

# ===============================
# COMMIT 1 – DeliverySchedule DTO
# ===============================
Reset-Index
git add src/main/java/com/karan/village_milk_app/Dto/DeliveryScheduleDto.java
git commit -m "feat(dto): introduce DeliveryScheduleDto for custom subscriptions"
Write-Host "Commit 1 done" -ForegroundColor Green


# ===============================
# COMMIT 2 – SubscriptionDeliveryRule entity
# ===============================
Reset-Index
git add src/main/java/com/karan/village_milk_app/model/SubscriptionDeliveryRule.java
git commit -m "feat(model): add SubscriptionDeliveryRule entity"
Write-Host "Commit 2 done" -ForegroundColor Green


# ===============================
# COMMIT 3 – Delivery rule repository
# ===============================
Reset-Index
git add src/main/java/com/karan/village_milk_app/Repositories/SubscriptionDeliveryRuleRepo.java
git commit -m "feat(repository): add SubscriptionDeliveryRule repository"
Write-Host "Commit 3 done" -ForegroundColor Green


# ===============================
# COMMIT 4 – Custom subscription request
# ===============================
Reset-Index
git add src/main/java/com/karan/village_milk_app/Request/CreateCustomSubscriptionRequest.java
git commit -m "feat(request): add CreateCustomSubscriptionRequest"
Write-Host "Commit 4 done" -ForegroundColor Green


# ===============================
# COMMIT 5 – Delivery rule response DTO
# ===============================
Reset-Index
git add src/main/java/com/karan/village_milk_app/Response/DeliveryRuleResponse.java
git commit -m "feat(response): add DeliveryRuleResponse DTO"
Write-Host "Commit 5 done" -ForegroundColor Green


# ===============================
# COMMIT 6 – Rename Subscription DTO
# ===============================
Reset-Index
git rm src/main/java/com/karan/village_milk_app/Response/SubscriptionDto.java
git add src/main/java/com/karan/village_milk_app/Response/SubscriptionResponse.java
git commit -m "refactor(response): rename SubscriptionDto to SubscriptionResponse"
Write-Host "Commit 6 done" -ForegroundColor Green


# ===============================
# COMMIT 7 – CreateSubscriptionRequest update
# ===============================
Reset-Index
git add src/main/java/com/karan/village_milk_app/Dto/CreateSubscriptionRequest.java
git commit -m "feat(dto): extend CreateSubscriptionRequest for custom rules"
Write-Host "Commit 7 done" -ForegroundColor Green


# ===============================
# COMMIT 8 – Subscription plan repository
# ===============================
Reset-Index
git add src/main/java/com/karan/village_milk_app/Repositories/SubscriptionPlanRepository.java
git commit -m "refactor(repository): enhance SubscriptionPlanRepository"
Write-Host "Commit 8 done" -ForegroundColor Green


# ===============================
# COMMIT 9 – Subscriptions repository
# ===============================
Reset-Index
git add src/main/java/com/karan/village_milk_app/Repositories/SubscriptionsRepository.java
git commit -m "refactor(repository): update SubscriptionsRepository logic"
Write-Host "Commit 9 done" -ForegroundColor Green


# ===============================
# COMMIT 10 – Subscription service interface
# ===============================
Reset-Index
git add src/main/java/com/karan/village_milk_app/Service/SubscriptionService.java
git commit -m "feat(service): extend SubscriptionService for custom delivery"
Write-Host "Commit 10 done" -ForegroundColor Green


# ===============================
# COMMIT 11 – Subscription service impl
# ===============================
Reset-Index
git add src/main/java/com/karan/village_milk_app/Service/Impl/SubscriptionServiceImpl.java
git commit -m "feat(service): implement custom subscription delivery rules"
Write-Host "Commit 11 done" -ForegroundColor Green


# ===============================
# COMMIT 12 – Admin subscription service
# ===============================
Reset-Index
git add `
src/main/java/com/karan/village_milk_app/Service/AdminSubscriptionService.java `
src/main/java/com/karan/village_milk_app/Service/Impl/AdminSubscriptionServiceImpl.java
git commit -m "feat(admin): manage custom subscription delivery rules"
Write-Host "Commit 12 done" -ForegroundColor Green


# ===============================
# COMMIT 13 – Subscription controllers
# ===============================
Reset-Index
git add `
src/main/java/com/karan/village_milk_app/Controller/SubscriptionController.java `
src/main/java/com/karan/village_milk_app/Controller/AdminSubscriptionController.java
git commit -m "feat(controller): expose custom subscription APIs"
Write-Host "Commit 13 done" -ForegroundColor Green


# ===============================
# COMMIT 14 – Product, User, Admin services
# ===============================
Reset-Index
git add `
src/main/java/com/karan/village_milk_app/Service/AdminService.java `
src/main/java/com/karan/village_milk_app/Service/Impl/AdminServiceImpl.java `
src/main/java/com/karan/village_milk_app/Service/ProductService.java `
src/main/java/com/karan/village_milk_app/Service/Impl/ProductServiceImpl.java `
src/main/java/com/karan/village_milk_app/Service/UserService.java `
src/main/java/com/karan/village_milk_app/Service/Impl/UserServiceImpl.java
git commit -m "refactor(service): align services with subscription rules"
Write-Host "Commit 14 done" -ForegroundColor Green


# ===============================
# COMMIT 15 – Config, controllers & models
# ===============================
Reset-Index
git add `
scripts/commit_and_push.ps1 `
src/main/java/com/karan/village_milk_app/Config/ProjectConfig.java `
src/main/java/com/karan/village_milk_app/Controller/UserController.java `
src/main/java/com/karan/village_milk_app/Controller/productController.java `
src/main/java/com/karan/village_milk_app/model/Product.java `
src/main/java/com/karan/village_milk_app/model/Subscriptions.java `
src/main/java/com/karan/village_milk_app/model/Type/PlanType.java
git commit -m "chore(core): finalize custom subscription integration"
Write-Host "Commit 15 done" -ForegroundColor Green


# ===============================
# PUSH
# ===============================
git push origin main
Write-Host "All 15 commits created and pushed successfully." -ForegroundColor Cyan
