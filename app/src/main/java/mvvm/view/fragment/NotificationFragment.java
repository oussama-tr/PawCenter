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
import mvvm.adapter.NotificationListAdapter;
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
public class NotificationFragment extends Fragment{

    private boolean isAdmin;
    private View view;
    private Context appContext;
    NotificationViewModel notificationViewModel;
    private RecyclerView mListView;
    private List<Notification> mList = new ArrayList<>();
    private NotificationListAdapter mAdapter;

    public NotificationFragment(Context context, Boolean isAdmin) {
        this.appContext = context;
        this.isAdmin = isAdmin;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification, container, false);

        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        mListView = (RecyclerView) view.findViewById(R.id.notif_listView);
        mListView.setLayoutManager(new LinearLayoutManager(appContext));
        mAdapter = new NotificationListAdapter(mList, appContext);
        mListView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isAdmin)
        {
            notificationViewModel.getUserNotifications().observe(this, new Observer<List<Notification>>() {
                @Override
                public void onChanged(List<Notification> notifications) {
                    mList.clear();
                    mList.addAll(notifications);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
        else {
            notificationViewModel.getAdminNotifications().observe(this, new Observer<List<Notification>>() {
                @Override
                public void onChanged(List<Notification> notifications) {
                    mList.clear();
                    mList.addAll(notifications);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

    }
}
