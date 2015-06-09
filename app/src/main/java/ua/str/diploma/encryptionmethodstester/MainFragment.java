package ua.str.diploma.encryptionmethodstester;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;

public class MainFragment extends Fragment {
    public static final String KEY_LAST_SELECTED_BTN_ID = "last_selected_btn_id";
    private ActionProcessButton mVerySmallTestBtn;
    private ActionProcessButton mSmallTestBtn;
    private ActionProcessButton mMiddleTestBtn;
    private ActionProcessButton mBigTestBtn;
    private ActionProcessButton mVeryBigTestBtn;
    private TextView tvProgressDescription;
    private DbHelper db;
    private int lastSelectedBtnId;
    private ActionProcessButton[] processButtons;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVerySmallTestBtn = getInitializedProcessBtn(view, R.id.very_small_test_btn);
        mSmallTestBtn = getInitializedProcessBtn(view, R.id.small_test_btn);
        mMiddleTestBtn = getInitializedProcessBtn(view, R.id.middle_test_btn);
        mBigTestBtn = getInitializedProcessBtn(view, R.id.big_test_btn);
        mVeryBigTestBtn = getInitializedProcessBtn(view, R.id.very_big_test_btn);
        tvProgressDescription = (TextView) view.findViewById(R.id.tv_progress_description);
        processButtons = new ActionProcessButton[]{mVerySmallTestBtn, mSmallTestBtn,
                mMiddleTestBtn, mBigTestBtn, mVeryBigTestBtn};
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new DbHelper(getActivity());
        if (savedInstanceState != null) {
            lastSelectedBtnId = savedInstanceState.getInt(KEY_LAST_SELECTED_BTN_ID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAllButtons(true);
        setAllButtonsEnabled(true);
    }

    private void refreshAllButtons(boolean onResume) {
        if (lastSelectedBtnId > 0) {
            for(ActionProcessButton btn : processButtons) {
                btn.setProgress(0);
                btn.setOnClickListener(onClickListener);
                if (lastSelectedBtnId == btn.getId()) {
                    btn.setProgress(onResume? 100: 50);
                    btn.setOnClickListener(chartListener);
                }
            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActionProcessButton button = mVerySmallTestBtn;
            int mode = EncryptionAsyncTask.MODE_VERY_SMALL;
            switch (v.getId()) {
                case R.id.very_small_test_btn:
                    button = mVerySmallTestBtn;
                    mode = EncryptionAsyncTask.MODE_VERY_SMALL;
                    break;
                case R.id.small_test_btn:
                    button = mSmallTestBtn;
                    mode = EncryptionAsyncTask.MODE_SMALL;
                    break;

                case R.id.middle_test_btn:
                    button = mMiddleTestBtn;
                    mode = EncryptionAsyncTask.MODE_MIDDLE;
                    break;

                case R.id.big_test_btn:
                    button = mBigTestBtn;
                    mode = EncryptionAsyncTask.MODE_BIG;
                    break;

                case R.id.very_big_test_btn:
                    button = mVeryBigTestBtn;
                    mode = EncryptionAsyncTask.MODE_VERY_BIG;
                    break;
            }
            lastSelectedBtnId = button.getId();
            setAllButtonsEnabled(false);
            refreshAllButtons(false);
            new EncryptionAsyncTask(getActivity(), button, tvProgressDescription, db).execute(mode);
        }
    };

    View.OnClickListener chartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().startActivity(new Intent(getActivity(), EncryptionSpeedChartActivity.class));
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_LAST_SELECTED_BTN_ID, lastSelectedBtnId);
    }

    private ActionProcessButton getInitializedProcessBtn(View view, int id) {
        ActionProcessButton btn = (ActionProcessButton) view.findViewById(id);
        btn.setMode(ActionProcessButton.Mode.ENDLESS);
        btn.setOnClickListener(onClickListener);
        return btn;
    }

    private void setAllButtonsEnabled(boolean enabled) {
        mVerySmallTestBtn.setEnabled(enabled);
        mSmallTestBtn.setEnabled(enabled);
        mMiddleTestBtn.setEnabled(enabled);
        mBigTestBtn.setEnabled(enabled);
        mVeryBigTestBtn.setEnabled(enabled);
    }
}
