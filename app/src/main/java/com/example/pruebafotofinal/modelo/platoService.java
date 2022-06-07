package com.example.pruebafotofinal.modelo;

import java.util.ArrayList;
import java.util.List;

public class platoService {
    public static List<plato> platos=new ArrayList<>();

    public static void addPlato(plato plato){
        platos.add(plato);  //agregar un nuevo plato
    }

    public static void  removePlato(plato plato){
        platos.remove(plato);   //eliminar un plato
    }

    public static void updatePlato(plato plato){
        platos.set(platos.indexOf(plato),plato); //busca la posicion para el update
    }
}
