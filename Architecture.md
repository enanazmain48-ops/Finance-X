# Finance Management Application - Architecture Documentation

## System Architecture

### Layered Architecture

```
┌─────────────────────────────────────────────────┐
│           UI Layer (Swing Components)            │
│  (LoginFrame, DashboardFrame, Panels)           │
├─────────────────────────────────────────────────┤
│           Model Layer (Data Objects)             │
│      (User, Expense, Budget Models)             │
├─────────────────────────────────────────────────┤
│           Utility Layer (Data Handler)           │
│          (CSVHandler for Persistence)           │
├─────────────────────────────────────────────────┤
│         Storage Layer (CSV Files)                │
│      (finance_data/ directory structure)        │
└─────────────────────────────────────────────────┘
```

## Module Breakdown

### 1. Entry Point
**File**: `FinanceApp.java`

```
Purpose: Application bootstrap and initialization
- Sets up Swing look and feel
- Creates initial LoginFrame
- Starts event dispatch thread
Dependencies: LoginFrame
```

### 2. Data Models (com.finance.model)

#### User.java
```
Class: User
Attributes:
  - username: String (unique identifier)
  - password: String (encrypted)
  - email: String
  - createdDate: long (timestamp)

Methods:
  - Getters/Setters for all attributes
  - toString() for logging
```

#### Expense.java
```
Class: Expense
Attributes:
  - id: String (unique, auto-generated)
  - date: LocalDate
  - amount: double
  - category: String (predefined list)
  - description: String

Methods:
  - generateId(): creates unique identifier
  - getFormattedDate(): returns yyyy-MM-dd format
  - Getters/Setters
```

#### Budget.java
```
Class: Budget
Attributes:
  - id: String (unique, auto-generated)
  - category: String
  - amount: double
  - period: String (MONTHLY or WEEKLY)
  - createdDate: long

Methods:
  - generateId(): creates unique identifier
  - Getters/Setters
```

### 3. Utility Layer (com.finance.util)

#### CSVHandler.java
```
Class: CSVHandler (Static Methods Only - Utility Class)

Directory Structure Management:
  - initializeDirectories()
    Creates: finance_data/
            finance_data/expenses/
            finance_data/budgets/

User Operations:
  - saveUser(User): boolean
  - getUser(String username): User
  - userExists(String username): boolean

Expense Operations:
  - saveExpense(String username, Expense): boolean
  - getExpenses(String username): List<Expense>
  - deleteExpense(String username, String expenseId): boolean

Budget Operations:
  - saveBudget(String username, Budget): boolean
  - getBudgets(String username): List<Budget>
  - deleteBudget(String username, String budgetId): boolean

Helper Methods:
  - parseCSVLine(String): String[]
  - sanitizeCSVField(String): String
  - encryptPassword(String): String
  - decryptPassword(String): String

File Format:
  - CSV with comma separation
  - Quoted fields for special characters
  - One record per line
  - First line is header
```

### 4. UI Layer (com.finance.ui)

#### LoginFrame.java
```
Class: LoginFrame extends JFrame

Components:
  - CardLayout for switching between login/register panels
  - Two panels: loginPanel, registerPanel

Login Panel:
  - Username field (JTextField)
  - Password field (JPasswordField)
  - Login button
  - Register link button

Register Panel:
  - Username field
  - Email field
  - Password field
  - Confirm password field
  - Register button
  - Login link button

Methods:
  - createLoginPanel(): JPanel
  - createRegisterPanel(): JPanel
  - createStyledTextField(): JTextField
  - createStyledPasswordField(): JPasswordField
  - createStyledButton(String, Color): JButton
  - handleLogin(): void
  - handleRegister(): void

Event Handling:
  - Login button: validates credentials, opens DashboardFrame
  - Register button: creates new user, saves to CSV
  - Links: toggle between login and register views
```

