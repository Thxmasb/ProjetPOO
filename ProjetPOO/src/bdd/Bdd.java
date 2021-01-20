package bdd;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Bdd {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "mariamaria";

	String query;
	String sql;
	Statement stmt = null;
	String type;
	public ArrayList <ArrayList<String>> ResultList=new ArrayList<ArrayList<String>>();

	public Bdd(String query,String type) {

		this.query=query;
		this.type=type;

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

			System.out.println("Creating database...");
			stmt = conn.createStatement();

			String sql = "CREATE DATABASE bddpoo2020";
			try{
				stmt.executeUpdate(sql);
				System.out.println("Database created successfully...");
			}catch (SQLException e){
				System.out.println("La base existe deja");
			}
			
			sql = "USE bddpoo2020";

			ResultSet rs1 = stmt.executeQuery(sql);
			
			sql = "CREATE TABLE `history` ( `ipsrc` VARCHAR(200) NOT NULL , `ipdest` VARCHAR(200) NOT NULL , `message` VARCHAR(15000) NOT NULL , `dateheure` VARCHAR(30) NOT NULL )";
			try{
				stmt.executeUpdate(sql);
				System.out.println("On a bien crée la table history");
			}catch (SQLException e){
				//e.printStackTrace();
				System.out.println("La table existe deja");
			}

			
			sql = query;

			if(type.equals("SELECT")) {
				ResultSet rs2 = stmt.executeQuery(sql);
				ResultList=display(rs2);
				rs2.close();

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


	public ArrayList<ArrayList<String>> display(ResultSet rs) throws SQLException {

		
		//STEP 5: Extract data from result set
		while(rs.next()){
			//Retrieve by column name
			
			ArrayList<String> Result= new ArrayList<String>();
			String ipsrc  = rs.getString("ipsrc");
			String ipdest = rs.getString("ipdest");
			String message = rs.getString("message");
			String dateheure = rs.getString("dateheure");
			

			Result.add(ipsrc);
			Result.add(ipdest);
			Result.add(message);
			Result.add(dateheure);			
			System.out.println(Result);
			ResultList.add(Result);
			

			//Display values
			
//			  System.out.print("ipsrc: " + ipsrc); System.out.print(", ipdest: " + ipdest);
//			  System.out.print(", message: " + message); System.out.println(", dateheure: "
//			  + dateheure);
			  System.out.println(ResultList);
			  
			 
		}
		
		return ResultList;
	}

	public void update(String sql) throws SQLException {


		stmt.executeUpdate(sql);

		System.out.println("Ajout de "+sql);

	}





	public static void main(String[] args) { 
		//new Bdd("INSERT INTO history VALUES ('NRTJFKN', 'cococ2kl', 'jkfghdng' ,'2021/01/18 16:54:15')","INSERT");
		new Bdd("SELECT * FROM history","SELECT"); }

}//end FirstExample