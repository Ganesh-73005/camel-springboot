@echo off
echo ===============================================
echo Camel-Drools REST API Testing Script (Windows)
echo ===============================================

set BASE_URL=http://localhost:8080/api

echo.
echo 1. Health Check
echo ===============
curl -X GET "%BASE_URL%/orders/health" -H "Content-Type: application/json"
echo.
echo.

echo 2. Test High Value Order (^>= 1000) - Should be auto-approved
echo =============================================================
curl -X POST "%BASE_URL%/orders/process" -H "Content-Type: application/json" -d "{\"orderId\":\"ORD-001\",\"customerId\":\"CUST-001\",\"amount\":1500.00,\"category\":\"REGULAR\"}"
echo.
echo.

echo 3. Test Premium Customer Order - Should get 10%% discount
echo ========================================================
curl -X POST "%BASE_URL%/orders/process" -H "Content-Type: application/json" -d "{\"orderId\":\"ORD-002\",\"customerId\":\"CUST-002\",\"amount\":800.00,\"category\":\"PREMIUM\"}"
echo.
echo.

echo 4. Test Regular Customer Order (^>= 500) - Should get 5%% discount
echo =================================================================
curl -X POST "%BASE_URL%/orders/process" -H "Content-Type: application/json" -d "{\"orderId\":\"ORD-003\",\"customerId\":\"CUST-003\",\"amount\":600.00,\"category\":\"REGULAR\"}"
echo.
echo.

echo 5. Test Low Value Order (^< 100) - Should be rejected
echo ====================================================
curl -X POST "%BASE_URL%/orders/process" -H "Content-Type: application/json" -d "{\"orderId\":\"ORD-004\",\"customerId\":\"CUST-004\",\"amount\":50.00,\"category\":\"REGULAR\"}"
echo.
echo.

echo 6. Test Standard Order (100-999) - Should be approved
echo =====================================================
curl -X POST "%BASE_URL%/orders/process" -H "Content-Type: application/json" -d "{\"orderId\":\"ORD-005\",\"customerId\":\"CUST-005\",\"amount\":300.00,\"category\":\"REGULAR\"}"
echo.
echo.

echo 7. Test Invalid Order (missing orderId) - Should return error
echo =============================================================
curl -X POST "%BASE_URL%/orders/process" -H "Content-Type: application/json" -d "{\"customerId\":\"CUST-006\",\"amount\":200.00,\"category\":\"REGULAR\"}"
echo.
echo.

echo 8. Test Invalid Order (negative amount) - Should return error
echo =============================================================
curl -X POST "%BASE_URL%/orders/process" -H "Content-Type: application/json" -d "{\"orderId\":\"ORD-007\",\"customerId\":\"CUST-007\",\"amount\":-100.00,\"category\":\"REGULAR\"}"
echo.
echo.

echo ===============================================
echo Testing completed!
echo ===============================================
pause