package ua.str.diploma.encryptionmethodstester;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;

public class MainActivityFragment extends Fragment {
    private ActionProcessButton mStartButton;
    private TextView tvProgressDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStartButton = (ActionProcessButton) view.findViewById(R.id.start_button);
        mStartButton.setMode(ActionProcessButton.Mode.ENDLESS);
        mStartButton.setOnClickListener(onClickListener);
        tvProgressDescription = (TextView) view.findViewById(R.id.tv_progress_description);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EncryptionAsyncTask encryptionAsyncTask =
                    new EncryptionAsyncTask(getActivity(), mStartButton, tvProgressDescription);
            switch (v.getId()) {
                case R.id.start_button:
                    encryptionAsyncTask.execute(EncryptionAsyncTask.MODE_VERY_SMALL);
                    break;
            }
        }
    };
}