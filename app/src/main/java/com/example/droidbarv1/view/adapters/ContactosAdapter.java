package com.example.droidbarv1.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.droidbarv1.R;
import com.example.droidbarv1.model.data.Contactos;
import com.example.droidbarv1.model.data.Factura;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ContactosAdapter extends RecyclerView.Adapter <ContactosAdapter.ItemHolder> {


    private LayoutInflater inflater;
    private List<Contactos> contactosList;
    private Context miContexto;
    private String direccion;
    private String nomEmpleado;
    private Factura finF = new Factura();

    public ContactosAdapter(Context context) {
        inflater= LayoutInflater.from(context);
        miContexto = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= inflater.inflate(R.layout.item_contacto,parent,false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if(contactosList != null){
            final Contactos current = contactosList.get(position);
            holder.tvNombre.setText(current.getNombre());
            holder.tvCorreo.setText(current.getEmail());
            holder.cl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mandarEmail(nomEmpleado, current, finF);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        int elementos = 0;
        if(contactosList != null){
            elementos = contactosList.size();
        }
        return elementos;
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre, tvCorreo;
        private CardView cl;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre=itemView.findViewById(R.id.tvNombre);
            tvCorreo = itemView.findViewById(R.id.tvCorreo);
            cl = itemView.findViewById(R.id.cvContacto);
        }
    }

    public void mandarEmail(String nomEmpleado, Contactos contacto, Factura factura){
        String TO[] = {contacto.getEmail()};
        String CC[] = {""};


        String mensaje = "Gracias por venir a DroidBar. \nLa información de su factura:\n"
                + "Factura Nº: " + factura.getId()
                +"\nMesa: " + factura.getTable()
                +"\nLe atendio: " + nomEmpleado
                +"\nTotal: " + factura.getTotal();

        Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
        mailIntent.setData(Uri.parse("mailto:"));

        mailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        mailIntent.putExtra(Intent.EXTRA_CC, CC);

        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Factura de DroidBar");
        mailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);

        String title = "Mandar este email con....";

        Intent chooser = Intent.createChooser(mailIntent, title);
        if(mailIntent.resolveActivity(miContexto.getPackageManager()) != null){
            miContexto.startActivity(chooser);
        }

    }

    public void setContactosList(List<Contactos> contactosList) {
        this.contactosList = contactosList;
        notifyDataSetChanged();
    }

    public void setFacturaEmpleado(Factura finF, String nomEmpleado){
        this.finF = finF;
        this.nomEmpleado = nomEmpleado;
        notifyDataSetChanged();
    }
}
