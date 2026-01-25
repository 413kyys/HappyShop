# HappyShop 2.0 - E-Commerce System

This is my enhanced version of the HappyShop project. I've added proper user authentication, payment processing, better UI styling, and sound effects.

## What I Added:

- **Login System**: Users need to log in now with hashed passwords (using BCrypt)
- **Payment System**: Customers can pay with credit/debit cards or PayPal before checkout
- **Better UI**: Changed from the bright green/pink colours to a proper blue and white theme
- **Sound Effects**: Plays sounds when you login, complete orders, or get errors
- **Database Updates**: Everything saves to the database now including payments

## Running the Project

You need Java 21 and Maven installed.

### Setup Steps

1. Clone from GitHub:
```bash
git clone https://github.com/413kyys/HappyShop.git
cd HappyShop
```

2. Set up the database:
```bash
mvn compile
```
Then run `SetDatabase.java` to create all the tables.

3. Start the application:
```bash
mvn javafx:run
```

## Login Details

I created these default users you can test with:
- Admin: `admin` / `admin123`
- Customer: `customer1` / `customer123`

## Testing Payments

Use this test card: `4532015112830366`
- Expiry: `12/25`
- CVV: `123`

It's a valid test number for checking the payment system works.

## How It's Built

I used several design patterns:
- MVC for separating the interface from logic
- DAO pattern for database access
- Singleton for things like AuthenticationManager
- Strategy pattern for the different payment types

## Database Tables

- UserTable, CustomerTable, StaffTable - for login and users
- ProductTable - for the products
- TransactionTable - records all payments

## Technologies Used

- JavaFX 21
- Apache Derby database
- BCrypt for password security
- Maven for dependencies

## Author

Kristal Alem - University of Brighton - CI553 Object Oriented Development and Testing