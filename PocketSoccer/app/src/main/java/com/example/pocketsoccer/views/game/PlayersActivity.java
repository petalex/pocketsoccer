package com.example.pocketsoccer.views.game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketsoccer.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayersActivity extends AppCompatActivity {
    private ViewPager team1Pager;

    private ViewPagerAdapter team1Adapter;

    private ViewPager team2Pager;

    private ViewPagerAdapter team2Adapter;

    private int team1;

    private int team2;

    private boolean computer1 = false;

    private boolean computer2 = false;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        setContentView(R.layout.activity_players);

        try {
            Drawable background = Drawable.createFromStream(getAssets().open("backgrounds/main.jpg"), null);
            ConstraintLayout playersLayout = findViewById(R.id.players_layout);
            playersLayout.setBackground(background);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/Chunkfive.otf");
        Typeface menuFont = Typeface.createFromAsset(getAssets(), "fonts/Sanson.otf");

        TextView player1Title = findViewById(R.id.player1_title);
        player1Title.setTypeface(menuFont);

        TextView player2Title = findViewById(R.id.player2_title);
        player2Title.setTypeface(menuFont);

        ImageView back = findViewById(R.id.players_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final EditText player1 = findViewById(R.id.player1);

        final ImageView chekbox1 = findViewById(R.id.player1_checkbox);
        chekbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (computer1) {
                    chekbox1.setImageDrawable(getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp));
                } else {
                    chekbox1.setImageDrawable(getDrawable(R.drawable.ic_check_box_white_24dp));
                }
                computer1 = !computer1;
            }
        });

        team1Pager = findViewById(R.id.team1_pager);
        team1Adapter = new ViewPagerAdapter();
        team1Pager.setAdapter(team1Adapter);
        team1Pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int count = team1Adapter.getCount();
                if (i == 0) {
                    setTeam1(count - 2);
                } else if (i == count - 1) {
                    setTeam1(1);
                } else {
                    setTeam1(i);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        ImageView prev1 = findViewById(R.id.team1_prev);
        prev1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = team1Pager.getCurrentItem();
                int count = team1Adapter.getCount();
                if (position == 0) {
                    setTeam1(count - 2);
                } else {
                    setTeam1(position - 1);
                }
            }
        });

        ImageView next1 = findViewById(R.id.team1_next);
        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = team1Pager.getCurrentItem();
                int count = team1Adapter.getCount();
                if (position == count - 1) {
                    setTeam1(0);
                } else {
                    setTeam1(position + 1);
                }
            }
        });

        final EditText player2 = findViewById(R.id.player2);

        final ImageView chekbox2 = findViewById(R.id.player2_checkbox);
        chekbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (computer2) {
                    chekbox2.setImageDrawable(getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp));
                } else {
                    chekbox2.setImageDrawable(getDrawable(R.drawable.ic_check_box_white_24dp));
                }
                computer2 = !computer2;
            }
        });

        team2Pager = findViewById(R.id.team2_pager);
        team2Adapter = new ViewPagerAdapter();
        team2Pager.setAdapter(team2Adapter);
        team2Pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int count = team2Adapter.getCount();
                if (i == 0) {
                    setTeam2(count - 2);
                } else if (i == count - 1) {
                    setTeam2(1);
                } else {
                    setTeam2(i);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        ImageView prev2 = findViewById(R.id.team2_prev);
        prev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = team2Pager.getCurrentItem();
                int count = team2Adapter.getCount();
                if (position == 0) {
                    setTeam2(count - 2);
                } else {
                    setTeam2(position - 1);
                }
            }
        });

        ImageView next2 = findViewById(R.id.team2_next);
        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = team2Pager.getCurrentItem();
                int count = team2Adapter.getCount();
                if (position == count - 1) {
                    setTeam2(0);
                } else {
                    setTeam2(position + 1);
                }
            }
        });

        TextView play = findViewById(R.id.play);
        play.setTypeface(titleFont);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String player1Name = player1.getText().toString();
                String player2Name = player2.getText().toString();
                if (player1Name.equals("")) {
                    Toast.makeText(v.getContext(), "Please enter Player 1's name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (player2Name.equals("")) {
                    Toast.makeText(v.getContext(), "Please enter Player 2's name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent gameIntent = new Intent(v.getContext(), GameActivity.class);
                gameIntent.putExtra("new", true);
                gameIntent.putExtra("player1", player1Name);
                gameIntent.putExtra("player2", player2Name);
                gameIntent.putExtra("computer1", computer1);
                gameIntent.putExtra("computer2", computer2);
                gameIntent.putExtra("team1", team1);
                gameIntent.putExtra("team2", team2);
                startActivity(gameIntent);
            }
        });

        setTeam1(1);
        setTeam2(2);
    }

    private void setTeam1(int team) {
        team1 = team;
        team1Pager.setCurrentItem(team);
    }

    private void setTeam2(int team) {
        team2 = team;
        team2Pager.setCurrentItem(team);
    }

    private class ViewPagerAdapter extends PagerAdapter {
        private List<Drawable> teams;

        private static final int TEAM_COUNT = 35;

        public ViewPagerAdapter() {
            teams = new ArrayList<>();
            teams.add(null);
            for (int i = 1; i <= TEAM_COUNT; ++i) {
                try {
                    teams.add(Drawable.createFromStream(getAssets().open("teams/t" + i + ".png"), null));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            teams.add(null);
        }

        @Override
        public int getCount() {
            return teams.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.view_pager_item, null);
            ImageView image = view.findViewById(R.id.pager_image);
            image.setImageDrawable(teams.get(position));
            ((ViewPager) container).addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }
}
