package br.ufal.testesw.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import br.ufal.testesw.Torneio;

public class DAOTorneio {

	public static void excluir(Torneio t) {
		try (Connection conn = DBMgr.getConnection(); Statement statement = conn.createStatement();) {
			statement.execute("DELETE FROM torneio WHERE id=" + t.getId());
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
