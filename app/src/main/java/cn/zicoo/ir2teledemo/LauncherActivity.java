package cn.zicoo.ir2teledemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

public class LauncherActivity extends KJActivity {
//    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        handler = new Handler();
//        handler.postDelayed(runnable, 500);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        if (getIntent() != null && getIntent().getExtras() != null)
            i.putExtras(getIntent().getExtras());
        startActivity(i);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        if (intent != null && intent.getExtras() != null)
            i.putExtras(intent.getExtras());
        startActivity(i);
        finish();
    }

    @Override
    public void setRootView() {

    }
}
