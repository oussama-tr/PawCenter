package mvvm.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import mvvm.model.Notification;
import mvvm.model.Vet;
import mvvm.repository.NotificationRepository;
import mvvm.repository.PersistResponseCallback;
import mvvm.repository.VetRepository;
import okhttp3.MultipartBody;

public class NotificationViewModel extends ViewModel {

    private NotificationRepository notificationRepository = NotificationRepository.getInstance();

    public NotificationViewModel(){}


    public void createAdminAppointmentNotification(Notification notification)
    {
        notificationRepository.createAdminAppointmentNotification(notification);
    }

    public void createUserAppointmentNotification(Notification notification)
    {
        notificationRepository.createUserAppointmentNotification(notification);
    }

    public MutableLiveData<List<Notification>> getUserNotifications() {
        return notificationRepository.getUserNotifications();
    }

    public MutableLiveData<List<Notification>> getAdminNotifications() {
        return notificationRepository.getAdminNotifications();
    }


}
