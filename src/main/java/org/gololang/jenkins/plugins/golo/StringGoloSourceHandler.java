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

import hudson.Extension;
import hudson.FilePath;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Daniel Petisme <daniel.petisme@gmail.com>
 */
public class StringGoloSourceHandler extends AbstractGoloSourceHandler {

   public static final DescriptorImpl descriptor = new DescriptorImpl();
   private static Logger LOGGER = Logger.getLogger(StringGoloSourceHandler.class.getName());
   public final String command;

   @DataBoundConstructor
   public StringGoloSourceHandler(String command) {
      this.command = command;
   }

   @Override
   public FilePath getScriptFile(FilePath workspace, AbstractBuild<?, ?> build, BuildListener listener) throws IOException, InterruptedException {
      return workspace.createTextTempFile("jenkins-", GOLO_SUFFIX, command, true);
   }

   @Override
   public boolean cleanScriptFile(FilePath script, BuildListener listener) throws IOException, InterruptedException {
      boolean isSuccess = false;
      try {
         if (script != null)
            isSuccess = script.delete();
      } catch (IOException e) {
         Util.displayIOException(e, listener);
         e.printStackTrace(listener.fatalError(Messages.StringGoloSourceHandler_UnableToDelete(script)));
         throw e;
      } catch (InterruptedException e) {
         e.printStackTrace(listener.fatalError(Messages.StringGoloSourceHandler_UnableToDelete(script)));
         throw e;
      }

      return isSuccess;
   }

   @Extension
   public static class DescriptorImpl extends Descriptor<AbstractGoloSourceHandler> {
      @Override
      public String getDisplayName() {
         return Messages.StringGoloSource_DisplayName();
      }

   }
}

