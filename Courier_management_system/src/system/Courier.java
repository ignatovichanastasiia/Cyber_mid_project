package system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Courier implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final int ORDERS_FOR_BONUS = 5;
	private static final String ID_TEXT = "courier";
	private static final String DIRECTORY_PATH = "serializ";
	private static final String NULL = null;
	private static final double SALARY = 60;
	private static final double PENALTY_COST = 250;
	private static final double BONUS_COST = 500;
	private static int counter = 0;

	private static Scanner sc = new Scanner(System.in);
	private String id;
	private String category; //// REFRIGERATED, OVERSIZE, LIGHT
	private Duration workingHours;
	private int penalty;
	private int blockStatus;
	private boolean onlineStatus;
	private int bonusPoints;

	/*
	 * The constructor takes the transportation category as an attribute. All other
	 * attributes are generated during the object creation process. Part of the id
	 * is the date and time the courier was registered. Also, inside the
	 * constructor, the created object immediately goes to the list of couriers in
	 * the CourierManager class.
	 * 
	 * (RUS)Конструктор берет атрибутом категорию перевозки. Все остальные атрибуты
	 * генерируются в процессе создания объекта. Частью id является дата и время
	 * регистрации курьера. Так же внутри конструктора создающийся объект сразу
	 * попадает в лист курьеров в классе КурьерМенеджера
	 * 
	 */
	public Courier(String category) {



		this.id = genID();
		this.category = category;
		this.workingHours = Duration.ofHours(0);
		this.penalty = 0;
		this.blockStatus = 0;
		this.onlineStatus = false;

		ArrayList<Courier> couriers = new ArrayList<Courier>(CourierManager.getCourierList());
		couriers.add(this);
		CourierManager.setCourierList(couriers);
	}


	/*
	 * Confirmation of order acceptance. The method takes a list of distributed
	 * orders and returns a list of accepted orders.
	 * 
	 * (RUS)Подтверждение принятия заказа. Метод берет список распределенных
	 * заказов, а возвращает список принятых.
	 * 
	 */
	public ArrayList<Order> confirmOrder(ArrayList<Order> orders) {
		ArrayList<Order> confirmList = new ArrayList<Order>();
		orders.forEach(o -> {
			System.out.println("Add order? Y/N\n" + o.toString());
			String answer = sc.nextLine();
			if (answer.equalsIgnoreCase("Y")) {
				confirmList.add(o);
			} else {
				o.setStatusAccepted(false);
//				o.setCourierIDcomplete(NULL);
			}
		});
		this.bonusPoints = confirmList.size() / ORDERS_FOR_BONUS;
		return confirmList;

	}

	/*
	 * Salary calculation taking into account fines and bonuses. (RUS)Расчет
	 * зарплаты с учетом штрафов и премий.
	 * 
	 */
	public double calculateSalary() {
		Duration duration = workingHours;
		double hours = duration.toHours();
		double totalPenalty = penalty * PENALTY_COST;
		penalty = 0;
		double totalBonus = bonusPoints * BONUS_COST;
		bonusPoints = 0;
		return hours * SALARY - totalPenalty + totalBonus;
	}

	/*
	 * Adds penalty.
	 * 
	 * (RUS)Добавляет единицу штрафа.
	 * 
	 */
	public int addPenalty() {
		return ++penalty;
	}

	/*
	 * Blocks the courier for 2 days due to exceeding the working hours limit.
	 * 
	 * (RUS)Блокирует курьера на 2 дня из-за превышения лимита рабочих часов.
	 */
	public void blockCourier() {
		if (blockStatus > 0) {
			throw new IllegalArgumentException(
					"Logic error: a blocked courier took over or the wrong courier received the block.");
		}
		blockStatus = 2;
	}

	/*
	 * Serialization of courier data.
	 * 
	 * (RUS)Сериализация данных курьера.
	 * 
	 */
	public boolean serialize() {
		File directoryForSerialization = new File(DIRECTORY_PATH);
		File serFile = new File(DIRECTORY_PATH, id + ".ser");
		if (!directoryForSerialization.exists()) {
			directoryForSerialization.mkdir();
		}
		if(!serFile.exists()) {
			try {
			serFile.createNewFile();
			System.out.println("Created file for serialization.");
			}catch(IOException e) {
				System.out.println("Problem with creating file for serialization: "+e.getMessage());
			}
		}
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(serFile))) {
			out.writeObject(this);
			System.out.println("Courier was serialized into file: " + serFile.getPath());
			return true;
		} catch (IOException e) {
			System.out.println("We have some problem with saving courier files: " + e.getMessage());
			return false;
		}
	}

	/*
	 * Deserialization of courier data from a file.
	 * 
	 * (RUS)Десериализация данных курьера из файла.
	 * 
	 */
	public boolean deserialize() {
		File serFile = new File(DIRECTORY_PATH, id + ".ser");
		if (serFile.exists()) {
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(serFile.getPath()))) {
				Courier deserializedEntry = (Courier) in.readObject();
				if (blockStatus > 0) {
					this.blockStatus = --blockStatus;
				}
				System.out.println("Courier was deserialized: " + deserializedEntry.id);
				System.out.println(deserializedEntry.toString());
				return true;
			} catch (IOException | ClassNotFoundException e) {
				System.out.println("We have troble with deserialization: " + e.getMessage());
				return false;
			}
		}
		return false;		
	}

	public String getId() {
		return id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Duration getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(Duration workingHours) {
		this.workingHours = workingHours;
	}

	public int getPenalty() {
		return penalty;
	}

	public void setPenalty(int penalty) {
		this.penalty = penalty;
	}

	public boolean isOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(boolean onlineStatus) {
		this.onlineStatus = onlineStatus;
		if(onlineStatus) System.out.println("Courier "+id+" is online! Welcome to this day!");
	}

	public boolean isBlockStatus() {
		return blockStatus > 0;
	}

	public int getBlockStatus() {
		return blockStatus;
	}

	public void setBlockStatus(int blockStatus) {
		this.blockStatus = blockStatus;
	}

	@Override
	public String toString() {
		return "Courier [id=" + id + ", category=" + category + ", workingHours=" + workingHours + ", penalty="
				+ penalty + ", blockStatus=" + blockStatus + ", onlineStatus=" + onlineStatus + "]\n";
	}
	
	public static String getDirectoryPath() {
		return DIRECTORY_PATH;
	}
	
	private static String genID() {
		counter++;
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return ID_TEXT + now.format(formatter)+counter;
	}

}

//o	Атрибуты: 
//	id (идентификатор курьера)
//	category (категория машины)
//	workingHours (время работы)
//	penalty (штрафы)
//	blockStatus (блокировка)
//o	Методы: 
//	confirmOrder() – подтверждение принятия заказа.
//	calculateSalary() – расчет зарплаты с учетом штрафов и премий.
//	addPenalty() – добавление штрафа.
//	blockCourier() – блокировка курьера.
//	serialize() – сериализация данных курьера.
//	deserialize() – десериализация данных курьера из файла.
