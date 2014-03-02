package com.quickblox.qmunicate.core.concurrency;

import android.app.Activity;
import android.app.DialogFragment;
import android.util.Log;

import com.quickblox.internal.core.exception.QBResponseException;
import com.quickblox.qmunicate.ui.utils.DialogUtils;
import com.quickblox.qmunicate.ui.utils.ErrorUtils;

import java.lang.ref.WeakReference;

public abstract class BaseErrorAsyncTask<Params, Progress, Result> extends BaseAsyncTask<Params, Progress, Result> {

    private static final String TAG = BaseErrorAsyncTask.class.getName();

    protected WeakReference<Activity> activityRef;

    protected BaseErrorAsyncTask(Activity activity) {
        this.activityRef = new WeakReference<Activity>(activity);
    }

    @Override
    public void onException(Exception e) {
        Log.e(TAG, "Cannot perform async task ", e);

        Activity parentActivity = activityRef.get();

        if (e instanceof QBResponseException) {
            ErrorUtils.showError(parentActivity, e);
            DialogUtils.show(parentActivity, e.getMessage());
        }
    }

    protected void showDialog(DialogFragment dialog) {
        showDialog(dialog, null);
    }

    protected void showDialog(DialogFragment dialog, String tag) {
        if (activityRef.get() != null) {
            dialog.show(activityRef.get().getFragmentManager(), tag);
        }
    }

    protected void hideDialog(DialogFragment dialog) {
        if (dialog.getActivity() != null) {
            dialog.dismissAllowingStateLoss();
        }
    }

    protected boolean isActivityAlive() {
        Activity activity = activityRef.get();
        return activity != null && !activity.isFinishing();
    }
}