#### DashboardFrame.java
```
Class: DashboardFrame extends JFrame

Components:
  - BorderLayout with navigation sidebar
  - CardLayout contentPanel for multiple views

Navigation Panel:
  - Welcome label with username
  - Four navigation buttons
  - Logout button

Content Panel (CardLayout):
  - DashboardPanel (overview)
  - ExpensePanel (expense management)
  - BudgetPanel (budget management)
  - ReportPanel (analytics)

Methods:
  - createNavigationPanel(): JPanel
  - createNavButton(String, String, JPanel): JButton
  - handleLogout(): void
  - refreshContent(String): void

Data Management:
  - Passes currentUser to all child panels
  - Handles panel switching and refresh
```

#### DashboardPanel.java
```
Class: DashboardPanel extends JPanel

Display:
  - 4 statistics cards in 2x2 grid

Card Types:
  1. Total Expenses (all time)
  2. This Month (current month)
  3. This Week (last 7 days)
  4. Transactions (count)

Calculations:
  - calculateMonthExpenses(YearMonth): double
  - calculateWeekExpenses(): double

Data Source:
  - Reads from CSVHandler.getExpenses()
  - Refreshes on panel load
```

#### ExpensePanel.java
```
Class: ExpensePanel extends JPanel

Layout:
  - BorderLayout with left input form, right data table

Input Form (Left):
  - Date spinner (JSpinner)
  - Amount field (JTextField)
  - Category dropdown (JComboBox)
  - Description area (JTextArea)
  - Add button (JButton)

Data Table (Right):
  - JTable with columns: Date, Category, Amount, Description
  - Read-only table
  - Delete and Refresh buttons

Categories (8 predefined):
  - Food & Dining
  - Transportation
  - Shopping
  - Entertainment
  - Utilities
  - Health & Fitness
  - Education
  - Other

Methods:
  - createHeaderPanel(): JPanel
  - createInputPanel(): JPanel
  - createTablePanel(): JPanel
  - addExpense(...): void
  - deleteSelectedExpense(): void
  - refreshTable(): void

Validation:
  - Amount > 0
  - All fields required
  - Proper date format
```

#### BudgetPanel.java
```
Class: BudgetPanel extends JPanel

Layout:
  - Similar to ExpensePanel with form on left, table on right

Input Form (Left):
  - Category dropdown
  - Budget amount field
  - Period dropdown (MONTHLY/WEEKLY)
  - Set Budget button

Data Table (Right):
  - Columns: Category, Budget, Spent, Period, Progress %
  - Shows budget vs actual spending
  - Color-coded progress bars

Methods:
  - createHeaderPanel(): JPanel
  - createInputPanel(): JPanel
  - createTablePanel(): JPanel
  - addBudget(...): void
  - deleteSelectedBudget(): void
  - calculateSpentAmount(Budget): double
  - refreshTable(): void

Spending Calculation:
  - MONTHLY: Sum expenses in current month for category
  - WEEKLY: Sum expenses in last 7 days for category

Validation:
  - Amount > 0
  - Prevent duplicate budgets (same category + period)
```

#### ReportPanel.java
```
Class: ReportPanel extends JPanel

Layout:
  - Navigation buttons on left, chart display on right
  - Uses CardLayout for switching reports (internal)

Available Reports:
  1. Category Distribution (Pie Chart)
  2. Monthly Trend (Bar Chart)
  3. Budget Adherence (Progress Bars)
  4. Period Spending (Bar Chart)

Methods:
  - createHeaderPanel(): JPanel
  - createControlPanel(): JPanel
  - addReportButton(JPanel, String, String): void
  - showReport(String): void
  - showCategoryDistribution(): void
  - showMonthlyTrend(): void
  - showBudgetAdherence(): void
  - showPeriodSpending(): void
  - createBudgetAdherencePanel(Budget): JPanel
  - calculateSpentAmount(Budget): double

Inner Classes:

  PieChart extends JPanel
  ├─ Displays spending by category
  ├─ Colors: 8 color palette
  ├─ Shows percentage labels
  └─ Legend with amounts

  BarChart extends JPanel
  ├─ Displays comparative data
  ├─ Shows axes, grid, labels
  ├─ Color-coded bars
  └─ Value labels on bars

Visualizations:
  - Custom painted components (paintComponent override)
  - RenderingHints for smooth graphics
  - Color arrays for distinct categories
  - Labels, legend, axes
```

