package com.example.pruebafotofinal.vistas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pruebafotofinal.R;
import com.example.pruebafotofinal.modelo.plato;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.pruebafotofinal.PrincipalActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NuevoPlatoFragment extends Fragment {
    EditText txtnombre, txtprecio,txtdescripcion;
    Button btn,btn_Galeria,btn_Camara;
    ImageView img;

    //url de la imagen
    Uri uri_img;

    public NuevoPlatoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nuevo_plato, container, false);

        txtnombre = view.findViewById(R.id.txtnombre);
        txtprecio = view.findViewById(R.id.txtprecio);
        txtdescripcion = view.findViewById(R.id.txtdescripcion);
        btn = view.findViewById(R.id.btn_add);
        btn_Galeria = view.findViewById(R.id.btn_abrirGaleria);
        btn_Camara = view.findViewById(R.id.btn_abrirCamara);
        img = view.findViewById(R.id.plato_img);

        //guardamos en firebase
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = txtnombre.getText().toString();
                String pre = txtprecio.getText().toString();
                String des = txtdescripcion.getText().toString();

                if (nom.equals("") || pre.equals("") || des.equals("") || img.getDrawable() == null)
                {
                    validacion();
                }
                else
                {
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("Subiendo el registro...");

                    progressDialog.show();

                    StorageReference storageRef = FirebaseStorage.getInstance().getReference(); //obtenemos la instacia y obtenemos la referencia a el storage
                    StorageReference folderRef = storageRef.child("PLATILOS_IMG"); //creamos la carpeta dentro del storage
                    StorageReference fotoRef = folderRef.child(new Date().toString()); //creamos la carpeta de las fotos, le agregamos la fecha para que se diferencie de las demas

                    fotoRef.putFile(uri_img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() { //subimos la imagen a firebase
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl(); //obtenemos la url de descarga de nuestra foto en firebase
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadUri = uriTask.getResult();

                            plato plato = new plato();
                            plato.setNombre(txtnombre.getText().toString());
                            plato.setPrecio(Double.parseDouble(txtprecio.getText().toString()));
                            plato.setDescripcion(txtdescripcion.getText().toString());

                            plato.setUrl(downloadUri.toString()); //le ponemos la uwl a el plato

                            //insercion de datos
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("PLATOS");
                            reference.push().setValue(plato);


                            Toast.makeText(getActivity(), "Agregado", Toast.LENGTH_LONG).show();
                            limpiarCajas();
                            img.setImageResource(0);
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Subiendo a la base de datos... "+(int)progress+"%");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Fallo el registro "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        //metodo para abrir la galeria
        btn_Galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PrincipalActivity.CODE_GALLERY); //llamamos a nuestras constantes de accion para la galeria

            }
        });

        btn_Camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //capturar la imagen de la camara
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //leemos los permisos previamente declarados en el manifest
                File foto = new File(getActivity().getExternalFilesDir(null),"test.jpg"); //guardamos la imagen como un archivo temporal
                uri_img = FileProvider.getUriForFile(getActivity(),getActivity().getPackageName()+".provider",foto);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri_img);
                startActivityForResult(intent,PrincipalActivity.CODE_CAMERA); //al iniciar la camara se va a ejecutar una accion, y al hacer la foto devuelve un codigo que va al principal activity
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)//switch para tambien usar tanto la camara o la galeria
        {
            case PrincipalActivity.CODE_GALLERY: //en caso del que principalactivity. devuelva un code ya sea camera o gallery se ejecutaran estos codigos
                if(data != null){
                    uri_img = data.getData(); //obtiene los datos de la url de la imagen
                    try{
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri_img);
                        img.setImageBitmap(bitmap);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PrincipalActivity.CODE_CAMERA:
                Bitmap bitmap = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null)+"/test.jpg");//decodificamos la imagen con bitmapfactory
                img.setImageBitmap(bitmap);
                break;
        }
    }
    public void limpiarCajas(){
        txtnombre.setText("");
        txtdescripcion.setText("");
        txtprecio.setText("");
        img.setImageBitmap(null);
    }
    public void validacion(){
        String nom=txtnombre.getText().toString();
        String pre=txtprecio.getText().toString();
        String det=txtdescripcion.getText().toString();

        if(nom.equals("")){
            txtnombre.setError("Agregue un nombre al platillo");
        }
        else if(pre.equals("")){
            txtprecio.setError("Agregue un precio");
        }
        else if(det.equals("")){
            txtdescripcion.setError("Agregue detalles");
        }
        else if(img.getDrawable() == null)
        {
            Toast.makeText(getActivity(), "Falta seleccionar una imagen de su galeria", Toast.LENGTH_LONG).show();
        }
    }
}