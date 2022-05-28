package cn.zicoo.ir2teledemo.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    public Date currentTime;
    private TimeSetListener listener;

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public void setListener(TimeSetListener listener) {
        this.listener = listener;
    }
    public static TimePickerFragment newInstance(Date currentTime,TimeSetListener listener){
        TimePickerFragment frag = new TimePickerFragment();
        frag.setCurrentTime(currentTime);
        frag.setListener(listener);
        return frag;
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
        cal.set(Calendar.MINUTE,minute);
        listener.onTimeSet(cal.getTime());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (currentTime == null)
            currentTime = new Date();
        // Instantiate a new TimePickerDemo, passing in the handler, the current
        // time to display, and whether or not to use 24 hour format:
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);

        // Return the created TimePickerDemo:
        return new TimePickerDialog
                (getContext(), this, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
    }
    public  interface TimeSetListener{
        void onTimeSet(Date date);
    }
}
