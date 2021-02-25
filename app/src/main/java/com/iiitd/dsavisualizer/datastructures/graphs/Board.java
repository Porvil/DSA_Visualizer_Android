package com.iiitd.dsavisualizer.datastructures.graphs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Pair;
import android.util.TypedValue;
import android.widget.ImageView;

import com.iiitd.dsavisualizer.R;
import com.iiitd.dsavisualizer.runapp.others.CustomCanvas;
import com.iiitd.dsavisualizer.utility.Util;
import com.iiitd.dsavisualizer.utility.UtilUI;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

// Used by GraphActivity Class
public class Board {

    Context context;

    // Constants
    private final int topAngle = 45;               // in degrees
    private final int bottomAngle = 45;            // in degrees
    private final int nodeSize = 7;                // in mm
    private final float circleRatio = 0.66f;       // in ratio [0,1]
    private final float edgeArrowRatio = 0.24f;    // in ratio [0,1]
    private final float nodeRadius;                // in pixels [Radius of one node]
    private final float coordinatesOffset;         // in ratio [0,1]
    private final int textSizeCoordinates;         // in sp
    private final int textSize;                    // in sp
    private final int edgeWidth;                   // in dp
    private final int edgeArrowWidth;              // in dp
    private final int arrowLength;                 // in pixels

    // Board Variables
    public float X;                                // Width of Board
    public float Y;                                // Height of Board
    public int xCount;                             // No. of columns
    public int yCount;                             // No. of rows
    public float xSize;                            // One Column Width
    public float ySize;                            // One Row Height
    public BoardElement[][] boardElements;         // Contains data about board elements
    public int maxVertices;                        // Max No. of Vertices possible
    public CustomCanvas customCanvas;              // Custom Canvas holds all canvases

    // Paint Variables
    private Paint paintGrid;                       // Grid
    private Paint paintGridCoordinates;            // Grid Coordinates
    private Paint paintVertex;                     // Vertex
    private Paint paintVertexText;                 // Vertex Text
    private Paint paintEdge;                       // Edge
    private Paint paintEdgeArrows;                 // Edge Arrows
    private Paint paintEdgeWeight;                 // Edge Weight
    private Paint paintVertexAnim;                 // Animation Vertex
    private Paint paintVertexTextAnim;             // Animation Vertex Text
    private Paint paintEdgeAnim;                   // Animation Edge
    private Paint paintEdgeArrowsAnim;             // Animation Edge Arrows
    private Paint paintEdgeWeightAnim;             // Animation Edge Weight


    public Board(Context context, CustomCanvas customCanvas) {
        this.context = context;
        this.X = customCanvas.imageViewGraph.getWidth();
        this.Y = customCanvas.imageViewGraph.getHeight();
        this.customCanvas = customCanvas;

        // px = 1mm
        float px = UtilUI.mmToPx(context, 1);
        float cm = px * nodeSize;
        float x = (X / cm);
        float y = (Y / cm);

        xCount = (int) Math.ceil(x);
        yCount = (int) Math.ceil(y);

        xSize = (this.X / xCount);
        ySize = (this.Y / yCount);
        maxVertices = yCount * xCount;

        System.out.println("xSize = " + xSize + " | " + " ySize = " + ySize);
        System.out.println("X = " + X + " | " + " Y = " + Y);
        System.out.println("no of rows(ycount) = " + yCount + " | " + "no of columns(xcount) = " + xCount);
        System.out.println("max count = " + yCount * xCount );

        float minSide = Math.min(xSize, ySize);
        this.nodeRadius = ( minSide * circleRatio) / 2;
        this.arrowLength = (int) (( minSide * edgeArrowRatio) / 2);
        this.coordinatesOffset = 0.95f;

        this.boardElements = new BoardElement[yCount][xCount];
        for (int r = 0; r < yCount; r++) {
            for (int c = 0; c < xCount; c++) {
                boardElements[r][c] = new BoardElement();
            }
        }

        this.textSizeCoordinates = context.getResources().getDimensionPixelSize(R.dimen.coordinatesText);
        this.textSize = context.getResources().getDimensionPixelSize(R.dimen.nodeText);
        this.edgeWidth = context.getResources().getDimensionPixelSize(R.dimen.edgeWidth);
        this.edgeArrowWidth = context.getResources().getDimensionPixelSize(R.dimen.edgeArrowWidth);

        // Initializes all Paint Variables
        initPaints();

        // Draw Grid on Grid ImageView
        drawGrid();
    }

