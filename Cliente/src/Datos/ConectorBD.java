package Datos;

import Negocios.RestauranteDisponibilidad;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Jonnathan Campoberde
 */

public class ConectorBD extends ConexionBD {

    boolean conexionExitosa = ConexionBD.conectarBD();

    public synchronized ArrayList<String[]> selecionar(RestauranteDisponibilidad objeto) {//objeto.getCondicion por defecto va a ser ""
        //En caso de que me devuelva null es porque no se encontro respuestas a la consulta
        ArrayList<String[]> arrayRespuesta = new ArrayList<>();
        String sql = "SELECT ";
        Statement consulta = null;
        ResultSet respuesta = null;
        String[] objetoRespuesta = null;
        ResultSetMetaData miMetaData;
        try {
            if (objeto.getCondicion().compareTo("") == 0) {
                sql = sql + objeto.getColumnas() + " FROM BAR_RESTAURANTE." + objeto.getTabla();
            } else {
                sql = sql + objeto.getColumnas() + " FROM BAR_RESTAURANTE." + objeto.getTabla() + " WHERE " + objeto.getCondicion() + " ";
            }
            consulta = conexionBD.createStatement();

            respuesta = consulta.executeQuery(sql);
            miMetaData = respuesta.getMetaData();
            int columnas = respuesta.getMetaData().getColumnCount();
            String camposColumna[] = new String[columnas];

            for (int i = 0; i < columnas; i++) {

                camposColumna[i] = miMetaData.getColumnName(i + 1);
            }

            arrayRespuesta.add(camposColumna);

            if (respuesta != null) {

                while (respuesta.next()) {
                    objetoRespuesta = new String[columnas];
                    for (int i = 0; i < columnas; i++) {
                        objetoRespuesta[i] = respuesta.getString(i + 1);
                        //System.out.println("Los valores son:" + objetoRespuesta[i]);
                    }
                    arrayRespuesta.add(objetoRespuesta);
                }
            }

        } catch (SQLException ex) {
            try {
                respuesta.close();
                consulta.close();
            } catch (SQLException ex1) {
                respuesta = null;
                consulta = null;
            }
            arrayRespuesta = null;
        }
        return arrayRespuesta;
    }

    public synchronized boolean insert(RestauranteDisponibilidad objeto) {
        boolean exitoConsulta = false;
        Statement consulta = null;
        String sql = "INSERT INTO BAR_RESTAURANTE." + objeto.getTabla() + " VALUES (" + objeto.getDatosInsertar()+") ";
        try {
            consulta = conexionBD.createStatement();
            //System.out.println("La consulta:"+sql);
            consulta.execute(sql);
            exitoConsulta = true;
            //conexionBD.commit();

        } catch (Exception e) {
           /* try {
                conexionBD.rollback();
            } catch (SQLException ex) {
                exitoConsulta = false;
            }*/
            System.out.println("Excepcion:"+e.getMessage());
            exitoConsulta = false;
        }

        return exitoConsulta;
    }

    public boolean update(RestauranteDisponibilidad objeto) {
        boolean exitoConsulta = false;
        //PreparedStatement consulta = null;
        Statement consulta = null;
        //UPDATE HR.DEPARTMENTS SET DEPARTMENT_NAME= ?,MANAGER_ID= ?,LOCATION_ID=? WHERE DEPARTMENT_ID=?"
        String sql = "UPDATE BAR_RESTAURANTE." + objeto.getTabla() + " SET " + objeto.getDatosModificar() + " WHERE " + objeto.getCondicion();
        // System.out.println("Consulta:"+cosulta);
        //"UPDATE BAR_RESTAURANTE.? SET ? WHERE ?"
        //System.out.println("La consulta es:" + sql);
        try {
            consulta = conexionBD.createStatement();
            consulta.execute(sql);
            //conexionBD.commit();
            exitoConsulta = true;
            conexionBD.commit();
        } catch (Exception e) {
            System.out.println("Excepcion " + e.getMessage());
            try {
                conexionBD.rollback();
            } catch (SQLException ex) {
                exitoConsulta = false;
            }
            exitoConsulta = false;
        }

        return exitoConsulta;
    }

}
