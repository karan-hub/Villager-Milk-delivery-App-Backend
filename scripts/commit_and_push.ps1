#!/bin/bash

set -e

echo "🚀 Starting structured commit & push flow..."

# ---------------- COMMIT 1: SECURITY ----------------
echo "🔐 Commit 1: Security"
git add src/main/java/**/security/** || true
git commit -m "feat(security): finalize public, user and admin endpoint access rules" || echo "No security changes"
git push origin main

# ---------------- COMMIT 2: PRODUCT ----------------
echo "🥛 Commit 2: Product fixes"
git add src/main/java/**/product/** || true
git commit -m "fix(product): prevent entity id mutation during product update" || echo "No product changes"
git push origin main

# ---------------- COMMIT 3: SUBSCRIPTIONS CORE ----------------
echo "🔁 Commit 3: Subscription core"
git add src/main/java/**/subscription/SubscriptionService* || true
git add src/main/java/**/subscription/SubscriptionController* || true
git add src/main/java/**/subscription/SubscriptionStatus.java || true
git commit -m "feat(subscription): add pause, resume, cancel and expired subscription flow" || echo "No subscription core changes"
git push origin main

# ---------------- COMMIT 4: DELIVERY EVENTS ----------------
echo "🚚 Commit 4: Delivery events"
git add src/main/java/**/subscription/SubscriptionEvents* || true
git add src/main/java/**/subscription/EventStatus.java || true
git commit -m "feat(delivery): implement subscription delivery event lifecycle" || echo "No delivery event changes"
git push origin main

# ---------------- COMMIT 5: ADMIN SUBSCRIPTIONS ----------------
echo "👮 Commit 5: Admin subscription APIs"
git add src/main/java/**/admin/AdminSubscription* || true
git commit -m "feat(admin): add filtered subscription and delivery listing APIs" || echo "No admin subscription changes"
git push origin main

# ---------------- COMMIT 6: ORDERS ----------------
echo "🛒 Commit 6: Orders (Buy Once)"
git add src/main/java/**/order/** || true
git commit -m "feat(order): add one-time order (buy once) functionality" || echo "No order changes"
git push origin main

# ---------------- COMMIT 7: ADMIN DELIVERY ----------------
echo "📦 Commit 7: Admin delivery management"
git add src/main/java/**/admin/AdminDelivery* || true
git commit -m "feat(admin): add admin delivery management APIs" || echo "No admin delivery changes"
git push origin main

# ---------------- COMMIT 8: ANALYTICS ----------------
echo "📊 Commit 8: Admin analytics"
git add src/main/java/**/admin/analytics/** || true
git commit -m "feat(analytics): add admin dashboard and analytics APIs" || echo "No analytics changes"
git push origin main

# ---------------- COMMIT 9: SCHEDULERS ----------------
echo "⏰ Commit 9: Schedulers"
git add src/main/java/**/scheduler/** || true
git commit -m "feat(scheduler): automate subscription expiry and daily deliveries" || echo "No scheduler changes"
git push origin main

# ---------------- COMMIT 10: CLEANUP ----------------
echo "🧹 Commit 10: Cleanup & stability"
git add src/main/java/**/exception/** || true
git add src/main/java/**/config/** || true
git commit -m "chore: improve exception handling and production stability" || echo "No cleanup changes"
git push origin main

echo "✅ All commits processed successfully!"
