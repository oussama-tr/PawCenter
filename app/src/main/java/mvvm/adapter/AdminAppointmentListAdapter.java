package mvvm.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidminiproject.R;

import java.util.List;

import mvvm.model.Appointment;
import mvvm.view.dialog.PetDialog;
import mvvm.view.fragment.AppointmentFragmentCallback;


public class AdminAppointmentListAdapter extends RecyclerView.Adapter<AdminAppointmentListAdapter.ViewHolder>{

    private List<Appointment> listdata;
    private Context context;
    AppointmentFragmentCallback appointmentFragmentCallback;

    public AdminAppointmentListAdapter(List<Appointment> listdata, Context context, AppointmentFragmentCallback appointmentFragmentCallback) {
        this.listdata = listdata;
        this.context = context;
        this.appointmentFragmentCallback = appointmentFragmentCallback;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.layout_admin_appointment_listitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Appointment myListData = listdata.get(position);

        String app_status = myListData.isConfirmed() ? "Scheduled for" : "Pending for";
        holder.status.setText(app_status);
        holder.date.setText(myListData.getDate());
        holder.pet_name.setText(myListData.getPet().getName());
        holder.vet_name.setText("Veterinarian : Dr. " + myListData.getVet().getFirstname());
        holder.reason.setText("For " + myListData.getReason());

        if(myListData.isConfirmed()){

            holder.status_icon.setImageResource(R.drawable.ic_tick);
            holder.status_icon.setColorFilter(context.getResources().getColor(R.color.white));

            holder.main_layout.getBackground().setTint(context.getResources().getColor(R.color.link_blue));
            holder.status.setTextColor(context.getResources().getColor(R.color.white));
            holder.reason.setTextColor(context.getResources().getColor(R.color.light_cyan));
            holder.cancel_btn.setColorFilter(context.getResources().getColor(R.color.white));
            holder.pet_info.setColorFilter(context.getResources().getColor(R.color.white));
            holder.pet_name.setTextColor(context.getResources().getColor(R.color.white));
            holder.vet_name.setTextColor(context.getResources().getColor(R.color.light_cyan));

            if(holder.button_layout.indexOfChild(holder.accept_btn) != -1)
                holder.button_layout.removeView(holder.accept_btn);

        }

        holder.pet_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent petIntent = new Intent(context, PetDialog.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("pet", myListData.getPet());
                petIntent.putExtras(bundle);
                context.startActivity(petIntent);
            }
        });

        holder.cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete
                appointmentFragmentCallback.onDeletePressed(myListData);
            }
        });

        holder.accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // confirm
                appointmentFragmentCallback.onConfirmPressed(myListData);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView status_icon, pet_info, cancel_btn, accept_btn;
        public TextView status, date, pet_name, vet_name, reason;
        public LinearLayout main_layout, button_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.status = (TextView) itemView.findViewById(R.id.tv_app_status);
            this.date = (TextView) itemView.findViewById(R.id.tv_app_date);
            this.pet_name = (TextView) itemView.findViewById(R.id.tv_app_pet);
            this.vet_name = (TextView) itemView.findViewById(R.id.tv_app_vet);
            this.reason = (TextView) itemView.findViewById(R.id.tv_app_reason);
            this.status_icon = (ImageView) itemView.findViewById(R.id.ic_app_status);
            this.pet_info = (ImageView) itemView.findViewById(R.id.ic_app_pet_info);
            this.cancel_btn = (ImageView) itemView.findViewById(R.id.ic_app_cancel);
            this.accept_btn = (ImageView) itemView.findViewById(R.id.ic_app_accpet);
            this.main_layout = (LinearLayout) itemView.findViewById(R.id.main_layout);
            this.button_layout = (LinearLayout) itemView.findViewById(R.id.button_layout);
        }
    }

}
