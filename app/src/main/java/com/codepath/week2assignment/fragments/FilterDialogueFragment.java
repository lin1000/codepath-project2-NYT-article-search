package com.codepath.week2assignment.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.week2assignment.R;
import com.codepath.week2assignment.model.UIFilter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by lin1000 on 2017/2/24.
 */

public class FilterDialogueFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private EditText mEditText;
    private Spinner spinner;
    private Button btnSave;
    private Button btnClear;
    private CheckBox checkbox_arts;
    private CheckBox checkbox_fashion;
    private CheckBox checkbox_sports;
    private FilterDialogueFragmentCallBackInterface mCallback;
    Calendar myCalendar = Calendar.getInstance();

    private UIFilter uiFilter;

    public FilterDialogueFragment() {
        //Bundle args = new Bundle();
        //args.putSerializable("uiFilter", (Serializable) uiFilter);
        //frag.setArguments(args);
    }

    private static String getCurrentDate() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(date);
    }

    private String getDefaultSort(){
        return getResources().getString(R.string.sort_arry_newest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        //getDialog().setTitle(getResources().getString(R.string.advanced_search));

        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.etBeginDate);
        spinner = (Spinner) view.findViewById(R.id.spSort);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnClear = (Button) view.findViewById(R.id.btnClear);
        checkbox_arts = (CheckBox) view.findViewById(R.id.checkbox_arts);
        checkbox_fashion = (CheckBox) view.findViewById(R.id.checkbox_fashion);
        checkbox_sports = (CheckBox) view.findViewById(R.id.checkbox_sports);

        if(uiFilter==null){
            //Default Setting
            initUIFilter();
            //populate
            populateLayoutValues();
        }

        mEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showDatePicker();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uiFilter.setActivated(true);
                uiFilter.setBeginDate(mEditText.getText().toString());
                uiFilter.setSort(spinner.getSelectedItem().toString());
                // Check if the checkbox is checked
                uiFilter.setCheck_Arts(checkbox_arts.isChecked());
                uiFilter.setCheck_Fashion(checkbox_fashion.isChecked());
                uiFilter.setCheck_Sports(checkbox_sports.isChecked());

                FilterDialogueFragmentCallBackInterface listener = (FilterDialogueFragmentCallBackInterface) getActivity();
                listener.onFinishSettingDialog(uiFilter);
                dismiss();

            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initUIFilter();
                populateLayoutValues();
                FilterDialogueFragmentCallBackInterface listener = (FilterDialogueFragmentCallBackInterface) getActivity();
                listener.onFinishSettingDialog(uiFilter);
                dismiss();

            }
        });

        // Show soft keyboard automatically and request focus to field
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void initUIFilter(){
        uiFilter = new UIFilter();
        uiFilter.setBeginDate(getCurrentDate());
        uiFilter.setSort(getDefaultSort());
        uiFilter.setCheck_Arts(true);
        uiFilter.setCheck_Fashion(false);
        uiFilter.setCheck_Fashion(false);
        uiFilter.setActivated(false);
    }

    private void populateLayoutValues(){
        //populate default setting
        mEditText.setText(uiFilter.getBeginDate().toString());
        if (uiFilter.getSort().equals(getResources().getString(R.string.sort_arry_oldest)))
            spinner.setSelection(0);
        else
            spinner.setSelection(1);

         checkbox_arts.setChecked(uiFilter.Check_Arts);
         checkbox_fashion.setChecked(uiFilter.Check_Fashion);
         checkbox_sports.setChecked(uiFilter.Check_Sports);
    }

    private void showDatePicker(){
        DatePickerDialogueFragment datePickerDialogueFragment = new DatePickerDialogueFragment();
        datePickerDialogueFragment.show(getChildFragmentManager(), "date Picker");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            if (context instanceof FilterDialogueFragmentCallBackInterface){
                mCallback = (FilterDialogueFragmentCallBackInterface) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        mEditText.setText(formatter.format(c.getTime()));

    }


    // Defines the listener interface
    public interface FilterDialogueFragmentCallBackInterface {
        void onFinishSettingDialog(UIFilter uiFilter);
    }

}
