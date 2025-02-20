package system;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class CourierManager {
	private static final int MAX_WORK_HOURS = 11;
	private static final int NORMAL_WORK_HOURS = 8;
	private static ArrayList<Courier> courierList = new ArrayList<Courier>();
	private static Map<String, ArrayList<Order>> courierOrders = new HashMap();
	private static boolean normal;
	private static boolean done;
	public static Scanner sc;

	// Добавление нового курьера
	public static Courier addCourier(String category) {
		System.out.println("Creating new Courier.");
		Courier courier = new Courier(category);
		return courier;
	}

	// изменение статуса - выход на линию. Разрешение на распределение заказов !!!
	// CHANGED
	public static void changeStatusToOnline(String id) {
		Optional<Courier> courier = getCourierFromID(id);
		if (courier.isPresent()) {
			if (!courier.get().isBlockStatus()) {
				courier.get().setOnlineStatus(true);
				System.out.println("Courier id: " + courier.get().getId() + " online today.");
			} else {
				System.out.println("Courier " + id + " is block");
			}
		}
	}

	public static Optional<Courier> getCourierFromID(String id) {
		done = false;
		Optional<Courier> courier = Optional.empty();
		courierList.forEach(c -> {
			if (c.getId().equalsIgnoreCase(id)) {
				courier.of(c);
				done = true;
			}
		});
		if (!done)
			System.out.println("Courier with id: " + id + " not found");
		return courier;
	}

	public static ArrayList getCourierListOfOrdersById(String ID) {
		return courierOrders.get(ID);
	}

	// Назначение курьера на заказ.
	public static void assignCourierToOrder(String id, ArrayList<Order> orders) {
		sc = new Scanner(System.in);
		ArrayList<Order> newOne = new ArrayList<Order>();
		courierList.forEach(c -> {
			if (c.getId().equalsIgnoreCase(id)) {
				System.out.println("\nCourier " + id + " assign orders:\n");
				orders.forEach(o -> {
					System.out.println("Order " + o.getId() + ": category=" + o.getCategory()
							+ ", loading time=" + o.getLoadingTime().toMinutes() + ", travelTime="
							+ o.getTravelTime().toMinutes() + ", priority=" + o.isPriority() + "\n");
				});
				System.out.println("\nAssign all this orders? Y/N\n");
				if (takeAnswer()) {
					courierOrders.put(c.getId(), orders);
					return;
				} else {
					orders.forEach(o -> {
						System.out.println("Order " + o.getId() + "\nAssign this order? Y/N");
						if (takeAnswer()) {
							newOne.add(o);
						} else {
							o.setStatusAccepted(false);
						}
					});
					courierOrders.put(c.getId(), newOne);
				}
			}
		});
	}

	//  checkWorkTime() – проверка времени работы курьера.
	// ДЛЯ ИТОГОВЫХ ПОДСЧЕТОВ
	public static boolean checkWorkTime(String id) {
		normal = true;
		courierList.forEach(c -> {
			if (c.getId().equals(id)) {
				Duration duration = c.getWorkingHours();
				if (duration.toHours() > NORMAL_WORK_HOURS) {
					c.addPenalty();
					normal = false;
				}
				if (duration.toHours() > MAX_WORK_HOURS) {
					c.blockCourier();
				}
			}
		});
		return normal;
	}

	private static boolean takeAnswer() {
		String answer = sc.nextLine().trim();
		return answer.equalsIgnoreCase("Y");
	}

	//  serializeCouriers() – сериализация всех курьеров.
	public static void serializeCouriers() {
		Courier.serializeCouriers();
	}

	//  deserializeCouriers() – десериализация курьеров.
	public static void deserializeCouriers() {
		Courier.deserializeCouriers();
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
//order.isAssepted() - false;

//o	Атрибуты: 
//	courierList (список курьеров)
//o	Методы: 
//	addCourier() – добавление нового курьера.
//	assignCourierToOrder() – назначение курьера на заказ.
//	checkWorkTime() – проверка времени работы курьера.
//	serializeCouriers() – сериализация всех курьеров.
//	deserializeCouriers() – десериализация курьеров.
