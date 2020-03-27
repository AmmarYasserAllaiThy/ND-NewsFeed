package com.example.newsfeed;

import android.content.SharedPreferences;
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

        Preference pageSize = findPreference(getString(R.string.settings_page_size_key));
        Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
        Preference type = findPreference(getString(R.string.settings_type_key));
        Preference sectionId = findPreference(getString(R.string.settings_section_id_key));

        bindPreferenceSummaryToValue(pageSize, orderBy, type, sectionId);
    }

    private void bindPreferenceSummaryToValue(Preference... prefs) {
        for (Preference pref : prefs) {
            pref.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(pref.getContext());
            String preferenceString = preferences.getString(pref.getKey(), "");
            onPreferenceChange(pref, preferenceString);
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