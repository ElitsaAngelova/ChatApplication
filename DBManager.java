package netJava2020MsC_fn26393_project_final;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	private static Connection connection = null;

	public static void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		System.out.println("Opened database successfully");
	}

	public static void createDB() {
		Statement stmt = null;
		dropAllTables();
		try {
			stmt = connection.createStatement();
			String sqlUser = "CREATE TABLE User " + 
								"(ID INT PRIMARY KEY	NOT NULL," + 
								" NICKNAME	TEXT, " + 
								" MAIL	CHAR(50) )" ;
			stmt.executeUpdate(sqlUser);
			stmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Table created successfully");
	}

	public static void addUser(int id, String nickname,String mail) {
		try {
			String sql = "INSERT INTO USER (ID,NICKNAME,MAIL) " + "VALUES (?, ?, ?);";
			
			PreparedStatement p = connection.prepareStatement(sql);
			p.setInt(1, id);
			p.setString(2, nickname);
			p.setString(3, mail);
			p.executeUpdate();
			p.close();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("User added successfully");
	}
	
	public static String getEmail(int id) {
		String mail_check = null;
		try {
			String sql = "SELECT MAIL FROM USER WHERE ID = ?;";
			PreparedStatement p = connection.prepareStatement(sql);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				mail_check = rs.getString("mail");
			}
			rs.close();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return mail_check;
	}
	
	public static void removeUser(int id) {
		try {
			String sql = "DELETE from USER where ID = ?;";
			
			PreparedStatement p = connection.prepareStatement(sql);
			p.setInt(1, id);
			p.executeUpdate();
			p.close();
			

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void showUsers() {
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM USER;");

			while (rs.next()) {
				int id_check = rs.getInt("id");
				String nickname_check = rs.getString("nickname");
				String mail_check = rs.getString("mail");

				System.out.println("ID = " + id_check);
				System.out.println("NICKNAME = " + nickname_check);
				System.out.println("MAIL = " + mail_check);
				System.out.println();
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}

	public static void dropAllTables() {
		Statement stmt = null;

		try {
			stmt = connection.createStatement();
			String sql = "DROP TABLE IF EXISTS USER;";
			stmt.executeUpdate(sql);

			stmt.close();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void disconnect() {

		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		System.out.println("Closed database successfully");
	}
}
