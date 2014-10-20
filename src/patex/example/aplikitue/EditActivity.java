package patex.example.aplikitue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditActivity extends Activity implements OnClickListener{

	private String filePath;
	
	
	private DrawingView drawView;
	private File pictureFile;
	private Uri pictureUri;
	
	private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn;
	
	private float smallBrush, mediumBrush, largeBrush;
	
	private Button backButton;


	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		//get picture's uri
		Bundle extra = getIntent().getExtras();
        filePath = extra.getString("photoPath");
        pictureFile = new File(filePath);
        pictureUri = Uri.fromFile(pictureFile);
        
       
        //Brush Button
  		smallBrush = getResources().getInteger(R.integer.small_size);
  		mediumBrush = getResources().getInteger(R.integer.medium_size);
  		largeBrush = getResources().getInteger(R.integer.large_size);
  		drawBtn = (ImageButton)findViewById(R.id.draw_btn);
  		drawBtn.setOnClickListener(this);

  		//Erase Button
  		eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
  		eraseBtn.setOnClickListener(this);
  		
  		//New Button
  		newBtn =(ImageButton)findViewById(R.id.new_btn);
  		newBtn.setOnClickListener(this);
		
		//Save Button
		saveBtn=(ImageButton)findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(this);
        
        backButton=(Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        
        drawView = (DrawingView)findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        
        setBitmap(pictureUri);
        
        drawView.setBrushSize(mediumBrush);
                
        
        
	}
	
	public void paintClicked(View view){
	    //use chosen color
		drawView.setErase(false);
		drawView.setBrushSize(drawView.getLastBrushSize());
		if(view!=currPaint){
			//update color
			ImageButton imgView = (ImageButton)view;
			String color = view.getTag().toString();
			drawView.setColor(color);
			imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
			currPaint=(ImageButton)view;
		}
	}
	
	public void setBitmap(Uri picUri){
		Bitmap mBitmap;
        try {
        	mBitmap = Media.getBitmap(this.getContentResolver(), pictureUri);
        	drawView.setPicBitmap(mBitmap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Log.d("EditActivity", "end of oncreate");
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		//Check if the draw button is clicked
		if(view.getId()==R.id.draw_btn){
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Brush size:");
			brushDialog.setContentView(R.layout.brush_chooser);
			
			ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setBrushSize(smallBrush);
			        drawView.setLastBrushSize(smallBrush);
			        drawView.setErase(false);
			        brushDialog.dismiss();
			    }
			});
			
			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setBrushSize(mediumBrush);
			        drawView.setLastBrushSize(mediumBrush);
			        drawView.setErase(false);
			        brushDialog.dismiss();
			    }
			});
			 
			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setBrushSize(largeBrush);
			        drawView.setLastBrushSize(largeBrush);
			        drawView.setErase(false);
			        brushDialog.dismiss();
			    }
			});
		
			brushDialog.show();
			
		}
		
		else if(view.getId()==R.id.erase_btn){
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Eraser size:");
			brushDialog.setContentView(R.layout.brush_chooser);
			
			ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			    	drawView.setErase(true);
			        drawView.setBrushSize(smallBrush);
			        brushDialog.dismiss();
			    }
			});
			
			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			    	drawView.setErase(true);
			        drawView.setLastBrushSize(mediumBrush);
			        brushDialog.dismiss();
			    }
			});
			 
			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			    	drawView.setErase(true);
			        drawView.setLastBrushSize(largeBrush);
			        brushDialog.dismiss();
			    }
			});
			brushDialog.show();	
		}
		else if(view.getId()==R.id.new_btn){
			AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
			newDialog.setTitle("New drawing");
			newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
			newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			        drawView.startNew();
			        dialog.dismiss();
			    }
			});
			newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			        dialog.cancel();
			    }
			});
			newDialog.show();
		}
		
		else if(view.getId()==R.id.save_btn){
			drawView.setDrawingCacheEnabled(true);
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
			saveDialog.setTitle("Save drawing");
			saveDialog.setMessage("Save drawing to device Gallery?");
			saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			        //save drawing
			    	String imgSaved = MediaStore.Images.Media.insertImage(
			    		    getContentResolver(), drawView.getDrawingCache(),
			    		    UUID.randomUUID().toString()+".png", "drawing");
			    	if(imgSaved!=null){
			    	    Toast savedToast = Toast.makeText(getApplicationContext(), 
			    	        "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
			    	    savedToast.show();
			    	}
			    	else{
			    	    Toast unsavedToast = Toast.makeText(getApplicationContext(), 
			    	        "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
			    	    unsavedToast.show();
			    	}
			    }
			});
			
			drawView.destroyDrawingCache();
			
			saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			        dialog.cancel();
			    }
			});
			saveDialog.show();
		}
		else if(view.getId()==R.id.backButton){
			Intent t = new Intent(EditActivity.this,MainActivity.class);
			startActivity(t);
			finish();
		}

	}


	
}
