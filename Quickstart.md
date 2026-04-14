# Quick Start Guide - Finance Management Application

## 5-Minute Setup

### Step 1: Compile the Application

**On Windows:**
1. Open Command Prompt
2. Navigate to the project directory
3. Run: `compile.bat`

**On macOS/Linux:**
1. Open Terminal
2. Navigate to the project directory
3. Run: `bash compile.sh`

**Manual Compilation (All Platforms):**
```bash
mkdir -p com/finance/model com/finance/util com/finance/ui
javac FinanceApp.java com/finance/model/*.java com/finance/util/*.java com/finance/ui/*.java
```

### Step 2: Run the Application
```bash
java FinanceApp
```

The login window should appear!

---

## Usage Walkthrough

### Registration (First Time)

1. Click **"Don't have an account? Register here"**
2. Fill in the form:
   ```
   Username: john_doe
   Email: john@example.com
   Password: MyPassword123
   Confirm Password: MyPassword123
   ```
3. Click **Register**
4. You should see "Registration successful!"

### Login

1. Enter your credentials:
   ```
   Username: john_doe
   Password: MyPassword123
   ```
2. Click **Login**
3. Dashboard will appear

---

## First Transactions - Step by Step

### Adding Your First Expense

1. Click **Expenses** in the left menu
2. You'll see the expense form on the left panel

**Example 1: Coffee Purchase**
- Date: *Today's date (auto-selected)*
- Amount: `5.50`
- Category: *Select "Food & Dining"*
- Description: `Morning coffee at cafe`
- Click **Add Expense**

**Example 2: Gas**
- Date: *Today's date*
- Amount: `45.00`
- Category: *Select "Transportation"*
- Description: `Gas fill-up`
- Click **Add Expense**

**Example 3: Movie**
- Date: *Today's date*
- Amount: `12.00`
- Category: *Select "Entertainment"*
- Description: `Movie tickets`
- Click **Add Expense**

You should now see three expenses in the table on the right!

---

### Setting Up Your First Budget

1. Click **Budgets** in the left menu
2. Fill in the budget form:

**Budget Example 1: Monthly Food Budget**
- Category: `Food & Dining`
- Budget Amount: `300.00`
- Period: `MONTHLY`
- Click **Set Budget**

**Budget Example 2: Weekly Transport**
- Category: `Transportation`
- Budget Amount: `50.00`
- Period: `WEEKLY`
- Click **Set Budget**

You'll see your budgets listed with progress:
- "Spent" shows current spending in this period
- "Progress %" shows percentage of budget used
- Green indicates you're within budget
- Yellow indicates caution (80-100%)
- Red indicates you've exceeded budget

---

### Viewing Your Dashboard

Click **Dashboard** to see:
- **Total Expenses**: Sum of all your expenses
- **This Month**: Current month's total
- **This Week**: Last 7 days' total
- **Transactions**: Total number of expenses logged

Each card is color-coded:
- **Red**: Total of all expenses
- **Blue**: Monthly spending
- **Green**: Weekly spending
- **Purple**: Transaction count

---

### Generating Reports

Click **Reports** to analyze your spending.

#### Available Reports:

1. **Category Distribution** (Pie Chart)
   - Shows spending breakdown by category
   - First report loaded by default
   - Example: If you spent $5.50 on food, $45 on gas, $12 on entertainment
   - Shows: Food 7%, Transportation 57%, Entertainment 15%

2. **Monthly Trend** (Bar Chart)
   - Shows last 6 months of spending
   - Helpful to see if spending is increasing/decreasing
   - Example: January $200, February $250, March $300

3. **Budget Adherence** (Progress Bars)
   - Shows how much of each budget you've used
   - Color-coded by percentage
   - Budget: $300, Spent: $80 = 26.7% used (Green)
   - Budget: $300, Spent: $240 = 80% used (Yellow)
   - Budget: $300, Spent: $350 = 116% used (Red - Over budget!)

