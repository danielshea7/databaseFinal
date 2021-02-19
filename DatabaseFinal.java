//This is the main class and should be run to test the program
//There is a readme file provided with this application 
//DANIEL SHEA 01717794 
//Databases Final Project

import java.sql.*;
import java.util.Scanner;


public class DatabaseFinal {
	//main
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		//making the menu in console
		int choice = 0;
		Scanner input = new Scanner(System.in);
			
		System.out.println("Welcome to the Auto Accident DB");
		System.out.println("Select your desired operation:");
		System.out.println("1: Add Accident");
		System.out.println("2: Retrieve Accident");
		System.out.println("3: Search");
		//System.out.println("-1: to quit");
		
		//store choice and apply switch statement 
		choice = Integer.parseInt(input.nextLine());
		
		switch (choice) {
		case 1:
			//add accident
			addA();
			break;
		case 2:
			//retrieve accident
			retrieve();
			break;
		case 3: 
			//search
			search();
			break;
		case -1:
			//quit
			choice = -1;
			input.close();
			return;
		default: 
			System.out.println("Invalid selection");
			input.close();
			return;
		}
		
		//close scanner
		input.close();
		
		//Connection con = ConnectDb.connect();
		
	}

	//add function 
	public static void addA() {
		//making another scanner
		Scanner in = new Scanner(System.in);
		//ask user to input data
		System.out.println("Enter date(YYYY-MM-DD),city,state(2 Letter Abbreviation)");
		String userInput = in.nextLine();
		//parsing user input 
		String[] parsed = userInput.split(",");
		System.out.println(parsed[0]);
		System.out.println(parsed[1]);
		System.out.println(parsed[2]);
		//connect 
		Connection con = ConnectDb.connect();
		
		//making strings for inputs
		int aid = 0;
		String accident_date = parsed[0];
		String city = parsed [1];
		String state = parsed[2];
		//query max aid to add one 
		try {
			Statement stmt = con.createStatement();
			String query = "SELECT MAX(aid) FROM accidents";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				aid = rs.getInt("MAX(aid)") + 1;
			}
		}catch(SQLException e){
			//print error
			System.out.println(e.toString());
		}
		
		PreparedStatement ps = null;
		//insert data ...YYYY-MM-DD
		try {
			//formatting data 
			//System.out.println("test1");
			String sql = "INSERT INTO accidents (aid, accident_date, city, state) VALUES(?,?,?,?) ";
			//System.out.println("test2");
			ps = con.prepareStatement(sql);
			
			//setting values  
			ps.setInt(1, aid);
			ps.setString(2, accident_date);
			ps.setString(3, city);
			ps.setString(4, state);
			ps.executeUpdate();
		
			
			System.out.println("Accident Added!");
			
		} catch(SQLException e){
			//print error
			System.out.println(e.toString());
		}
		
		
		//adding looping feature to add drivers
		int number=1;
		while(number !=0){
		//second part of add accidents 
		System.out.println("Enter the vin,damages(in $),and driver ssn(with dashes):");
		String userInput2 =null;
		userInput2 = in.nextLine();
		
		//parsing user input 
		String[] parsed2 = null;
		parsed2 = userInput2.split(",");
		System.out.println(userInput2);
		System.out.println(parsed2[0]);
		System.out.println(parsed2[1]);
		System.out.println(parsed2[2]);
		//setting values 
		String vin = parsed2[0];
		int damages = Integer.parseInt(parsed2[1]);
		String driver_ssn = parsed2[2];
		
		
		try {
			//insert data
			String sql = "INSERT INTO involvements (aid, vin, damages, driver_ssn) VALUES(?,?,?,?)";
			//System.out.println("test2");
			ps = con.prepareStatement(sql);
			
			//setting values  
			ps.setInt(1, aid);
			ps.setString(2, vin);
			ps.setInt(3, damages);
			ps.setString(4, driver_ssn);
			ps.executeUpdate();
			
			System.out.println("Involvement Added!");
		}catch(SQLException e) {
			//print error
			System.out.println(e.toString());
		}
		System.out.println("Add another driver?(1=Yes,0=No)");
		number = Integer.parseInt(in.nextLine());
		
		if(number==0) break;
		//while loop end
		}
		//close scanner
		in.close();
	}
	
	//retrieving accident data
	public static void retrieve() {
		//making another scanner
		Scanner in = new Scanner(System.in);
		//ask user to enter aid
		int aid=0;
		System.out.println("Enter accident id:");
		aid = in.nextInt();
		//connect 
		Connection con = ConnectDb.connect();
		//query dB
		try {
			PreparedStatement ps = null;
			ResultSet rs=null;
			String sql ="SELECT * FROM accidents WHERE aid = ?";
			
			ps = con.prepareStatement(sql);
			
			ps.setInt(1,aid);
			
			rs = ps.executeQuery();
			//print rs 
			while(rs.next()) {
				System.out.println("Accident Info----------------");
				//printing the rs from the accidents table 
				String accident_date = rs.getString("accident_date");
				String city =rs.getString("city");
				String state = rs.getString("state");
				System.out.println("accident date: "+accident_date);
				System.out.println("city: "+city);
				System.out.println("state: "+state);
			}
			
		}catch(SQLException e){
			//print error
			System.out.println(e.toString());
		}
		//finding and printing data from involvments table 
		try {
			PreparedStatement ps = null;
			ResultSet rs=null;
			String sql ="SELECT * FROM involvements WHERE aid = ?";
			
			ps = con.prepareStatement(sql);
			
			ps.setInt(1,aid);
			
			rs = ps.executeQuery();
			//print rs 
			while(rs.next()) {
				System.out.println("Involement Info-----------------");
				//printing the rs from the accidents table 
				String vin = rs.getString("vin");
				int damages =rs.getInt("damages");
				String driver_ssn = rs.getString("driver_ssn");
				System.out.println("vin: "+vin);
				System.out.println("damages: "+damages);
				System.out.println("driver ssn: "+driver_ssn);
			}
			
		}catch(SQLException e){
			//print error
			System.out.println(e.toString());
		}
		
		in.close();
	}

	//searching accident data
	public static void search() {
		//making another scanner
		Scanner in = new Scanner(System.in);
		
		//setting up some values for the query
		//may be switched up later
		String date0, date1 =null;
		int avgDamage0, avgDamage1;
		int totalDamage0, totalDamage1;
		
		//explain to user
		System.out.println("Enter range of dates(YYYY-MM-DD,YYYY-MM-DD)(0000-00-00,9999-99-99 if not needed)");
		//collect data
		String userInput = in.nextLine();
		//parsing user input 
		String[] parse = null;
		parse = userInput.split(",");
		date0= parse[0];
		date1= parse[1];
		
		
		System.out.println("Enter range of average damage(0,9999 if not needed)");
		//collect data
		String userInput1 = in.nextLine();
		//parsing user input 
		String[] parse1 = null;
		parse1 = userInput1.split(",");
		avgDamage0= Integer.parseInt(parse1[0]);
		avgDamage1= Integer.parseInt(parse1[1]);
		
		
		System.out.println("Enter range of total damage(0,9999 if not needed)");
		String userInput2 = in.nextLine();
		//parsing user input 
		String[] parse2 = null;
		parse2 = userInput2.split(",");
		totalDamage0= Integer.parseInt(parse2[0]);
		totalDamage1= Integer.parseInt(parse2[1]);
		
		Connection con = ConnectDb.connect();
		//query dB
		try {
			PreparedStatement ps = null;
			ResultSet rs=null;
			String sql ="SELECT *, AVG(damages) a, SUM(damages) t, accident_date FROM accidents INNER JOIN involvements on accidents.aid = involvements.aid GROUP BY involvements.aid "
					+"HAVING accident_date BETWEEN ? and ? "
					+"AND a BETWEEN ? AND ? AND t BETWEEN ? AND ?"
					;
			
			ps = con.prepareStatement(sql);
			
			ps.setString(1,date0);
			ps.setString(2, date1);
			ps.setInt(3, avgDamage0);
			ps.setInt(4, avgDamage1);
			ps.setInt(5, totalDamage0);
			ps.setInt(6, totalDamage1);
			
			rs = ps.executeQuery();
			//print rs 
			while(rs.next()) {
				System.out.println("Criteria Match--------------------------------");
				//printing the rs from the accidents table 
				int aid = rs.getInt("aid");
				int avg_d = rs.getInt("a");
				int total_d = rs.getInt("t");
				String accident_date = rs.getString("accident_date");
				String city =rs.getString("city");
				String state = rs.getString("state");
				System.out.println("accident id: "+ aid);
				System.out.println("accident date: "+accident_date);
				System.out.println("city: "+city);
				System.out.println("state: "+state);
				System.out.println("average damage: $"+avg_d);
				System.out.println("total damage: $"+total_d);
				System.out.println(" ");
			}
			
		}catch(SQLException e){
			//print error
			System.out.println(e.toString());
		}
		
		
		
		
		
		in.close();
	}
	
}
