package Negocios;

import java.awt.Color;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

public class Cargar extends Thread {

    JProgressBar progress;

    public Cargar(JProgressBar progress) {
        super();
        this.progress = progress;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            progress.setValue(i);
            pausa(20);
        }
    }

    public void pausa(int mlSeg) {
        try {
            Thread.sleep(mlSeg);
        } catch (Exception e) {

        }
    }
}
