import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SqlConnector {
	static Connection connection;
	static ResultSet rs;
	
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
			
			rs = statement.executeQuery("select count(Character_id) AS speciesNum From Character where species = \'" + speciesType + "\'");
			
			
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
			
			rs = statement.executeQuery("Select Movie_title From Movie, Series Where Movie.Series_id = Series.Series_id and world = \'" + universeName + "\'");
			
			while(rs.next()){
				
				movieTitles.add(rs.getString("Movie_title"));
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return movieTitles;
		
	}
		

//sql queries should be obvious what they do from method names
	public ArrayList<String> moviesByReleaseDate() throws SQLException {
	
		ArrayList<String> movieTitles = new ArrayList<String>();
		
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);
	
		rs = statement.executeQuery("select movie_title from movie order by release_date");
	
		while(rs.next()){
			movieTitles.add(rs.getString("movie_title"));
		}
		
		return movieTitles;
	}

	public ArrayList<String> moviesByTimeline() throws SQLException {
	
		ArrayList<String> movieTitles = new ArrayList<String>();
	
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);

		rs = statement.executeQuery("select movie_title from voie natural join abitrary_timeline order by Date");
		
		while(rs.next()){
			movieTitles.add(rs.getString("movie_title"));
		}
		
		return movieTitles;
	}

	public ArrayList<String> listCharacters() throws SQLException {
		
		ArrayList<String> characterName = new ArrayList<String>();
		
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);
		
		rs = statement.executeQuery("select name from character_name natural join character order by species");
		
		while(rs.next()){
			characterName.add(rs.getString("name"));
		}
		
		return characterName;
		
	}

	public ArrayList<String> listSeries() throws SQLException {
		
		ArrayList<String> worlds = new ArrayList<String>();
		
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);
		
		rs = statement.executeQuery("select world from series");
		
		while(rs.next()){
			worlds.add(rs.getString("name"));
		}
		
		return worlds;
		
	}

	public ArrayList<String> searchMovieByCharacter(String characterName) throws SQLException {
		
		ArrayList<String> movieTitles = new ArrayList<String>();
		
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);
		
		rs = statement.executeQuery("select movie_title from movie natural join exists_in natural join character_name where name = \'" + characterName + "\'");
		
		while(rs.next()){
			movieTitles.add(rs.getString("movie_title"));
		}
		
		return movieTitles;
		
	}	

	ArrayList<String> searchMovieByActor(String actorName) throws SQLException {
		
		ArrayList<String> movieTitles = new ArrayList<String>();
		
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);
		
		rs = statement.executeQuery("select movie_title from movie natural join acts_in natural join actor where actor_name = \'" + actorName + "\'");
		
		while(rs.next()){
			movieTitles.add(rs.getString("movie_title"));
		}
		
		return movieTitles;
	}

	ArrayList<String> searchMovieByDirector(String directorName) throws SQLException {
		
		ArrayList<String> movieTitles = new ArrayList<String>();
		
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);
		
		rs = statement.executeQuery("select movie_title from director where name = \'" + directorName + "\'");
		
		while(rs.next()){
			movieTitles.add(rs.getString("movie_title"));
		}
		
		return movieTitles;
	}

	public String characterInMostMovies() throws SQLException {
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);

		rs = statement.executeQuery("select name from character_name natural join character where character_id = (select character_id from character_name natural join exists_in group by character_id having max(count(*)))");
	
		return rs.getString("name");
	}

//user manipulatoin of tables
	void userInsertIntoAbility(String abilityName, String characterName) throws SQLException {
	
		
		PreparedStatement ps =  connection.prepareStatement("insert values into ability(\'" + abilityName + "\', select character_id from character_name where name = \'" + characterName + "\')");
		ps.executeUpdate();
	}

void userDeleteAbility(String abilityName, String characterName) throws SQLException {
	//same as above I'm not sure what the command is
	PreparedStatement ps = connection.prepareStatement("delete from ability where ability = \'" + abilityName + " and character_id = (select character_id from character_name where name = \'" + characterName + "\');");

	ps.executeUpdate();
}

void renameAnAbility(String oldName, String newName) throws SQLException {
	//once again not sure if it's called update
	PreparedStatement ps = connection.prepareStatement("update ability set type = \'" + newName + "\' where type = \'" + oldName + "\'");

	ps.execute();
}

//queries that use joins
ArrayList<String> listAbilitiesOfCharacter(String characterName) throws SQLException {
	
	ArrayList<String> abilities = new ArrayList<String>();
	
	Statement statement = connection.createStatement();
	statement.setQueryTimeout(30);

	rs = statement.executeQuery("select type from ability natural join character_name where character_name = \'" + characterName + "\';");
	
	while(rs.next()){
		abilities.add(rs.getString("type"));
	}
	
	return abilities;

}

ArrayList<String> listSuperheroesWithNoPower() throws SQLException {
	ArrayList<String> characterName = new ArrayList<String>();
	
	Statement statement = connection.createStatement();
	statement.setQueryTimeout(30);

	rs = statement.executeQuery("select name from character_name.character_id = ability.character_id where type = null;");
	
	while(rs.next()){
		characterName.add(rs.getString("name"));
	}
	
	return characterName;
}
}
