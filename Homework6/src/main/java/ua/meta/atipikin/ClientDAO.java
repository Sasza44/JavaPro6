package ua.meta.atipikin;

import java.sql.Connection;

public class ClientDAO extends AbstractDAO<Client>{
	public ClientDAO(Connection con, String table) {
		super(con, table);
	}
}