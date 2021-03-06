/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.designer.eclipse.util;

import java.util.List;

import org.activiti.designer.eclipse.common.ActivitiPlugin;
import org.activiti.designer.eclipse.extension.palette.IPaletteProvider;
import org.activiti.designer.integration.servicetask.CustomServiceTaskDescriptor;
import org.activiti.designer.util.extension.ExtensionUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;

public class PaletteExtensionUtil {
	
	public static void pushPaletteExtensions(){
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cfgs = registry.getConfigurationElementsFor(ActivitiPlugin.PALETTE_EXTENSION_PROVIDER_EXTENSIONPOINT_ID);
		for (IConfigurationElement cfg : cfgs){
			try {
				Object o = cfg.createExecutableExtension("class");
				if (o instanceof IPaletteProvider){
					executeExtension((IPaletteProvider)o);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private static void executeExtension(final IPaletteProvider provider) {
		ISafeRunnable runnable = new ISafeRunnable() {
			@Override
			public void handleException(Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void run() throws Exception {
				List<CustomServiceTaskDescriptor> descriptors = provider.provide();
				ExtensionUtil.addProvidedCustomServiceTaskDescriptors(descriptors);
			}
		};
		SafeRunner.run(runnable);
	}
}
