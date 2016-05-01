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

void moviesByReleaseDate() {
	rs = statement.executeQuery("select movie_title from movie order by release_date;");
}

void moviesByTimeline() {
	rs = statement.executeQuery("select name, species from character_name natural join character order by species;")
}

void listCharacters() {
	rs = statement.executeQuery("select name from character_name natural join character order by species;");
}

void listSeries() {
	rs = statement.executeQuery("select world from series;");
}

void numberOfSpecies(String species) {
	rs = statment.executeQuery("select count(characer_id) from character where species = \'" + species "\';");
}

void searchMovie(String movieName) {
	rs = statement.executeQuery("select movie_title from movie where movie_title = \'" + species + "\';");
}

void searchMovieByCharacter(String characterName) {
	rs = statement.executeQuery("select movie_title from movie natural join exists_in natural join character_name where name = \'" + characterName "\';");
}

void searchMovieByWorld(String worldName) {
	rs = statement.executeQuery("select movie_title from movie natural join series where world = \'" + worldName + "\';");
}

void searchMovieByActor(String actorName) {
	rs = statement.executeQuery("select movie_title from movie natural join acts_in natural join actor where actor_name = \'" + actorName + "\';");
}

void searchMovieByDirector(String directorName) {
	rs = statement.executeQuery("select movie_title from director where name = \'" + directorName + "\';");
}


