package com.iiitd.dsavisualizer.algorithms.sorting.quick;

import android.content.Context;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iiitd.dsavisualizer.R;
import com.iiitd.dsavisualizer.algorithms.sorting.merge.MergeSortInfo;
import com.iiitd.dsavisualizer.constants.AppSettings;
import com.iiitd.dsavisualizer.runapp.others.AnimationDirection;
import com.iiitd.dsavisualizer.runapp.others.AnimationState;
import com.iiitd.dsavisualizer.runapp.others.ElementAnimationData;
import com.iiitd.dsavisualizer.utility.Util;
import com.iiitd.dsavisualizer.utility.UtilUI;

import java.util.ArrayList;
import java.util.Random;

public class QuickSort {

    Context context;
    int arraySize;
    int[] data;
    QuickSortData[] quickSortData;
    View[] views;
    int[] positions;
    LinearLayout linearLayout;
    QuickSortSequence sequence;
    Random random;
    int width;
    int height;
    int textSize;
    boolean isRandomize;
    int[] rawInput;
    int comparisons;
    ArrayList<Pair<Integer, Integer>> sortedIndexes;

    public QuickSort(Context context, LinearLayout linearLayout, int arraySize) {
        this.context = context;
        this.random = new Random();
        this.arraySize = arraySize;
        this.isRandomize = true;
        this.linearLayout = linearLayout;
        this.comparisons = 0;
        this.sortedIndexes = new ArrayList<>();
        rawInput = null;

        init();
    }

    public QuickSort(Context context, LinearLayout linearLayout, int[] rawInput) {
        this.context = context;
        this.random = new Random();
        this.arraySize = rawInput.length;
        this.isRandomize = false;
        this.linearLayout = linearLayout;
        this.comparisons = 0;
        this.sortedIndexes = new ArrayList<>();
        this.rawInput = rawInput;

        init();
    }

    private void init() {
        if(arraySize > 8){
            textSize = AppSettings.TEXT_SMALL;
        }
        else{
            textSize = AppSettings.TEXT_MEDIUM;
        }

        int totalWidth = linearLayout.getWidth();
        int totalHeight = linearLayout.getHeight();
        this.width = totalWidth / arraySize;
        this.height =  totalHeight / 2;

        int MAX = 0;

        this.data = new int[arraySize];
        this.quickSortData = new QuickSortData[arraySize];
        this.views = new View[arraySize];
        this.positions = new int[arraySize];
        if(isRandomize){
            for (int i = 0; i < data.length; i++) {
                data[i] = random.nextInt(20) + 1;
                MAX = Math.max(data[i], MAX);
            }
        }
        else{
            for (int i = 0; i < data.length; i++) {
                data[i] = rawInput[i];
                MAX = Math.max(data[i], MAX);
            }
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, height, 1);
        LayoutInflater vi = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i=0;i<data.length;i++){
            float x = (float)data[i] / (float)MAX;
            float h = (x * .75f) + .20f;

            View myView = vi.inflate(R.layout.element_quick_sort, null);
            myView.setLayoutParams(layoutParams);
            myView.setPadding(5,5,5,5);
            TextView tv = myView.findViewById(R.id.tv_elementvalue);
            tv.setText(String.valueOf(data[i]));
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(textSize);
            tv.getLayoutParams().height = (int) (height * h * .75f);
            tv.setBackground(UtilUI.getDrawable(context, AppSettings.ROUNDED_RECT_ELEMENT));
            linearLayout.addView(myView);

            QuickSortData quickSortData1 = new QuickSortData();
            quickSortData1.data = data[i];
            quickSortData1.index = i;
            views[i] = myView;
            positions[i] = i;
            quickSortData[i] = quickSortData1;
        }

        this.sequence = new QuickSortSequence(0);
        this.sequence.setViews(views);
        this.sequence.setPositions(positions);
        this.sequence.setAnimateViews(height, width, context);

