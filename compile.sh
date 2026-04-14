#!/bin/bash

# Finance X - Compilation Script
# This script compiles all Java source files for the Finance Management App

echo "=========================================="
echo "Finance X"
echo "Compilation Script"
echo "=========================================="
echo ""

# Create directory structure
echo "Creating directory structure..."
mkdir -p com/finance/model
mkdir -p com/finance/util
mkdir -p com/finance/ui
echo "✓ Directory structure created"
echo ""

# Check if Java compiler is available
if ! command -v javac &> /dev/null; then
    echo "✗ Error: javac not found. Please install JDK 11 or higher."
    exit 1
fi

echo "Java Version:"
javac -version
echo ""

# Compile all Java files
echo "Compiling Java files..."
echo "- Compiling main application..."
javac FinanceApp.java

echo "- Compiling model classes..."
javac com/finance/model/User.java
javac com/finance/model/Expense.java
javac com/finance/model/Budget.java

echo "- Compiling utility classes..."
javac com/finance/util/CSVHandler.java

echo "- Compiling UI classes..."
javac com/finance/ui/LoginFrame.java
javac com/finance/ui/DashboardFrame.java
javac com/finance/ui/DashboardPanel.java
javac com/finance/ui/ExpensePanel.java
javac com/finance/ui/BudgetPanel.java
javac com/finance/ui/ReportPanel.java

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✓ Compilation Successful!"
    echo "=========================================="
    echo ""
    echo "To run the application, execute:"
    echo "  java FinanceApp"
    echo ""
else
    echo ""
    echo "✗ Compilation Failed"
    echo "Please check the errors above"
    exit 1
fi