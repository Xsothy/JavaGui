# Expense Management System

A Java Swing application for managing staff and expenses.

## Architecture

The application follows a clean architecture pattern with the following structure:

### Layers

1. **Model Layer** - Contains the data models (Staff, Expense)
2. **Repository Layer** - Handles data access to the database
3. **Controller Layer** - Contains business logic and coordinates between UI and data
4. **UI Layer** - Contains all UI components

### Key Packages

- `Model` - Contains data models/entities
- `Repository` - Contains repository classes for data access
- `Controller` - Contains controllers for business logic
- `Support` - Contains utility classes and infrastructure
- `Components` - Contains UI components for different views

### UI Component Structure

The UI is built using a component-based approach:

1. **Dashboard** - Main container with navigation
2. **Components**:
   - `StaffPanel` - Panel for displaying and managing staff
   - `StaffDetailsPanel` - Panel for displaying staff details
   - `StaffFormPanel` - Panel for adding/editing staff
   - `ExpensePanel` - Panel for displaying and managing expenses
   - `ExpenseFormPanel` - Panel for adding/editing expenses

### Navigation

The application uses a `Router` class to handle navigation between different views. The router is a singleton that registers components and routes to them based on path identifiers.

## Database

The application uses SQLite for data storage, with the following tables:

1. `staff` - Stores staff information
2. `expenses` - Stores expense information with references to staff

## Usage

1. Run the `Main` class to start the application
2. Login with valid credentials
3. Navigate between staff and expense views using the sidebar
4. Add, edit, and delete staff and expenses using the respective UI components 