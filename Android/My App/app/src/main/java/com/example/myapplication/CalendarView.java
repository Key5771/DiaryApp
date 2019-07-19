package com.example.myapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Attr;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarView extends LinearLayout {
    public CalendarView(Context context, TextView dateTitle) {
        super(context);
        this.dateTitle = dateTitle;
    }

    private static final String DAY_OF_WEEK_TEXT = "dayOfWeekText";
    private static final String DAY_OF_WEEK_LAYOUT = "dayOfWeek";
    private static final String DAY_OF_MONTH_LAYOUT = "dayOfMonth";
    private static final String DAY_OF_MONTH_TEXT = "dayOfMonthText";
    private static final String DAY_OF_MONTH_BACKGROUND = "dayOfMonthBackground";
    private static final String DAY_OUT_MONTH_CIRCLE = "dayOfMonthCircle";

    private TextView dateTitle;
    private ImageView leftButton;
    private ImageView rightButton;
    private View rootView;
    private ViewGroup CalendarMonthLayout;
    private CalendarListener CalendarListener;
    @NonNull
    private Calendar currentCalendar = Calendar.getInstance();
    @Nullable
    private Calendar lastSelectedDayCalendar;

    private final OnClickListener onDayOfMonthClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewGroup dayOfMonthContainer = (ViewGroup) v;
            String tagID = (String) dayOfMonthContainer.getTag();
            tagID = tagID.substring(DAY_OF_MONTH_LAYOUT.length(),tagID.length());
            TextView dayOfMonthText = rootView.findViewWithTag(DAY_OF_MONTH_TEXT+tagID);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,currentCalendar.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH,currentCalendar.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH,Integer.valueOf(dayOfMonthText.getText().toString()));

            markDayAsSelectedDay(calendar.getTime());

            if(CalendarListener == null){
                throw new IllegalStateException("");
            }
            else{
                CalendarListener.onDayClick(calendar.getTime());
            }
        }
    };



    private final OnLongClickListener onDayOfMonthLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            ViewGroup dayOfMonthContainer = (ViewGroup) view;
            String tagID = (String) dayOfMonthContainer.getTag();
            tagID = tagID.substring(DAY_OF_MONTH_LAYOUT.length(),tagID.length());
            TextView dayOfMonthText = rootView.findViewWithTag(DAY_OF_MONTH_TEXT+tagID);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,currentCalendar.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH,currentCalendar.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH,Integer.valueOf(dayOfMonthText.getText().toString()));

            markDayAsSelectedDay(calendar.getTime());

            if(CalendarListener == null){
                throw new IllegalStateException("");
            }
            else{
                CalendarListener.onDayLongClick(calendar.getTime());
            }
            return true;
        }
    };

    private boolean shortWeekDays = false;

    public CalendarView(Context context){
        super(context);
        init(null);
    }

    public CalendarView(Context context,@Nullable AttributeSet attrs){
        super (context, attrs);
        init(attrs);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(attrs);
    }

    @RequiresApi (api = Build.VERSION_CODES.P)
    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context,attrs,defStyleAttr,defStyleRes);
        init(attrs);
    }

    private static String checkSpecificLocales(String dayOfWeekString, int i){

        if(i == 4 && "ES".equals(Locale.getDefault().getCountry())){
            dayOfWeekString = "X";
        } else {
            dayOfWeekString = dayOfWeekString.substring(0,1).toUpperCase();
        }
        return dayOfWeekString;
    }

    private static int getDayIndexByDate(Calendar currentCalendar){
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        return currentDay + monthOffset;
    }

    private static int getMonthOffset(Calendar currentCalendar){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH,1);
        int firstDayWeekPosition = calendar.getFirstDayOfWeek();
        int dayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        if(firstDayWeekPosition == 1){
            return dayPosition -1;
        }
        else{
            if(dayPosition == 1) {
                return 6;
            } else {
                return dayPosition -2;
            }
        }
    }

    private static int getWeekIndex(int weekIndex, Calendar currentCalendar){
        int firstDayWeekPosition = currentCalendar.getFirstDayOfWeek();

        if(firstDayWeekPosition == 1){
            return weekIndex;
        } else{

            if(weekIndex == 1){
                return 7;
            } else {
                return weekIndex -1;
            }
        }
    }

    private static boolean areInTheSameDay(@NonNull Calendar calendarOne, @NonNull Calendar calendarTwo){
        return calendarOne.get(Calendar.YEAR) == calendarTwo.get(Calendar.YEAR) && calendarOne.get(Calendar.DAY_OF_YEAR) == calendarTwo.get(Calendar.DAY_OF_YEAR);
    }

    private void init(@Nullable AttributeSet set){
        if(isInEditMode()){
            return;
        }

        LayoutInflater inflater =(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.calendar_view,this,true);
        findViewsById(rootView);

        currentCalendar = Calendar.getInstance();
        setDate(currentCalendar.getTime());
    }

    public void setDate(@NonNull Date date){
        currentCalendar.setTime(date);
        updateView();
    }

    @NonNull
    public Date getDate(){return currentCalendar.getTime();}

    @Nullable
    public Date getSelectedDay(){return lastSelectedDayCalendar.getTime();}

    public void markDayAsSelectedDay(@NonNull Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        lastSelectedDayCalendar = calendar;

        ViewGroup dayOfTheMonthBackground = getDayOfMonthBackground(calendar);
        dayOfTheMonthBackground.setBackgroundResource(R.drawable.circle);

        TextView dayOfMonth = getDayOfMonthText(calendar);
        dayOfMonth.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        ImageView circleImage = getCircleImage(calendar);

        if(circleImage.getVisibility() == VISIBLE){
            DrawableCompat.setTint(circleImage.getDrawable(),ContextCompat.getColor(getContext(),R.color.white));
        }
    }

    public void clearSelectedDay(){
        if(lastSelectedDayCalendar != null){
            ViewGroup dayOfMonthBackground = getDayOfMonthBackground(lastSelectedDayCalendar);

            Calendar nowCalendar = Calendar.getInstance();
            if(nowCalendar.get(Calendar.YEAR) == lastSelectedDayCalendar.get(Calendar.YEAR) && nowCalendar.get(Calendar.DAY_OF_YEAR) == lastSelectedDayCalendar.get(Calendar.DAY_OF_YEAR)){
                dayOfMonthBackground.setBackgroundResource(R.drawable.ring);
            }else{
                dayOfMonthBackground.setBackgroundResource(android.R.color.transparent);
            }

            TextView dayOfMonth = getDayOfMonthText(lastSelectedDayCalendar);
            dayOfMonth.setTextColor(ContextCompat.getColor(getContext(),R.color.dark));

            ImageView circleImage = getCircleImage(lastSelectedDayCalendar);
            if(circleImage.getVisibility() == VISIBLE){
                DrawableCompat.setTint(circleImage.getDrawable(),ContextCompat.getColor(getContext(),R.color.red_pink));
            }
        }
    }

    public void setShortWeekDays(boolean shortWeekDays){this.shortWeekDays = shortWeekDays;}

    public void markCircleImage(@NonNull Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        ImageView circleImage = getCircleImage(calendar);
        circleImage.setVisibility(View.VISIBLE);
        if(lastSelectedDayCalendar != null && areInTheSameDay(calendar, lastSelectedDayCalendar)){
            DrawableCompat.setTint(circleImage.getDrawable(),ContextCompat.getColor(getContext(),R.color.white));
        }else {
            DrawableCompat.setTint(circleImage.getDrawable(),ContextCompat.getColor(getContext(),R.color.red_pink));
        }
    }

    public void ShowDateTitle(boolean show){
        if(show){
            CalendarMonthLayout.setVisibility(VISIBLE);
        } else{
            CalendarMonthLayout.setVisibility(GONE);
        }
    }

    public void setCalendarListener(CalendarListener calendarListener){
        this.CalendarListener = calendarListener;
    }

    private void findViewsById(View view){

        CalendarMonthLayout = view.findViewById(R.id.CalendarDateTitle);
        leftButton = view.findViewById(R.id.leftButton);
        rightButton = view.findViewById(R.id.rightButton);
        dateTitle = view.findViewById(R.id.monthText);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(int i=0; i<42; i++){

            int weekIndex = (i%7) + 1;
            ViewGroup dayOfTheWeekLayout = view.findViewWithTag(DAY_OF_WEEK_LAYOUT + weekIndex);


            View dayOfMonthLayout = inflater.inflate(R.layout.calendar_day_of_month,null);
            View dayOfMonthText = dayOfMonthLayout.findViewWithTag(DAY_OF_MONTH_TEXT);
            View dayOfMonthBackground = dayOfMonthLayout.findViewWithTag(DAY_OF_MONTH_BACKGROUND);
            View dayOfMonthCircleImage = dayOfMonthLayout.findViewWithTag(DAY_OUT_MONTH_CIRCLE);


            int viewIndex = i+1;
            dayOfMonthLayout.setTag(DAY_OF_WEEK_LAYOUT+ viewIndex);
            dayOfMonthText.setTag(DAY_OF_MONTH_TEXT + viewIndex);
            dayOfMonthBackground.setTag(DAY_OF_MONTH_BACKGROUND+viewIndex);
            dayOfMonthCircleImage.setTag(DAY_OUT_MONTH_CIRCLE + viewIndex);

            dayOfTheWeekLayout.addView(dayOfMonthLayout);
        }
    }


    private void setUpMonthLayout(){
        String dateText = new DateFormatSymbols(Locale.getDefault()).getMonths()[currentCalendar.get(Calendar.MONTH)];
        dateText = dateText.substring(0,1).toUpperCase()+ dateText.subSequence(1,dateText.length());
        Calendar calendar = Calendar.getInstance();

        if(currentCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)){
            dateTitle.setText(dateText);
        } else{
            dateTitle.setText(String.format("%s %s", dateText, currentCalendar.get(Calendar.YEAR)));
        }
    }

    private void setUpWeekDaysLayout(){
        TextView dayOfWeek;
        String dayOfWeekString;
        String[] weekDaysArray = new DateFormatSymbols(Locale.getDefault()).getWeekdays();
        int length = weekDaysArray.length;

        for(int i = 1; i<length; i++){
            dayOfWeek = rootView.findViewWithTag(DAY_OF_WEEK_TEXT + getWeekIndex(i, currentCalendar));
            dayOfWeekString = weekDaysArray[i];
            if (shortWeekDays){
                dayOfWeekString = checkSpecificLocales(dayOfWeekString,i);
            } else {
                dayOfWeekString = dayOfWeekString.substring(0,1).toUpperCase() + dayOfWeekString.substring(1,3);
            }

            dayOfWeek.setText(dayOfWeekString);
        }
    }

    private void SetUpDaysOfMonthLayout(){
        TextView dayOfTheMonthText;
        View circleImage;

        ViewGroup dayOfTheMonthContainer;
        ViewGroup dayOfTheMonthBackground;

        for (int i = 1; i < 43; i++) {

            dayOfTheMonthContainer = rootView.findViewWithTag(DAY_OF_MONTH_LAYOUT + i);
            dayOfTheMonthBackground = rootView.findViewWithTag(DAY_OF_MONTH_BACKGROUND + i);
            dayOfTheMonthText = rootView.findViewWithTag(DAY_OF_MONTH_TEXT + i);
            circleImage = rootView.findViewWithTag(DAY_OUT_MONTH_CIRCLE + i);


            dayOfTheMonthText.setVisibility(View.INVISIBLE);
            circleImage.setVisibility(View.GONE);


            // Apply styles
            dayOfTheMonthText.setBackgroundResource(android.R.color.transparent);
            dayOfTheMonthText.setTypeface(null, Typeface.NORMAL);
            dayOfTheMonthText.setTextColor(ContextCompat.getColor(getContext(), R.color.dark));
            dayOfTheMonthContainer.setBackgroundResource(android.R.color.transparent);
            dayOfTheMonthContainer.setOnClickListener(null);
            dayOfTheMonthBackground.setBackgroundResource(android.R.color.transparent);


        }
    }


    private void setUpDaysInCalendar(){

        Calendar auxCalendar = Calendar.getInstance(Locale.getDefault());
        auxCalendar.setTime(currentCalendar.getTime());
        auxCalendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfMonth = auxCalendar.get(Calendar.DAY_OF_WEEK);
        TextView dayOfTheMonthText;
        ViewGroup dayOfTheMonthContainer;
        ViewGroup dayOfTheMonthLayout;

        // Calculate dayOfTheMonthIndex
        int dayOfTheMonthIndex = getWeekIndex(firstDayOfMonth, auxCalendar);

        for (int i = 1; i <= auxCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++, dayOfTheMonthIndex++) {
            dayOfTheMonthContainer = rootView.findViewWithTag(DAY_OF_MONTH_LAYOUT + dayOfTheMonthIndex);
            dayOfTheMonthText = rootView.findViewWithTag(DAY_OF_MONTH_TEXT + dayOfTheMonthIndex);
            if (dayOfTheMonthText == null) {
                break;
            }
            dayOfTheMonthContainer.setOnClickListener(onDayOfMonthClickListener);
            dayOfTheMonthContainer.setOnLongClickListener(onDayOfMonthLongClickListener);
            dayOfTheMonthText.setVisibility(View.VISIBLE);
            dayOfTheMonthText.setText(String.valueOf(i));
        }

        for (int i = 36; i < 43; i++) {
            dayOfTheMonthText = rootView.findViewWithTag(DAY_OF_MONTH_TEXT + i);
            dayOfTheMonthLayout = rootView.findViewWithTag(DAY_OF_MONTH_LAYOUT + i);
            if (dayOfTheMonthText.getVisibility() == INVISIBLE) {
                dayOfTheMonthLayout.setVisibility(GONE);
            } else {
                dayOfTheMonthLayout.setVisibility(VISIBLE);
            }
        }


    }



    private void markDayAsCurrentDay(){

        Calendar nowCalendar = Calendar.getInstance();

        if(nowCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) && nowCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)){
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(nowCalendar.getTime());

            ViewGroup dayOfMonthBackground = getDayOfMonthBackground(currentCalendar);
            dayOfMonthBackground.setBackgroundResource(R.drawable.ring);
        }
    }



    private void updateView(){
        setUpMonthLayout();
        setUpWeekDaysLayout();
        SetUpDaysOfMonthLayout();
        setUpDaysInCalendar();
        markDayAsCurrentDay();
    }


    private ViewGroup getDayOfMonthBackground(Calendar currentCalendar){
        return (ViewGroup) getView(DAY_OF_MONTH_BACKGROUND, currentCalendar);
    }

    private TextView getDayOfMonthText(Calendar currentCalendar){
        return (TextView) getView(DAY_OF_MONTH_TEXT, currentCalendar);
    }

    private ImageView getCircleImage(Calendar currentCalendar){
        return (ImageView) getView(DAY_OUT_MONTH_CIRCLE, currentCalendar);
    }

    private View getView(String key, Calendar currentCalendar){
        int index = getDayIndexByDate(currentCalendar);
        return rootView.findViewWithTag(key + index);
    }


    public interface CalendarListener{
        void onDayClick(Date date);

        void onDayLongClick(Date date);
    }


}
