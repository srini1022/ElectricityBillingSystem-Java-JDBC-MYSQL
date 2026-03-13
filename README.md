# ⚡ Electricity Billing System (Java + MySQL)

Hi there! 👋
This is a **desktop-based Electricity Billing System** built using **Java Swing** for the interface and **MySQL** for database management.

The goal of this project is to make electricity billing simple, organized, and digital. It helps **admins** manage customer data and generate bills easily, while **customers** can view their bills and update their details — all from one clean interface.

---

## 🧩 What You Can Do

### 👨‍💼 Admin
- Add new customers and their meter details
- Update or delete customer records (users are also removed to prevent login after deletion)
- Calculate monthly electricity bills
- View and manage all billing records

### 👤 Customer
- Sign up using a valid meter number
- Log in with a username/password
- View monthly bills
- Check billing history
- Update contact information

---

## 🧱 Tech Stack
- **Language:** Java
- **GUI Framework:** Swing & AWT
- **Database:** MySQL
- **Connector:** JDBC

---

## 🛠️ Database Setup (MySQL)
1. Start your MySQL server.
2. Create the database and tables using the provided SQL script:
   ```sql
   -- Run this in MySQL CLI or a tool like MySQL Workbench
   SOURCE database/ss.sql;
   ```
3. Verify that the following tables exist:
   - `users` (authentication data)
   - `customers` (customer profile data)
   - `meters` (meter details linked to customers)
   - `bill` (monthly bill records)
   - `tax` (rate and tax information)

> **Note:** The project assumes the MySQL database is called `nebs` and uses `root` / `root` as credentials by default. Update `src/electricity/billing/system/database.java` if you need different settings.

---

## 🚀 Running the Application

### Option A — Run from an IDE (recommended)
1. Import the project into your Java IDE (IntelliJ IDEA, Eclipse, NetBeans).
2. Ensure the project has a Java SDK set (Java 8 or higher).
3. Run the `Splash.java` class to launch the application.

### Option B — Run from the command line
1. Compile the source files:
   ```bash
   javac -d out $(find src -name "*.java")
   ```
2. Run the app:
   ```bash
   java -cp out electricity.billing.system.Splash
   ```

---

## 🗂️ Key Code Structure
- `src/electricity/billing/system/` contains most UI screens and business logic.
- `database.java` manages the JDBC connection.
- `delete_customer.java` now also removes the linked `users` record so deleted customers can no longer log in.

---

## ✅ Notes / Known Behaviors
- A customer account is linked to a `meter_id` in `users`. Deleting a customer will remove their meter record and corresponding login entry.
- If you need to change the DB credentials, update **`src/electricity/billing/system/database.java`**.

---

## 🙋‍♂️ Help / Troubleshooting
- If you see `Communications link failure`, make sure MySQL is running and accessible.
- If login fails, verify the `users` table contains the expected row and that `meter_id` matches a `meters.id`.

---

Thanks for exploring the project! ⚡
