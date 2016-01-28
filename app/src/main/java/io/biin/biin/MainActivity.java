package io.biin.biin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import io.biin.biin.dal.NetworkManager;
import io.biin.biin.dal.service.testservice;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.simple.SimpleTextRequest;



public class MainActivity extends AppCompatActivity {

    private SpiceManager spiceManager = new SpiceManager(testservice.class);


    private SimpleTextRequest loremRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loremRequest = new SimpleTextRequest("https://dev-biin-backend.herokuapp.com/mobile/biinies/f6acfe50-da36-4d04-8996-7f39b0b16df6");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //
        //NetworkManager nw = new NetworkManager();
        //nw.GetRequest("http://dev-biin-backend.herokuapp.com/mobile/initialData/798b569f-bd80-4c27-bbbc-fb55790a50a4/9.738549/-83.9988");
    }

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
        getSpiceManager().execute(loremRequest, "json", DurationInMillis.ONE_MINUTE, new LoremRequestListener());
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }



    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================

    public final class LoremRequestListener implements RequestListener<String> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final String result) {
            Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
        }
    }
}
