package us.stupidx.dailygoal;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import us.stupidx.config.DailyGoal_tbl;
import us.stupidx.db.GoalOpenHelper;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.provider.OpenableColumns;
import android.widget.RemoteViews;

public class DgWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 1, 60000);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private class MyTime extends TimerTask {
        RemoteViews remoteViews;
        AppWidgetManager appWidgetManager;
        Context context;
        ComponentName thisWidget;

        public MyTime(Context context, AppWidgetManager appWidgetManager) {
            this.appWidgetManager = appWidgetManager;
            this.context = context;
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            thisWidget = new ComponentName(context, DgWidget.class);
        }

        public void run() {

            Date date = new Date();
            Calendar calendar = new GregorianCalendar(2010, 06, 11);
            long days = (((calendar.getTimeInMillis() - date.getTime()) / 1000)) / 86400;
            GoalOpenHelper openHelper = new GoalOpenHelper(context);
            Cursor goal = openHelper.readCurrentGoal();
            String today_goal = "";
            if (goal.getCount() > 0) {
                goal.moveToFirst();
                today_goal = goal.getString(goal.getColumnIndex(DailyGoal_tbl.GoalColumn.COL_CTN));
            }

           //  remoteViews.setTextViewText(R.id.today_goal_in_widget, today_goal);
            appWidgetManager.updateAppWidget(thisWidget, remoteViews);

        }

    }
}
