# Courier Management System

## Project Overview
The **Courier Management System** organizes the process of distributing orders among couriers, considering various conditions such as vehicle categories, couriers' working time limits, and penalties for exceeding work hours. The program operates through three main stages:

1. **Order and Courier Registration**: 
   - Register new orders based on vehicle category and transportation parameters.
   - Register couriers based on their category, working hours, possible penalties, blockages, and status changes (starting a shift).

2. **Order Distribution**: 
   - Distribute orders among couriers, where each courier confirms or declines the order.
   - In case of many orders, the system balances their distribution, considering work time limits.
   - Each courier, after checking availability, starts the delivery.

3. **Finalization and Reporting**: 
   - Generate reports on completed orders, considering working hours, penalties, and bonuses.
   - Calculate the final income of couriers and form a list of completed and uncompleted orders.
   - All data (orders, couriers, salaries) is saved in files using serialization.

---

## Classes and Methods

### 1. **Order (Order)**
   **Attributes**:
   - `id`: Order identifier
   - `category`: Transportation category
   - `loadingTime`: Time of loading
   - `expectedTime`: Expected travel time
   - `urgency`: Urgency level of the order
   - `status`: Status of the order completion

   **Methods**:
   - `calculateTravelTime()`: Calculates travel time based on the vehicle category.
   - `setStatus()`: Sets the status of the order.
   - `serialize()`: Serializes the order object.
   - `deserialize()`: Deserializes the order from a file.

### 2. **Courier (Courier)**
   **Attributes**:
   - `id`: Courier identifier
   - `category`: Vehicle category
   - `workingHours`: Courier's working hours
   - `penalty`: Any penalties imposed
   - `blockStatus`: Blockage status (whether the courier is blocked)

   **Methods**:
   - `confirmOrder()`: Confirms the acceptance of an order.
   - `calculateSalary()`: Calculates salary considering penalties and bonuses.
   - `addPenalty()`: Adds a penalty to the courier.
   - `checkForPenalty()`: Checks if any penalties or blockages apply.
   - `blockCourier()`: Blocks the courier from accepting new orders.
   - `serialize()`: Serializes courier data.
   - `deserialize()`: Deserializes courier data from a file.

### 3. **OrderManager (Order Manager)**
   **Attributes**:
   - `ordersList`: List of orders
   
   **Methods**:
   - `addOrder()`: Adds a new order to the list.
   - `distributeOrders()`: Distributes orders among available couriers.
   - `checkUrgencyOrders()`: Checks for urgent orders.
   - `serializeOrders()`: Serializes all orders.
   - `deserializeOrders()`: Deserializes orders from a file.

### 4. **CourierManager (Courier Manager)**
   **Attributes**:
   - `courierList`: List of couriers

   **Methods**:
   - `addCourier()`: Adds a new courier.
   - `assignCourierToOrder()`: Assigns a courier to an order.
   - `checkWorkTime()`: Verifies if the courier is within the permissible working hours.
   - `serializeCouriers()`: Serializes all couriers.
   - `deserializeCouriers()`: Deserializes couriers from a file.
   - `editWorkTime()`: Automatically adjusts the remaining working time when a courier receives an order.

### 5. **Report (Report)**
   **Attributes**:
   - `completedOrders`: List of completed orders
   - `uncompletedOrders`: List of uncompleted orders
   - `workingTime`: Courier working time
   - `penalties`: Penalties applied
   - `premiums`: Bonuses or premiums applied

   **Methods**:
   - `generateReport()`: Generates a final report on completed and uncompleted orders.
   - `serializeReport()`: Serializes the report.
   - `deserializeReport()`: Deserializes the report from a file.

### 6. **Main (Main Program Class)**
   **Methods**:
   - `startRegistration()`: Starts the first stage (registration of orders and couriers).
   - `startDistribution()`: Starts the second stage (order distribution).
   - `generateFinalReport()`: Starts the third stage (finalization and report generation).

---

## Interaction Flow

### First Stage:
- The user enters data for orders (transportation category, loading time, mileage) and couriers (vehicle category, working hours). This data is saved using serialization.
- Orders and couriers are added to the corresponding lists (`OrderManager` and `CourierManager`).
- The system verifies if the courier’s working hours meet the set limits (8, 10, and 11 hours) and applies penalties or blockages accordingly.

### Second Stage:
- Orders are distributed among couriers, who confirm or decline the orders.
- In case of a large number of orders, the system automatically balances the distribution to ensure no courier exceeds 9 working hours.
- If a courier does not confirm an order, it stays in the system marked as urgent (it will be moved to the next day).

### Third Stage:
- The program generates reports on completed and uncompleted orders.
- If an order is not completed, a penalty is applied, and the courier may be blocked for one day.
- The program calculates the courier's salary, considering penalties and bonuses for order completion.
- All final data is serialized and saved. 

---

## How It Works:
Interaction between classes occurs through method calls, handling:
- Registration of orders and couriers
- Order distribution
- Calculation of penalties and bonuses
- Report generation

All data is saved and retrieved from files using serialization, enabling the program to continue working with the data across sessions.
