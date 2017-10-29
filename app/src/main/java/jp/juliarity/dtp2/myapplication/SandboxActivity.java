package jp.juliarity.dtp2.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by DTP2 on 2017/10/29.
 */

public class SandboxActivity extends Activity {
    private boolean TEXT_VISIBILITY = true;
    private TextView mTextView;
    private Animation anim_start;
    private Animation anim_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sandbox_layout);

        mTextView = (TextView) findViewById(R.id.text_view);
        Button button = (Button) findViewById(R.id.button);
        anim_start = AnimationUtils.loadAnimation(this, R.anim.anim_start);
        anim_end = AnimationUtils.loadAnimation(this, R.anim.anim_end);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TEXT_VISIBILITY) {
                    TEXT_VISIBILITY = false;
                    mTextView.startAnimation(anim_start);
                    mTextView.setVisibility(View.VISIBLE);
                } else {
                    TEXT_VISIBILITY = true;
                    mTextView.startAnimation(anim_end);
                    mTextView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
