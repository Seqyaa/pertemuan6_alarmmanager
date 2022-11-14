package com.example.pertemuan6_alarmmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.pertemuan6_alarmmanager.databinding.ActivityMainBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import java.util.Calendar;
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
//mengaktifkan view binding
binding = ActivityMainBinding.inflate(getLayoutInflater()); setContentView(binding.getRoot());
        createNotificationChannel();
        binding.selectedTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
           }
        });
        binding.setAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm();
            }
        });

        binding.cancelAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm();
            }
        });
    }
    private void cancelAlarm() {
//untuk menggagalkan alarm yang sudah disetel
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        if (alarmManager == null) {
            alarmManager = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Cancelled",
                Toast.LENGTH_SHORT).show();
    }

    private void setAlarm() {
//untuk menjalankan alarm yang sudah disetel
        alarmManager = (AlarmManager)
        getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class); pendingIntent =
                PendingIntent.getBroadcast(this, 0,
                        intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
                Toast.makeText(this, "Alarm Set Successfully",
                Toast.LENGTH_SHORT).show();

    }
    private void showTimePicker() {
//memunculkan dialog timepicker menggunakan library dari android
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build(); picker.show(getSupportFragmentManager(), "AlarmManager");
//mengeset waktu didalam view
picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { if (picker.getHour() > 12) {
                binding.selectedTime.setText( String.format("%02d : %02d",
                        picker.getHour(), picker.getMinute())
                );
            } else {
                binding.selectedTime.setText(picker.getHour()
                        + " : " + picker.getMinute() + " ");
            }
//menangkap inputan jam kalian lalu memulai alarm
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,
                        picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }
        });
    }
    private void createNotificationChannel() {
//mendeskripsikan channel notifikasi yang akan dibangun
        CharSequence name = "INI ALARM MANAGER";
        String description = "PRAKTIKUM BAB5 TENTANG ALARM MANAGER";

//tingkat importance = high ( penting sekali )
        int importance = NotificationManager.IMPORTANCE_HIGH; NotificationChannel channel = new
                NotificationChannel("AlarmManager", name, importance); channel.setDescription(description);

//membuka izin pengaturan dari aplikasi untuk memulai service notifikasi
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
