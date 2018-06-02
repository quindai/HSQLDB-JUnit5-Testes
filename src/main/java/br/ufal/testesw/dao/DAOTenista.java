package br.ufal.testesw.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.ufal.testesw.Tenista;

public class DAOTenista {

	public static Tenista obter(int idTenista) {
		Tenista t = new Tenista();
		try (Connection connection = DBMgr.getConnection(); Statement statement = connection.createStatement();) {
			ResultSet rs = statement.executeQuery("SELECT * FROM tenista WHERE id=" + idTenista);
			while(rs.next()) {
				t.setId(rs.getLong("id"));
				t.setNome(rs.getString("nome"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return t;
		}
	}
	
	public static void excluir(Tenista t) {
		try (Connection conn = DBMgr.getConnection(); Statement statement = conn.createStatement();) {
			statement.execute("DELETE FROM tenista WHERE id=" + t.getId());
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void adicionar(Tenista t) {
		try (Connection conn = DBMgr.getConnection(); Statement statement = conn.createStatement();) {
			statement.execute("INSERT INTO tenista(nome) VALUES("+t.getNome()+")" );
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void editar(int idTenista, String nome) {
		try (Connection conn = DBMgr.getConnection(); Statement statement = conn.createStatement();) {
			statement.executeUpdate(String.format(
					"UPDATE tenista SET NOME='%s' WHERE id=%d", nome,idTenista));
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
