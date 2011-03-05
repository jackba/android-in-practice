package org.flexware.dnd;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.StackView;

public class DndActivity extends Activity {
	StackView stack;
	StackView stack2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);
				
		stack = (StackView) findViewById(R.id.stack);
		Bitmap[] bmps = new Bitmap[5];
		Resources res = getResources();
		bmps[0] = BitmapFactory.decodeResource(res, R.drawable.donut);
		bmps[1] = BitmapFactory.decodeResource(res, R.drawable.eclair);
		bmps[2] = BitmapFactory.decodeResource(res, R.drawable.froyo);
		bmps[3] = BitmapFactory.decodeResource(res, R.drawable.gingerbread);
		bmps[4] = BitmapFactory.decodeResource(res, R.drawable.honeycomb);
		ImgAdapter adapter = new ImgAdapter(bmps, stack);
		stack.setAdapter(adapter);
		
		stack2 = (StackView) findViewById(R.id.stack2);
		stack2.setAdapter(new ImgAdapter(bmps, stack2));
		
		findViewById(R.id.topLeft).setOnDragListener(new BoxDragListener());
		findViewById(R.id.bottomLeft).setOnDragListener(new BoxDragListener());
		findViewById(R.id.topRight).setOnDragListener(new BoxDragListener());
		findViewById(R.id.bottomRight).setOnDragListener(new BoxDragListener());
		
	}
	
	class ImgAdapter extends BaseAdapter{
		private Bitmap[] bmps;
		private Context ctx = DndActivity.this;
		private ViewGroup owner;
		
		ImgAdapter(Bitmap[] bmps, ViewGroup owner){
			this.bmps = bmps;
			this.owner = owner;
		}
		
		@Override
		public int getCount() {
			return bmps.length;
		}

		@Override
		public Object getItem(int index) {
			return bmps[index];
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@Override
		public View getView(int index, View recycledView, ViewGroup parent) {
			if (recycledView == null){
				recycledView = new ImageView(ctx);
			}
			ImageView imgView = (ImageView) recycledView;
			imgView.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View view) {
					ClipData data = ClipData.newPlainText("foo","bar");
					DragShadowBuilder shadowBuilder = new DragShadowBuilder(owner);
					owner.startDrag(data, shadowBuilder, owner, 0);
					return true;
				}
				
			});
			imgView.setImageBitmap(bmps[index]);
			return imgView;
		}
		
	}
	
	class BoxDragListener implements OnDragListener{
		boolean insideOfMe = false;
		Drawable border = null;
		Drawable redBorder = getResources().getDrawable(R.drawable.border3);
		@Override
		public boolean onDrag(View self, DragEvent event) {
			if (event.getAction() == DragEvent.ACTION_DRAG_STARTED){
				border = self.getBackground();
				self.setBackgroundDrawable(redBorder);
			} else if (event.getAction() == DragEvent.ACTION_DRAG_ENTERED){ 
				insideOfMe = true;
			} else if (event.getAction() == DragEvent.ACTION_DRAG_EXITED){
				insideOfMe = false;
			} else if (event.getAction() == DragEvent.ACTION_DROP){
				if (insideOfMe){
					View view = (View) event.getLocalState();
					ViewGroup owner = (ViewGroup) view.getParent();
					owner.removeView(view);
					LinearLayout container = (LinearLayout) self;
					if (container.getChildCount() > 0){
						container.addView(view, container.getChildCount());
					} else {
						container.addView(view);
					}
				}
			} else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED){
				self.setBackgroundDrawable(border);					
			}
			return true;
		}
	}
}