4. **Spending by Period** (Bar Chart)
   - Compares this week vs. last week
   - Quick way to see if spending is trending up/down

---

## Common Tasks

### Delete an Expense
1. Go to **Expenses** tab
2. Click on the expense in the table to select it
3. Click **Delete Selected**
4. Confirm the deletion

### Modify a Budget
1. Go to **Budgets** tab
2. Delete the old budget: Select it and click **Delete Selected**
3. Set a new budget with the updated amount

### Clear All Data
- Delete the `finance_data/` folder from the project directory
- Restart the application
- Create a new account

### Export Data (Manual)
- Find the files in `finance_data/`
- Copy and open with any spreadsheet application
- Files are readable CSV format

---

## Troubleshooting

### "Cannot find symbol" Error
- Make sure directory structure is created:
  ```
  com/finance/model/
  com/finance/util/
  com/finance/ui/
  ```
- All .java files should be in their respective folders

### "Invalid username or password" at Login
- Double-check spelling and capitalization
- Password is case-sensitive
- Ensure account was registered successfully

### Expenses/Budgets Not Showing
- Click **Refresh** button on the panel
- Check that entries were actually saved (you saw the success message)
- Verify the `finance_data/` folder was created

### UI Text Cut Off
- Try resizing the window
- Restart the application
- Try a different JDK version (11+ recommended)

### Cannot Add Expense with Error
- Ensure amount is a valid number (e.g., 25.50, not 25,50)
- Check that you didn't leave any required fields blank
- Try refreshing the page

---

## Tips & Tricks

### Best Practices

1. **Regular Updates**
   - Log expenses as they happen for accuracy
   - Update daily or weekly

2. **Category Organization**
   - Use consistent category names
   - Group similar items

3. **Budget Setting**
   - Start with monthly budgets
   - Adjust based on actual spending
   - Use weekly budgets for flexible items

4. **Report Review**
   - Check category distribution monthly
   - Use monthly trend to spot patterns
   - Watch for budget warnings

### Keyboard Shortcuts
- **Tab**: Move to next field
- **Enter**: Submit form or select table row
- **Delete**: Clear field content

### Data Export
All data is stored as CSV in `finance_data/` folder:
- Open with Excel, Google Sheets, or any text editor
- Can be imported into other financial tools
- Back up regularly

---

## Example Scenarios

### Scenario 1: Track Monthly Expenses
```
Week 1:
- Food: $75 (groceries)
- Transportation: $25 (gas)
- Entertainment: $20 (movie)

Week 2:
- Food: $85 (groceries + dining)
- Shopping: $100 (clothes)

Check Dashboard: Total spent = $305
Check Reports: Category Distribution shows breakdown
Check Budgets: See if you're within $300/month food budget (No, you're over!)
```

### Scenario 2: Optimize Spending
```
1. Add all expenses for 2 weeks
2. Go to Reports → Category Distribution
3. Identify highest spending category
4. Set a budget for that category
5. Track daily against that budget
6. Adjust spending to stay within budget
```

### Scenario 3: Weekly vs Monthly Tracking
```
Monday: Gas $50 (transportation)
Wednesday: Groceries $75 (food)
Friday: Dinner out $35 (food)

Set Weekly Transportation Budget: $50
Set Monthly Food Budget: $300

See which is easier to track
Use whichever works better for your lifestyle
```

---

## Default Test Credentials

If you want to test the app, use these:
```
Username: testuser
Email: test@example.com
Password: test123
```

---

## Next Steps

1. ✓ Install and run the app
2. ✓ Create your account
3. ✓ Add some sample expenses
4. ✓ Set budgets for your categories
5. ✓ Check the reports
6. ✓ Plan your actual budget
7. ✓ Start tracking real expenses

---

## Need Help?

- Check the **README.md** for detailed documentation
- Review the **Troubleshooting** section above
- Check source code comments for implementation details
- Verify file structure matches the directory layout

**Happy budgeting!** 💰