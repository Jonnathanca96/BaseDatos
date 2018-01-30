package Negocios;

import Interfaz.SolicitarServicio;

/** 
 * @author Jonnathan Campoberde
 */
public class RestauranteDisponibilidad {
    
    String columnas;
    
    String condicion="";
    
    String tabla;
    
    String datosInsertar;
    
    String datosModificar;

    public String getDatosModificar() {
        return datosModificar;
    }

    public void setDatosModificar(String datosModificar) {
        this.datosModificar = datosModificar;
    }

    public String[] getServicioDatos(){
        String []datosServicio=SolicitarServicio.getDatosClienteServicio();
        return datosServicio;
    }
    
   public String disponibilidadRB(){
        String servicioBd=SolicitarServicio.getServicio();
        return servicioBd;
    }

    public String getColumnas() {
        return columnas;
    }

    public void setColumnas(String columnas) {
        this.columnas = columnas;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getDatosInsertar() {
        return datosInsertar;
    }

    public void setDatosInsertar(String datosInsertar) {
        this.datosInsertar = datosInsertar;
    }
    
}
