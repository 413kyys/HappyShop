# HappyShop 2.0 - E-Commerce System

Enhanced version of the HappyShop project with user authentication, payment processing, modern UI styling, and sound effects.

## What I Added

* **Login System:** Secure authentication with BCrypt password hashing
* **Payment System:** Credit/Debit card and PayPal support with Luhn algorithm validation
* **Modern UI:** Professional blue and white theme with CSS styling
* **Sound Effects:** Audio feedback for login, orders, and errors
* **Order Management:** Real-time order tracking and processing
* **Database Integration:** Complete persistence with Apache Derby

## Running the Project

**Requirements:**
- Java 21
- Maven

**Setup Steps:**

1. Clone the repository:
```bash
git clone https://github.com/413kyys/HappyShop.git
cd HappyShop
```

2. Set up the database:
```bash
mvn compile
```
Then run `SetDatabase.java` to create all tables.

3. Start the application:
```bash
mvn javafx:run
```

Or run `Launcher.java` directly from IntelliJ.

## Login Credentials

The system has two user types with different access levels:

### Customer Account
- **Username:** `customer1`
- **Password:** `customer123`
- **Access:** Shopping interface only
- **Features:** Search products, add to basket, checkout with payment

### Staff Account
- **Username:** `admin`
- **Password:** `admin123`
- **Access:** All management interfaces
- **Features:** Warehouse management, order processing, order tracking, system controls

## User Interface by Role

### Customer View (1 window)
When logged in as a customer, you get the shopping interface where you can:
- Search products by ID
- Add items to trolley
- Complete checkout with payment
- View order receipts

### Staff View (4 windows)
When logged in as staff (admin), you get access to:
1. **Warehouse Window:** Manage inventory, add/edit/delete products, update stock levels
2. **Picker Window:** Process customer orders, mark orders as progressing/collected
3. **Order Tracker:** Monitor all orders in real-time
4. **Emergency Exit:** System shutdown control

## Testing the Payment System

Use these test credentials for payment:

**Test Card Number:** `4532015112830366`
- Expiry: `12/25`
- CVV: `123`

This is a valid test number that passes Luhn algorithm validation.

## Technical Implementation

### Design Patterns Used:
- **MVC Pattern:** Separates UI, logic, and data across all client interfaces
- **DAO Pattern:** Encapsulates database access (UserDAO, TransactionDAO, ProductDAO)
- **Singleton Pattern:** AuthenticationManager, OrderHub, SoundManager
- **Strategy Pattern:** Different payment types (CardPayment, PayPalPayment)
- **Observer Pattern:** OrderHub notifies Picker and Tracker of order changes

### Database Schema:
- **UserTable:** Stores all users with hashed passwords
- **CustomerTable:** Customer-specific data (loyalty points)
- **StaffTable:** Staff-specific data (roles)
- **ProductTable:** Product inventory
- **TransactionTable:** Payment records with timestamps

### Technologies:
- Java 21
- JavaFX 21.0.7
- Apache Derby 10.16.1.1
- BCrypt 0.4
- Maven

## Project Structure
```
HappyShop/
├── src/main/java/ci553/happyshop/
│   ├── authentication/     # Login, user management
│   ├── payment/           # Payment processing, Luhn validation
│   ├── client/            # All UI views (Customer, Warehouse, Picker, Tracker)
│   ├── storageAccess/     # Database DAOs and connection
│   ├── orderManagement/   # Order creation and tracking
│   ├── audio/             # Sound effects manager
│   └── catalogue/         # Product and order models
├── src/main/resources/
│   ├── styles/            # CSS theme files
│   ├── sounds/            # Audio files (.wav)
│   └── images/            # Product images
├── orders/                # Order file storage
│   ├── ordered/           # New orders
│   ├── progressing/       # Orders being processed
│   └── collected/         # Completed orders
├── happyShopDB/          # Apache Derby database
└── README.md
```

## Key Features

### Authentication System
- Secure password hashing with BCrypt (12 rounds)
- Role-based access control (Customer vs Staff)
- Session management with Singleton AuthenticationManager

### Payment Processing
- Multiple payment methods (Credit Card, Debit Card, PayPal)
- Luhn algorithm validation for card numbers
- Transaction recording in database
- Sound feedback on success/failure

### Order Management
- File-based order storage with state tracking
- Real-time updates to Picker and Tracker windows
- Automatic order lifecycle management
- 10-second cleanup for collected orders

### User Interface
- Modern CSS styling with professional color scheme
- Responsive layouts with JavaFX
- Consistent theme across all windows
- Loading indicators and visual feedback

## Author

**Kristal Alem**  
University of Brighton  
CI553 Object-Oriented Development and Testing  
January 2026

## Repository

GitHub: https://github.com/413kyys/HappyShop