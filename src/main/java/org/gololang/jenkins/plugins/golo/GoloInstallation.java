/*
 * The MIT License
 * 
 * Copyright (c) 2013, Daniel Petisme
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gololang.jenkins.plugins.golo;

import hudson.*;
import hudson.model.EnvironmentSpecific;
import hudson.model.Node;
import hudson.model.TaskListener;
import hudson.remoting.Callable;
import hudson.slaves.NodeSpecific;
import hudson.tools.ToolDescriptor;
import hudson.tools.ToolInstallation;
import hudson.tools.ToolProperty;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author Daniel Petisme <daniel.petisme@gmail.com>
 */
public final class GoloInstallation extends ToolInstallation implements EnvironmentSpecific<GoloInstallation>, NodeSpecific<GoloInstallation> {

   private static Logger LOGGER = Logger.getLogger(GoloInstallation.class.getName());

   private static final String GOLO_HOME_ENV_VAR_NAME = "GOLO_HOME";
   private static final String GOLOGOLO_WIN_CMD = "gologolo.bat";
   private static final String GOLOGOLO_UNIX_CMD = "gologolo";

   public final String id = UUID.randomUUID().toString();

   @DataBoundConstructor
   public GoloInstallation(String name, String home, List<? extends ToolProperty<?>> properties) {
      super(name, home, properties);
   }


   public GoloInstallation forEnvironment(EnvVars envVars) {
      return new GoloInstallation(getName(), envVars.expand(getHome()), getProperties().toList());
   }

   public GoloInstallation forNode(Node node, TaskListener log) throws IOException, InterruptedException {
      return new GoloInstallation(getName(), translateFor(node, log), getProperties().toList());
   }

   @Override
   public void buildEnvVars(EnvVars env) {
      env.put(GOLO_HOME_ENV_VAR_NAME, getHome());
   }

   /**
    * Gets the executable path of this Golo installation on the given target system.
    */
   public String getExecutable(Launcher launcher) throws IOException, InterruptedException {
      return launcher.getChannel().call(new Callable<java.lang.String, IOException>() {
         public java.lang.String call() throws IOException {
            File exe = getExeFile();
            if (exe.exists())
               return exe.getPath();
            return null;
         }
      });
   }

   private File getExeFile() {
      String execName = Functions.isWindows() ? GOLOGOLO_WIN_CMD : GOLOGOLO_UNIX_CMD;
      String home = Util.replaceMacro(getHome(), EnvVars.masterEnvVars);

      return new File(home, "bin/" + execName);
   }


   /**
    * Returns true if the executable exists.
    */
   public boolean getExists() throws IOException, InterruptedException {
      return getExecutable(new Launcher.LocalLauncher(TaskListener.NULL)) != null;
   }


   @Extension
   public static class DescriptorImpl extends ToolDescriptor<GoloInstallation> {

      @Override
      public String getDisplayName() {
         return Messages.GoloInstallation_DisplayName();
      }

      @Override
      public GoloInstallation[] getInstallations() {
         return Jenkins.getInstance().getDescriptorByType(GoloBuilder.GoloBuilderDescriptor.class).getInstallations();
      }

      @Override
      public void setInstallations(GoloInstallation... installations) {
         Jenkins.getInstance().getDescriptorByType(GoloBuilder.GoloBuilderDescriptor.class).setInstallations(installations);
      }

   }
}

