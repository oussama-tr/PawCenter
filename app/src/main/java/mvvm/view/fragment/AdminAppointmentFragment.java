package mvvm.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidminiproject.R;

import java.util.ArrayList;
import java.util.List;

import mvvm.adapter.AdminAppointmentListAdapter;
import mvvm.adapter.AppointmentListAdapter;
import mvvm.model.Appointment;
import mvvm.model.Notification;
import mvvm.model.User;
import mvvm.repository.PersistResponseCallback;
import mvvm.util.Session;
import mvvm.view_model.AppointmentViewModel;
import mvvm.view_model.AppointmentViewModelFactory;
import mvvm.view_model.NotificationViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAppointmentFragment extends Fragment implements PersistResponseCallback, AppointmentFragmentCallback {


    private View view;
    private Context appContext;
    TextView title;
    AppointmentViewModel appointmentViewModel;
    NotificationViewModel notificationViewModel;
    private RecyclerView mListView;
    private List<Appointment> mList = new ArrayList<>();
    private AdminAppointmentListAdapter mAdapter;

    public AdminAppointmentFragment(Context context) {
        this.appContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_appointment, container, false);

        title = view.findViewById(R.id.appointment_bar_text);
        title.setText("Appointments");
        appointmentViewModel = ViewModelProviders.of(this, new AppointmentViewModelFactory(this)).get(AppointmentViewModel.class);
        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);

        mListView = (RecyclerView) view.findViewById(R.id.app_listView);
        mListView.setLayoutManager(new LinearLayoutManager(appContext));
        mAdapter = new AdminAppointmentListAdapter(mList, appContext, this);
        mListView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        appointmentViewModel.getAdminAppointments().observe(this, new Observer<List<Appointment>>() {
            @Override
            public void onChanged(List<Appointment> appointments) {
                mList.clear();
                mList.addAll(appointments);
                mAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public void onAsyncCallFinished(Object result) {
        mList.clear();
        mAdapter.notifyDataSetChanged();

        appointmentViewModel.getAdminAppointments().observe(this, new Observer<List<Appointment>>() {
            @Override
            public void onChanged(List<Appointment> appointments) {
                mList.addAll(appointments);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDeletePressed(Appointment appointment) {
        appointmentViewModel.removeAppointment(appointment);
        String notificationContent = "Your appointment on " + appointment.getDate() + " for "
                + appointment.getPet().getName() + " with Dr. " + appointment.getVet().getFirstname() + " ( reason : pet " + appointment.getReason() +")";
        if(appointment.isConfirmed()) notificationContent+=" has been canceled. Please try rescheduling";
        else notificationContent+=" has been denied. Please try rescheduling";
        Notification notification = new Notification(appointment.getUser(), notificationContent, false);
        notificationViewModel.createUserAppointmentNotification(notification);
    }

    @Override
    public void onConfirmPressed(Appointment appointment) {
        appointmentViewModel.confirmAppointment(appointment);
        String notificationContent = "Your appointment on " + appointment.getDate() + " for "
                + appointment.getPet().getName() + " with Dr. " + appointment.getVet().getFirstname() + " ( reason : pet " + appointment.getReason() +") has been confirmed";
        Notification notification = new Notification(appointment.getUser(), notificationContent, false);
        notificationViewModel.createUserAppointmentNotification(notification);
    }
}