## Data Flow

### Registration Flow
```
User Input (LoginFrame)
    ↓
handleRegister() validates input
    ↓
CSVHandler.userExists() checks for duplicates
    ↓
User object created
    ↓
CSVHandler.saveUser() writes to users.csv
    ↓
Success message displayed
    ↓
Ready to login
```

### Login Flow
```
User Input (LoginFrame)
    ↓
handleLogin() validates input
    ↓
CSVHandler.getUser(username) retrieves user
    ↓
Password comparison (encrypted)
    ↓
Success: DashboardFrame created, LoginFrame closed
Failure: Error message, password field cleared
```

### Add Expense Flow
```
User Input (ExpensePanel)
    ↓
Form validation (amount > 0, all fields filled)
    ↓
Expense object created
    ↓
CSVHandler.saveExpense() writes to username.csv
    ↓
List reloaded from CSV
    ↓
Table refreshed
    ↓
Success message
```

### View Report Flow
```
User clicks Report button (ReportPanel)
    ↓
showReport() called
    ↓
Expense data fetched from CSVHandler
    ↓
Data processed (aggregation, filtering)
    ↓
Chart component created (PieChart or BarChart)
    ↓
Chart drawn in paintComponent()
    ↓
Display to user
```

## Database Schema (CSV Format)

### users.csv
```
username | password | email | createdDate
---------|----------|-------|-------------
john_doe | *encoded | j@e.c | 1234567890
jane_doe | *encoded | j@e.c | 1234567891
```

### expenses/{username}.csv
```
id | date | amount | category | description
---|------|--------|----------|-------------
EX_1 | 2024-01-15 | 25.50 | Food & Dining | Coffee
EX_2 | 2024-01-16 | 50.00 | Transportation | Gas
```

### budgets/{username}.csv
```
id | category | amount | period | createdDate
---|----------|--------|--------|-------------
BU_1 | Food & Dining | 300.00 | MONTHLY | 1234567890
BU_2 | Transportation | 100.00 | WEEKLY | 1234567891
```

## Design Patterns Used

### 1. **Singleton Pattern**
- CSVHandler: All methods static, no instantiation needed
- Single point of access for all file operations

### 2. **MVC Pattern**
- **Model**: User, Expense, Budget classes
- **View**: All JPanel and JFrame classes
- **Controller**: Event listeners in UI classes

### 3. **Factory Pattern**
- Styled button creation: createStyledButton()
- Styled text field creation: createStyledTextField()

### 4. **Strategy Pattern**
- Different report strategies: showCategoryDistribution(), showMonthlyTrend(), etc.
- Different chart strategies: PieChart, BarChart

### 5. **Observer Pattern**
- Swing event listeners (ActionListener, MouseListener)
- Component state changes trigger actions

## Thread Safety

- All file operations happen on Event Dispatch Thread (EDT)
- No explicit threading needed due to Swing's EDT
- CSV operations are atomic (read entire file, modify, write back)

## Error Handling

### File Operations
```java
try {
    Files.readAllLines(path);
    // Process data
} catch (IOException e) {
    e.printStackTrace();
    return null; // or false
}
```

### User Input Validation
```java
if (amount <= 0) {
    JOptionPane.showMessageDialog(this, "Error message");
    return;
}
```

### UI Feedback
- Success messages: JOptionPane.INFORMATION_MESSAGE
- Warnings: JOptionPane.WARNING_MESSAGE
- Errors: JOptionPane.ERROR_MESSAGE

