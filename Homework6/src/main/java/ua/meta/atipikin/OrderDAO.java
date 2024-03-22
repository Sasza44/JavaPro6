package ua.meta.atipikin;

import java.sql.Connection;

public class OrderDAO extends AbstractDAO{
	public OrderDAO(Connection con, String table) {
		super(con, table);
	}
}