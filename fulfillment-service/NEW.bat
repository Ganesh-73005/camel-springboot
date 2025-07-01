@echo off
SETLOCAL

echo.
echo [1] Testing Fulfillment Service Health Check
curl http://localhost:8081/api/fulfillment/health
timeout /t 2 >nul

echo.
echo [2] Testing Order Processing - Valid Premium Order (should approve)
curl -X POST http://localhost:8080/api/orders/process -H "Content-Type: application/json" -d "{\"orderId\":\"TEST-PREMIUM\",\"customerId\":\"CUST-1\",\"amount\":1500,\"category\":\"PREMIUM\"}"
timeout /t 2 >nul

echo.
echo [3] Testing Order Processing - Valid Regular Order (should approve)
curl -X POST http://localhost:8080/api/orders/process -H "Content-Type: application/json" -d "{\"orderId\":\"TEST-REGULAR\",\"customerId\":\"CUST-2\",\"amount\":750,\"category\":\"REGULAR\"}"
timeout /t 2 >nul

echo.
echo [4] Testing Order Processing - Low Value Order (should reject)
curl -X POST http://localhost:8080/api/orders/process -H "Content-Type: application/json" -d "{\"orderId\":\"TEST-LOW\",\"customerId\":\"CUST-3\",\"amount\":50,\"category\":\"REGULAR\"}"
timeout /t 2 >nul

echo.
echo [5] Testing Order Processing - Missing Required Field (should error)
curl -X POST http://localhost:8080/api/orders/process -H "Content-Type: application/json" -d "{\"orderId\":\"\",\"customerId\":\"CUST-4\",\"amount\":200,\"category\":\"REGULAR\"}"
timeout /t 2 >nul

echo.
echo [6] Testing Direct Fulfillment Endpoint
curl -X POST http://localhost:8081/api/fulfillment/orders -H "Content-Type: application/json" -d "{\"orderId\":\"TEST-DIRECT\",\"customerId\":\"CUST-5\",\"amount\":1200,\"category\":\"PREMIUM\",\"status\":\"APPROVED\"}"
timeout /t 2 >nul

echo.
echo [7] Final Health Check Verification
curl http://localhost:8080/api/orders/health
curl http://localhost:8081/api/fulfillment/health

echo.
echo TEST SEQUENCE COMPLETE
pause