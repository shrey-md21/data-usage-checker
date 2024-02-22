package com.example.datausage;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class DataUsageActivity extends AppCompatActivity {

    private TextView dataUsageTextView;
    private NetworkStatsManager networkStatsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_usage);

        dataUsageTextView = findViewById(R.id.data_usage_text_view);
        networkStatsManager = (NetworkStatsManager) getSystemService(Context.NETWORK_STATS_SERVICE);

        long dataUsage = getDataUsage();
        dataUsageTextView.setText("Data Usage: " + dataUsage + " bytes");
    }

    private long getDataUsage() {
        long startTime = getStartTimeOfToday();
        long endTime = System.currentTimeMillis();

        try {
            NetworkStats.Bucket bucket;
            NetworkStats networkStats = networkStatsManager.querySummary(
                    ConnectivityManager.TYPE_MOBILE,
                    "", startTime, endTime);

            long totalDataUsage = 0;

            while (networkStats.hasNextBucket()) {
                bucket = new NetworkStats.Bucket();
                networkStats.getNextBucket(bucket);

                // Aggregate data usage for your app (UID) here
                totalDataUsage += bucket.getRxBytes() + bucket.getTxBytes();
            }

            return totalDataUsage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private long getStartTimeOfToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
}
