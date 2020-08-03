package mvvm.view.dialog;

import mvvm.model.User;
import mvvm.model.Vet;

public interface AppointmentDialogCallback {
    void onVetSelected(Vet vet);
}
