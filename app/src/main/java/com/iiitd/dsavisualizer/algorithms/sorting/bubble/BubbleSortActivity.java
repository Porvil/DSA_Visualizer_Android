package com.iiitd.dsavisualizer.algorithms.sorting.bubble;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.iiitd.dsavisualizer.R;
import com.iiitd.dsavisualizer.constants.AppSettings;
import com.iiitd.dsavisualizer.utility.UtilUI;

import java.util.Timer;
import java.util.TimerTask;

public class BubbleSortActivity extends AppCompatActivity {

    Context context;

    DrawerLayout dl_main;
    View v_main;
    View v_menu;
    ViewStub vs_main;
    ViewStub vs_menu;
    LinearLayout ll_anim;
    ConstraintLayout cl_info;
    ImageButton btn_play;
    ImageButton btn_back;
    ImageButton btn_menu;
    ImageButton btn_code;
    ImageButton btn_info;
    ImageButton btn_backward;
    ImageButton btn_forward;
    SeekBar sb_animspeed;
    TextView tv_seqno;
    TextView tv_info;
    ConstraintLayout cl_psuedocode;
    ScrollView sv_psuedocode;
    LinearLayout ll_psuedocode;

    SeekBar sb_arraysize;
    TextView tv_arraysize;
    ImageButton btn_closemenu;
    Button btn_generate;
    Button btn_clear;
    Switch sw_randomarray;
    EditText et_customarray;

    BubbleSort bubbleSort;
    TextView[] textViews;

