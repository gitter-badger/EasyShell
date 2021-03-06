/*******************************************************************************
 * Copyright (c) 2014 - 2016 Andre Bossert.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Bossert - initial API and implementation and/or initial documentation
 *******************************************************************************/

package de.anbos.eclipse.easyshell.plugin.preferences;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.Constants;
import de.anbos.eclipse.easyshell.plugin.legacy.PrefsV1_4;
import de.anbos.eclipse.easyshell.plugin.legacy.PrefsV1_5;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.types.Version;

public class Initializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
	    // get the actual preference store
	    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	    // set default values
		setDefaults(store);
		// migrate from old store
        migrate(store);
	}

    private void setDefaults(IPreferenceStore store) {
        String defaultCommandsPreset = PreferenceValueConverter.asCommandDataString(CommandDataDefaultCollection.getCommandsNative(null));
        String defaultCommands = "";
        String defaultMenu    = PreferenceValueConverter.asMenuDataString(CommandDataDefaultCollection.getCommandsNativeAsMenu(true));
        store.setDefault(Constants.PREF_COMMANDS_PRESET, defaultCommandsPreset);
        store.setDefault(Constants.PREF_COMMANDS, defaultCommands);
        store.setDefault(Constants.PREF_MENU, defaultMenu);
        store.setDefault(Constants.PREF_MIGRATED, false);
    }

    private void migrate(IPreferenceStore store) {
        if (!store.getBoolean(Constants.PREF_MIGRATED)) {
            int migrateState = -1; // -1 = old store not found, 0 (Yes) = migrated, 1 (No) = no migration wanted by user, 2 (Cancel) = try to migrate again
            for (int i=Version.values().length-2;i>0;i--) {
                Version version = Version.values()[i];
                String versionName = version.getName();
                if (version.toString().startsWith("v1_")) {
                    migrateState = migrate_from_v1(store, version, migrateState);
                } else {
                    migrateState = migrate_from_v2(store, version, migrateState);
                }
                // if no old store for this version found continue, else break
                if (migrateState != -1) {
                    switch(migrateState) {
                        case 0: Utils.showToolTipWarning(null, Activator.getResourceString("easyshell.plugin.name"), MessageFormat.format(
                                     Activator.getResourceString("easyshell.message.warning.migrated.yes"),
                                     versionName));

                        break;
                        case 1: Utils.showToolTipWarning(null, Activator.getResourceString("easyshell.plugin.name"), MessageFormat.format(
                                    Activator.getResourceString("easyshell.message.warning.migrated.no"),
                                    versionName));
                        break;
                        case 2: Utils.showToolTipWarning(null, Activator.getResourceString("easyshell.plugin.name"), MessageFormat.format(
                                    Activator.getResourceString("easyshell.message.warning.migrated.cancel"),
                                    versionName));
                        break;

                    }
                    break;
                }
            }
            // we have first startup without old store
            if (migrateState == -1) {
                Utils.showToolTipWarning(null, Activator.getResourceString("easyshell.plugin.name"), Activator.getResourceString("easyshell.message.warning.migrated.default"));
            }
            // do not set migration flag if user canceled and want to do it later
            if (migrateState != 2) {
                store.setValue(Constants.PREF_MIGRATED, true);
            }
        }
    }

    private int migrate_from_v2(IPreferenceStore store, Version version, int migrateState) {
        // get the old v2 store
        IPreferenceStore oldStore = Activator.getDefault().getPreferenceStoreByVersion(version.name());
        // check preferences for default values
        migrateState = migrate_check_pref_and_ask_user(oldStore, version, new ArrayList<String>(Arrays.asList(Constants.PREF_COMMANDS)), migrateState);
        if (migrateState == 0) {
            store.setValue(Constants.PREF_COMMANDS, PreferenceValueConverter.migrateCommandDataList(version, oldStore.getString(Constants.PREF_COMMANDS)));
        }
        migrateState = migrate_check_pref_and_ask_user(oldStore, version, new ArrayList<String>(Arrays.asList(Constants.PREF_MENU)), migrateState);
        if (migrateState == 0) {
            store.setValue(Constants.PREF_MENU, PreferenceValueConverter.migrateMenuDataList(version, oldStore.getString(Constants.PREF_MENU)));
        }
        return migrateState;
    }

    private int migrate_check_pref_and_ask_user(IPreferenceStore store, Version version, List<String> prefList, int migrateState) {
        // if cancel or no just skip this time
        if (migrateState == 1 || migrateState == 2) {
            return migrateState;
        }
        boolean migrationPossible = false;
        for (String pref : prefList) {
            if (!store.isDefault(pref)) {
                migrationPossible = true;
                break;
            }
        }
        if (migrationPossible) {
            // ask user if not already asked and said yes
            if (migrateState != 0) {
                String title = Activator.getResourceString("easyshell.plugin.name");
                String question = MessageFormat.format(
                        Activator.getResourceString("easyshell.question.migrate"),
                        version.getName());
                MessageDialog dialog = new MessageDialog(
                        null, title, null, question,
                        MessageDialog.QUESTION,
                        new String[] {"Yes", "No", "Cancel"},
                        0); // no is the default
                migrateState = dialog.open();
            }
        }
        return migrateState;
    }

    private int migrate_from_v1(IPreferenceStore store, Version version, int migrateState) {
        // get the old v1_5 store
        IPreferenceStore oldStore = Activator.getDefault().getLegacyPreferenceStore();
        // check if we want version 1.5 or 1.4
        if (version == Version.v1_5) {
            // check preferences for default values
            migrateState = migrate_check_pref_and_ask_user(oldStore, version, PrefsV1_5.getPreferenceList(), migrateState);
            if (migrateState == 0) {
                CommandDataList cmdDataList = new CommandDataList();
                MenuDataList menuDataList = CommandDataDefaultCollection.getCommandsNativeAsMenu(true);
                if (PrefsV1_5.loadStore(oldStore, Utils.getOS(), cmdDataList, menuDataList)) {
                    store.setValue(Constants.PREF_COMMANDS, PreferenceValueConverter.asCommandDataString(cmdDataList));
                    store.setValue(Constants.PREF_MENU, PreferenceValueConverter.asMenuDataString(menuDataList));
                }
            }
        } else if (version == Version.v1_4) {
            // check preferences for default values
            migrateState = migrate_check_pref_and_ask_user(oldStore, version, PrefsV1_4.getPreferenceList(), migrateState);
            CommandDataList cmdDataList = new CommandDataList();
            MenuDataList menuDataList = CommandDataDefaultCollection.getCommandsNativeAsMenu(true);
            if (PrefsV1_4.loadStore(oldStore, Utils.getOS(), cmdDataList, menuDataList)) {
                store.setValue(Constants.PREF_COMMANDS, PreferenceValueConverter.asCommandDataString(cmdDataList));
                store.setValue(Constants.PREF_MENU, PreferenceValueConverter.asMenuDataString(menuDataList));
            }
        }
        return migrateState;
    }

}
