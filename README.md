# TrackIt: Budget Management Console Application

**TrackIt** is a budgeting and expense management console application that helps users manage their categories, monthly income, budgets, transactions, 
and overall financial report. This Java-based application provides features like:

- Managing categories
- Tracking monthly income
- Setting and editing budgets
- Adding and editing transactions
- Viewing monthly reports
- Handling date changes

## Features

- **Category Management**: Allows users to create, edit, and delete categories.
- **Income Management**: Users can manage and update their monthly income.
- **Budget Management**: Allows users to set and edit budgets for various categories. Warnings are shown if a transaction exceeds the budget.
- **Transaction Management**: Users can add, edit, and delete transactions, with warnings for exceeding budget limits.
- **Reports**: Provides a summary of monthly transactions and a complete financial overview.

## Installation

To run this application, you need Java 8 or later installed on your machine.

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/trackit.git
2. Navigate to the project directory:

cd trackit

3. Compile the project using Java:

javac TrackIt.java

4. Run the application:

java TrackIt

## Usage

Once the program starts, the user will be presented with a menu offering various financial management options. The user can navigate through the menu 
to perform actions like managing categories, adding transactions, and viewing reports.Main Menu Options:

    1. Manage Categories: View, add, edit, or delete categories.
    2. Keep Track of Monthly Income: View or update the current monthly income.
    3. Manage Budgets: Set and manage budgets for different categories.
    4. Manage Transactions: Add, edit, or delete transactions for the current month.
    5. Get An Overall Monthly View: View a summary of all transactions and budgets for the current month.
    6. Change Date: Change the current month and year for the financial management.
    7. Exit: Exit the application.

## Technologies Used

    -Java 
    -File Storage: Transactions and budgets are stored in text files and CSV files for simplicity.
    -Data Handling: The application handles the current month's transactions, budgets, and income, with the option to change the date as needed.
    
