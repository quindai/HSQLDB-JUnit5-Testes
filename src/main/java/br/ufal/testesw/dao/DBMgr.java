package br.ufal.testesw.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBMgr {

	public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:db", "sa", "");
}
}
