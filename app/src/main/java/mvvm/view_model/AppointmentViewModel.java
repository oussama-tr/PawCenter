package mvvm.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import mvvm.model.Appointment;
import mvvm.repository.AppointmentRepository;
import mvvm.repository.PersistResponseCallback;

public class AppointmentViewModel extends ViewModel {

    private AppointmentRepository appointmentRepository = AppointmentRepository.getInstance();
    private PersistResponseCallback persistResponseCallback;

    public AppointmentViewModel(){}

    public AppointmentViewModel(PersistResponseCallback persistResponseCallback){
        this.persistResponseCallback = persistResponseCallback;
    }

    public void createAppointment(Appointment appointment)
    {
        appointmentRepository.createAppointment(appointment, persistResponseCallback);
    }

    public MutableLiveData<List<Appointment>> getUserAppointments() {
        return appointmentRepository.getUserAppointments();
    }

    public MutableLiveData<List<Appointment>> getAdminAppointments() {
        return appointmentRepository.getAdminAppointments();
    }

    public void removeAppointment(Appointment appointment)
    {
        appointmentRepository.removeAppointment(appointment.getId(), persistResponseCallback);
    }

    public void confirmAppointment(Appointment appointment)
    {
        appointmentRepository.confirmAppointment(appointment.getId(), persistResponseCallback);
    }

}
