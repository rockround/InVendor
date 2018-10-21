
import java.sql.*;

// Notice, do not import com.mysql.jdbc.*
// or you will have problems!

public class JDBCTest {
	public static void main(String[] args) {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            //Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println(verify("AkitoIto", "dfg"));
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	private static int verify(String username, String password) throws SQLException
	{
		Connection con = DriverManager.getConnection("jdbc:mysql://invendor-database.mysql.database.azure.com:3306/data_schema?serverTimezone=UTC", "invendoradmin@invendor-database", "UTDhack2018");
		Statement stmt = con.createStatement();
		
		ResultSet result = stmt.executeQuery("SELECT password_hash FROM data_schema.user where userName = '" + username + "'");
		int returnVal = 1;
		result.next();
		try {
			long tempPassword = result.getLong(1);
			con.close();
			return (int)(Math.abs(hashPassword(password) - tempPassword));
		}
		catch (SQLException e)
		{
			con.close();
			return -1;
		}
	}
	
	private static long hashPassword(String password)
	{
		// replace with more meaningful shit
		return (long)(password.hashCode());
	}
}