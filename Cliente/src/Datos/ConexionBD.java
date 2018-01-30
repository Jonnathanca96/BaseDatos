package Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Jonnathan Campoberde
 */
public class ConexionBD {

    private static final String urlConexion = "jdbc:oracle:thin:@localhost:1521:XE";

    private static boolean stateConnect = false;

    public  static Connection conexionBD = null;
    
    
public static boolean conectarBD() {

        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            conexionBD = DriverManager.getConnection(urlConexion, "CLIENTE", "1234");
            stateConnect = true;
            conexionBD.setAutoCommit(false);

        } catch (SQLException ex) {
           stateConnect=false;
        }finally
        {        
            return stateConnect;
        }
    }

    public static void cerrarConexionBD()  {
        if (conexionBD != null) {
            try {
                if(!conexionBD.isClosed()){
                    conexionBD.close();
                    stateConnect=false;
                    conexionBD=null;
                }
            } catch (SQLException ex) {
                stateConnect=false;
                conexionBD=null;
            }
            
        }  
    }
    
    public boolean rollbackBD(){
        try {
            this.conexionBD.rollback();
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
    
    public static Connection getConnection(){
        return conexionBD;
    }
    
    public static boolean getStateConection(){
        return stateConnect;
    }
    
    public void cerrarConexion(){
        try {
            conexionBD.close();
        } catch (Exception ex) {
            conexionBD=null;
        }
    }
    public boolean commitBD(){
         try {
            this.conexionBD.commit();
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
   
   
    
}
