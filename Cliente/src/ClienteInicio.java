
import Interfaz.Menu;
import Interfaz.SolicitarServicio;
import Interfaz.Splash;
import Negocios.Conector;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

/** 
 * @author Jonnathan Campoberde
 */
public class ClienteInicio {
    
    public static void main(String[] args) {
        try {//Para el skin de las pantallas de la aplicacion
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Menu menu=new Menu();
        SolicitarServicio servicio=new SolicitarServicio();
        Splash splash=new Splash();
        //splash.setVisible(true);
        Conector conecta=new Conector(menu, servicio,splash);
        conecta.iniciarSplash();
    }

}
