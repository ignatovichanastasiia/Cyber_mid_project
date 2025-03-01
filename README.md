## Courier Management System

### Project Overview
The Courier Management System organizes the process of distributing orders among couriers, considering various conditions such as vehicle categories, couriers' working time limits, and penalties for exceeding work hours. The program operates through three main stages:

Order and Courier Registration:

Register new orders based on vehicle category and transportation parameters.
Register couriers based on their category, working hours, possible penalties, blockages, and status changes (starting a shift).
Order Distribution:

Distribute orders among couriers, where each courier confirms or declines the order.
In case of many orders, the system balances their distribution, considering work time limits.
Each courier, after checking availability, starts the delivery.
Finalization and Reporting:

Generate reports on completed orders, considering working hours, penalties, and bonuses.
Calculate the final income of couriers and form a list of completed and uncompleted orders.
All data (orders, couriers, salaries) is saved in files using serialization.

### Main class:

#### Main Method:
The `main` method contains the primary methods that control the activation of various stages of the program's operation. The main stages are:
1. Registration of couriers and orders.
2. Assignment of orders to available couriers.
3. Generating a report on the results of the work.

#### startRegistration Method:
This method starts the first stage of the program’s operation – registration. It begins with the deserialization of existing objects, such as couriers and orders already present in the database. Following that, the menu is activated, offering various options for managing couriers, processing orders, and administrative tasks like modifying objects, printing object lists, and preparing random files for test mode.

#### startDistribution Method:
This method triggers the order distribution process, assigning orders from the database to couriers who are currently online. Couriers who receive orders must confirm their workload sequentially.

#### generateFinalReport Method:
This method initiates the third stage of the program’s operation: summarizing results and generating reports.

#### deserializationForStart Method:
This is the first logical step in the program, which involves restoring couriers and orders from the file.

#### RegistrationFirstStep Method:
The first part of the customer’s interaction with the menu.

#### RegistrationSecondStep Method:
The second part of the customer's interaction with the menu, which organizes the menu after entering the client’s role.

#### menuCourier Method:
The courier menu allows for registration and changing the status to "online," indicating readiness to receive orders.

#### menuOrders Method:
This method collects parameters for creating an order based on customer input, such as category, loading time, and travel time.

#### menuAdmin Method:
The administrator menu provides access to manage all data, including the creation of random couriers and orders for test mode.

#### takeCategory Method:
A utility method that retrieves the category parameter from customer input.

#### takeNum Method:
A utility method that retrieves a number from customer input.

#### takeAnswer Method:
A utility method that retrieves a boolean value from customer input.

#### changeCourierParam Method:
Allows the administrator to modify courier data by entering information into the console.

#### changeOrderParam Method:
Allows the administrator to modify order data by entering information into the console.

#### bufOrdersAndCouriers Method:
This method creates 10 couriers and 100 orders for program testing purposes.

#### deleteAllCouriers Method:
This method allows the administrator to delete all courier data, both in the list and in serialized files in the folder.

#### deleteAllOrders Method:
This method allows the administrator to delete all order data, both in the list and in serialized files in the folder.

---

### Courier class:

#### Courier Constructor:
The constructor takes the transportation category as an attribute. Other attributes are generated during the object creation process. Part of the ID is based on the registration date and time of the courier. Additionally, the created object is added to the list of couriers in the `CourierManager` class.

#### calculateSalary Method:
Calculates the courier’s salary, taking into account fines and bonuses.

#### addPenalty Method:
Adds a penalty to the courier's record and returns the number of penalties.

#### blockCourier Method:
Blocks the courier for 2 days due to exceeding the working hours limit.

#### serializeCouriers Method:
Serializes the static courier list to a specified file.

#### deserializeCouriers Method:
Deserializes the list of couriers from a specified file.

#### genID Method:
Generates a unique ID for the courier.

---

### CourierManager class:

#### addCourier Method:
Creates and returns a new courier using the provided category parameter.

#### changeStatusToOnline Method:
Changes a courier's status to "online," enabling them to receive orders.

