package com.example.droidbarv1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.droidbarv1.R;
import com.example.droidbarv1.model.data.Contactos;
import com.example.droidbarv1.model.data.Factura;
import com.example.droidbarv1.view.adapters.ContactosAdapter;

import java.util.ArrayList;

public class EmailActivity extends AppCompatActivity {

    protected final int SOLICITUD_PERMISO_CONTACTOS=0;
    private static final String A1 ="ABC" ;
    private ArrayList<Contactos> datos_contactos = new ArrayList<Contactos>();


    private RecyclerView rvContactos;
    private ContactosAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Factura factura = new Factura();
    private String nomEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        Toolbar toolbar = findViewById(R.id.toolbar4);
        toolbar.setTitle(R.string.tituloEmail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        factura = intent.getParcelableExtra("facturaF");

        nomEmpleado=intent.getStringExtra("nomEmpleado");

        initComponents();
        solicitarPermisos();

    }

    private void initComponents() {
        rvContactos = findViewById(R.id.rvContactos);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ContactosAdapter(this);

        rvContactos.setLayoutManager(layoutManager);
        rvContactos.setAdapter(adapter);
    }


    public void solicitarPermisos (){

        int tengo_permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        // Here, thisActivity is the current activity
        if (tengo_permiso!= PackageManager.PERMISSION_GRANTED  ) {

            // ¿Enseñar explicación?
            Boolean deboexplicar = ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS);
            if (deboexplicar) {
                Toast.makeText(this, "Esta APP NO TIENE PERMISOS para leer contactos. Necesitas conceder permisos!!! ", Toast.LENGTH_LONG);
            } else {
                // Solicito el permiso al usuario
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        SOLICITUD_PERMISO_CONTACTOS);
            }
        } else {
            mostrarAgenda();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SOLICITUD_PERMISO_CONTACTOS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Permiso concedido
                    mostrarAgenda();

                } else {
                    Toast.makeText(this,"Permiso no concedido", Toast.LENGTH_LONG);
                    finish();
                }

                return;
            }
        }
    }

    public void mostrarAgenda(){
        String ident, nombre, email, tipo;
        Contactos contacto;

        // Voy rellenando los datos de la query
        String[] proyeccion = new String[] {ContactsContract.Data._ID,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.TYPE};

        /* Aquí vamos a poner la selección. Vamos a obtener aquellos contactos que sean de tipo email, cuyo email no sea null
          */
        String seleccion = ContactsContract.Data.MIMETYPE + "= '" +
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "' AND " +
                ContactsContract.CommonDataKinds.Email.ADDRESS + " IS NOT NULL";

        String orden = ContactsContract.Data.DISPLAY_NAME + " ASC";
        System.out.println(ContactsContract.Data.CONTENT_URI.toString());
        Cursor micursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                proyeccion, seleccion, null, orden);

        if (micursor != null){
            int i=0;
            while (micursor.moveToNext()){
                ident = micursor.getString(0);
                nombre = micursor.getString(1);
                email = micursor.getString(2);
                tipo = micursor.getString(3);
                //texto.append ("Identificador : " + ident + "\nNombre : " + nombre + "\nTeléfono : " + tlfn + "\nTIpo : " + tipo + "\n");
                contacto = new Contactos(nombre, email) ;
                datos_contactos.add(contacto);
                i++;
            }
        } else {
            Toast.makeText(this,"El cursor está vacío", Toast.LENGTH_LONG);
        }

        adapter.setContactosList(datos_contactos);
        adapter.setFacturaEmpleado(factura, nomEmpleado);

    }





}

