package ua.meta.atipikin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

public abstract class AbstractDAO<T> {
	private static final Object String = null;
	private final Connection con;
	private final String table;
	public AbstractDAO(Connection con, String table) {
		this.con = con;
		this.table = table;
	}
	
	private Field getPrimaryKeyField(Field[] fields) {
		Field result = null;
		for(Field f: fields) {
			if(f.isAnnotationPresent(Id.class)) {
				result = f;
				result.setAccessible(true);
				break;
			}
		}
		if(result == null) throw new RuntimeException("No Id field found");
		return result;
	}
	
	// запис у файл списку наявних сортів
	public void getAll(Class<T> cls, String fileName) throws SQLException, InstantiationException, IllegalAccessException,
	IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		String s1 = "SELECT * FROM " + table; // текст запиту в базу даних
		StringBuilder sb1 = new StringBuilder(); // формування тексту для запису в файл
		
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(s1);
		ResultSetMetaData md = rs.getMetaData();
		while(rs.next()) {
			T t = (T) cls.getConstructor().newInstance();
			for(int i = 1; i <= md.getColumnCount(); i++) {
				String columnName = md.getColumnName(i);
				Field f = cls.getDeclaredField(columnName);
				f.setAccessible(true);
				f.set(t, rs.getObject(columnName));
			}
			sb1.append(t + "\n");
		}
		rs.close();
		st.close();
		try {
			FileWriter fw = new FileWriter(fileName); // запис рядків таблиці в файл (для вибору при формуванні замовлення)
			fw.write(sb1.toString());
			fw.close();
		} catch (IOException e) {
            System.out.println("Writing error: " + e.getMessage());
        }
	}
	
	public void add(T t) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = t.getClass().getDeclaredFields();
		Field id = getPrimaryKeyField(fields);
		
		StringBuilder names = new StringBuilder();
		StringBuilder values = new StringBuilder();
		for(Field f: fields) {
			if(f != id) {
				f.setAccessible(true);
				names.append(f.getName()).append(", "); // перелік назв стовпців, які співпадають з назвами полів
				values.append('"').append(f.get(t)).append("\", "); // перелік значень полів
			}
		}
		names.delete(names.length() - 2, names.length() - 1); // видаляємо кому з пробілом у кінці
		values.delete(values.length() - 2, values.length() - 1);
		
		String s1 = "INSERT INTO " + table + " (" + names.toString() + ") VALUES (" + values.toString() + ")";
		try (Statement st = con.createStatement()) {
			st.execute(s1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(T t) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = t.getClass().getDeclaredFields();
		Field id = getPrimaryKeyField(fields);
		
		StringBuilder sb = new StringBuilder();
		for(Field f: fields) {
			if(f != id) {
				f.setAccessible(true);
				sb.append(f.getName()).append(" = \"").append(f.get(t)).append("\", ");
			}
		}
		sb.delete(sb.length() - 2, sb.length() - 1);
		
		String s1 = "UPDATE " + table + " SET " + sb.toString() + " WHERE " + id.getName() + " = \"" + id.get(t) + '"';
		try (Statement st = con.createStatement()) {
			st.execute(s1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// метод для зчитування об'єкту (рядка таблиці) по id
	public T read(Class<T> cls, String primaryKey) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException,
	InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		T t = (T) cls.getConstructor().newInstance(); // створюємо новий об'єкт класу
		Field[] fields = cls.getDeclaredFields(); // масив полів класу
		Field id = getPrimaryKeyField(fields);
		
		if(id.getType().toString().equals("int")) { // умова, коли первинний ключ ціле число
			id.set(t, Integer.parseInt(primaryKey));
		}
		else if(id.getType().toString().equals("class java.lang.String")) { // умова, коли первинний ключ рядок
			id.set(t, primaryKey);
		}
		String s1 = "SELECT * FROM " + table + " WHERE " + id.getName() + " = \"" + id.get(t) + '"';
		
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(s1);
		ResultSetMetaData md = rs.getMetaData();
		
		while(rs.next()) { // хоч рядок лише один, але без цього методи класу ResultSet не працюють
			for(int i = 2; i <= md.getColumnCount(); i++) { // перший стовпчик таблиці - ідентифікатор
				String columnName = md.getColumnName(i);
				Field f = cls.getDeclaredField(columnName);
				f.setAccessible(true);
				f.set(t, rs.getObject(columnName));
			}
		}
		rs.close();
		st.close();
		return t;
	}
	
	public void delete(T t) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = t.getClass().getDeclaredFields();
		Field id = getPrimaryKeyField(fields);
		
		String s1 = "DELETE FROM  " + table + " WHERE " + id.getName() + " = \"" + id.get(t) + '"';
		try (Statement st = con.createStatement()) {
			st.execute(s1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}