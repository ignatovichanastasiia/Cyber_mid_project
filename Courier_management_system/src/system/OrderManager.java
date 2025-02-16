package system;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;

public class OrderManager {

	private static ArrayList<Order> listOrdersToDo = new ArrayList<Order>();
	private static Duration limitWorkingHours = Duration.ofHours(8);

//	checkPriorityOrders() – проверка приоритетности заказов. 
//	displayAssingedOrders(Courier ID) – отображение назначенных конкретному курьеру заказов. 
//	serializeOrders() – сериализация всех заказов. - это перевел в класс Заказов.
//	deserializeOrders() – десериализация заказов. - это перевел в класс Заказов.

	private static void addOrder(Order order) {
		// пока оставил, но не вижу смысла в этом методе.
    }

	private static void distributeOrders() {
		for (Courier couriers : CourierManager.getCourierList()) {
			if (limitWorkingHours.compareTo(couriers.getWorkingHours()) >= 0) {
				System.out.println("The courier " + couriers.getId() + "expired all working hours");
			} else {OrderManager.selectOrdersWithinLimit(limitWorkingHours);
			ArrayList<Order> selectedOrders = selectOrdersWithinLimit(couriers.getWorkingHours());
          // вот здесь нам надо поженить курьеров и заказы: couriers.addOrders(selectedOrders);
			
			// Будем здесь создавать сообщение о том, что списки для каждого курьера созданы?
			}
		}
	}
	
	// Метод для сортировки и отбора заказов на основе времени загрузки и времени в пути, а также лимита
    public static ArrayList<Order> selectOrdersWithinLimit(Duration limit) {
        // Получаем текущий список заказов из геттера
        ArrayList<Order> orders = Order.getOrders();
        ArrayList<Order> sortedOrders = new ArrayList<>(orders);

        // Сортируем заказы по (время загрузки + время в пути)
        sortedOrders.sort(Comparator.comparing(order -> order.getLoadingTime().plus(order.getExpectedTime())));

        listOrdersToDo.clear(); // Очищаем список для новых отобранных заказов
        Duration totalTime = Duration.ZERO;

        // Отбираем заказы, суммарное время которых не превышает лимит
        for (Order order : sortedOrders) {
            Duration orderTime = order.getLoadingTime().plus(order.getExpectedTime());
            if (totalTime.plus(orderTime).compareTo(limit) <= 0) {
                listOrdersToDo.add(order);
                totalTime = totalTime.plus(orderTime);
            } else {
                break;
            }
        }
        return listOrdersToDo;
    }
	
	
}
