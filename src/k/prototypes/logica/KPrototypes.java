/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k.prototypes.logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author user
 */
public class KPrototypes {

    ArrayList<ArrayList<String>> firstprototypes, Lastprototipo, datos;
    int ks,iteraciones,atributos; 
    String firstDatos;

    public KPrototypes(ArrayList<ArrayList<String>> datos, int ks) {

        this.datos = datos;
        atributos = datos.get(0).size();
        this.ks = ks;

    }

    public String algoritmo() {
        //Paso 2: Se eligen k-protipos aleatoriamente
        iteraciones=0;
        boolean flag;
        int numero;
        ArrayList<ArrayList<String>> prototipos = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < ks; i++) {
            flag = true;
            while (flag) {
                numero = (int) (Math.random() * datos.size());
                if (!prototipos.contains(datos.get(numero))) {
                    prototipos.add(datos.get(numero));
                    flag = false;
                }
            }
        }

        firstprototypes = (ArrayList<ArrayList<String>>) prototipos.clone();
        //Paso 3 y paso 4

        distancia(prototipos);
        
        //Asignación inicial de los objetos al prototipo 
        
       firstDatos="";
        for (int i = 0; i < datos.size(); i++) {
            firstDatos += "ID: " + i + " ";
            for (int j = 0; j < atributos + 1; j++) {
                
                if (j == atributos) {
                    firstDatos += "Prototipo: " + datos.get(i).get(j) + "\n";
                } else {
                    firstDatos += datos.get(i).get(j) + " ";
                }

            }
        }
        

        

        //PASO 5 y 6: K-PROTOTIPOS SERAN RECALCULADOS      
        Lastprototipo = (ArrayList<ArrayList<String>>) prototipos.clone();

        prototipos = calculoPrototipos(prototipos);

        //Paso 8 y 9
        while (!Lastprototipo.equals(prototipos)) {
            iteraciones+=1;
            distancia(prototipos);
            Lastprototipo = (ArrayList<ArrayList<String>>) prototipos.clone();
            prototipos = calculoPrototipos(prototipos);

        }
        
        return documentar();

    }

    public boolean isInteger(String numero) {
        try {
            Integer.parseInt(numero);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public ArrayList<String> newPrototipo() {
        ArrayList<String> dato = new ArrayList<String>();
        for (int i = 0; i < atributos; i++) {
            dato.add("0");
        }
        return dato;
    }

    public void distancia(ArrayList<ArrayList<String>> prototipos) {
        for (int i = 0; i < datos.size(); i++) {
            ArrayList<String> dato = datos.get(i);
            double distancia = Double.POSITIVE_INFINITY;
            int asignacion = 0;
            for (int j = 0; j < ks; j++) {
                ArrayList<String> prototipo = prototipos.get(j);
                double distanciaInt = 0;
                double distanciaString = 0;
                for (int l = 0; l < atributos; l++) {
                    if (isInteger(dato.get(l))) {
                        double datoint = Double.parseDouble(dato.get(l));
                        double prototipoint = Double.parseDouble(prototipo.get(l));
                        distanciaInt += Math.pow(datoint - prototipoint, 2);
                    } else {
                        if (!dato.get(l).equals(prototipo.get(l))) {
                            distanciaString += 1;
                        }
                    }
                }
                distanciaInt = Math.sqrt(distanciaInt);
                if (distancia > (distanciaInt + distanciaString)) {                   
                    distancia = distanciaInt + distanciaString;
                    asignacion = j;
                }
            }

            //PASO 4: ASIGNACION DE PROTOTIPO
            if (dato.size() > atributos) {               
                dato.set(atributos, Integer.toString(asignacion));
            } else {
                dato.add(Integer.toString(asignacion));
            }
            datos.set(i, dato);

        }
    }

    public ArrayList<ArrayList<String>> calculoPrototipos(ArrayList<ArrayList<String>> prototipos) {
        //PASO 5: K-PROTOTIPOS SERAN RECALCULADOS
        for (int i = 0; i < ks; i++) {
            ArrayList<String> prototipoN = newPrototipo();
            for (int j = 0; j < atributos; j++) {
                boolean isInt = false;
                int asignados = 0;
                Map<String, Integer> diccionario = new HashMap<String, Integer>();
                for (int t = 0; t < datos.size(); t++) {
                    if (datos.get(t).get(atributos).equals(Integer.toString(i))) {
                        ArrayList<String> dato = datos.get(t);
                        asignados += 1;
                        if (isInteger(dato.get(j))) {
                            int datoint = Integer.parseInt(dato.get(j));
                            int protoInt = Integer.parseInt(prototipoN.get(j));
                            prototipoN.set(j, Integer.toString(datoint + protoInt));
                            isInt = true;
                        } else {
                            if (diccionario.containsKey(dato.get(j))) {
                                diccionario.computeIfPresent(dato.get(j), (k, v) -> v + 1);
                            } else {
                                diccionario.put(dato.get(j), 1);
                            }
                        }
                    }
                }
                if (isInt) {
                    double protoInt = Double.parseDouble(prototipoN.get(j));
                    prototipoN.set(j, String.valueOf(protoInt / asignados));
                } else {
                    Iterator it = diccionario.keySet().iterator();
                    int comparacion = 0;
                    String moda = "";
                    while (it.hasNext()) {
                        String key = (String) it.next();
                        if (comparacion < diccionario.get(key)) {
                            comparacion = diccionario.get(key);
                            moda = key;
                        }

                    }
                    prototipoN.set(j, moda);

                }

            }
            prototipos.set(i, prototipoN);
        }

        return prototipos;
    }

    public ArrayList<ArrayList<String>> getFirstprototypes() {
        return firstprototypes;
    }

    public void setFirstprototypes(ArrayList<ArrayList<String>> firstprototypes) {
        this.firstprototypes = firstprototypes;
    }

    public ArrayList<ArrayList<String>> getDatos() {
        return datos;
    }

    public void setDatos(ArrayList<ArrayList<String>> datos) {
        this.datos = datos;
    }

    public String documentar() {
        String Documento = "K-Prototypes iniciales \n \n";

        for (int i = 0; i < firstprototypes.size(); i++) {
            Documento += "ID: " + i + " ";
            for (int j = 0; j < atributos; j++) {
                
                if (j == atributos - 1) {
                    Documento += firstprototypes.get(i).get(j) + "\n";
                } else {
                    Documento += firstprototypes.get(i).get(j) + " ";
                }

            }
        }

        Documento += "\nAsignación inicial de los objetos al prototipo \n \n" + firstDatos;
       

        Documento += "\nK-Prototypes finales \n \n";

        for (int i = 0; i < Lastprototipo.size(); i++) {
             Documento += "ID: " + i + " ";
            for (int j = 0; j < atributos; j++) {
               
                if (j == atributos - 1) {
                    Documento += Lastprototipo.get(i).get(j) + "\n";
                } else {
                    Documento += Lastprototipo.get(i).get(j) + " ";
                }

            }
        }

        Documento += "\nAsignación final de los objetos al prototipo \n \n";

        for (int i = 0; i < datos.size(); i++) {
            Documento += "ID: " + i + " ";
            for (int j = 0; j < atributos + 1; j++) {
                
                if (j == atributos) {
                    Documento += "Prototipo: " + datos.get(i).get(j) + "\n";
                } else {
                    Documento += datos.get(i).get(j) + " ";
                }

            }
        }  
        
      
        return Documento;

    }

}
