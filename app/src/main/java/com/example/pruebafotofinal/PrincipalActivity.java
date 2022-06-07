package com.example.pruebafotofinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.pruebafotofinal.lectorQR.IntentIntegrator;
import com.example.pruebafotofinal.vistas.NuevoPlatoFragment;
import com.example.pruebafotofinal.vistas.OtroFragment;
import com.example.pruebafotofinal.vistas.PlatosFragment;
import com.google.android.material.tabs.TabLayout;

public class PrincipalActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    AdapterPager adapterPager;

    //fragmentos
    PlatosFragment p1;
    NuevoPlatoFragment p2;
    OtroFragment p3;

    //constantes de accion
    public static final int CODE_CAMERA = 21;
    public static final int CODE_GALLERY = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        toolbar = findViewById(R.id.tool_bar);
        tabLayout = findViewById(R.id.tab_bar);
        viewPager = findViewById(R.id.view_pager);

        //creamos los fragmentos
        p1  = new PlatosFragment();
        p2  = new NuevoPlatoFragment();
        p3  = new OtroFragment();

        setSupportActionBar(toolbar);

        //definir configuraciones del nuevo adaptador(adapterpager)
        adapterPager = new AdapterPager(getSupportFragmentManager());

        //declaramos el adapterpager
        viewPager.setAdapter(adapterPager);

        //hereda la configuracion del viewpager
        tabLayout.setupWithViewPager(viewPager);

        //definimos nuestros iconos
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_platos);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_nuevo);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_otro);


    }
    public class AdapterPager extends FragmentPagerAdapter{


        public AdapterPager(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) { // 0 - 1 - 2  para pasar de fragment a otro
            switch (position){
                case 0:
                    return p1;
                case 1:
                    return p2;
                case 2:
                    return p3;
            }
            return null;
        }

        @Nullable
        @Override //metodo para el nombre de los tab
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Platos";
                case 1:
                    return "Nuevo";
                case 2:
                    return "Otro";
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //identificar que accion se ha ejecutado - sirve para identificar los distintos tipos de casos de los botones accionados en el fragment. nuevo plato y que no se pierda el foco
        switch (requestCode){ //y estos se envian al fragment nuevoplato
            case IntentIntegrator.REQUEST_CODE: //esto es para el scan
                p3.onActivityResult(requestCode,resultCode,data);
                break;

            case CODE_GALLERY: //galeria
                p2.onActivityResult(requestCode,resultCode,data);
                break;

            case CODE_CAMERA: //camara
                p2.onActivityResult(requestCode,resultCode,data);
                break;

        }

    }
}