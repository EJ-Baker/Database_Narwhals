import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SqlConnector {
	static Connection connection;
	
	public SqlConnector() throws ClassNotFoundException{
		
		Class.forName("org.sqlite.JDBC");
		connection = null;     
		
	    try  {   
	    	// create a database connection   
	    	connection = DriverManager.getConnection("jdbc:sqlite:marvelDatabase");
	    } catch (SQLException e) {
	    	System.err.println(e.getMessage());
	    } 
	}
	
	public static void closeConnection(){
		
		try  {
    		if(connection != null)   
    			connection.close();  
    		System.out.println("connection closed");
    	} catch(SQLException e) 
    	{        
    		// connection close failed.      
    		System.err.println(e);   
    		}
		
	}
	
	public static int countSpecies(String speciesType){
		
		int count = 0;
		
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			ResultSet rs = statement.executeQuery("select count(Character_id) AS speciesNum From Character where species = \'" + speciesType + "\'");
			
			
			count = rs.getInt("speciesNum");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
		
	}
	
	public static ArrayList<String> moviesInUniverse(String universeName){
		
		ArrayList<String> movieTitles = new ArrayList<String>();
		
		try{
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			ResultSet rs = statement.executeQuery("Select Movie_title From Movie, Series Where Movie.Series_id = Series.Series_id and world = \'" + universeName + "\'");
			
			while(rs.next()){
				
				movieTitles.add(rs.getString("Movie_title"));
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return movieTitles;
		
	}
		
	}