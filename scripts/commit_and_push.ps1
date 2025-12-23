Write-Host "🚀 Structured commit & push started..." -ForegroundColor Cyan

function CommitAndPush($message) {
    if (-not (git diff --cached --quiet)) {
        git commit -m "$message"
        git push origin main
    } else {
        Write-Host "⚠️ Nothing staged for: $message" -ForegroundColor Yellow
    }
}

# --------------------------------------------------
# COMMIT 1: SECURITY + CONFIG
# --------------------------------------------------
git add src/main/java/com/karan/village_milk_app/Config
git add src/main/java/com/karan/village_milk_app/Security
CommitAndPush "feat(security): stabilize jwt, cors and security configuration"

# --------------------------------------------------
# COMMIT 2: ADMIN CONTROLLERS
# --------------------------------------------------
git add src/main/java/com/karan/village_milk_app/Controller/Admin*
CommitAndPush "feat(admin): finalize admin controllers for product, subscription and delivery"

# --------------------------------------------------
# COMMIT 3: SUBSCRIPTIONS CORE + EVENTS
# --------------------------------------------------
git add src/main/java/com/karan/village_milk_app/Service/Subscription*
git add src/main/java/com/karan/village_milk_app/model/Subscription*
git add src/main/java/com/karan/village_milk_app/Dto/Subscription*
CommitAndPush "feat(subscription): complete subscription lifecycle and delivery events"

# --------------------------------------------------
# COMMIT 4: ORDERS (BUY ONCE)
# --------------------------------------------------
git add src/main/java/com/karan/village_milk_app/Service/Order*
git add src/main/java/com/karan/village_milk_app/Controller/OrderController.java
git add src/main/java/com/karan/village_milk_app/model/Order*
git add src/main/java/com/karan/village_milk_app/Dto/Order*
CommitAndPush "feat(order): implement one-time order (buy once) flow"

# --------------------------------------------------
# COMMIT 5: PRODUCT + SUBSCRIPTION PLAN
# --------------------------------------------------
git add src/main/java/com/karan/village_milk_app/model/Product.java
git add src/main/java/com/karan/village_milk_app/Dto/ProductDto.java
git add src/main/java/com/karan/village_milk_app/Service/Impl/ProductServiceImpl.java
git add src/main/java/com/karan/village_milk_app/Service/SubscriptionPlanService*
git add src/main/java/com/karan/village_milk_app/model/SubscriptionPlan.java
CommitAndPush "feat(product): stabilize product and subscription plan management"

# --------------------------------------------------
# COMMIT 6: ANALYTICS (ADMIN DASHBOARD)
# --------------------------------------------------
git add src/main/java/com/karan/village_milk_app/Controller/AdminAnalyticsController.java
git add src/main/java/com/karan/village_milk_app/Service/AdminAnalyticsService.java
git add src/main/java/com/karan/village_milk_app/Service/Impl/AdminAnalyticsServiceImpl.java
CommitAndPush "feat(analytics): add admin dashboard analytics APIs"

# --------------------------------------------------
# COMMIT 7: REFRESH TOKEN SERVICE
# --------------------------------------------------
git add src/main/java/com/karan/village_milk_app/Service/RefreshTokenService.java
git add src/main/java/com/karan/village_milk_app/Service/Impl/RefreshTokenServiceImpl.java
CommitAndPush "feat(auth): add refresh token service implementation"

# --------------------------------------------------
# COMMIT 8: SCRIPT ITSELF
# --------------------------------------------------
git add scripts
CommitAndPush "chore: add windows powershell script for structured commits"

Write-Host "✅ All commits completed successfully!" -ForegroundColor Green
