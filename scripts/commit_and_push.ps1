# =========================================
# Admin Analytics & Subscription Commits
# =========================================

Write-Host "Starting commit & push process..." -ForegroundColor Green
Set-Location (git rev-parse --show-toplevel)

Write-Host "Starting commit & push process..." -ForegroundColor Green

git add .
git commit -m "Fix time-type mismatch between LocalDateTime and Instant in analytics

Resolved runtime errors caused by mixing LocalDateTime and Instant in repository
queries and service logic. Standardized timestamp-based analytics to use Instant
consistently, preventing InvalidDataAccessApiUsageException.

Future improvement: Define and document clear time-handling guidelines
to avoid temporal type mismatches across layers."

Write-Host "Commit 1 completed" -ForegroundColor Green

# Commit 2
git add .
git commit -m "Align SubscriptionEvents queries with LocalDate deliveryDate

Corrected delivery analytics by using LocalDate for deliveryDate as per domain
design. Removed incorrect Instant-based comparisons and fixed service logic
to reflect day-level delivery semantics.

Future improvement: Add shared utility methods for Instant to LocalDate
conversion at service boundaries."

Write-Host "Commit 2 completed" -ForegroundColor Green

# Commit 3
git add .
git commit -m "Fix Spring Data repository method names for entity relationships

Updated repository method naming to correctly reference nested entity
properties (e.g., subscription.id, user.id). This prevents query resolution
failures caused by non-existent direct ID fields.

Future improvement: Add repository-level tests to validate Spring Data
query method parsing during CI."

Write-Host "Commit 3 completed" -ForegroundColor Green

# Commit 4
git add .
git commit -m "Correct OrderRepository to use Instant-based createdAt analytics

Aligned OrderRepository analytics methods with Instant-based createdAt field
in Orders entity. Replaced incorrect LocalDateTime parameters with Instant
and fixed aggregation queries accordingly.

Future improvement: Add database indexes on created_at for improved
analytics performance."

Write-Host "Commit 4 completed" -ForegroundColor Green

# Commit 5
git add .
git commit -m "Stabilize admin dashboard analytics with consistent date handling

Finalized dashboard analytics by consistently using Instant for revenue and
user metrics, and LocalDate for delivery metrics. Fixed incorrect 'today'
calculations caused by mixing date and time logic.

Future improvement: Refactor dashboard analytics into smaller components
and add unit tests for timezone and boundary cases."

Write-Host "Commit 5 completed" -ForegroundColor Green

Write-Host "All commits created successfully." -ForegroundColor Cyan
