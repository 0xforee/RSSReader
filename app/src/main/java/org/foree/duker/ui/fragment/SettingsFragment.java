package org.foree.duker.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import org.foree.duker.ui.activity.SettingsActivityTmp;
import org.foree.duker.utils.ColorUtils;
import com.fourmob.colorpicker.ColorPickerDialog;
import com.fourmob.colorpicker.ColorPickerSwatch;
import org.foree.duker.R;

/**
 * Created by foree on 4/16/15.
 * Settings Fragment
 */
//settingFragment的内部类实现
public class SettingsFragment extends PreferenceFragment {
    private static final String TAG = "SettingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //get theme
        int colorPicked = sp.getInt(SettingsActivityTmp.KEY_CHANGE_THEME, ColorUtils.AQUAMARINE);

        //save theme
        final SharedPreferences.Editor editor = sp.edit();

        //init dialog
        final ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
        colorPickerDialog.initialize(R.string.pick_color, new int[]{
                ColorUtils.AQUAMARINE,
                ColorUtils.BLUE,
                ColorUtils.GREEN,
                ColorUtils.ORANGE,
                ColorUtils.RED,
                ColorUtils.GRAY
        }, colorPicked, 3, 2);

        //set Listener
        colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                switch (color) {
                    case ColorUtils.AQUAMARINE:
                        editor.putInt(SettingsActivityTmp.KEY_CHANGE_THEME, ColorUtils.AQUAMARINE);
                        break;
                    case ColorUtils.BLUE:
                        editor.putInt(SettingsActivityTmp.KEY_CHANGE_THEME, ColorUtils.BLUE);
                        break;
                    case ColorUtils.GREEN:
                        editor.putInt(SettingsActivityTmp.KEY_CHANGE_THEME, ColorUtils.GREEN);
                        break;
                    case ColorUtils.ORANGE:
                        editor.putInt(SettingsActivityTmp.KEY_CHANGE_THEME, ColorUtils.ORANGE);
                        break;
                    case ColorUtils.RED:
                        editor.putInt(SettingsActivityTmp.KEY_CHANGE_THEME, ColorUtils.RED);
                        break;
                    case ColorUtils.GRAY:
                        editor.putInt(SettingsActivityTmp.KEY_CHANGE_THEME, ColorUtils.GRAY);
                        break;
                }
                //reload activity
                Intent intent = new Intent(getActivity(), SettingsActivityTmp.class);

                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                editor.apply();
            }
        });

        // add preference content from a xml file
        addPreferencesFromResource(R.xml.preference_all);

        findPreference(SettingsActivityTmp.KEY_CHANGE_THEME).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //when darkTheme is open, can't change theme
                if (sp.getBoolean(SettingsActivityTmp.KEY_DARK_THEME, false)) {
                    preference.setEnabled(false);
                    Toast.makeText(getActivity(), "请关闭夜间模式", Toast.LENGTH_SHORT).show();
                } else
                colorPickerDialog.show(getActivity().getFragmentManager(), "colorPick");
                return true;
            }
        });
    }
}