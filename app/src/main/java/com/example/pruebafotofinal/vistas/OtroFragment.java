package com.example.pruebafotofinal.vistas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pruebafotofinal.R;
import com.example.pruebafotofinal.lectorQR.IntentIntegrator;
import com.example.pruebafotofinal.lectorQR.IntentResult;
import com.example.pruebafotofinal.modelo.plato;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtroFragment extends Fragment {
    EditText txt_buscar;
    Button btn_Buscar, btn_Scan;

    public OtroFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otro, container, false);

        txt_buscar = view.findViewById(R.id.txt_buscar);
        btn_Buscar = view.findViewById(R.id.btn_Buscar);
        btn_Scan = view.findViewById(R.id.btn_Scan);

        btn_Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance(); //aperturamos nuestra firebase
                DatabaseReference reference = database.getReference("PLATOS"); //seleccionamos a que rama vamos a referenciar
                //armamos un query para la busqueda
                //tomamos una consulta por hijo
                Query query = reference.orderByChild("nombre").equalTo(txt_buscar.getText().toString()); //realizamos la comparacion de datos.

                //si la query es correcta se ejecuta este listener
                //datasnapshot contiene toda la informacion del platillo
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren())//recorrer cada hijo
                        {
                            plato p = ds.getValue(plato.class);
                            p.setId(ds.getKey());
                            Log.e("info","---------->"+p.getNombre());
                            Toast.makeText(getActivity(),p.getNombre()+" S/."+p.getPrecio()+ '\n' + p.getDescripcion(),Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("info","---------->");
                    }
                });
            }
        });

        btn_Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator cam = new IntentIntegrator(getActivity());
                cam.initiateScan(); //iniciamos la camara para el scaneo qr
            }
        });
        return view;
    }

    @Override//metodo para recepcionar los datos del codigo qr
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(intentResult != null)
        {
            String contenido = intentResult.getContents();
            txt_buscar.setText(contenido);
            Toast.makeText(getActivity(),contenido,Toast.LENGTH_SHORT).show();
            Log.e("info",contenido);

        }
        else
        {
            Toast.makeText(getActivity(),"Ups, hubo un problema...",Toast.LENGTH_SHORT).show();
        }
    }
}