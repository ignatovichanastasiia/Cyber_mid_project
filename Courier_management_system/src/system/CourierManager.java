package system;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CourierManager {
	private static final int MAX_WORK_HOURS = 11;
	private static final int NORMAL_WORK_HOURS = 8;
	private static ArrayList<Courier> courierList = new ArrayList<Courier>();
	private static Map<String, ArrayList<Order>> courierOrders = new HashMap();
	private static boolean normal;

	// Добавление нового курьера - метод символически поддерживает логику.
	public static void addCourier(Courier courier) {
		if (!courierList.contains(courier)) {
			courierList.add(courier);
		}
	}

	// Назначение курьера на заказ.
	// ВНИМАНИЕ! У КОГО ЛИСТЫ С ЗАКАЗАМИ???
	public static void assignCourierToOrder(String id, ArrayList<Order> orders) {
		if (courierOrders.isEmpty()) {
			courierList.forEach(c -> {
				courierOrders.put(c.getId(), new ArrayList<Order>());

			});
		}
		ArrayList<Order> newOne = new ArrayList(orders);
		courierOrders.put(id, newOne);
	}

	//  checkWorkTime() – проверка времени работы курьера.
	public static boolean checkWorkTime(String id) {
		normal = true;
		courierList.forEach(c -> {
			if(c.getId().equals(id)){
				Duration duration = c.getWorkingHours();
				if(duration.toHours()>NORMAL_WORK_HOURS) {
					c.addPenalty();
					normal = false;
				}
				if(duration.toHours()>MAX_WORK_HOURS) {
					c.blockCourier();
				}
			}
		});
		return normal;
	}

	//  serializeCouriers() – сериализация всех курьеров.
	public static void serializeCouriers() {
		courierList.forEach(c -> {
			c.serialize();
		});
	}

	//  deserializeCouriers() – десериализация курьеров.
	public static void deserializeCouriers() {
		courierList.forEach(c -> {
			c.deserialize();
		});
	}

	public static ArrayList<Courier> getCourierList() {
		return courierList;
	}

	public static void setCourierList(ArrayList<Courier> courierList) {
		CourierManager.courierList = courierList;
	}

	public static Map<String, ArrayList<Order>> getCourierOrders() {
		return courierOrders;
	}

	public static void setCourierOrders(Map<String, ArrayList<Order>> courierOrders) {
		CourierManager.courierOrders = courierOrders;
	}
	
	

}

//o	Атрибуты: 
//	courierList (список курьеров)
//o	Методы: 
//	addCourier() – добавление нового курьера.
//	assignCourierToOrder() – назначение курьера на заказ.
//	checkWorkTime() – проверка времени работы курьера.
//	serializeCouriers() – сериализация всех курьеров.
//	deserializeCouriers() – десериализация курьеров.
