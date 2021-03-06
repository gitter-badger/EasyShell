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

package de.anbos.eclipse.easyshell.plugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import de.anbos.eclipse.easyshell.plugin.EditorPropertyTester;
import de.anbos.eclipse.easyshell.plugin.actions.Action;
import de.anbos.eclipse.easyshell.plugin.actions.ActionDelegate;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;

public class Execute extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
        String commandID  = event.getCommand().getId();
        ResourceType resourceType = ResourceType.getFromEnum(event.getParameter("de.anbos.eclipse.easyshell.plugin.commands.parameter.resource"));
        CommandType commandType = CommandType.getFromAction(event.getParameter("de.anbos.eclipse.easyshell.plugin.commands.parameter.type"));
        String commandValue = event.getParameter("de.anbos.eclipse.easyshell.plugin.commands.parameter.value");
        String commandWorkingDir = event.getParameter("de.anbos.eclipse.easyshell.plugin.commands.parameter.workingdir");
        ActionDelegate action = EditorPropertyTester.getActionExactResourceType(activePart, resourceType);
        if (action != null) {
        	action.setResourceType(resourceType);
        	action.setCommandType(commandType);
        	action.setCommandValue(commandValue);
        	action.setCommandWorkingDir(commandWorkingDir);
            Action act = new Action(commandID);
            action.run((IAction)act);
        }
        action = null;
        return null;
    }
}