    private void initPaints(){

        int shade = UtilUI.getCurrentThemeColor(context, R.attr.shade);
        int light = UtilUI.getCurrentThemeColor(context, R.attr.light);
        int base = UtilUI.getCurrentThemeColor(context, R.attr.base);
        int medium = UtilUI.getCurrentThemeColor(context, R.attr.medium);
        int dark = UtilUI.getCurrentThemeColor(context, R.attr.dark);
        int white = Color.WHITE;

        // Grid Lines
        this.paintGrid = new Paint();
        this.paintGrid.setColor(light);

        // Grid Coordinates
        this.paintGridCoordinates = new Paint();
        this.paintGridCoordinates.setTextAlign(Paint.Align.RIGHT);
        this.paintGridCoordinates.setTextSize(textSizeCoordinates);
        this.paintGridCoordinates.setColor(light);

        // Vertex
        this.paintVertex = new Paint();
        this.paintVertex.setColor(base);

        // Vertex Text
        this.paintVertexText = new Paint();
        this.paintVertexText.setTextAlign(Paint.Align.CENTER);
        this.paintVertexText.setTextSize(textSize);
        this.paintVertexText.setColor(white);
        this.paintVertexText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // Edge
        this.paintEdge = new Paint();
        this.paintEdge.setColor(medium);
        this.paintEdge.setStrokeWidth(edgeWidth);

        // Edge Arrows
        this.paintEdgeArrows = new Paint();
        this.paintEdgeArrows.setColor(medium);
        this.paintEdgeArrows.setStrokeWidth(edgeArrowWidth);

        // Edge Weight
        this.paintEdgeWeight = new Paint();
        this.paintEdgeWeight.setTextAlign(Paint.Align.CENTER);
        this.paintEdgeWeight.setTextSize(textSize);
        this.paintEdgeWeight.setColor(medium);
        this.paintEdgeWeight.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // Animation Vertex
        this.paintVertexAnim = new Paint();
        this.paintVertexAnim.setColor(dark);

        // Animation Vertex Text
        this.paintVertexTextAnim = new Paint();
        this.paintVertexTextAnim.setTextAlign(Paint.Align.CENTER);
        this.paintVertexTextAnim.setTextSize(textSize);
        this.paintVertexTextAnim.setColor(white);
        this.paintVertexTextAnim.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // Animation Edge
        this.paintEdgeAnim = new Paint();
        this.paintEdgeAnim.setColor(dark);
        this.paintEdgeAnim.setStrokeWidth(edgeWidth);

        // Animation Edge Arrows
        this.paintEdgeArrowsAnim = new Paint();
        this.paintEdgeArrowsAnim.setColor(dark);
        this.paintEdgeArrowsAnim.setStrokeWidth(edgeArrowWidth);

        // Animation Edge Weight
        this.paintEdgeWeightAnim = new Paint();
        this.paintEdgeWeightAnim.setTextAlign(Paint.Align.CENTER);
        this.paintEdgeWeightAnim.setTextSize(textSize);
        this.paintEdgeWeightAnim.setColor(dark);
        this.paintEdgeWeightAnim.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    private void drawGrid() {
        Rect rect = new Rect();

        for(int i=0; i<xCount+1; i++){
            int left = (int) (i*xSize);
            int right = left + 1;
            int top = 0;
            int bottom = (int) Y;
            rect.set(left, top, right, bottom);
            customCanvas.canvasGrid.drawRect(rect, paintGrid);
        }

        for(int i=0; i<yCount+1; i++){
            int top = (int) (i*ySize);
            int bottom = top + 1;
            int left = 0;
            int right = (int) X;
            rect.set(left, top, right, bottom);
            customCanvas.canvasGrid.drawRect(rect, paintGrid);
        }

        for (int r = 0; r < yCount; r++) {
            for (int c = 0; c < xCount; c++) {
                String text = "(" + r + "," + c + ")";
                Rect rect1 = getRect(r, c);
                int x = (int) (rect1.left + (rect1.width() * coordinatesOffset));
                int y = (int) (rect1.top + (rect1.height() * coordinatesOffset));
                Rect rectText = new Rect();
                paintGridCoordinates.getTextBounds(text, 0, text.length(), rectText);
                customCanvas.canvasCoordinates.drawText(text, x, y , paintGridCoordinates);
            }
        }

    }

    // Re-Draws the complete graphOld
    public void update(Graph graph){

        System.out.println("REDRAWING CANVAS");
        // clears canvas
        clearCanvasGraph();

        boolean weighted = graph.weighted;

        // Nodes
        for (int r = 0; r < yCount; r++) {
            for (int c = 0; c < xCount; c++) {
                if(boardElements[r][c].occupied){
                    Rect rect = getRect(r, c);
                    drawNodeGraph(rect, boardElements[r][c].value);
                }
            }
        }

        //Edges
        for(Map.Entry<Integer, ArrayList<Edge>> vertex : graph.edgeListMap.entrySet() ){

            for (Edge edge : vertex.getValue()) {
                System.out.println(vertex.getKey() + ":" + edge);

                int[] vertex1 = getCoordinates(vertex.getKey());
                int[] vertex2 = getCoordinates(edge.des);

                Rect rect1 = getRect(vertex1[0], vertex1[1]);
                Rect rect2 = getRect(vertex2[0], vertex2[1]);

                drawEdgeGraph(rect1, rect2, edge, weighted);
            }
        }

        refreshGraph();
    }

    // Draws a single Node
    public void drawNodeGraph(Rect rect, int name) {
        __drawNode(customCanvas.canvasGraph, rect, name, paintVertex, paintVertexText);
    }

    // Draws a single Node
    public void drawNodeAnim(Rect rect, int name) {
        __drawNode(customCanvas.canvasAnimation, rect, name, paintVertexAnim, paintVertexTextAnim);
    }

    // Draws a single Node
    public void __drawNode(Canvas canvas, Rect rect, int name, Paint paintV, Paint paintT) {
        int x = rect.centerX();
        int y = rect.centerY();

//        float radius = getRadius(rect);
//        System.out.println(radius + " ==" + nodeRadius);
        String text = String.valueOf(name);

        canvas.drawCircle(x, y, nodeRadius, paintV);

        Rect rectText = new Rect();
        paintT.getTextBounds(text, 0, text.length(), rectText);
        canvas.drawText(text, x, y - (paintT.descent() + paintT.ascent()) / 2, paintVertexText);
    }

    // Draws a single EdgeOld
    public void drawEdgeGraph(Rect rect1, Rect rect2, Edge edge, boolean weighted) {
        __drawEdge(customCanvas.canvasGraph,
                rect1, rect2,
                paintEdge, paintEdgeArrows, paintEdgeWeight,
                edge, weighted);
    }

    // Draws a single EdgeOld
    public void drawEdgeAnim(Rect rect1, Rect rect2, Edge edge, boolean weighted) {
        __drawEdge(customCanvas.canvasAnimation,
                rect1, rect2,
                paintEdgeAnim, paintEdgeArrowsAnim, paintEdgeWeightAnim,
                edge, weighted);
    }

    public void __drawEdge(Canvas canvas,
                           Rect rect1, Rect rect2,
                           Paint paintE, Paint paintEA, Paint paintEW,
                           Edge edge, boolean weighted) {
        double[] lineCoordinates = getLineCoordinates(rect1, rect2);

        float lx1 = (float) lineCoordinates[0];
        float ly1 = (float) lineCoordinates[1];

        float lx2 = (float) lineCoordinates[2];
        float ly2 = (float) lineCoordinates[3];

        double degree = getAngle(lx1, ly1, lx2, ly2);

        float x = lx1 + (lx2 - lx1)/2;
        float y = ly1 + (ly2 - ly1)/2;

        if(edge != null && weighted) {
            canvas.save();
            canvas.rotate((float) degree, x, y);
            canvas.drawText(String.valueOf(edge.weight), x, y-20, paintEW);
            canvas.restore();
        }

        canvas.drawLine(lx1, ly1, lx2, ly2, paintE);
        arrow12(lx1, ly1, lx2, ly2, canvas, paintEA);
    }

    public void arrow12(float x, float y, float x1, float y1, Canvas canvas, Paint paintEA) {
        __arrow12(canvas, x, y, x1, y1, paintEA);
    }

    private void __arrow12(Canvas canvas, float x, float y, float x1, float y1, Paint paintEA) {
        double degree = Util.calculateDegree(x, x1, y, y1);

        float endX1 = (float) (x1 + ((arrowLength) * Math.cos(Math.toRadians((degree-topAngle)+90))));
        float endY1 = (float) (y1 + ((arrowLength) * Math.sin(Math.toRadians(((degree-topAngle)+90)))));

        float endX2 = (float) (x1 + ((arrowLength) * Math.cos(Math.toRadians((degree-bottomAngle)+180))));
        float endY2 = (float) (y1 + ((arrowLength) * Math.sin(Math.toRadians(((degree-bottomAngle)+180)))));

        canvas.drawLine(x1, y1, endX1, endY1, paintEA);
        canvas.drawLine(x1, y1, endX2, endY2, paintEA);
    }

    // Adds VertexOld element to grid element and calls drawNode
    public void addVertex(float xAxisPos, float yAxisPos, int name) {
        // Row and Col of the vertexOld
        int col = (int) xAxisPos;
        int row = (int) yAxisPos;

        // Change its state and add vertexOld reference
        boardElements[row][col].occupied = true;
        boardElements[row][col].value = name;
//        data[row][col].vertexOld = vertexOld;

        Rect rect = getRect(row, col);
        drawNodeGraph(rect, boardElements[row][col].value);
    }

    // Adds VertexOld element to grid element and calls drawNode
    public void addVertex(int row, int col, int name) {
        // Change its state and add vertexOld reference
        boardElements[row][col].occupied = true;
        boardElements[row][col].value = name;
//        data[row][col].vertexOld = vertexOld;

        Rect rect = getRect(row, col);
        drawNodeGraph(rect, boardElements[row][col].value);
    }

    // Adds VertexOld element to grid element and calls drawNode
    public void removeVertex(int row, int col) {
        // Change its state and add vertexOld reference
        boardElements[row][col].occupied = false;
        boardElements[row][col].value = -1;
//        data[row][col].vertexOld = vertexOld;

    }

    // Returns state of the grid element, whether it is being used or not
    public boolean getState(float xAxisPos, float yAxisPos){
        int col = (int) xAxisPos;
        int row = (int) yAxisPos;

        return boardElements[row][col].occupied;
    }

    // Returns state of the grid element, whether it is being used or not
    public boolean getState(int row, int col){
        return boardElements[row][col].occupied;
    }

    public int[] getCoordinates(int key){
        for (int r = 0; r < yCount; r++) {
            for (int c = 0; c < xCount; c++) {
                if(boardElements[r][c].value == key){
                    return new int[]{r, c};
                }
            }
        }

        return null;
    }

    // Returns Rect for given grid[row][col]
    public Rect getRect(float row, float col) {
        int c = (int) col;
        int r = (int) row;

        int left = (int) (c * xSize);
        int top = (int) (r * ySize);
        int right = (int) (left + xSize);
        int bottom = (int) (top + ySize);

        return new Rect(left, top, right, bottom);
    }

    // Returns Rect for given key value in graph's board
    public Rect getRect(int key) {

        int[] coordinates = getCoordinates(key);
        int c = (int) coordinates[1];
        int r = (int) coordinates[0];

        int left = (int) (c * xSize);
        int top = (int) (r * ySize);
        int right = (int) (left + xSize);
        int bottom = (int) (top + ySize);

        return new Rect(left, top, right, bottom);
    }

    // Returns radius for the node
    private float getRadius(Rect rect){
        int width = rect.width();
        int height = rect.height();

//        System.out.println("rect = " + width + "x" + height);
        float diameter = Math.min(width, height) * circleRatio;
        float radius = diameter / 2;

        return radius;
    }

    // Returns double[4] = {startX, startY, endX, endY} for edge Line
    public double[] getLineCoordinates(Rect rect1, Rect rect2){
        double x1 = rect1.centerX();
        double y1 = rect1.centerY();

        double x2 = rect2.centerX();
        double y2 = rect2.centerY();

        double r1 = getRadius(rect1);
        double r2 = getRadius(rect2);

        double u = Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));

