package com.iiitd.dsavisualizer.algorithms.sorting.merge;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

//HARDCODED INFORMATION, Handle with care
public class MergeSortInfo {

    /*4*/  public static final String LS = "Sorting Left Half";
    /*14*/ public static final String LS_U = "Sorting Left Half Done";
    /*5*/  public static final String RS = "Sorting Right Half";
    /*14*/ public static final String RS_U = "Sorting Right Half Done";
    /*11*/ public static final String L_LESSEQUAL_R = "Left <= Right";
    /*13*/ public static final String L_GREATER_R = "Left > Right";
    /*11*/ public static final String L_EXTRAS = "Left Extras";
    /*13*/ public static final String R_EXTRAS = "Right Extras";
    /*9*/  public static final String MERGE_STARTED = "Array copy for merging";
    /*1*/  public static final String SINGLE_MERGE = "Single element is already sorted";

    public static HashMap<String, Integer[]> map = new HashMap<>();

    static {
        map.put(LS, new Integer[]{4});
        map.put(LS_U, new Integer[]{14});
        map.put(RS, new Integer[]{5});
        map.put(RS_U, new Integer[]{14});
        map.put(L_LESSEQUAL_R, new Integer[]{11});
        map.put(L_GREATER_R, new Integer[]{13});
        map.put(L_EXTRAS, new Integer[]{11});
        map.put(R_EXTRAS, new Integer[]{13});
        map.put(MERGE_STARTED, new Integer[]{9});
        map.put(SINGLE_MERGE, new Integer[]{1});
    }

           public static final String[] psuedocode = new String[]{
    /*0*/         "MergeSort(data, start, end):",
    /*1*/         "    if start > end",
    /*2*/         "        return",
    /*3*/         "    mid = (start+end)/2",
    /*4*/         "    mergeSort(data, start, mid)",
    /*5*/         "    mergeSort(data, mid+1, end)",
    /*6*/         "    merge(data, start, mid, end)",
    /*7*/         "",
    /*8*/         "merge(data, start, mid, end)",
    /*9*/         "Copy both arrays into 2 temporary arrays",
    /*10*/        "While(any element is left in any array):",
    /*11*/        "    if(left array's element <= right array's element):",
    /*12*/        "        copy left array's element to final sorted array",
    /*13*/        "    else:",
    /*14*/        "        copy right array's element to final sorted array"
    };



}