        this.quicksort();
    }

    public void forward(){
        sequence.forward();
    }

    public void backward(){
        sequence.backward();
    }

    private void quicksort(){
        AnimationState animationState = new AnimationState(QuickSortInfo.QS,
                QuickSortInfo.getQuickSortString(0, quickSortData.length-1));
        sequence.addAnimSeq(animationState);
        sort(quickSortData, 0, quickSortData.length-1);
    }

    private int partition(QuickSortData arr[], int low, int high){
        int i = low+1;
        QuickSortData pivotElement = arr[low];

        AnimationState animationState5 = new AnimationState(QuickSortInfo.PA, QuickSortInfo.getPartitionString(low, high));
        for(int z=low;z<=high;z++){
            animationState5.addElementAnimationData(new ElementAnimationData(quickSortData[z].index, new Pair<>(AnimationDirection.DOWN, 1)));
        }
        sequence.addAnimSeq(animationState5);

        AnimationState animationState = new AnimationState(QuickSortInfo.PI, QuickSortInfo.getPivot(pivotElement.data));
        animationState.addPointers(new Pair<>(pivotElement.index, "P"));
        sequence.addAnimSeq(animationState);

        for (int j=low+1; j<=high; j++){
            comparisons++;
            Pair<Integer, String> pairP = new Pair<>(pivotElement.index, "P");
            Pair<Integer, String> pairI = new Pair<>(arr[i].index, "I");
            Pair<Integer, String> pairJ = new Pair<>(arr[j].index, "J");
            AnimationState animationState1;
            if (arr[j].data < pivotElement.data){
                int val = j-i;
                animationState1 = new AnimationState(QuickSortInfo.E_LESSER_P,
                        QuickSortInfo.getComparedString(arr[j].data, pivotElement.data, j, i));
                animationState1.addElementAnimationData(new ElementAnimationData(arr[i].index, new Pair<>(AnimationDirection.RIGHT, val)));
                animationState1.addElementAnimationData(new ElementAnimationData(arr[j].index, new Pair<>(AnimationDirection.LEFT, val)));
                Util.swap(arr[i], arr[j]);
                i++;
            }
            else{
                animationState1 = new AnimationState(QuickSortInfo.E_GREATEREQUAL_P,
                        QuickSortInfo.getComparedString(arr[j].data, pivotElement.data, j, i));
            }
            animationState1.addPointers(pairP, pairI, pairJ);
            sequence.addAnimSeq(animationState1);
        }

        int val2 = i-1-low;
        AnimationState animationState2 = new AnimationState(QuickSortInfo.SWAP_END, QuickSortInfo.getEndSwap(low, i-1));
        animationState2.addElementAnimationData(new ElementAnimationData(arr[low].index, new Pair<>(AnimationDirection.RIGHT, val2)));
        animationState2.addElementAnimationData(new ElementAnimationData(arr[i-1].index, new Pair<>(AnimationDirection.LEFT, val2)));
        animationState2.addPointers(new Pair<>(pivotElement.index, "P"), new Pair<>(arr[i-1].index, "I-1"));
        sequence.addAnimSeq(animationState2);
        Util.swap(arr[low], arr[i-1]);

        AnimationState animationState6 = new AnimationState(QuickSortInfo.PA_U, QuickSortInfo.PA_U);
        animationState6.addHighlightIndexes(arr[i-1].index);
        for(int z=low;z<=high;z++){
            animationState6.addElementAnimationData(new ElementAnimationData(quickSortData[z].index, new Pair<>(AnimationDirection.UP, 1)));
        }
        sequence.addAnimSeq(animationState6);

        sortedIndexes.add(new Pair<>(sequence.animationStates.size(), arr[i-1].index));
        return i-1;
    }

    private void sort(QuickSortData arr[], int low, int high){
        if (low < high) {
            int pi = partition(arr, low, high);

            AnimationState animationState = new AnimationState(QuickSortInfo.LS, QuickSortInfo.getQuickSortString(low, pi-1));
            sequence.addAnimSeq(animationState);
            sort(arr, low, pi-1);

            AnimationState animationState1 = new AnimationState(QuickSortInfo.RS, QuickSortInfo.getQuickSortString(pi+1, high));
            sequence.addAnimSeq(animationState1);
            sort(arr, pi+1, high);
        }
        else if (low >=0 && low < arr.length && high >=0 && high <arr.length){
            AnimationState animationState = new AnimationState(QuickSortInfo.SINGLE_PARTITION, QuickSortInfo.SINGLE_PARTITION);
            sequence.addAnimSeq(animationState);
            sortedIndexes.add(new Pair<>(sequence.animationStates.size(), arr[low].index));
        }
    }

}