# Finance Management Application

A comprehensive personal finance management application built with Java Swing. Track expenses, set budgets, and analyze spending patterns with visual reports.

## Features

### 1. **User Authentication**
- User registration with email and password
- Secure login system
- User data stored in CSV format
- Password encryption (basic implementation - use BCrypt for production)

### 2. **Expense Management**
- Add expenses with date, amount, category, and description
- 8 predefined categories:
  - Food & Dining
  - Transportation
  - Shopping
  - Entertainment
  - Utilities
  - Health & Fitness
  - Education
  - Other
- View all expenses in a table format
- Delete expenses
- Auto-save to CSV file

### 3. **Budget Management**
- Set monthly or weekly budgets for different categories
- Visual budget adherence indicators
- Real-time spending tracking against budgets
- Track remaining budget amounts
- Delete or modify budgets

### 4. **Financial Reports & Analytics**
- **Category Distribution**: Pie chart showing spending by category
- **Monthly Trend**: Bar chart showing spending trends over 6 months
- **Budget Adherence**: Progress bars showing budget usage for each category
- **Period Spending**: Weekly spending comparison (this week vs last week)
- Color-coded visualizations
- Interactive report selection

### 5. **Dashboard Overview**
- Total expenses summary
- Current month spending
- Weekly spending (last 7 days)
- Total transaction count
- Quick statistics cards

## Project Structure

```
FinanceApp.java                  # Main entry point
├── com/finance/model/
│   ├── User.java               # User model
│   ├── Expense.java            # Expense model
│   └── Budget.java             # Budget model
├── com/finance/util/
│   └── CSVHandler.java         # CSV file operations
└── com/finance/ui/
    ├── LoginFrame.java         # Login/Registration UI
    ├── DashboardFrame.java     # Main dashboard container
    ├── DashboardPanel.java     # Dashboard overview panel
    ├── ExpensePanel.java       # Expense management panel
    ├── BudgetPanel.java        # Budget management panel
    └── ReportPanel.java        # Reports and analytics panel
```

## Data Storage

All data is stored in CSV format in the `finance_data/` directory:

```
finance_data/
├── users.csv                   # User credentials and info
├── expenses/
│   └── {username}.csv          # User's expenses
└── budgets/
    └── {username}.csv          # User's budgets
```

### CSV File Formats

**users.csv:**
```
username,password,email,createdDate
john_doe,encrypted_password,john@example.com,1234567890
```

**expenses/{username}.csv:**
```
id,date,amount,category,description
EXP_1234567890,2024-01-15,25.50,Food & Dining,Lunch at restaurant
EXP_1234567891,2024-01-16,50.00,Transportation,Taxi ride
```

**budgets/{username}.csv:**
```
id,category,amount,period,createdDate
BUD_1234567890,Food & Dining,200.00,MONTHLY,1234567890
BUD_1234567891,Transportation,100.00,WEEKLY,1234567890
```

## Compilation Instructions

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- Command-line terminal/shell

### Step 1: Create Directory Structure
```bash
mkdir -p com/finance/model
mkdir -p com/finance/util
mkdir -p com/finance/ui
```

### Step 2: Place Source Files
Place all Java files in the appropriate directories:
- `FinanceApp.java` → root directory
- `User.java`, `Expense.java`, `Budget.java` → `com/finance/model/`
- `CSVHandler.java` → `com/finance/util/`
- `LoginFrame.java`, `DashboardFrame.java`, `DashboardPanel.java`, `ExpensePanel.java`, `BudgetPanel.java`, `ReportPanel.java` → `com/finance/ui/`

### Step 3: Compile All Classes
```bash
# Compile all Java files
javac FinanceApp.java com/finance/model/*.java com/finance/util/*.java com/finance/ui/*.java
```

### Step 4: Run the Application
```bash
java FinanceApp
```

## Usage Guide

### First Time Setup

1. **Register a New Account**
   - Click "Don't have an account? Register here"
   - Enter username, email, and password
   - Click "Register"

2. **Login**
   - Enter your username and password
   - Click "Login"

### Managing Expenses

1. Navigate to **Expenses** tab
2. Fill in the expense form on the left:
   - Select date (defaults to today)
   - Enter amount
   - Choose category
   - Add description
3. Click "Add Expense"
4. View all expenses in the table on the right
5. Select an expense and click "Delete Selected" to remove it

### Setting Budgets

1. Navigate to **Budgets** tab
2. Fill in the budget form on the left:
   - Choose category
   - Enter budget amount
   - Select period (Monthly or Weekly)
3. Click "Set Budget"
4. View budgets with spending progress
5. The "Progress %" column shows how much of the budget is used

### Viewing Reports

