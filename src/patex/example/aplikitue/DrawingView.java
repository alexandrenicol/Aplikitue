package patex.example.aplikitue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View{

	private boolean erase=false;
	
	
	//drawing path
	private Path drawPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//initial color
	private int paintColor = 0xFF660000;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap, picBitmap,  picBitmapScaled;
	private float brushSize, lastBrushSize;


	private String actualColor = "#FF660000";
	
	
	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupDrawing();
		// TODO Auto-generated constructor stub
		 Log.d("DrawingView", "simplelog - construc");
	}

	private void setupDrawing() {
		// TODO Auto-generated method stub
		brushSize = getResources().getInteger(R.integer.medium_size);
		lastBrushSize = brushSize;
		
		drawPath = new Path();
		drawPaint = new Paint();
		
		drawPaint.setColor(paintColor);
		
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(brushSize);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		
		canvasPaint = new Paint(Paint.DITHER_FLAG);
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		//view given size
		super.onSizeChanged(w, h, oldw, oldh);
		
        
        Integer height = this.getMeasuredHeight();
        Integer width = this.getMeasuredWidth();
		
        picBitmapScaled = getResizedBitmap(picBitmap, height, width);
        
		canvasBitmap = picBitmapScaled.copy(Bitmap.Config.ARGB_8888, true);
		drawCanvas = new Canvas(canvasBitmap);
		//drawCanvas.drawColor(0, Mode.CLEAR);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
	//draw view
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	//detect user touch    
		float touchX = event.getX();
		float touchY = event.getY();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		    drawPath.moveTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_MOVE:
		    drawPath.lineTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_UP:
		    drawCanvas.drawPath(drawPath, drawPaint);
		    drawPath.reset();
		    break;
		default:
		    return false;
		}
		
		invalidate();
		return true;
	}
	
	public void setColor(String newColor){
		//set color     
		invalidate();
		if (!erase){
			actualColor = newColor;
		}
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}
	
	public void drawPic(Bitmap pic){
		invalidate();
		drawCanvas.drawBitmap(pic, 0, 0, canvasPaint);
	}

	public Bitmap getPicBitmap() {
		return picBitmap;
	}

	public void setPicBitmap(Bitmap picBitmap) {
		this.picBitmap = picBitmap;
	}
	
	public void setBrushSize(float newSize){
		//update size
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
			    newSize, getResources().getDisplayMetrics());
		brushSize=pixelAmount;
		drawPaint.setStrokeWidth(brushSize);
	}
	
	public void setLastBrushSize(float lastSize){
	    lastBrushSize=lastSize;
	}
	public float getLastBrushSize(){
	    return lastBrushSize;
	}
	
	public void setErase(boolean isErase){
		//set erase true or false
		erase=isErase;
		if(erase){
			//drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			this.setColor("#FFFFFFFF");
		}
		else{
			//drawPaint.setXfermode(null);
			this.setColor(actualColor);
		}
	}
	
	public void startNew(){
		Integer height = this.getMeasuredHeight();
        Integer width = this.getMeasuredWidth();
		canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
		invalidate();
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    
	    Log.d("BITMAP",String.valueOf(width));
	    Log.d("BITMAP",String.valueOf(height));
	    Log.d("BITMAP",String.valueOf((float) width / height));
	    
	    
	    Log.d("BITMAP",String.valueOf(newWidth));
	    Log.d("BITMAP",String.valueOf(newHeight));
	    Log.d("BITMAP",String.valueOf((float) newWidth / newHeight));
	    
	    
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    
	    
	    
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    //matrix.postScale(scaleWidth, scaleHeight);
	
	    matrix.setRectToRect(new RectF(0, 0, width, height), new RectF(0, 0, newWidth, newHeight), Matrix.ScaleToFit.CENTER);
	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}


}
