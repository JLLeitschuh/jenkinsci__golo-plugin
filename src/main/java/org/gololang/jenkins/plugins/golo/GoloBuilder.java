/*
 * The MIT License
 * 
 * Copyright (c) 2013, Daniel PETISME
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

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Computer;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Daniel Petisme <daniel.petisme@gmail.com>
 */
public class GoloBuilder extends AbstractGolo {

   private static Logger LOGGER = Logger.getLogger(GoloBuilder.class.getName());
   public final String installationId;

   @DataBoundConstructor
   public GoloBuilder(String installationId, AbstractGoloSourceHandler sourceHandler) {
      super(sourceHandler);
      this.installationId = installationId;
   }

   @Override
   public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
      boolean isSuccess = false;
      if (sourceHandler != null) {
         FilePath goloScript = null;
         try {
            goloScript = sourceHandler.getScriptFile(build.getWorkspace(), build, listener);
         } catch (IOException e) {
            throw e;
         }

         GoloInstallation installation = ((AbstractGoloBuilderDescriptor) getDescriptor()).getGoloInstallationById(this.installationId);

         if (installation != null) {
            EnvVars env = build.getEnvironment(listener);
            installation = installation.forNode(Computer.currentComputer().getNode(), listener);
            installation = installation.forEnvironment(env);
            String exe = installation.getExecutable(launcher);

            try {
               isSuccess = launcher.launch().cmds(exe, goloScript.getRemote()).envs(env).stdout(listener).pwd(build.getWorkspace()).join() == 0;
            } catch (IOException e) {
               Util.displayIOException(e, listener);
               e.printStackTrace(listener.fatalError(Messages.GoloBuilder_ExecutionFailed()));
               throw e;
            } finally {
               sourceHandler.cleanScriptFile(goloScript, listener);
            }

         } else {
            listener.fatalError(Messages.GoloBuilder_NoInstallationDefined());
         }


      } else {
         listener.fatalError(Messages.GoloBuilder_NoScriptDefined());
      }
      return isSuccess;
   }

   public static final class GoloBuilderDescriptor extends AbstractGoloBuilderDescriptor {

      public GoloBuilderDescriptor() {
         super(GoloBuilder.class);
      }

      @Override
      public String getDisplayName() {
         return Messages.GoloBuilder_DisplayName();
      }
   }
}