#### getCourierFromID Method:
Searches for a courier by their ID and returns an `Optional<Courier>` for handling null values.

#### getCourierListOfOrdersById Method:
Returns a list of orders assigned to a specific courier by their ID.

#### assignCourierToOrder Method:
Assigns a courier to a list of orders, allowing them to accept all or some of the orders.

#### checkWorkTime Method:
Checks the courier's working hours and determines if the limit has been exceeded.

#### takeAnswer Method:
A utility method for working with client input, returning a boolean value based on the client’s response.

#### serializeCouriers Method:
Passes the responsibility of serializing couriers from the logic (main) class to the `CourierManager` class.

#### deserializeCouriers Method:
Passes the responsibility of deserializing couriers from the logic (main) class to the `CourierManager` class.

---

### Order class:

#### Order Constructor:
Initializes an order with the given parameters, generates a unique ID for the order, and adds it to the static orders list.

#### randomizeDelay Method:
Generates a random delivery delay, ranging from 0 to 59 minutes.

#### findOrderById Method:
Searches for an order by its ID and returns the order if found, or `null` otherwise.

#### serializeOrders Method:
Serializes the static list of orders to a specified file.

#### deserializeOrders Method:
Deserializes the list of orders from a specified file.

#### nullsNumber Method:
A helper method that determines the number of leading zeros required for the order ID.

---

### OrderManager class:

#### distributeOrders Method:
Distributes orders among couriers. It iterates through the list of couriers and assigns orders to those who still have available working hours. If a courier’s working hours are exhausted, a message is printed. This method calls the `selectOrdersWithinLimit` method to ensure the orders stay within the courier's working hours limit.

#### selectOrdersWithinLimit Method:
Selects orders within the given working hours limit for a courier. It first clears the list of orders, then adds priority orders and remaining orders while ensuring the total time does not exceed the limit.

#### addOrdersToListToDo Method:
Sorts and adds orders to the list of orders to be completed while ensuring the total time doesn’t exceed the courier’s working hours limit.

#### addOrdersToPriorityList Method:
Adds priority orders to the list of priority orders. If no priority orders are found, it returns `null`.

---

### Report class:

#### Report Constructor:
Creates a report object based on completed and uncompleted orders, as well as couriers.

#### calculateWorkingTimeOrder Method:
Calculates the total working time for completed orders.

#### calculateDelaysOrder Method:
Calculates the total delay time for completed orders.

#### sortingOrdersByReadyStatus Method:
Sorts orders by their completion status and updates the respective lists.

#### generateReport Method:
Generates a detailed report containing statistics about completed and uncompleted orders, total working hours, delay time, courier earnings, and completed orders.

#### serializeReports Method:
Serializes the reports list to a file.

#### formatDuration Method:
Converts a `Duration` object into a readable string format (e.g., "x hours y minutes").

#### deserializeReports Method:
Deserializes the reports list from a file.

## Interaction Flow
#### First Stage:
The user enters data for orders (transportation category, loading time, mileage) and couriers (vehicle category, working hours). This data is saved using serialization.
Orders and couriers are added to the corresponding lists (OrderManager and CourierManager).
The system verifies if the courier’s working hours meet the set limits (8, 10, and 11 hours) and applies penalties or blockages accordingly.
#### Second Stage:
Orders are distributed among couriers, who confirm or decline the orders.
In case of a large number of orders, the system automatically balances the distribution to ensure no courier exceeds 9 working hours.
If a courier does not confirm an order, it stays in the system marked as urgent (it will be moved to the next day).
#### Third Stage:
The program generates reports on completed and uncompleted orders.
If an order is not completed, a penalty is applied, and the courier may be blocked for one day.
The program calculates the courier's salary, considering penalties and bonuses for order completion.
All final data is serialized and saved.

### How It Works:
#### Interaction between classes occurs through method calls, handling:

Registration of orders and couriers
Order distribution
Calculation of penalties and bonuses
Report generation
All data is saved and retrieved from files using serialization, enabling the program to continue working with the data across sessions.
