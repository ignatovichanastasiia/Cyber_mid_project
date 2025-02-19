package system;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;

public class OrderManager {

	private static ArrayList<Order> listOrdersToDo = new ArrayList<Order>();
	private static ArrayList<Order> priorityOrders = new ArrayList<Order>();
	private static final Duration LIMIT_WORKING_HOURS = Duration.ofHours(8);

	// displayAssingedOrders(Courier ID) – отображение назначенных конкретному

    /**
     * Distributes orders among couriers. It iterates through the list of couriers and 
     * assigns orders to those who still have available working hours. If a courier's 
     * working hours are exhausted, it prints a message. Calls selectOrdersWithinLimit 
     * method to select orders within the courier's working hours limit.
     */
	public static void distributeOrders() {
	    for (Courier courier : CourierManager.getCourierList()) {
	        if (!courier.isOnlineStatus()) {
	            System.out.println("The courier " + courier.getId() + " is offline");
	        } else {
	            // Выбор заказов в пределах оставшихся рабочих часов курьера
	            selectOrdersWithinLimit(LIMIT_WORKING_HOURS);
	            // Назначение курьера на выбранные заказы
	            CourierManager.assignCourierToOrder(courier.getId(), listOrdersToDo);
	            System.out.println("Orders have been assigned to courier " + courier.getId());

	            // Помечаем назначенные заказы как принятые
	            for (Order order : listOrdersToDo) {
	                order.setStatusAccepted(true);
	            }
	        }
	    }
	}


	/**
     * Selects orders within the given time limit. First clears listOrdersToDo, then adds 
     * priority orders and remaining orders to the list. Calls addOrdersToListToDo method 
     * to add orders while ensuring the total time does not exceed the limit.
     *
     * @param limit - the remaining working hours of the courier
     * @return ArrayList<Order> - list of selected orders
     */
	public static ArrayList<Order> selectOrdersWithinLimit(Duration limit) {
	    listOrdersToDo.clear(); // Очищаем список для новых отобранных заказов
	    Duration totalTime = Duration.ZERO;
	    // Добавляем приоритетные заказы
	    totalTime = addOrdersToListToDo(priorityOrders, totalTime, limit);
	    // Добавляем оставшиеся заказы
	    for (Order order : Order.getOrders()) {
	        if (!order.isStatusAccepted()) { // Проверяем статус заказа
	        	Duration orderTime = order.getLoadingTime().plus(order.getTravelTime());
	            if (totalTime.plus(orderTime).compareTo(limit) <= 0) {
	                listOrdersToDo.add(order);
	                totalTime = totalTime.plus(orderTime);
	            }
	        }
	    }
	    return listOrdersToDo;
	}

	 /**
     * Helper method that sorts the given list of orders based on the sum of loading time 
     * and expected time. Adds orders to listOrdersToDo if the total time including the 
     * order's time does not exceed the limit. Returns the updated total time.
     *
     * @param orders - list of orders to sort and add
     * @param totalTime - current total time of completing orders
     * @param limit - limit of time for completing orders
     * @return totalTime - updated total time of completing orders
     */
	private static Duration addOrdersToListToDo(ArrayList<Order> orders, Duration totalTime, Duration limit) {
		ArrayList<Order> sortedOrders = new ArrayList<>(orders);
		sortedOrders.sort(Comparator.comparing(order -> order.getLoadingTime().plus(order.getTravelTime())));
		for (Order order : sortedOrders) {
			Duration orderTime = order.getLoadingTime().plus(order.getTravelTime());
			if (totalTime.plus(orderTime).compareTo(limit) <= 0 && !order.isStatusAccepted()) {
				listOrdersToDo.add(order);
				totalTime = totalTime.plus(orderTime);
			} else {
				break;
			}
		}
		return totalTime;
	}

	/**
     * Adds priority orders to the priorityOrders list. Iterates through the list of 
     * orders and adds those marked as priority. If no priority orders are found, prints 
     * a message and returns null. Otherwise, returns the priorityOrders list.
     *
     * @return priorityOrders or null - list of priority orders or null if no such orders exist
     */
	public static ArrayList<Order> addOrdersToPriorityList() {
		for (Order order : Order.getOrders()) {
			if (order.isPriority()) {
				priorityOrders.add(order);
			}
		}
		if (priorityOrders.isEmpty()) {
			System.out.println("There's no priority orders left");
			return null; // необходимо будет учесть в клиентском коде проверку на нулл.
		}
		return priorityOrders;
	}

}