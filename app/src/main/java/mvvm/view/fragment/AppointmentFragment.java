package mvvm.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidminiproject.R;
import java.util.ArrayList;
import java.util.List;
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
public class AppointmentFragment extends Fragment implements PersistResponseCallback, AppointmentFragmentCallback {


    private View view;
    private Context appContext;
    NotificationViewModel notificationViewModel;
    AppointmentViewModel appointmentViewModel;
    private RecyclerView mListView;
    private List<Appointment> mList = new ArrayList<>();
    private AppointmentListAdapter mAdapter;
    public AppointmentFragment(Context context) {
        this.appContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_appointment, container, false);

        appointmentViewModel = ViewModelProviders.of(this, new AppointmentViewModelFactory(this)).get(AppointmentViewModel.class);
        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        mListView = (RecyclerView) view.findViewById(R.id.app_listView);
        mListView.setLayoutManager(new LinearLayoutManager(appContext));
        mAdapter = new AppointmentListAdapter(mList, appContext, this);
        mListView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        appointmentViewModel.getUserAppointments().observe(this, new Observer<List<Appointment>>() {
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
        appointmentViewModel.getUserAppointments().observe(this, new Observer<List<Appointment>>() {
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
        if(appointment.isConfirmed())
        {
            User user = Session.getInstance().getUser();
            String notificationContent = user.getUsername() + " has canceled his appointment on " + appointment.getDate() + " for his "
                    + appointment.getPet().getSpecies() + " with Dr. " + appointment.getVet().getFirstname();

            Notification notification = new Notification(Session.getInstance().getUser(), notificationContent, true);
            notificationViewModel.createAdminAppointmentNotification(notification);
        }
    }

    @Override
    public void onConfirmPressed(Appointment appointment) {
        // do nothing here as only the admin can confirm an appointment
    }
}
