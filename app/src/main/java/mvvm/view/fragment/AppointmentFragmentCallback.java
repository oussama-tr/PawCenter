package mvvm.view.fragment;

import mvvm.model.Appointment;
import mvvm.model.Vet;

public interface AppointmentFragmentCallback {
    void onDeletePressed(Appointment appointment);
    void onConfirmPressed(Appointment appointment);
}