1. Navigate to **Reports** tab
2. Select from available reports on the left:
   - **Category Distribution**: Pie chart of spending by category
   - **Monthly Trend**: 6-month spending history
   - **Budget Adherence**: Progress towards budget limits
   - **Spending by Period**: Weekly comparison
3. Color-coded visualizations help identify patterns

### Dashboard Overview

1. The **Dashboard** tab shows:
   - Total expenses (all-time)
   - Current month spending
   - Current week spending (last 7 days)
   - Total number of transactions
   - Each stat card is color-coded for quick reference

## Features in Detail

### Budget Adherence Visualization

- **Green**: 0-80% of budget used (safe)
- **Yellow**: 80-100% of budget used (caution)
- **Red**: Over 100% of budget (exceeded)

### Expense Categories

The app includes 8 predefined categories that can be easily extended by modifying the `CATEGORIES` array in `ExpensePanel.java` and `BudgetPanel.java`.

### Data Validation

- Amount fields must be positive numbers
- Passwords must match during registration
- Usernames must be unique
- Budget duplication prevents is enforced (same category + period)
- All fields are required for expense/budget entry

## Customization

### Adding New Expense Categories

Edit `ExpensePanel.java` and `BudgetPanel.java`:
```java
private final String[] CATEGORIES = {
    "Food & Dining",
    "Transportation",
    "Shopping",
    "Entertainment",
    "Utilities",
    "Health & Fitness",
    "Education",
    "Your New Category",  // Add here
    "Other"
};
```

### Changing Color Scheme

Colors are defined throughout the UI classes. Main colors:
- Primary Blue: `new Color(41, 128, 185)`
- Secondary Blue: `new Color(52, 152, 219)`
- Success Green: `new Color(46, 204, 113)`
- Danger Red: `new Color(231, 76, 60)`
- Purple: `new Color(155, 89, 182)`

### Modifying Report Types

Edit the `ReportPanel.java` class to add new report types in the `showReport()` method.

## Security Considerations

**WARNING**: The current password encryption is basic and NOT suitable for production use.

### Recommended Improvements
1. Use BCrypt for password hashing
2. Implement proper database instead of CSV
3. Add data encryption
4. Use HTTPS for API calls (if networked)
5. Implement session management
6. Add input sanitization

### Current Implementation
- Simple character shift encryption (NOT SECURE)
- CSV file storage (limited scalability)
- No API authentication

## Troubleshooting

### Issue: "Cannot find symbol" errors during compilation
**Solution**: Ensure all files are in the correct directory structure and compile with proper package names.

### Issue: Data not persisting
**Solution**: Check that `finance_data/` directory is writable. The app creates this directory automatically.

### Issue: UI elements not displaying correctly
**Solution**: Ensure JDK 11+ is installed. Try setting a different look and feel by modifying `FinanceApp.java`.

### Issue: Compilation fails with package errors
**Solution**: 
```bash
# Ensure you're compiling from the root directory
cd /path/to/project/root
javac FinanceApp.java com/finance/model/*.java com/finance/util/*.java com/finance/ui/*.java
```

## Requirements

- **JDK Version**: 11 or higher
- **Operating System**: Windows, macOS, Linux
- **RAM**: 100MB minimum
- **Disk Space**: 10MB for application + data storage
- **Dependencies**: None (uses only standard Java Swing library)

## File Size Reference

- Each user file: ~1-5KB
- Expenses CSV: Grows with usage (1 expense ≈ 100-200 bytes)
- Budgets CSV: Small (1 budget ≈ 100 bytes)

## Future Enhancement Ideas

1. **Database Integration**: Replace CSV with SQLite/MySQL
2. **Export Features**: Export reports as PDF/Excel
3. **Multi-user Dashboard**: Shared budgets and expenses
4. **Mobile App**: Java Android version
5. **Cloud Sync**: Backup to cloud storage
6. **Advanced Analytics**: Machine learning predictions
7. **Recurring Expenses**: Auto-add monthly subscriptions
8. **Receipt OCR**: Upload and scan receipts
9. **Multi-currency**: Support for different currencies
10. **Notifications**: Budget alerts and reminders

## License

This project is provided as-is for educational purposes.

## Support

For issues or questions:
1. Check the Troubleshooting section above
2. Verify the file structure and compilation steps
3. Review error messages carefully
4. Check that all Java files are in correct package directories

## Changelog

**Version 1.0** (Initial Release)
- User authentication (registration/login)
- Expense tracking with categories
- Budget management (monthly/weekly)
- Financial reports with visualizations
- Dashboard with statistics
- CSV-based data storage
- Complete UI with 6 main panels

---

**Created with Java Swing** - A robust personal finance management solution