package system;
//by Ivan
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;

public class OrderManager {

	private static ArrayList<Order> listOrdersToDo = new ArrayList<Order>();
	private static ArrayList<Order> priorityOrders = new ArrayList<Order>();
	private static Duration limitWorkingHours = Duration.ofHours(8);

//	displayAssingedOrders(Courier ID) – отображение назначенных конкретному курьеру заказов. 


	private static void addOrder(Order order) {
		// пока оставил, но не вижу смысла в этом методе.
    }

	/**
	 * This method distributes orders among couriers. It iterates through
     * the list of couriers and assigns orders to those who still have available
     * working hours. If a courier's working hours are exhausted, it prints a
     * message. It calls the selectOrdersWithinLimit method to select orders within
     * the courier's working hours limit.
	 */
	private static void distributeOrders() {
	    for (Courier courier : CourierManager.getCourierList()) {
	        // Проверяет, истекли ли рабочие часы курьера
	        if (courier.getWorkingHours().compareTo(Duration.ZERO) <= 0) { //!!!!!!
	            System.out.println("The courier " + courier.getId() + " expired all working hours");
	        } else {
	            // Выбор заказов в пределах оставшихся рабочих часов курьера
	            ArrayList<Order> selectedOrders = selectOrdersWithinLimit(courier.getWorkingHours());

	            // Добавление заказов курьеру
	           // courier.addOrders(selectedOrders);

	            // Сообщение о том, что заказы были назначены курьеру
	            System.out.println("Orders have been assigned to courier " + courier.getId());
	        }
	    }
	}

	
	/**
	 * This method selects orders within the given time limit. It first
     * clears the listOrdersToDo, then adds priority orders and remaining orders to
     * the list. It calls the addOrdersToListToDo method to add orders while
     * ensuring the total time does not exceed the limit.
	 * @param limit - courier's working hours left 
	 * @return ArrayList <Order> listOrdersToDo 
	 */
	public static ArrayList<Order> selectOrdersWithinLimit(Duration limit) { 
	    listOrdersToDo.clear(); // Очищаем список для новых отобранных заказов
	    Duration totalTime = Duration.ZERO;
	    // Добавляем приоритетные заказы
	    totalTime = addOrdersToListToDo(priorityOrders, totalTime, limit);
	    // Добавляем оставшиеся заказы
	    addOrdersToListToDo(Order.getOrders(), totalTime, limit);
	    return listOrdersToDo;
	}

	/**
	 * This helper method sorts the given list of orders based on the
     * sum of loading time and expected time. It then adds orders to the
     * listOrdersToDo if the total time including the order's time does not exceed
     * the limit. It returns the updated total time.
	 * @param orders
	 * @param totalTime
	 * @param limit
	 * @return totalTime
	 */
	private static Duration addOrdersToListToDo(ArrayList<Order> orders, Duration totalTime, Duration limit) {
	    ArrayList<Order> sortedOrders = new ArrayList<>(orders);
	    sortedOrders.sort(Comparator.comparing(order -> order.getLoadingTime().plus(order.getExpectedTime())));
	    for (Order order : sortedOrders) {
	        Duration orderTime = order.getLoadingTime().plus(order.getExpectedTime());
	        if (totalTime.plus(orderTime).compareTo(limit) <= 0) {
	            listOrdersToDo.add(order);
	            totalTime = totalTime.plus(orderTime);
	        } else {
	            break;
	        }
	    }
	    return totalTime;
	}

	/**
	 * This method adds priority orders to the priorityOrders list. It
     * iterates through the list of orders and adds those marked as priority. If no
     * priority orders are found, it prints a message and returns null. Otherwise,
     * it returns the priorityOrders list.
	 * @return priorityOrders or null;
	 */
    public static ArrayList <Order> addOrdersToPriorityList() {
    	for (Order order: Order.getOrders()) {
    		if (order.isPriority()){
    			priorityOrders.add(order);
    			} 
    		}
    	if (priorityOrders.isEmpty()) {
    		System.out.println("There's no priority orders left");
    		return null; // необходимо будет учесть в клиентском коде проверку на нулл.
    	} return priorityOrders;
    } 
    
}
	