## Performance Considerations

### Data Loading
- Entire file loaded into memory on each read
- Acceptable for small datasets (<10,000 records)
- Consider pagination for larger datasets

### Rendering
- Custom painting for charts (PieChart, BarChart)
- Graphics2D used for smooth rendering
- Adequate for typical screens (1080p)

### CSV Operations
- Linear search for user lookup: O(n)
- Rebuild and rewrite file for deletions
- Adequate for typical usage

## Security (Current Implementation)

### Password Encryption
```java
// Simple character shift (NOT SECURE)
(char)(c + 5)
```

### Improvements Needed
1. Use BCrypt or similar hashing algorithm
2. Add salt to password encryption
3. Implement proper authentication tokens
4. Use HTTPS if network enabled
5. Validate and sanitize all inputs

## Scalability Limitations

### Current Design
- CSV storage limits to local machine
- Single-threaded file access
- All data in memory for processing

### Growth Path
1. **Small scale** (< 100 users): Current CSV approach works
2. **Medium scale** (100-10K users): Migrate to SQLite
3. **Large scale** (10K+ users): Move to proper database (MySQL, PostgreSQL)
4. **Enterprise**: Add REST API, authentication, cloud sync

## Code Organization

### Package Structure
```
FinanceApp.java
com/
├── finance/
    ├── model/
    │   ├── User.java
    │   ├── Expense.java
    │   └── Budget.java
    ├── util/
    │   └── CSVHandler.java
    └── ui/
        ├── LoginFrame.java
        ├── DashboardFrame.java
        ├── DashboardPanel.java
        ├── ExpensePanel.java
        ├── BudgetPanel.java
        └── ReportPanel.java
```

### Class Relationships
```
FinanceApp
    └── LoginFrame
        └── User (on login success)
            └── DashboardFrame
                ├── DashboardPanel (reads Expense, Budget)
                ├── ExpensePanel (creates/deletes Expense)
                ├── BudgetPanel (creates/deletes Budget)
                └── ReportPanel (reads Expense, Budget)

All panels use CSVHandler for persistence
```

## Extension Points

### Adding New Features

**1. New Report Type**
```
- Add method showNewReport() in ReportPanel
- Create corresponding visualization component
- Add button in createControlPanel()
- Implement data aggregation logic
```

**2. New Expense Category**
```
- Add to CATEGORIES array in ExpensePanel
- Add to CATEGORIES array in BudgetPanel
- No database migration needed (CSV flexible)
```

**3. New Model Type** (e.g., Income)
```
- Create Income.java in model package
- Add static methods to CSVHandler
- Create IncomePanel for UI
- Add to DashboardFrame
```

**4. Data Export**
```
- Add export method to CSVHandler
- Implement file chooser dialog
- Support CSV, Excel, PDF formats
```

## Testing Considerations

### Unit Testing
- Model classes: Easy to test (pure data objects)
- CSVHandler: Requires test data directory
- UI classes: Difficult without mocking Swing components

### Integration Testing
- End-to-end user workflows
- File I/O operations
- Data persistence across sessions

### Manual Testing
- Test all CRUD operations
- Verify calculations (budget, spending)
- Check report accuracy
- Test edge cases (empty data, large amounts)

## Deployment

### JAR Packaging
```bash
jar cvfm FinanceApp.jar Manifest.txt \
  FinanceApp.class \
  com/finance/model/*.class \
  com/finance/util/*.class \
  com/finance/ui/*.class
```

### Distribution
- Single JAR file (no external dependencies)
- Cross-platform (Windows, macOS, Linux)
- Single-click execution

### System Requirements
- JDK 11+
- 100MB RAM minimum
- 10MB disk space
- No external dependencies

---

**This architecture provides a solid foundation for a personal finance application with clear separation of concerns and room for future enhancement.**