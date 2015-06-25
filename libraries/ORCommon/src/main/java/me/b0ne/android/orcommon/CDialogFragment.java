package me.b0ne.android.orcommon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by b0ne on 14/03/26.
 * 汎用的なDialogFragment
 * 参考したサイト： http://wada811.blogspot.com/2013/05/better-alert-dialog-fragment.html
 */
public class CDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{

    private static final String KEY_DIALOG_TITLE = "dialog-title";
    private static final String KEY_DIALOG_MESSAGE = "dialog-message";
    private static final String KEY_DIALOG_ITEMS = "dialog-items";
    private static final String KEY_DIALOG_CONTENT_VIEW = "dialog-content-view";
    private static final String KEY_DIALOG_POSITIVE_BUTTON_TEXT = "dialog-positive-btn-text";
    private static final String KEY_DIALOG_NEUTRAL_BUTTON_TEXT = "dialog-neutral-btn-text";
    private static final String KEY_DIALOG_NEGATIVE_BUTTON_TEXT = "dialog-negative-btn-text";
    private static final String KEY_DIALOG_IS_CLOSE_TO_FINISH = "dialog-is-close-to-finish";
    private static final String KEY_DIALOG_IS_LOADING = "dialog-is-loading";

    private static final int DEFAULT_INT_VALUE = 0;

    private CDialogListener mListener = null;
    private View mDialogView;

    // interface
    public interface CDialogListener {
        public void onPositiveBtnClick(String tag);
        public void onNeutralBtnClick(String tag);
        public void onNegativeBtnClick(String tag);
        public void onItemsClick(String tag, int position);
        public void onDialogCancel(String tag);
        public void onDialogDismiss(String tag);
    }

    /**
     * インスタンス生成とsetArguments
     * @return
     */
    public static CDialogFragment newInstance() {
        CDialogFragment commonDialogFragment = new CDialogFragment();
        Bundle args = new Bundle();
        commonDialogFragment.setArguments(args);
        return commonDialogFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CDialogListener == false) {
            throw new ClassCastException("Activity not implements CDialogListener.");
        }
        mListener = (CDialogListener)activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getTitle())
                .setMessage(getMessage());

        String positiveBtnText = getPositiveBtnText();
        String neutralBtnText = getNeutralBtnText();
        String negativeBtnText = getNegativeBtnText();
        if (positiveBtnText != null) {
            builder.setPositiveButton(positiveBtnText, this);
        }
        if (neutralBtnText != null) {
            builder.setNeutralButton(neutralBtnText, this);
        }
        if (negativeBtnText != null) {
            builder.setNegativeButton(negativeBtnText, this);
        }

        String[] items = getItems();
        if (items != null) {
            builder.setItems(items, this);
        }

        int contentViewId = getContentViewId();
        if(contentViewId != DEFAULT_INT_VALUE){
            mDialogView = getActivity().getLayoutInflater().inflate(contentViewId, null);
        }

        if (contentViewId != DEFAULT_INT_VALUE) {
            builder.setView(mDialogView);
        }

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

        if (getIsCloseToFinish()) {
            String negativeBtn = (negativeBtnText == null) ? "閉じる" : negativeBtnText;
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            getActivity().finish();
                            return true;
                    }
                    return false;
                }
            })
                    .setCancelable(false)
                    .setNegativeButton(negativeBtn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().finish();
                        }
                    });
            dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            return dialog;
        }

        if (isLoading()) {
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            return true;
                    }
                    return false;
                }
            }).setCancelable(false);
            dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            return dialog;
        }

        return dialog;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        String[] dialogItems = getItems();
        if (dialogItems != null && dialogItems.length > 0) {
            mListener.onItemsClick(getTag(), which);
            return;
        }

        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                if(mListener != null){
                    mListener.onPositiveBtnClick(getTag());
                }
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                if(mListener != null){
                    mListener.onNeutralBtnClick(getTag());
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                if(mListener != null){
                    mListener.onNegativeBtnClick(getTag());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog){
        super.onCancel(dialog);
        if(mListener != null){
            mListener.onDialogCancel(getTag());
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        if(mListener != null){
            mListener.onDialogDismiss(getTag());
        }
    }

    public void setTitle(String title) {
        getArguments().putString(KEY_DIALOG_TITLE, title);
    }

    public String getTitle() {
        return getArguments().getString(KEY_DIALOG_TITLE);
    }

    public void setMessage(String message) {
        getArguments().putString(KEY_DIALOG_MESSAGE, message);
    }

    public String getMessage() {
        return getArguments().getString(KEY_DIALOG_MESSAGE);
    }

    public void setContentViewId(int resId) {
        getArguments().putInt(KEY_DIALOG_CONTENT_VIEW, resId);
    }

    public int getContentViewId() {
        return getArguments().getInt(KEY_DIALOG_CONTENT_VIEW, DEFAULT_INT_VALUE);
    }

    public View getViewContent() {
        int resId = getContentViewId();
        if (resId == DEFAULT_INT_VALUE) return null;

        return mDialogView;
    }

    public void setItems(String[] items) {
        getArguments().putStringArray(KEY_DIALOG_ITEMS, items);
    }

    private String[] getItems() {
        return getArguments().getStringArray(KEY_DIALOG_ITEMS);
    }

    public void setPositiveBtnText(String text) {
        getArguments().putString(KEY_DIALOG_POSITIVE_BUTTON_TEXT, text);
    }

    public String getPositiveBtnText() {
        return getArguments().getString(KEY_DIALOG_POSITIVE_BUTTON_TEXT);
    }

    public void setNeutralBtnText(String text) {
        getArguments().putString(KEY_DIALOG_NEUTRAL_BUTTON_TEXT, text);
    }

    public String getNeutralBtnText() {
        return getArguments().getString(KEY_DIALOG_NEUTRAL_BUTTON_TEXT);
    }

    public void setNegativeBtnText(String text) {
        getArguments().putString(KEY_DIALOG_NEGATIVE_BUTTON_TEXT, text);
    }

    public String getNegativeBtnText() {
        return getArguments().getString(KEY_DIALOG_NEGATIVE_BUTTON_TEXT);
    }

    public void setCloseToFinish(boolean flag) {
        getArguments().putBoolean(KEY_DIALOG_IS_CLOSE_TO_FINISH, flag);
    }

    public boolean getIsCloseToFinish() {
        return getArguments().getBoolean(KEY_DIALOG_IS_CLOSE_TO_FINISH, false);
    }

    public void setLoading(boolean flag) {
        getArguments().putBoolean(KEY_DIALOG_IS_LOADING, flag);
    }

    public boolean isLoading() {
        return getArguments().getBoolean(KEY_DIALOG_IS_LOADING, false);
    }
}
