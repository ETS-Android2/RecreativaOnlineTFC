package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Ruleta;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdView;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Activities.CasinoActivity;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.Interfaces.OnAdsListener;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

public class RuletaFragment extends Fragment implements MenuItem.OnMenuItemClickListener, View.OnClickListener {
    private static final String[] sectors = { "32 red", "15 black",
            "19 red", "4 black", "21 red", "2 black", "25 red", "17 black", "34 red",
            "6 black", "27 red","13 black", "36 red", "11 black", "30 red", "8 black",
            "23 red", "10 black", "5 red", "24 black", "16 red", "33 black",
            "1 red", "20 black", "14 red", "31 black", "9 red", "22 black",
            "18 red", "29 black", "7 red", "28 black", "12 red", "35 black",
            "3 red", "26 black", "0 green"
    };
    private static final String[] sectorsBids = {"1 red", "2 black",
            "3 red", "4 black", "5 red", "6 black", "7 red", "8 black", "9 red"
            ,"10 black" ,"11 red","12 black", "13 red", "14 black", "15 red", "16 black",
            "17 red", "18 black", "19 red", "20 black", "21 red", "22 black",
            "23 red", "24 black", "25 red","26 black", "27 red", "28 black",
            "29 red", "30 black","31 red", "32 black", "33 red", "34 black",
            "35 red", "36 black","1","2","3"
    };

    private static final String[] monedasApuesta={
        "0,20","0,50","1","2","5"
    };

