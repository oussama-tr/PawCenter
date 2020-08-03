package mvvm.view.dialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.androidminiproject.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mvvm.adapter.VetArrayAdapter;
import mvvm.model.Appointment;
import mvvm.model.Notification;
import mvvm.model.Pet;
import mvvm.model.User;
import mvvm.model.Vet;
import mvvm.repository.PersistResponseCallback;
import mvvm.util.Session;
import mvvm.view.fragment.DatePickerFragment;
import mvvm.view.fragment.TimePickerFragment;
import mvvm.view_model.AppointmentViewModel;
import mvvm.view_model.AppointmentViewModelFactory;
import mvvm.view_model.NotificationViewModel;
import mvvm.view_model.VetViewModel;
import mvvm.view_model.VetViewModelFactory;

public class AppointmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, PersistResponseCallback, AppointmentDialogCallback {

    TextView form_title, form_btn;
    Pet pet;
    Vet vet;
    VetViewModel vetViewModel;
    List<Vet> vetList = new ArrayList<>();
    VetArrayAdapter vetAdapter;
    AutoCompleteTextView vet_dropdown, reason_dropdown;
    ImageView pick_date_btn, pick_time_btn;
    EditText date_text, time_text;

    String[] REASONS = new String[] {"Examination", "Vaccination", "Neutering", "Injury"};
    String currentDateString, currentTimeString;
    ArrayAdapter<String> reasonAdapter;

    AppointmentViewModel appointmentViewModel;
    NotificationViewModel notificationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_appointment);

        appointmentViewModel = ViewModelProviders.of(this, new AppointmentViewModelFactory(this)).get(AppointmentViewModel.class);
        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);


        form_title = findViewById(R.id.pet_form_title);
        form_btn = findViewById(R.id.form_btn);
        vet_dropdown = findViewById(R.id.vet_dropdown);
        reason_dropdown = findViewById(R.id.reason_dropdown);
        pick_date_btn = findViewById(R.id.pick_date_btn);
        pick_time_btn = findViewById(R.id.pick_time_btn);
        date_text = findViewById(R.id.date_field);
        time_text = findViewById(R.id.time_field);

        date_text.setKeyListener(null);
        time_text.setKeyListener(null);


        reasonAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, REASONS);
        reason_dropdown.setAdapter(reasonAdapter);

        pick_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });


        pick_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        form_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vet_dropdown.getText().equals("") || date_text.getText().equals("") || time_text.getText().equals("") || reason_dropdown.getText().equals(""))
                    Toast.makeText(AppointmentActivity.this, "Please fill the form before submitting !", Toast.LENGTH_SHORT).show();
                else
                {
                    Appointment newAppointment = new Appointment(pet, vet, currentDateString + " " + currentTimeString, reason_dropdown.getText().toString());
                    appointmentViewModel.createAppointment(newAppointment);
                }

            }
        });



        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        this.pet = (Pet) bundle.getSerializable("pet");
        form_title.setText("Schedule an appointment");

        findViewById(R.id.backArrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        vetViewModel = ViewModelProviders.of(this, new VetViewModelFactory()).get(VetViewModel.class);
        vetViewModel.getVets().observe(this, new Observer<List<Vet>>() {
            @Override
            public void onChanged(List<Vet> vets) {
                vetList.addAll(vets);
            }
        });

        vetAdapter = new VetArrayAdapter(this, R.layout.layout_vet_spinneritem, vetList, this);
        vet_dropdown.setAdapter(vetAdapter);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        this.currentDateString = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());
        date_text.setText(currentDateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.currentTimeString = hourOfDay + ":" + minute;
        time_text.setText(currentTimeString);
    }

    @Override
    public void onAsyncCallFinished(Object result) {
        if((Appointment) result != null)
        {
            // create notification
            Appointment appointment = (Appointment) result;
            User user = Session.getInstance().getUser();
            String notificationContent = user.getUsername() + " has requested an appointment on " + appointment.getDate() + " for his "
                    + appointment.getPet().getSpecies() + " with Dr. " + appointment.getVet().getFirstname() + " ( reason : pet " + appointment.getReason() +")";

            Notification notification = new Notification(Session.getInstance().getUser(), notificationContent, true);
            notificationViewModel.createAdminAppointmentNotification(notification);

            finish();
            Intent persistSuccessIntent = new Intent(AppointmentActivity.this, PersistSuccessDialog.class);
            persistSuccessIntent.putExtra("REDIRECT_TO", "");
            persistSuccessIntent.putExtra("MESSAGE", getResources().getString(R.string.appointment_success_dialog));
            persistSuccessIntent.putExtra("BTN_TEXT", "Dismiss");
            startActivity(persistSuccessIntent);
        }
        else
            Toast.makeText(AppointmentActivity.this, "An unexpected error occured !", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onVetSelected(Vet vet) {
        this.vet = vet;
    }
}