    Timer timer = new Timer();
    boolean isAutoPlay = false;
    boolean isRandomArray = true;
    boolean isPseudocode = true;
    int autoAnimSpeed = AppSettings.DEFAULT_ANIM_SPEED;
    final int LAYOUT = R.layout.activity_base;
    final int CONTROL = R.layout.controls_bubble_sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_base);
        context = this;

        // findViewById
        dl_main = findViewById(R.id.dl_main);
        vs_main = findViewById(R.id.vs_main);
        vs_menu = findViewById(R.id.vs_menu);
        vs_main.setLayoutResource(LAYOUT);
        vs_menu.setLayoutResource(CONTROL);
        v_main = vs_main.inflate();
        v_menu = vs_menu.inflate();

        ll_anim = v_main.findViewById(R.id.ll_anim);
        sb_animspeed = v_main.findViewById(R.id.sb_animspeed);
        btn_play = v_main.findViewById(R.id.btn_play);
        btn_menu = v_main.findViewById(R.id.btn_menu);
        btn_code = v_main.findViewById(R.id.btn_code);
        btn_info = v_main.findViewById(R.id.btn_info);
        btn_back = v_main.findViewById(R.id.btn_back);
        btn_backward = v_main.findViewById(R.id.btn_backward);
        btn_forward = v_main.findViewById(R.id.btn_forward);
        tv_seqno = v_main.findViewById(R.id.tv_seqno);
        tv_info = v_main.findViewById(R.id.tv_info);
        cl_psuedocode = v_main.findViewById(R.id.cl_psuedocode);
        sv_psuedocode = v_main.findViewById(R.id.sv_psuedocode);
        ll_psuedocode = v_main.findViewById(R.id.ll_pseudocode);
        cl_info = v_main.findViewById(R.id.cl_info);

        btn_closemenu = v_menu.findViewById(R.id.btn_closemenu);
        sb_arraysize = v_menu.findViewById(R.id.sb_arraysize);
        tv_arraysize = v_menu.findViewById(R.id.tv_arraysize);
        btn_generate = v_menu.findViewById(R.id.btn_generate);
        btn_clear = v_menu.findViewById(R.id.btn_clear);
        sw_randomarray = v_menu.findViewById(R.id.sw_randomarray);
        et_customarray = v_menu.findViewById(R.id.et_customarray);

        tv_arraysize.setText(String.valueOf(sb_arraysize.getProgress() + 1));

        addPseudocode();
        initViews();

        sw_randomarray.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isRandomArray = true;
                    et_customarray.setError(null);
                    sw_randomarray.setText(sw_randomarray.getTextOn());
                    tv_arraysize.setText(String.valueOf(sb_arraysize.getProgress()+1));
                }
                else {
                    isRandomArray = false;
                    sw_randomarray.setText(sw_randomarray.getTextOff());
                    String customArray = et_customarray.getText().toString();
                    if(customArray != null || !customArray.isEmpty()){
                        String[] customInput = customArray.split(",");
                        int length = customInput.length;
                        if(length > 16){
                            et_customarray.setError("Decrease elements");
                            tv_arraysize.setText("0");
                        }
                        else {
                            int[] data = new int[length];
                            try {
                                for (int i = 0; i < data.length; i++) {
                                    data[i] = Integer.parseInt(customInput[i]);
                                }
                                tv_arraysize.setText(String.valueOf(customInput.length));
                            } catch (NumberFormatException e) {
                                et_customarray.setError("Bad Input");
                                tv_arraysize.setText("0");
                            }
                        }
                    }
                }
            }
        });

        et_customarray.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(!isRandomArray) {
                    if (s != null || !s.toString().isEmpty()) {
                        String[] customInput = s.toString().split(",");
                        int length = customInput.length;
                        if(length > 16){
                            et_customarray.setError("Decrease elements");
                            tv_arraysize.setText("0");
                        }
                        else {
                            int[] data = new int[length];
                            try {
                                for (int i = 0; i < data.length; i++) {
                                    data[i] = Integer.parseInt(customInput[i]);
                                }
                                tv_arraysize.setText(String.valueOf(customInput.length));
                            } catch (NumberFormatException e) {
                                et_customarray.setError("Bad Input");
                                tv_arraysize.setText("0");
                            }
                        }
                    }
                }
            }
        });

        // Auto Animation Speed
        sb_animspeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 2500ms to 500ms
                autoAnimSpeed = (2000 - seekBar.getProgress() * 20) + 500;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
               if (bubbleSort != null && isAutoPlay) {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (bubbleSort.sequence.curSeqNo < bubbleSort.sequence.size)
                                onForwardClick();
                            else {
                                isAutoPlay = false;
                                btn_play.setImageDrawable(UtilUI.getDrawable(context, AppSettings.PLAY_BUTTON));
                                timer.cancel();
                            }
                        }
                    }, 0, autoAnimSpeed);
                }
            }
        });

        // Auto Animation Play/Pause Button
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bubbleSort != null){
                    if(isAutoPlay){
                        isAutoPlay = false;
                        btn_play.setImageDrawable(UtilUI.getDrawable(context, AppSettings.PLAY_BUTTON));
                        timer.cancel();
                    }
                    else{
                        isAutoPlay = true;
                        btn_play.setImageDrawable(UtilUI.getDrawable(context, AppSettings.PAUSE_BUTTON));
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (bubbleSort.sequence.curSeqNo < bubbleSort.sequence.size)
                                    onForwardClick();
                                else {
                                    isAutoPlay = false;
                                    btn_play.setImageDrawable(UtilUI.getDrawable(context, AppSettings.PLAY_BUTTON));
                                    timer.cancel();
                                }
                            }
                        }, 0, autoAnimSpeed);
                    }
                }
            }
        });

        // One Animation Step-Back
        btn_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAutoPlay = false;
                btn_play.setImageDrawable(UtilUI.getDrawable(context, AppSettings.PLAY_BUTTON));
                timer.cancel();
                onBackwardClick();
            }
        });

        // One Animation Step-Forward
        btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAutoPlay = false;
                btn_play.setImageDrawable(UtilUI.getDrawable(context, AppSettings.PLAY_BUTTON));
                timer.cancel();
                onForwardClick();
            }
        });

        // Info Button
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.layout_sorting_info, null);
                TextView tv_name = view.findViewById(R.id.tv_name);
                TextView tv_avg = view.findViewById(R.id.tv_avg);
                TextView tv_worst = view.findViewById(R.id.tv_worst);
                TextView tv_best = view.findViewById(R.id.tv_best);
                TextView tv_space = view.findViewById(R.id.tv_space);
                TextView tv_stable = view.findViewById(R.id.tv_stable);
                TextView tv_comparisons = view.findViewById(R.id.tv_comparisons);
                ImageButton btn_close = view.findViewById(R.id.btn_close);

                String comparisons = "-";
                if(bubbleSort != null) {
                    isAutoPlay = false;
                    btn_play.setImageDrawable(UtilUI.getDrawable(context, AppSettings.PLAY_BUTTON));
                    timer.cancel();
                    comparisons = String.valueOf(bubbleSort.comparisons);
                }

                tv_name.setText(BubbleSortStats.name);
                tv_avg.setText(BubbleSortStats.avg);
                tv_worst.setText(BubbleSortStats.worst);
                tv_best.setText(BubbleSortStats.best);
                tv_space.setText(BubbleSortStats.space);
                tv_stable.setText(BubbleSortStats.stable);
                tv_comparisons.setText(comparisons);

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(view);
                dialog.show();

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        // Menu Button
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dl_main.isOpen()) {
                    isAutoPlay = false;
                    btn_play.setImageDrawable(UtilUI.getDrawable(context, AppSettings.PLAY_BUTTON));
                    timer.cancel();
                    dl_main.openDrawer(Gravity.RIGHT);
                }
            }
        });

        // Stop auto animation on drawer open
        dl_main.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset){
                if(slideOffset <= 0.40){
                    isAutoPlay = false;
                    btn_play.setImageDrawable(UtilUI.getDrawable(context, AppSettings.PLAY_BUTTON));
                    timer.cancel();
                }
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView){}

            @Override
            public void onDrawerClosed(@NonNull View drawerView){}

            @Override
            public void onDrawerStateChanged(int newState) {}
        });

        // Array Size Slider for generation of random numbers
        sb_arraysize.setOnTouchListener(new SeekBar.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        dl_main.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        dl_main.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
                        break;
                }

                v.onTouchEvent(event);
                return true;
            }
        });

        // Array Size Slider for changing size of array
        sb_arraysize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(isRandomArray)
                    tv_arraysize.setText(String.valueOf(progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Close Menu Control
        btn_closemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl_main.closeDrawer(Gravity.RIGHT);
            }
        });

        // Generates Random Numbers
        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int arraySize = sb_arraysize.getProgress() + 1;
                if(bubbleSort != null){
                    bubbleSort.linearLayout.removeAllViews();
                    bubbleSort = null;
                }

                if(isRandomArray){
                    bubbleSort = new BubbleSort(context, ll_anim, arraySize);
                }
                else {
                    String customArray = et_customarray.getText().toString();
                    if(customArray != null || !customArray.isEmpty()){
                        String[] customInput = customArray.split(",");
                        int length = customInput.length;
                        if(length > 16){
                            et_customarray.setError("Decrease elements");
                            tv_arraysize.setText("0");
                        }
                        else {
                            int[] data = new int[customInput.length];
                            try {
                                for (int i = 0; i < data.length; i++) {
                                    data[i] = Integer.parseInt(customInput[i]);
                                }
                                bubbleSort = new BubbleSort(context, ll_anim, data);
                            }
                            catch (NumberFormatException e){
                                et_customarray.setError("Bad Input");
                                bubbleSort = null;
                            }
                        }
                    }
                }
                initViews();
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bubbleSort != null) {
                    bubbleSort.linearLayout.removeAllViews();
                    bubbleSort = null;
                }
                initViews();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAutoPlay = false;
                btn_play.setImageDrawable(UtilUI.getDrawable(context, AppSettings.PLAY_BUTTON));
                timer.cancel();

                View view = getLayoutInflater().inflate(R.layout.layout_back_confirmation, null);

                Button btn_cancel = view.findViewById(R.id.btn_cancel);
                Button btn_yes = view.findViewById(R.id.btn_yes);

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(view);
                dialog.show();

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        });

        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPseudocode) {
                    cl_psuedocode.setVisibility(View.GONE);
                    cl_psuedocode.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(bubbleSort != null){
                                int width = ll_anim.getWidth();
                                int div = width / bubbleSort.arraySize;

                                for(int i = 0; i< bubbleSort.arraySize; i++){
                                    int position = bubbleSort.positions[i];
                                    int x = position*div;
                                    float v1 = x - bubbleSort.views[i].getX();
                                    bubbleSort.views[i].animate().translationXBy(v1).start();
                                }
                            }
                        }
                    }, 0);
                }
                else {
                    cl_psuedocode.setVisibility(View.VISIBLE);
                    cl_psuedocode.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(bubbleSort != null){
                                int width = ll_anim.getWidth();
                                int div = width / bubbleSort.arraySize;

                                for(int i = 0; i< bubbleSort.arraySize; i++){
                                    int position = bubbleSort.positions[i];
                                    int x = position*div;
                                    float v1 = x - bubbleSort.views[i].getX();
                                    bubbleSort.views[i].animate().translationXBy(v1).start();
                                }
                            }
                        }
                    }, 0);

                }
                isPseudocode = !isPseudocode;
            }
        });

    }

    private void initViews() {
        if(bubbleSort != null){
            tv_seqno.setText("0 / " + bubbleSort.sequence.animationStates.size());
            UtilUI.setText(tv_info, bubbleSort.sequence.animationStates.get(0).info);
            UtilUI.highlightViews(context, bubbleSort.sequence.views,
                    bubbleSort.sequence.animationStates.get(0).highlightIndexes);
            String state = bubbleSort.sequence.animationStates.get(0).state;
            if(BubbleSortInfo.map.containsKey(state)){
                Integer[] integers = BubbleSortInfo.map.get(state);
                UtilUI.changeTextViewsColors(context, sv_psuedocode, textViews, integers);
            }
        }
        else{
            tv_seqno.setText("0 / 0");
            UtilUI.setText(tv_info, "-");
            UtilUI.changeTextViewsColors(context, sv_psuedocode, textViews, null);
        }

    }

    private void addPseudocode(){
        int sizeOfPseudocode = BubbleSortInfo.psuedocode.length;
        textViews = new TextView[sizeOfPseudocode];
        for(int i=0;i<sizeOfPseudocode;i++){
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(this);
            textView.setLayoutParams(lparams);
            textView.setText(BubbleSortInfo.psuedocode[i]);
            textView.setPadding(5, 0,0,0);
            textViews[i] = textView;
            ll_psuedocode.addView(textView);
        }

        for(int i : BubbleSortInfo.boldIndexes){
            textViews[i].setTypeface(textViews[i].getTypeface(), Typeface.BOLD);
        }
    }

    private void onForwardClick(){
        if (bubbleSort != null) {
            bubbleSort.forward();
            final int curSeqNo = bubbleSort.sequence.curSeqNo;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_seqno.setText(curSeqNo + " / " + bubbleSort.sequence.animationStates.size());
                    if(curSeqNo < bubbleSort.sequence.size) {
                        String state = bubbleSort.sequence.animationStates.get(curSeqNo).state;
                        if(BubbleSortInfo.map.containsKey(state)){
                            Integer[] integers = BubbleSortInfo.map.get(state);
                            UtilUI.changeTextViewsColors(context, sv_psuedocode, textViews, integers);
                        }
                        UtilUI.setText(tv_info, bubbleSort.sequence.animationStates.get(curSeqNo).info);
                        UtilUI.highlightCombined(context, bubbleSort.sortedIndexes,
                                bubbleSort.views, curSeqNo,
                                bubbleSort.sequence.animationStates.get(curSeqNo).highlightIndexes);
                    }
                    else{
                        UtilUI.changeTextViewsColors(context, sv_psuedocode, textViews, null);
                        UtilUI.setText(tv_info, "Array is sorted");
                        UtilUI.highlightCombined(context, bubbleSort.sortedIndexes,
                                bubbleSort.views, -1,
                                null);
                    }
                }
            });
        }
    }

    private void onBackwardClick(){
        if (bubbleSort != null) {
            bubbleSort.backward();
            final int curSeqNo = bubbleSort.sequence.curSeqNo;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_seqno.setText(curSeqNo + " / " + bubbleSort.sequence.animationStates.size());
                    UtilUI.setText(tv_info, bubbleSort.sequence.animationStates.get(curSeqNo).info);
                    String state = bubbleSort.sequence.animationStates.get(curSeqNo).state;
                    if (BubbleSortInfo.map.containsKey(state)) {
                        Integer[] integers = BubbleSortInfo.map.get(state);
                        UtilUI.changeTextViewsColors(context, sv_psuedocode, textViews, integers);
                    }

                    UtilUI.highlightCombined(context, bubbleSort.sortedIndexes,
                            bubbleSort.views, curSeqNo,
                            bubbleSort.sequence.animationStates.get(curSeqNo).highlightIndexes);


                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (dl_main.isDrawerOpen(Gravity.RIGHT)){
            dl_main.closeDrawer(Gravity.RIGHT);
        }
        else {
            btn_back.performClick();
        }
    }

}