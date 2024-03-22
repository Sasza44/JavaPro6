package ua.meta.atipikin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Second { // розв'язання 2 задачі
	
	static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/apartments"; // база даних квартир, які здаються в Києві
	static final String DB_USER = "root";
	static final String DB_PASSWORD = "Sasza1978March";
	
	static Connection con;
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		String neighborhood = getNeighborhood(); // район пошуку
		int fromArea = getMinArea(); // мінімальна площа
		int toArea = getMaxArea(); // максимальна площа
		int fromRooms = getMinRooms(); // мінімальна кількість кімнат
		int toRooms = getMaxRooms(); // максимальна кількість кімнат
		int fromPrice = getMinPrice(); // мінімальна ціна в умовних одиницях
		int toPrice = getMaxPrice(); // максимальна ціна в умовних одиницях
		try {
			con = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD); // підключення до бази даних
			getSelectedApartments(neighborhood, fromArea, toArea, fromRooms, toRooms, fromPrice, toPrice); // вибірка з таблиці
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getNeighborhood() {
		System.out.println("Вкажіть район пошуку: ");
		String neighborhood = sc.nextLine();
		return neighborhood;
	}
	public static int getMinArea() {
		System.out.println("Вкажіть мінімальну площу: ");
		String sfromArea = sc.nextLine();
		if(sfromArea.equals("")) sfromArea = "0";
		int fromArea = Integer.parseInt(sfromArea);
		return fromArea;
	}
	public static int getMaxArea() {
		System.out.println("Вкажіть максимальну площу: ");
		String stoArea = sc.nextLine();
		if(stoArea.equals("")) stoArea = "500";
		int toArea = Integer.parseInt(stoArea);
		return toArea;
	}
	public static int getMinRooms() {
		System.out.println("Вкажіть мінімальну кількість кімнат: ");
		String sfromRooms = sc.nextLine();
		if(sfromRooms.equals("")) sfromRooms = "1";
		int fromRooms = Integer.parseInt(sfromRooms);
		return fromRooms;
	}
	public static int getMaxRooms() {
		System.out.println("Вкажіть максимальну кількість кімнат: ");
		String stoRooms = sc.nextLine();
		if(stoRooms.equals("")) stoRooms = "8";
		int toRooms = Integer.parseInt(stoRooms);
		return toRooms;
	}
	public static int getMinPrice() {
		System.out.println("Вкажіть мінімальну ціну: ");
		String sfromPrice = sc.nextLine();
		if(sfromPrice.equals("")) sfromPrice = "0";
		int fromPrice = Integer.parseInt(sfromPrice);
		return fromPrice;
	}
	public static int getMaxPrice() {
		System.out.println("Вкажіть максимальну ціну: ");
		String stoPrice = sc.nextLine();
		if(stoPrice.equals("")) stoPrice = "3000";
		int toPrice = Integer.parseInt(stoPrice);
		return toPrice;
	}
	
	public static void getSelectedApartments(String neighborhood, int fromArea, int toArea, int fromRooms, int toRooms, int fromPrice, int toPrice) throws SQLException {
		StringBuilder query = new StringBuilder("SELECT * FROM apartment\nWHERE ");
		if(!neighborhood.equals("")) {
			query.append("neighborhood = '" + neighborhood + "'\nAND "); // за умови, якщо обрали конкретний район
		}
		query.append("area BETWEEN " + fromArea + " AND " + toArea + " \nAND "); // межі для площі
		query.append("number_of_rooms BETWEEN " + fromRooms + " AND " + toRooms + " \nAND "); // кількість кімнат
		query.append("price BETWEEN " + fromPrice + " AND " + toPrice); // ціна
		
		Statement st = con.createStatement();
		ResultSet res = st.executeQuery(query.toString());
		while(res.next()) {
			System.out.println(res.getString(1) + " | " + res.getString(2) + " | " + res.getString(3) + " | " + res.getString(4) + " | " +
					res.getString(5) + " | " + res.getString(6));
		}
		st.close();
	}
}