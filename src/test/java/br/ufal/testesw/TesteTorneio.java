package br.ufal.testesw;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.ufal.testesw.dao.DAOTorneio;

@DisplayName("Testes da classe torneio")
public class TesteTorneio {

	@BeforeEach
	public void init() throws ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		
		initDatabase();
	}
	
	@AfterEach
	public void destroy() throws SQLException, ClassNotFoundException, IOException {
		        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
		            statement.executeUpdate("DROP TABLE torneio");
		            connection.commit();
		        }
		    }
	
	private static void initDatabase() throws SQLException {
        try (Connection connection = getConnection(); Statement  statement = connection.createStatement();) {
            statement.execute("CREATE TABLE torneio (id INT NOT NULL IDENTITY, tenista INT,"

                    + "email VARCHAR(50) , PRIMARY KEY (id))");
            connection.commit();
        }
	}
	
	private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:db", "sa", "");
	}
	
	@Test
	public void getTotalRecordsTest() {
		try(Connection connection = getConnection();
				Statement statement = connection.createStatement();) {
				statement.executeUpdate(
				        "INSERT INTO torneio(tenista) VALUES ('1')");
				statement.executeUpdate("INSERT INTO torneio(tenista) VALUES ('2')");
				statement.executeUpdate("INSERT INTO torneio(tenista) VALUES ('3')");
				connection.commit();
				
		        assertEquals(3, getTotalRecords());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    }
	
	private int getTotalRecords() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            ResultSet result = statement.executeQuery("SELECT count(*) as total FROM torneio");
            if (result.next()) {
                return result.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
	
	private void backOnCheckNameExists() {
		try(Connection connection = getConnection();
		 Statement statement = connection.createStatement();) {
			statement.executeUpdate(
			        "INSERT INTO torneio(tenista) VALUES ('1')");
			statement.executeUpdate("INSERT INTO torneio(tenista) VALUES ('2')");
			statement.executeUpdate("INSERT INTO torneio(tenista) VALUES ('3')");
			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	 public void excluirTeste() {
		 backOnCheckNameExists();
		 
		 try (Connection connection = getConnection();
                Statement statement = connection.createStatement();) {
       	
			 List<Torneio> ts = new ArrayList<>();
			 ResultSet rs = statement.executeQuery("SELECT * FROM torneio");
			 while(rs.next()) {
				 ts.add(new Torneio(rs.getLong("id"), ""+rs.getInt("tenista")));
			 }
			 
			 DAOTorneio.excluir(ts.get(2));
			 
			 rs = statement.executeQuery("SELECT * FROM torneio");
			 ts.clear();
			 while(rs.next()) {
				 ts.add(new Torneio(rs.getLong("id"), ""+rs.getString("tenista")));
			 }
			 
			 assertEquals(2, ts.size());
		 } catch (SQLException e) {
			e.printStackTrace();
		}
	 }
}
