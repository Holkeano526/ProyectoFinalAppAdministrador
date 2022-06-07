package com.example.pruebafotofinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pruebafotofinal.modelo.plato;
import com.example.pruebafotofinal.modelo.platoService;
import com.example.pruebafotofinal.adapter.platoAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {
    EditText txtnombre, txtprecio;
    RecyclerView rc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtnombre = findViewById(R.id.txtnombre);
        txtprecio = findViewById(R.id.txtprecio);
        rc = findViewById(R.id.rc);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(RecyclerView.VERTICAL);
        rc.setLayoutManager(lm);

        platoAdapter adapter = new platoAdapter(platoService.platos,R.layout.item,this);

        rc.setAdapter(adapter);

        cargarDatosFireBase();
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

    public void agregar(View view) {
        plato plato = new plato();
        plato.setNombre(txtnombre.getText().toString());
        plato.setPrecio(Double.parseDouble(txtprecio.getText().toString()));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("PLATOS");
        reference.push().setValue(plato);
    }
}