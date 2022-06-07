package com.example.pruebafotofinal.vistas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pruebafotofinal.R;
import com.example.pruebafotofinal.adapter.platoAdapter;
import com.example.pruebafotofinal.modelo.plato;
import com.example.pruebafotofinal.modelo.platoService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlatosFragment extends Fragment {

    RecyclerView rc; //declaramos el recycler

    public PlatosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_platos, container, false);
        //se declara view que es donde capturamos al inflater
        rc = view.findViewById(R.id.rc);
        //forma en la que se van a distribuir los elementos
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(RecyclerView.VERTICAL);

        platoAdapter adapter = new platoAdapter(platoService.platos,R.layout.item,getActivity());

        rc.setAdapter(adapter);

        cargarDatosFireBase();

        rc.setLayoutManager(lm);
        return view;
    }
    public void cargarDatosFireBase(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("PLATOS");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                plato plato = dataSnapshot.getValue(plato.class);
                plato.setId(dataSnapshot.getKey());

                if(!platoService.platos.contains(plato)){
                    platoService.addPlato(plato); //se llama al metodo addPlato que se declaro en platoService
                }
                rc.getAdapter().notifyDataSetChanged(); //metodo para que al tener un cambio se actualize la lista
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                plato plato = dataSnapshot.getValue(plato.class);
                plato.setId(dataSnapshot.getKey());

                if(platoService.platos.contains(plato)){
                    platoService.updatePlato(plato); //se llama al metodo updatePlato que se declaro en platoService
                }
                rc.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                plato plato = dataSnapshot.getValue(plato.class);
                plato.setId(dataSnapshot.getKey());

                if(platoService.platos.contains(plato)){
                    platoService.removePlato(plato);     //se llama al metodo removePlato que se declaro en platoService
                }
                rc.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}