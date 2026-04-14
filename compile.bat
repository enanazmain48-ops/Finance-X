@echo off
REM Finance X - Compilation Script for Windows

echo.
echo ==========================================
echo Finance X
echo Compilation Script
echo ==========================================
echo.

REM Create directory structure
echo Creating directory structure...
if not exist "com\finance\model" mkdir com\finance\model
if not exist "com\finance\util" mkdir com\finance\util
if not exist "com\finance\ui" mkdir com\finance\ui
echo Directory structure created
echo.

REM Check if javac is available
where javac >nul 2>nul
if errorlevel 1 (
    echo Error: javac not found. Please install JDK 11 or higher.
    pause
    exit /b 1
)

echo Java Version:
javac -version
echo.

REM Compile all Java files
echo Compiling Java files...
echo - Compiling main application...
javac FinanceApp.java
if errorlevel 1 goto :error

echo - Compiling model classes...
javac com\finance\model\User.java
if errorlevel 1 goto :error

javac com\finance\model\Expense.java
if errorlevel 1 goto :error

javac com\finance\model\Budget.java
if errorlevel 1 goto :error

echo - Compiling utility classes...
javac com\finance\util\CSVHandler.java
if errorlevel 1 goto :error

echo - Compiling UI classes...
javac com\finance\ui\LoginFrame.java
if errorlevel 1 goto :error

javac com\finance\ui\DashboardFrame.java
if errorlevel 1 goto :error

javac com\finance\ui\DashboardPanel.java
if errorlevel 1 goto :error

javac com\finance\ui\ExpensePanel.java
if errorlevel 1 goto :error

javac com\finance\ui\BudgetPanel.java
if errorlevel 1 goto :error

javac com\finance\ui\ReportPanel.java
if errorlevel 1 goto :error

echo.
echo ==========================================
echo Compilation Successful!
echo ==========================================
echo.
echo To run the application, execute:
echo   java FinanceApp
echo.
pause
exit /b 0

:error
echo.
echo Compilation Failed
echo Please check the errors above
echo.
pause
exit /b 1