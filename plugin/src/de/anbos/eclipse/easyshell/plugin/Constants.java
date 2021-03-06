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

package de.anbos.eclipse.easyshell.plugin;

public interface Constants {

    // Plugin
    public static final String PLUGIN_ID = "de.anbos.eclipse.easyshell.plugin";

    // Images
    //public static final String IMAGE_EXT         = "gif";
    public static final String IMAGE_EXT         = "png";
    public static final String IMAGE_PATH        = "icons/";
    public static final String IMAGE_EASYSHELL   = "easyshell." + IMAGE_EXT;
    public static final String IMAGE_UNKNOWN     = "unknown." + IMAGE_EXT;
    public static final String IMAGE_DEFAULT     = "default." + IMAGE_EXT;
    public static final String IMAGE_OPEN        = "open." + IMAGE_EXT;
    public static final String IMAGE_RUN         = "run." + IMAGE_EXT;
    public static final String IMAGE_EXPLORE     = "explore." + IMAGE_EXT;
    public static final String IMAGE_CLIPBOARD   = "clipboard." + IMAGE_EXT;
    public static final String IMAGE_USER        = "user." + IMAGE_EXT;

	// Preferences
    public static final String PREF_COMMANDS_PRESET = "COMMANDS_PRESET";
	public static final String PREF_COMMANDS        = "COMMANDS";
	public static final String PREF_MENU            = "MENU";
	public static final String PREF_MIGRATED        = "MIGRATED";

	// Actions
	public static final String ACTION_UNKNOWN    = "de.anbos.eclipse.easyshell.plugin.commands.Unknown";
	public static final String ACTION_EXECUTE    = "de.anbos.eclipse.easyshell.plugin.commands.Execute";
	public static final String ACTION_CLIPBOARD  = "de.anbos.eclipse.easyshell.plugin.commands.Clipboard";
}