        double a1 = x1 + (((x2-x1) * r1) / u);
        double b1 = y1 + (((y2-y1) * r1) / u);

        double a2 = x2 - (((x2-x1) * r2) / u);
        double b2 = y2 - (((y2-y1) * r2) / u);

        return new double[]{a1,b1,a2,b2};
    }

    public Pair<Integer, Integer> getRandomAvailableNode(){
        ArrayList<Pair<Integer, Integer>> available = new ArrayList<>();
        for (int r = 0; r < yCount; r++) {
            for (int c = 0; c < xCount; c++) {
                if(!boardElements[r][c].occupied){
                    available.add(new Pair<>(r, c));
                }
            }
        }

        if(available.size() == 0)
            return null;

        return available.get(new Random().nextInt(available.size()));
    }

    public void reset(Graph graph){
        this.boardElements = new BoardElement[yCount][xCount];
        for (int r = 0; r < yCount; r++) {
            for (int c = 0; c < xCount; c++) {
                boardElements[r][c] = new BoardElement();
            }
        }

        update(graph);
    }

    public void clearCanvasGraph(){
        __clearCanvas(customCanvas.canvasGraph);
        refreshGraph();
    }

    public void clearCanvasAnim(){
        __clearCanvas(customCanvas.canvasAnimation);
        refreshAnim();
    }

    private void __clearCanvas(Canvas canvas){
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    public void refreshAnim(){
        __refresh(customCanvas.imageViewGraph);
    }

    public void refreshGraph(){
        __refresh(customCanvas.imageViewAnimation);
    }

    private void __refresh(ImageView imageView){
        imageView.invalidate();
    }

    public float getAngle(float x1, float y1, float x2, float y2) {
        float angle = (float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }

    public int[] getCurrentLimits(){
        int row_min = yCount-1;
        int row_max = 0;
        int col_min = xCount-1;
        int col_max = 0;

        for (int r = 0; r < yCount; r++) {
            for (int c = 0; c < xCount; c++) {
                if(boardElements[r][c].occupied){
                    row_min = Math.min(row_min, r);
                    row_max = Math.max(row_max, r);

                    col_min = Math.min(col_min, c);
                    col_max = Math.max(col_max, c);
                }
            }
        }

        return new int[]{row_min, row_max, col_min, col_max};
    }

    public float getZoomRatio(){
        int[] currentLimits = getCurrentLimits();

        int width = currentLimits[3] - currentLimits[2] + 1;
        int height = currentLimits[1] - currentLimits[0] + 1;

        System.out.println("row_min = " + currentLimits[0]);
        System.out.println("row_max = " + currentLimits[1]);
        System.out.println("col_min = " + currentLimits[2]);
        System.out.println("col_max = " + currentLimits[3]);


        System.out.println("width = " + width);
        System.out.println("height = " + height);


        float widthRatio = (float) xCount / width;
        float heightRatio = (float) yCount / height;

        System.out.println("&&" + widthRatio);
        System.out.println("&&" + heightRatio);

        System.out.println("zoom" + Math.min(widthRatio,  heightRatio));

        return Math.min(widthRatio,  heightRatio);
    }

    public Rect getZoomCentre(){
        int[] currentLimits = getCurrentLimits();

        int width = currentLimits[3] - currentLimits[2] + 1;
        int height = currentLimits[1] - currentLimits[0] + 1;

        int widthMid = currentLimits[2] + width/2;
        int heightMid = currentLimits[0] + height /2;

        System.out.println("widthMid = " + widthMid);
        System.out.println("heightMid = " + heightMid);

        Rect rect = getRect(widthMid, heightMid);
        System.out.println(rect.centerX());
        System.out.println(rect.centerY());

        return rect;
    }

}
