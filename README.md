# Cyber_mid_project
Project Name: Courier Management System
Brief Description of Functionality: The program organizes the process of distributing orders among couriers, considering various conditions such as vehicle categories, couriers' working time limits, and penalties for exceeding work hours. The program goes through three main stages during operation:
1.	Order and Courier Registration: Registration of new orders based on vehicle category and transportation parameters. Registration of couriers based on their category, working hours, possible penalties, and blockages, as well as status changes (starting a shift).
2.	Order Distribution: Orders are distributed among couriers, where each courier confirms or declines the order. If there are many orders, the system tries to balance their distribution, considering work time limits. Each courier, after checking availability, starts the delivery.
3.	Finalization and Reporting: Reports on completed orders are generated, considering working hours, penalties, and bonuses. The final income of couriers is calculated, and a list of completed and uncompleted orders is formed. All data (orders, couriers, salaries) are saved in files using serialization.
List of Classes and Methods:
Classes:
1.	Order (Order)
o	Attributes:
	id (order identifier)
	category (transportation category)
	loadingTime (loading time)
	expectedTime (expected travel time)
	urgency (urgency)
	status (status of completion)
o	Methods:
	calculateTravelTime() – calculates travel time based on vehicle category.
	setStatus() – sets the status of the order.
	serialize() – serializes the order object.
	deserialize() – deserializes the order from a file.
2.	Courier (Courier)
o	Attributes:
	id (courier identifier)
	category (vehicle category)
	workingHours (working hours)
	penalty (penalties)
	blockStatus (blockage status)
o	Methods:
	confirmOrder() – confirms acceptance of an order.
	calculateSalary() – calculates salary including penalties and bonuses.
	addPenalty() – adds a penalty.
	checkForPenalty() – checks for penalties or blockage conditions.
	blockCourier() – blocks the courier.
	serialize() – serializes courier data.
	deserialize() – deserializes courier data from a file.
3.	OrderManager (Order Manager)
o	Attributes:
	ordersList (list of orders)
o	Methods:
	addOrder() – adds a new order.
	distributeOrders() – distributes orders among couriers.
	checkUrgencyOrders() – checks for urgent orders.
	serializeOrders() – serializes all orders.
	deserializeOrders() – deserializes orders.
4.	CourierManager (Courier Manager)
o	Attributes:
	courierList (list of couriers)
o	Methods:
	addCourier() – adds a new courier.
	assignCourierToOrder() – assigns a courier to an order.
	checkWorkTime() – checks courier's working hours.
	serializeCouriers() – serializes all couriers.
	deserializeCouriers() – deserializes couriers.
	editWorkTime() – automatically adjusts the remaining working time of the courier when receiving an order.
5.	Report (Report)
o	Attributes:
	completedOrders (list of completed orders)
	uncompletedOrders (list of uncompleted orders)
	workingTime (working time)
	penalties (penalties)
	premiums (bonuses)
o	Methods:
	generateReport() – generates a final report.
	serializeReport() – serializes the report.
	deserializeReport() – deserializes the report.
6.	Main (Main Program Class)
o	Methods:
	startRegistration() – starts the first stage (registration of orders and couriers).
	startDistribution() – starts the second stage (order distribution).
	generateFinalReport() – starts the third stage (finalization and report generation).
Interaction Features:
•	First Stage:
o	The user inputs data about orders (transportation category, loading time, mileage (the program calculates travel time)) and couriers (category, working hours). This data is saved to files using serialization.
o	Orders and couriers are added to the corresponding lists (OrderManager and CourierManager).
o	The system checks whether the courier’s working hours meet the set limits (8, 10, and 11 hours) and applies penalties or blockages.
•	Second Stage:
o	Orders are distributed among couriers, who confirm or decline them.
o	In case of many orders, the system automatically distributes them among couriers, ensuring that no courier works more than 9 hours.
o	If a courier does not confirm an order, it remains in the system marked as urgent (it will carry over to the next day).
•	Third Stage:
o	The program gathers reports on completed and uncompleted orders.
o	If an order is not completed, a penalty is applied, and the courier may be blocked for one day.
o	The program calculates the courier's salary, including penalties and bonuses for completing orders.
o	All final data is serialized and saved. Interaction between classes occurs through method calls, which handle the registration of orders and couriers, their distribution, penalty and bonus calculation, and report generation. All information is saved and retrieved from files using serialization, allowing the program to continue working with data across sessions.

