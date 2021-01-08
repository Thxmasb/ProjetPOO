package bdd;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Bdd {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr";

	//  Database credentials
	static final String USER = "tp_servlet_007";
	static final String PASS = "johQuoG7";

	String query;
	String sql;
	Statement stmt = null;
	String type;

	public Bdd(String query,String type) {

		this.query=query;
		this.type=type;
		ResultSet rs = null;

		Connection conn = null;
		
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			
			
			sql = "USE tp_servlet_007";
			
			ResultSet rs1 = stmt.executeQuery(sql);
			
			sql = query;
			
			if(type.equals("SELECT")) {
				rs=display(sql);
				rs.close();
				
			}else if(type.equals("INSERT")) {
				update(sql);
			}
			//STEP 6: Clean-up environment
			
			stmt.close();
			conn.close();
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
		System.out.println("Goodbye!");
	}//end main
	
	
	public ResultSet display(String sql) throws SQLException {
		
		
		ResultSet rs = stmt.executeQuery(sql);

		//STEP 5: Extract data from result set
		while(rs.next()){
			//Retrieve by column name
			String ipsrc  = rs.getString("ipsrc");
			String ipdest = rs.getString("ipdest");
			String message = rs.getString("message");
			Timestamp dateheure = rs.getTimestamp("dateheure");

			//Display values
			System.out.print("ipsrc: " + ipsrc);
			System.out.print(", ipdest: " + ipdest);
			System.out.print(", message: " + message);
			System.out.println(", dateheure: " + dateheure);
		}
		
		return rs;
	}

	public void update(String sql) throws SQLException {


		stmt.executeUpdate(sql);

		System.out.println("Ajout de "+sql);

	}
	
	
	/*
	 * public static void main(String[] args) { //new
	 * Bdd("INSERT INTO history VALUES ('192.168.0.1','192.168.9.4', 'ntm2', '2021-01-08 15:57:01')"
	 * ,"INSERT"); new Bdd("SELECT * FROM history","SELECT"); }
	 */
}//end FirstExample