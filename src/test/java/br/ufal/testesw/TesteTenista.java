package br.ufal.testesw;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import br.ufal.testesw.dao.DAOTenista;

@DisplayName("Classe de Teste para os Bugs do JUnit5")
public class TesteTenista {

	@BeforeEach
	public void init() throws ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		
		initDatabase();
	}
	
	@AfterEach
	public void destroy() throws SQLException, ClassNotFoundException, IOException {
		        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
		            statement.executeUpdate("DROP TABLE tenista");
		            connection.commit();
		        }
		    }

	
	
	@Test
	@Tag("Adiciona")
	public void AdicionaTeste() {
		try(Connection connection = getConnection();
		Statement statement = connection.createStatement();) {
        ResultSet result = statement.executeQuery("SELECT count(*) as total FROM tenista");
        int v=0;
        if(result.next())
        	v = result.getInt("total");
        assertFalse(v == 1);
        
        assumeFalse(result == null);
        
        DAOTenista.adicionar(new Tenista());
		
        result = statement.executeQuery("SELECT count(*) as total FROM tenista");
        if(result.next())
        	v = result.getInt("total");
        assertTrue(v == 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void initDatabase() throws SQLException {
		        try (Connection connection = getConnection(); Statement  statement = connection.createStatement();) {
		            statement.execute("CREATE TABLE tenista (id INT NOT NULL IDENTITY, nome VARCHAR(50),"
	
		                    + "email VARCHAR(50) , PRIMARY KEY (id))");
		            connection.commit();
		        }
	}
	
	private static Connection getConnection() throws SQLException {
		        return DriverManager.getConnection("jdbc:hsqldb:mem:db", "sa", "");
	}

	private int getTotalRecords() {
		        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
		            ResultSet result = statement.executeQuery("SELECT count(*) as total FROM tenista");
		            if (result.next()) {
		                return result.getInt("total");
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		        return 0;
		    }

	@Test
	public void getTotalRecordsTest() {
		try(Connection connection = getConnection();
				Statement statement = connection.createStatement();) {
				statement.executeUpdate(
				        "INSERT INTO tenista(nome, email) VALUES ('Randy Q', 'vinod@dsdsdsd.com')");
				statement.executeUpdate("INSERT INTO tenista(nome, email) VALUES ('Ko G', 'kog@sdsdss.com')");
				statement.executeUpdate("INSERT INTO tenista(nome, email) VALUES ('Boris M', 'dsdssd@dfdfdfd.sds')");
				connection.commit();
				
		        assertEquals(3, getTotalRecords());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    }
	
	
	private void checkbackOnCheckNameExists1() throws SQLException {
		Connection connection = getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate("INSERT INTO tenista(name, email) VALUES ('Randy Q', 'vinod@dsdsdsd.com')");
		connection.commit();
	}

	@Test
	public void checkbackOnCheckNameExists() {
		assertThrows(SQLException.class, 
				()-> checkbackOnCheckNameExists1());
	}
	
	private void backOnCheckNameExists() {
		try(Connection connection = getConnection();
		 Statement statement = connection.createStatement();) {
				statement.executeUpdate(
				        "INSERT INTO tenista(nome, email) VALUES ('Randy Q', 'vinod@dsdsdsd.com')");
				statement.executeUpdate("INSERT INTO tenista(nome, email) VALUES ('Ko G', 'kog@sdsdss.com')");
				statement.executeUpdate("INSERT INTO tenista(nome, email) VALUES ('Boris Ma', 'dsdssd@dfdfdfd.sds')");
				statement.executeUpdate("INSERT INTO tenista(nome, email) VALUES ('Gi Ma', 'dsdssd@dfdfdfd.sds')");
				connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 @Test
     public void checkNameExistsTest() {
		 backOnCheckNameExists();
         try (Connection connection = getConnection();
                 Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);) {
        		
             ResultSet result = statement.executeQuery("SELECT nome FROM tenista");
             if (result.first()) {
                 assertThat("Randy Q", is(result.getString("nome")));
             }
             if (result.last()) {
                 assertThat("Gi Ma", is(result.getString("nome")));
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

	 
	 @Test
	 public void obterTest() {
		 backOnCheckNameExists();
		 
		 try (Connection connection = getConnection();
                 Statement statement = connection.createStatement();) {
        	
			 List<Tenista> ts = new ArrayList<>();
			 ResultSet rs = statement.executeQuery("SELECT * FROM tenista");
			 while(rs.next()) {
				 ts.add(new Tenista(rs.getLong("id"), rs.getString("nome")));
			 }
			 
		 assertAll("tenistas",
			 ()-> assertThat(DAOTenista.obter(1).getNome(), is("Ko G")),
			 ()-> assertEquals(4, ts.size()),
			 ()-> {
				 Predicate<String> isRandy = "Randy Q"::equalsIgnoreCase;
				 Predicate<String> isGiMa = "Gi Ma"::equalsIgnoreCase;
				 Predicate<String> isKoG = "Ko G"::equalsIgnoreCase;
				 Predicate<String> isBorisMa = "Boris Ma"::equalsIgnoreCase;
				 assertTrue(ts.stream().map(Tenista::getNome).allMatch(isRandy.or(isGiMa).or(isKoG).or(isBorisMa)));
			 }
				
			);
		 } catch (SQLException e) {
			e.printStackTrace();
		}
	 }
	 
	 @Test
	 public void excluirTeste() {
		 backOnCheckNameExists();
		 
		 try (Connection connection = getConnection();
                 Statement statement = connection.createStatement();) {
        	
			 List<Tenista> ts = new ArrayList<>();
			 ResultSet rs = statement.executeQuery("SELECT * FROM tenista");
			 while(rs.next()) {
				 ts.add(new Tenista(rs.getLong("id"), rs.getString("nome")));
			 }
			 
			 DAOTenista.excluir(ts.get(2));
			 
			 rs = statement.executeQuery("SELECT * FROM tenista");
			 ts.clear();
			 while(rs.next()) {
				 ts.add(new Tenista(rs.getLong("id"), rs.getString("nome")));
			 }
			 
			 assertEquals(3, ts.size());
		 } catch (SQLException e) {
			e.printStackTrace();
		}
	 }
	 
	 @Test
	 public void editarTeste() {
		 backOnCheckNameExists();
		 try (Connection connection = getConnection();
                 Statement statement = connection.createStatement();) {
        	
			 List<Tenista> ts = new ArrayList<>();
			 ResultSet rs = statement.executeQuery("SELECT * FROM tenista");
			 while(rs.next()) {
				 ts.add(new Tenista(rs.getLong("id"), rs.getString("nome")));
			 }
			 
			 assertEquals("Boris Ma", ts.get(2).getNome());
			 DAOTenista.editar(2, "Modificado");
			 
			 rs = statement.executeQuery("SELECT * FROM tenista");
			 ts.clear();
			 while(rs.next()) {
				 ts.add(new Tenista(rs.getLong("id"), rs.getString("nome")));
			 }
			 assertEquals("Modificado", ts.get(2).getNome());
			 
		 } catch (SQLException e) {
			e.printStackTrace();
		}
	 }

}
