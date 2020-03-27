package com.example.newsfeed;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

public class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_main);

        bindPreferenceSummaryToValue(
                findPreference(getString(R.string.settings_page_size_key)),
                findPreference(getString(R.string.settings_order_by_key)),
                findPreference(getString(R.string.settings_type_key)),
                findPreference(getString(R.string.settings_section_id_key)));
    }

    private void bindPreferenceSummaryToValue(Preference... prefs) {
        for (Preference pref : prefs) {
            pref.setOnPreferenceChangeListener(this);
            onPreferenceChange(pref, PreferenceManager
                    .getDefaultSharedPreferences(pref.getContext())
                    .getString(pref.getKey(), ""));
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);

            if (prefIndex >= 0) {
                CharSequence[] labels = listPreference.getEntries();
                preference.setSummary(labels[prefIndex]);
            }

        } else preference.setSummary(stringValue);

        return true;
    }
}