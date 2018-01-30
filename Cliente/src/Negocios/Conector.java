package Negocios;

import Datos.ConectorBD;
import Interfaz.Menu;
import Interfaz.SolicitarServicio;
import Interfaz.Splash;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * @author Jonnathan Campoberde
 */
public class Conector implements ActionListener {

    public static Menu menu;
    public static Splash splash;
    public static SolicitarServicio servicio;
    public static RestauranteDisponibilidad restaDis;
    static ConectorBD conectorBD = new ConectorBD();

    public Conector(Menu menu, SolicitarServicio servicio, Splash splash) {
        this.servicio = servicio;
        this.menu = menu;
        this.splash = splash;
        this.menu.jbtnRealizarPedido.addActionListener(this);
        this.servicio.jBtnSolicitarPedido.addActionListener(this);
    }

    public Conector() {
    }

    public static void stopSplash() {
        splash.stop();
    }

    public static void iniciarSplash() {
        splash.setVisible(true);
    }

    public static void stopSolicitarServicio() {
        servicio.stop();
    }

    public static void iniciarSolicitarServicio() {
        servicio.setVisible(true);
    }

    public static void stopMenu() {
        menu.stop();
    }

    public static void iniciarMenu() {
        menu.setVisible(true);
        menu.setDatosServicio();
    }

    public static String[] getDisponibles(String servicio) {
        ArrayList<String[]> datosRespuestaConsulta = new ArrayList<>();
        String[] disponibles = null;
        restaDis = new RestauranteDisponibilidad();
        if (servicio.compareTo("Restaurante") == 0) {
            restaDis.setTabla("MESAS");
            restaDis.setColumnas("CODIGO_MESA");
            restaDis.setCondicion("ESTADO=0");
        } else {
            restaDis.setTabla("BARRA_TARJETAS");
            restaDis.setColumnas("CODIGO_TARJETA");
            restaDis.setCondicion("ESTADO=0");
        }

        datosRespuestaConsulta = conectorBD.selecionar(restaDis);

        if (datosRespuestaConsulta != null) {
            disponibles = new String[datosRespuestaConsulta.size() - 1];
            //System.out.println("La longitud de los array es:" + disponibles.length);
            String[] datosAuxiliar = new String[1];
            for (int i = 1; i < datosRespuestaConsulta.size(); i++) {
                datosAuxiliar = datosRespuestaConsulta.get(i);
                disponibles[i - 1] = datosAuxiliar[0];
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay disponibilidad", "Asignacion.", JOptionPane.INFORMATION_MESSAGE);
        }
        return disponibles;
    }

    public static ArrayList<String[]> getProductos(String servicio) {
        ArrayList<String[]> datosRespuestaConsulta = new ArrayList<>();
        String[] disponibles = null;
        restaDis = new RestauranteDisponibilidad();
        if (servicio.compareTo("Restaurante") == 0) {
            restaDis.setTabla("PRODUCTOS");
            restaDis.setColumnas("CODIGO,NOMBRE,DESCRIPCION,PRECIO");
            restaDis.setCondicion("SERVICIO_PROD='R' ORDER BY CODIGO");
        } else {
            restaDis.setTabla("PRODUCTOS");
            restaDis.setColumnas("CODIGO,NOMBRE,DESCRIPCION,PRECIO");
            restaDis.setCondicion("SERVICIO_PROD='B' ORDER BY CODIGO");
        }

        datosRespuestaConsulta = conectorBD.selecionar(restaDis);
        return datosRespuestaConsulta;
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        String evento = e.getActionCommand().toString();
        String codPedido = null;
        if (evento.compareTo("Pedir Servicio") == 0) {
            //System.out.println("Evento falso");
            restaDis = new RestauranteDisponibilidad();
            String[] datosServicio = restaDis.getServicioDatos();
            if (datosServicio[0].compareTo("Restaurante") == 0) {
                restaDis.setTabla("MESAS");
                restaDis.setDatosModificar("ESTADO=1");
                restaDis.setCondicion("CODIGO_MESA=" + datosServicio[1]);
            } else {
                restaDis.setTabla("BARRA_TARJETAS");
                restaDis.setDatosModificar("ESTADO=1");
                restaDis.setCondicion("CODIGO_TARJETA=" + datosServicio[1]);
            }

            //System.out.println("Los datos son: " + restaDis.getTabla() + ", " + restaDis.getDatosModificar() + ", " + restaDis.getCondicion());
            if (conectorBD.update(restaDis)) {
                JOptionPane.showMessageDialog(null, "La reserva se realiso exitosamente.", "Reserva", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "La reserva no realiso no hay mesas disponibles.", "Reserva", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        } else if (evento.compareTo("Realizar Pedido") == 0) {
            ArrayList<String> pedidos = menu.getPedido();
            String[] datosPedido = pedidos.get(0).split(",");//Entre los datos esta 
            String servicioUsuario = datosPedido[0];//servicio bar o restaturante
            //System.out.println("Servicio Cliente:" + servicioUsuario);
            String lugarEncuentra = datosPedido[1];
            //System.out.println("Lugar en donde se encuentra" + lugarEncuentra);//donse ests ubicado
            restaDis = new RestauranteDisponibilidad();
            restaDis.setTabla("PEDIDOS");
            restaDis.setColumnas("MAX(COD_PEDIDO)");
            restaDis.setCondicion("");
            ArrayList<String[]> consulta = conectorBD.selecionar(restaDis);
            String mayor = consulta.get(1)[0];
            int numInserPedi = Integer.parseInt(mayor) + 1;
            //System.out.println("El numero del pedido es:" + numInserPedi);
            //System.out.println("El tamaño de pedidos es:" + pedidos.size());
///////////////////////////////////////////////////////////////////////////////////////////////
            
            restaDis = new RestauranteDisponibilidad();
            restaDis.setTabla("PEDIDOS");
            restaDis.setDatosInsertar(String.valueOf(numInserPedi)+",SYSDATE,"+"0");
            conectorBD.insert(restaDis);
                

///////////////////////////////////////////////////////////////////////////////////////////////
            for (int i = 1; i < pedidos.size(); i++) {
                restaDis = new RestauranteDisponibilidad();
                restaDis.setTabla("PEDIDO_PRODUCTOS");
                //System.out.println("Veces " + i+" "+pedidos.get(i));
                restaDis.setDatosInsertar(String.valueOf(numInserPedi) + "," + pedidos.get(i));
                conectorBD.insert(restaDis);
            }
            ////////
            
            restaDis = new RestauranteDisponibilidad();
            if (servicioUsuario.compareTo("Restaurante: ") == 0) {
                restaDis.setTabla("PEDIDO_MESAS");
                restaDis.setDatosInsertar(lugarEncuentra + "," + String.valueOf(numInserPedi));
            } else {
                restaDis.setTabla("PEDIDO_BARRA_TARJETAS");
                restaDis.setDatosInsertar(lugarEncuentra + "," + String.valueOf(numInserPedi));
            }

            if (!conectorBD.insert(restaDis)) {
                JOptionPane.showMessageDialog(null, "Algo salio mal en el pedido por favor acercese a caja.", "Pedido Estado", JOptionPane.INFORMATION_MESSAGE);
                conectorBD.rollbackBD();
            }else{
                JOptionPane.showMessageDialog(null, "El pedido fue procesado correctamente.", "Pedido Estado", JOptionPane.INFORMATION_MESSAGE);
                conectorBD.commitBD();
            }
        }
    }

}
