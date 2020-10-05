package com.iiitd.dsavisualizer.runapp.others;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class AnimateViews {

    private int oneh;
    private int onew;
    public Context context;

    public AnimateViews(int oneh, int onew) {
        this.oneh = oneh;
        this.onew = onew;
    }

    public AnimateViews(int oneh, int onew, Context context) {
        this.oneh = oneh;
        this.onew = onew;
        this.context = context;
    }

    void animateBottom(View view){
        view.animate().translationYBy(oneh).start();
    }

    void animateBottom(View view, int times){
        int by = times * oneh;
        view.animate().translationYBy(by);
    }

    void animateUp(View view){
        view.animate().translationYBy(-oneh).start();
    }

    void animateUp(View view, int times){
        int by = times * oneh;
        view.animate().translationYBy(-by);
    }

    void animateLeft(View view){
        view.animate().translationXBy(-onew).start();
    }

    void animateLeft(View view, int times){
        int by = times * onew;
        view.animate().translationXBy(-by);
    }

    void animateRight(View view){
        view.animate().translationXBy(onew).start();
    }

    void animateRight(View view, int times){
        int by = times * onew;
        view.animate().translationXBy(by);
    }

    public void animateInst(final View view, final int times, final String inst){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(inst.equals("U")){
                    animateUp(view, times);
                }
                else if(inst.equals("B")){
                    animateBottom(view, times);
                }
                else if(inst.equals("L")){
                    animateLeft(view, times);
                }
                else if(inst.equals("R")){
                    animateRight(view, times);
                }
                else{
                    System.out.println("NO ANIM INST.");
                }
            }
        });

//        if(inst.equals("U")){
//            animateUp(view, times);
//        }
//        else if(inst.equals("B")){
//            animateBottom(view, times);
//        }
//        else if(inst.equals("L")){
//            animateLeft(view, times);
//        }
//        else if(inst.equals("R")){
//            animateRight(view, times);
//        }
//        else{
//            System.out.println("NO ANIM INST.");
//        }
    }

}