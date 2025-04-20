
# 💸 Trading App

A simple desktop trading application built in Java using Swing. Users can register, log in, trade assets (buy/sell), track their portfolio, and view live market prices. The app includes an order book system for matching trades and uses the observer pattern to update balances and record transactions in real-time.

---

## 📦 Features

- 🔐 **User Registration & Login**  
  Register new accounts and log in securely with credentials.

- 💰 **Trading (Buy/Sell Assets)**  
  Place market and limit orders. Orders are matched via an order book for each asset.

- 📈 **Live Market View**  
  View available assets and their current prices.

- 💼 **User Portfolio Management**  
  Track owned assets, available cash balance, and total portfolio value.

- 🔄 **Order Book & Trade Matching**  
  A complete order book system to store, match, and execute trades.

- 🧪 **In-Memory Database with DI**  
  Easy switching between in-memory or persistent DB via dependency injection.

- 🧠 **Observer Pattern**  
  Automatically updates user balance and transaction history when orders are filled.

---

## 🧱 Models Overview

| Model                | Description |
|---------------------|-------------|
| `User`              | Represents a registered user. Has a portfolio and credentials. |
| `Portfolio`         | Holds user's cash balance and list of `PortfolioAsset`s. |
| `Asset`             | Abstract base for all tradable assets. Includes ID, price, name, and market cap. |
| `CryptoCoin`        | Extends `Asset`, includes creator and country. |
| `Stock`             | Extends `Asset`, includes company name and stock ticker. |
| `PortfolioAsset`    | Subclass of `Asset` owned by users, tracks quantity and portfolio ID. |
| `Transaction`       | Represents a completed trade (buy/sell) with price, quantity, and timestamp. |

---

## 📚 Order Book Models

| Model                | Description |
|---------------------|-------------|
| `Order`             | Represents a buy/sell request. Contains user ID, asset ID, type, quantity, price, etc. |
| `Limit`             | Represents a price level in the order book. Holds multiple orders at the same price. |
| `LimitLinkedList`   | A doubly-linked list of `Limit`s (price levels) for efficient matching and ordering. |
| `OrderBook`         | Contains `LimitLinkedList`s for buy and sell orders for a specific asset. |
| `MatchingOrderBook` | Core matching engine that processes and matches buy/sell orders automatically. |

---

## 👁️ Observer Pattern

| Observer            | Description |
|---------------------|-------------|
| `BalanceUpdater`    | Listens to order execution events and updates user's cash balance accordingly. |
| `TransactionUpdater`| Adds a transaction record to the database whenever an order is filled. |

These observers are registered to the `MatchingOrderBook` and triggered automatically when trades are matched.

---

## 🚀 App Functionalities

### 1. **Registration & Login**
- Register with username, email, and password.
- Log in to access trading features.
- Logged-in user is tracked via `userRepo.getCurrentUser()`.

### 2. **Market View**
- Lists all available assets.
- Sorted by market cap or price.
- Data refreshed every time the tab is opened.

### 3. **Trading**
- Select an asset from the dropdown.
- Enter quantity and price per unit.
- Choose to **Buy** or **Sell**.
- Orders are added to the `OrderBook` and matched via `MatchingOrderBook`.

### 4. **Portfolio Management**
- Displays:
  - Asset name
  - Quantity
  - Price per unit
  - Total value
- Above the table:
  - USD balance left
  - Total net worth (cash + assets)
- Updated dynamically every time the tab is visited.

### 5. **Order Matching**
- Supports limit orders with price levels.
- Matching engine uses price-time priority.
- Trades execute partially or fully depending on availability.

### 6. **Data Observers**
- Balance and transaction history update automatically via observer pattern.
- Observers decoupled from matching logic for better scalability.

---

## 🛠 Technologies Used

- Java
- Java Swing
- MVC architecture
- Observer Design Pattern
- Repository Pattern
- Custom in-memory database (pluggable)

---

## 📌 Future Improvements

- Persist data using SQL or NoSQL databases.
- Add support for market orders and advanced trading types.
- Implement real-time price feed integration.
- Add historical charting and analytics.
- Include a transaction history tab and order tracking.

---

## 🧪 Development Notes

- `SeedData()` is used to initialize sample assets and users.
- All tabs except Register/Login are refreshed on each open for real-time accuracy.
- Avoids circular dependencies using interfaces and observer callbacks.

---

## 👨‍💻 Kononov Daniil

Created as a learning project to simulate real-world trading systems and UI development with Java.
