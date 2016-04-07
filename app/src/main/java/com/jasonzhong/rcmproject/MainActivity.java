package com.jasonzhong.rcmproject;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.grandStaffContainer) RelativeLayout grandStaffContainer;
    @Bind(R.id.hint_button) ImageButton hint_button;
    @Bind(R.id.note_button) ImageButton note_button;
    @Bind(R.id.sharp_button) ImageButton sharp_button;
    @Bind(R.id.playback_button) ImageButton playback_button;
    @Bind(R.id.check_button) ImageButton check_button;
    int leftMargin = 200;
    int topMargin = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        note_button.setOnTouchListener(new MyTouchListener());
        sharp_button.setOnTouchListener(new MyTouchListener());
        grandStaffContainer.setOnDragListener(new MyDragListener());

        hint_button.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                displayHintPopup();
            }
        });

        playback_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MediaPlayer mediaPlayer= MediaPlayer.create(MainActivity.this, R.raw.scale_e);
                mediaPlayer.start();
            }
        });

}

    // This defines your touch listener
    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    handleDragAndDrop(v,event);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void handleDragAndDrop(View v, DragEvent event){
        // Dropped, reassign View to ViewGroup
        View view = (View) event.getLocalState();

        ViewGroup owner = (ViewGroup) view.getParent();
        int index = owner.indexOfChild(view);
        owner.removeView(view);

        RelativeLayout container = (RelativeLayout) v;
        RelativeLayout.LayoutParams relative_params = new RelativeLayout.LayoutParams(30, 30);
        relative_params.setMargins(leftMargin, topMargin, 0, 0);

        leftMargin = leftMargin + 50;

        Bitmap bitmap = null;
        if (index == 1) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.whole_note_sb);
        } else if (index == 2) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sharp_sb);
        }
        Bitmap resizedBM = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
        ImageButton button = new ImageButton(MainActivity.this);
        button.setImageBitmap(resizedBM);
        button.setLayoutParams(relative_params);
        container.addView(button);
        view.setVisibility(button.VISIBLE);

        ImageButton newButton = new ImageButton(MainActivity.this);
        newButton.setOnTouchListener(new MyTouchListener());
        Bitmap yourBitmap = null;
        if (index == 1) {
            yourBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.whole_note_sb);
        } else if (index == 2) {
            yourBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sharp_sb);
        }
        Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, 100, 100, true);
        newButton.setImageBitmap(resized);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
        params.setMargins(250, 90, 0, 0);

        newButton.setLayoutParams(params);

        if (index == 1) {
            owner.addView(newButton, 1);
        } else if (index == 2) {
            owner.addView(newButton, 2);
        }

        newButton.setVisibility(View.VISIBLE);
    }
    private void displayHintPopup(){
         LayoutInflater layoutInflater
                        = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.hint_popup, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        RadioGroup.LayoutParams.WRAP_CONTENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT);

                ImageView btnDismiss = (ImageView)popupView.findViewById(R.id.bgImage);
                btnDismiss.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }});

                popupWindow.showAsDropDown(hint_button, 0, -300);
    }
}