    private ImageView wheel;
    private ImageView arrow;
    private String numero_sacado;
    private WheelMotionAsync motionWheel;
    private TextView chrono;
    private androidx.gridlayout.widget.GridLayout recentNumbers;
    private AlertDialog dialog;
    private OnAdsListener mAdsListener;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ruleta, container, false);

        this.motionWheel=new WheelMotionAsync();

        this.wheel=root.findViewById(R.id.Wheel);

        this.arrow=root.findViewById(R.id.ArrowWheel);

        this.chrono=root.findViewById(R.id.chronometerWheel);

        this.recentNumbers=root.findViewById(R.id.recentNumbers);

        this.numero_sacado="";

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        motionWheel.execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        motionWheel.salir=true;
        motionWheel.cancel(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.ruleta_menu,menu);
        MenuItem item=menu.findItem(R.id.apuestaRuleta);
        item.setOnMenuItemClickListener(this);

    }

    private void dialogoApostar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v=getLayoutInflater().inflate(R.layout.dialog_apuesta_ruleta,null);
        androidx.gridlayout.widget.GridLayout numerosApuestas=v.findViewById(R.id.numerosApuestas);

        androidx.gridlayout.widget.GridLayout monedasApuestasgrid=v.findViewById(R.id.monedasApuestas);

        Button apostar=v.findViewById(R.id.apostarRuleta);
        apostar.setOnClickListener(this);

        Button cerrarDialogo=v.findViewById(R.id.cerrarDialogoApuesta);
        cerrarDialogo.setOnClickListener(this);

        AdView adView=v.findViewById(R.id.banner_apuesta_ruleta);
        this.mAdsListener.loadBannerAdView(adView);

        this.pintarNumerosApuestas(numerosApuestas);
        this.pintarGridMonedasApuestas(monedasApuestasgrid);


        builder.setView(v);
        builder.create();
        dialog=builder.show();

    }

    private void pintarNumerosApuestas(androidx.gridlayout.widget.GridLayout numerosApuestas){
        GridLayout.LayoutParams param =new GridLayout.LayoutParams();
        for (int i = 0; i < sectorsBids.length; i++) {
            param =new GridLayout.LayoutParams();
            Button boton = new Button(getContext());
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.rightMargin = 5;
            param.topMargin = 5;
            param.setGravity(Gravity.CENTER);

            if(sectorsBids[i].contains(" ")) {
                String sector[] = sectorsBids[i].split(" ");
                boton.setText(sector[0]);
                boton.setWidth(80);
                int color = 0;
                int colorLetter = 0;
                int span=0;
                if (sector[1].equalsIgnoreCase("red")) {
                    color = Color.RED;
                    colorLetter = Color.BLACK;
                    span=1;

                } else if (sector[1].equalsIgnoreCase("black")) {
                    color = Color.BLACK;
                    colorLetter = Color.WHITE;
                    span=1;
                } else {
                    color = Color.GREEN;
                    colorLetter = Color.BLACK;
                    span=3;
                }

                boton.setBackgroundColor(color);
                boton.setTextColor(colorLetter);
                param.columnSpec = GridLayout.spec(0,span);
                boton.setLayoutParams(param);
            }else{
                boton.setText(sectorsBids[i]);
                boton.setWidth(80);
                boton.setBackgroundColor(Color.GREEN);
                boton.setTextColor(Color.BLACK);
            }


            numerosApuestas.addView(boton);

        }
    }

    private void pintarGridMonedasApuestas(androidx.gridlayout.widget.GridLayout monedasApuestasgrid){

        for (int i = 0; i < monedasApuesta.length; i++) {
            Button boton = new Button(getContext());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(140, ActionBar.LayoutParams.WRAP_CONTENT);
            layoutParams.setMarginStart(10);
            boton.setLayoutParams(layoutParams);
            boton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.monedas_shape, null));
            boton.setText(monedasApuesta[i]);
            monedasApuestasgrid.addView(boton);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.apuestaRuleta:
                if(!chrono.getText().toString().equalsIgnoreCase("00:00")){
                    this.dialogoApostar();
                    break;
                }else{
                    Toast.makeText(getContext(), getString(R.string.no_se_puede_apostar), Toast.LENGTH_SHORT).show();
                }

        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cerrarDialogoApuesta:
                dialog.dismiss();
                break;
        }
    }


    public class WheelMotionAsync extends AsyncTask<String,String,String>{
        public boolean salir=false;
        private int degree = 0, degreeOld = 0;
        private AlertDialog.Builder dialogNumber;

        @Override
        protected String doInBackground(String... strings) {

            while(!salir){
                try {
                    publishProgress();
                    Thread.sleep(60000);
                    spinWheelWithResult();
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return strings[0];
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Timer chrono= (Timer) new Timer(60000,1000);
            chrono.start();
        }

        public void spinWheelWithResult(){
            degreeOld = degree % 360;
            // we calculate random angle for rotation of our wheel
            Random r=new Random();
            degree = r.nextInt(360) + 720;
            // rotation effect on the center of the wheel
            RotateAnimation rotateAnim = new RotateAnimation(degreeOld, degree,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

            rotateAnim.setDuration(7200);
            rotateAnim.setFillAfter(true);
            rotateAnim.setInterpolator(new DecelerateInterpolator());
            rotateAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // we empty the result text view when the animation start
                    if(dialog!=null){
                        dialog.dismiss();
                    }
                    dialogNumber=new AlertDialog.Builder(getActivity());
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // we display the correct sector pointed by the triangle at the end of the rotate animation
                    dialogNumber.setTitle(getString(R.string.numero_sacado));
                    numero_sacado=getSector(360 - (degree % 360));
                    addRecentNumberToGrid(numero_sacado);
                    dialogNumber.setMessage(numero_sacado);
                    dialogNumber.show();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            // we start the animation
            wheel.startAnimation(rotateAnim);
        }
        private static final float HALF_SECTOR = 360f / 37f / 2f;

        private String getSector(int degrees) {
            int i = 0;
            String text = null;

            do {
                // start and end of each sector on the wheel
                float start = HALF_SECTOR * (i * 2 + 1);
                float end = HALF_SECTOR * (i * 2 + 3);

                if (degrees >= start && degrees < end) {
                    // degrees is in [start;end[
                    // so text is equals to sectors[i];
                    text = sectors[i];
                }

                i++;
            } while (text == null  &&  i < sectors.length);

            return text;
        }

        private void addRecentNumberToGrid(String number){
            if(number.contains(" ")){
                String n_with_color[]=number.split(" ");
                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(210, ViewGroup.LayoutParams.WRAP_CONTENT);
                Button boton = new Button(getActivity());
                boton.setLayoutParams(layoutParams);
                boton.setText(n_with_color[0]);
                boton.setWidth(80);
                int color = 0;
                int colorLetter = 0;
                if (n_with_color[1].equalsIgnoreCase("red")) {
                    color = Color.RED;
                    colorLetter = Color.BLACK;

                } else if (n_with_color[1].equalsIgnoreCase("black")) {
                    color = Color.BLACK;
                    colorLetter = Color.WHITE;
                } else {
                    color = Color.GREEN;
                    colorLetter = Color.BLACK;
                }

                boton.setBackgroundColor(color);
                boton.setTextColor(colorLetter);

                if (recentNumbers.getChildCount() < 5) {
                    recentNumbers.addView(boton);
                } else {
                    recentNumbers.removeView(recentNumbers.getChildAt(0));
                    recentNumbers.addView(boton);
                }
            }



        }
    }


    public class Timer extends CountDownTimer{

        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
            // Used for formatting digit to be in 2 digits only
            NumberFormat f = new DecimalFormat("00");

            long min = (millisUntilFinished / 60000) % 60;
            long sec = (millisUntilFinished / 1000) % 60;

            chrono.setText(f.format(min) + ":" + f.format(sec));
        }
        // When the task is over it will print 00:00:00 there
        public void onFinish() {
            chrono.setText("00:00");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof CasinoActivity){
            Activity activity=(Activity)context;
            this.mAdsListener=(OnAdsListener)activity;
        }
    }
}