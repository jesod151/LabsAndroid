package com.example.reproductor;

import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.VolumeProvider;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView list;
    Button play, next, prev;
    SeekBar volume, progress;
    TextView songName;

    Runnable runnable;
    Handler handler = new Handler();

    AudioManager audioManager;

    int songID = 0;

    MediaPlayer player;

    List<String> names = Arrays.asList("Gorillaz-Humility",
                                        "Daft Punk-Something About Us",
                                        "311-Amber",
                                        "Cocofunka-Asturión",
                                        "Cocofunka-Melancolía",
                                        "Daft Punk-Instant Crush",
                                        "Childish Gambino-Summer" ,
                                        "Gorillaz-Broken" ,
                                        "Gorillaz-Dare",
                                        "Gorillaz-Stylo");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);

        play = findViewById(R.id.play); next = findViewById(R.id.next); prev = findViewById(R.id.previous);

        volume = (SeekBar) findViewById(R.id.volume); progress = (SeekBar) findViewById(R.id.progress);
        volume.setFocusable(true); progress.setFocusable(true);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        songName = findViewById(R.id.songName);

        player  = MediaPlayer.create(this, R.raw.gorillaz_humility);
        songName.setText(names.get(0));
        player.start();
        seekSong();

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songID = position;
                setMediaPlayer();
            }
        });

        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                player.seekTo(seekBar.getProgress());

            }
        });

        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        play.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                ImageView image = new ImageView(MainActivity.this);

                if(player.isPlaying()){
                    player.pause();
                    image.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                    play.setBackground(image.getBackground());
                }
                else{
                    player.start();
                    image.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    play.setBackground(image.getBackground());
                    seekSong();
                }
            }
        });

        prev.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                songID--;
                setMediaPlayer();
            }
        });

        next.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                songID++;
                setMediaPlayer();
            }
        });


    }


    public void seekSong(){

        progress.setProgress(player.getCurrentPosition());

        if(player.isPlaying()){
            runnable = new Runnable(){
                @Override
                public void run(){
                    seekSong();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

    public void setupSeekBar(){

        progress.setMax(player.getDuration());

    }

    public void setMediaPlayer(){

        player.stop();

        if(songID > 9){
            songID = 0;

        }
        else if(songID < 0){
            songID = 9;
        }

        switch(songID){

            case 0:
                player  = MediaPlayer.create(this, R.raw.gorillaz_humility);
                break;
            case 1:
                player  = MediaPlayer.create(this, R.raw.daftpunk_something_about_us);
                break;
            case 2:
                player  = MediaPlayer.create(this, R.raw.amber);
                break;
            case 3:
                player  = MediaPlayer.create(this, R.raw.cocofunka_asturion);
                break;
            case 4:
                player  = MediaPlayer.create(this, R.raw.cocofunka_melancolia);
                break;
            case 5:
                player  = MediaPlayer.create(this, R.raw.daft_punk_instant_crush);
                break;
            case 6:
                player  = MediaPlayer.create(this, R.raw.gamb);
                break;
            case 7:
                player  = MediaPlayer.create(this, R.raw.gorillaz_broken);
                break;
            case 8:
                player  = MediaPlayer.create(this, R.raw.gorillaz_dare);
                break;
            case 9:
                player  = MediaPlayer.create(this, R.raw.stylo_gorillaz);
                break;
        }

        songName.setText(names.get(songID));
        setupSeekBar();
        player.start();
        seekSong();
    }




}
