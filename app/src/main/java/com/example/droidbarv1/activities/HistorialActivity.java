package com.example.droidbarv1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.droidbarv1.R;
import com.example.droidbarv1.model.data.Factura;

import com.example.droidbarv1.model.contract.WaitResponseServer;
import com.example.droidbarv1.view.adapters.HistorialAdapter;
import com.example.droidbarv1.view.DroidBarViewModel;

import java.util.ArrayList;
import java.util.List;

public class HistorialActivity extends AppCompatActivity {
    private List<Factura> fact;
    private ArrayList<Factura> factTerminadas;
    private DroidBarViewModel viewModel;
    private HistorialAdapter adapter;
    //CREAR LIST DE COMANDAS Y PONERLAS BIEN EN EL ONSWIPE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        toolbar.setTitle(R.string.tituloHistorial);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel =  ViewModelProviders.of(this).get(DroidBarViewModel.class);
        adapter= new HistorialAdapter(this);
        cargaDatos();

    }

    private void cargaDatos() {
        viewModel.getFacturaList(new WaitResponseServer() {
            @Override
            public void waitingResponse(boolean success, List list) {
                if(success){
                    fact =list;
                    recuperaFactTerminadas();
                    viewModel.getEmpleadosList(new WaitResponseServer() {
                        @Override
                        public void waitingResponse(boolean success, List list) {
                            if (success){
                                adapter.setFacturaEmpleadoList(factTerminadas,list);
                                initComponents();
                            }
                        }
                    });
                }
            }
        });
    }

    private void recuperaFactTerminadas() {
        factTerminadas=new ArrayList<>();
        for (Factura f:fact) {
            if(f.getId_employee_finish()!=4){
                factTerminadas.add(f);
            }
        }
    }

    private void initComponents() {
        RecyclerView rvList = findViewById(R.id.rvHistorial);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);

    }


}
