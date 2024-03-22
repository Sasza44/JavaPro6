package ua.meta.atipikin;

import java.sql.Connection;

public class ProductDAO extends AbstractDAO {
	public ProductDAO(Connection con, String table) {
		super(con, table);
	}
}