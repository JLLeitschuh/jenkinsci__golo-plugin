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

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Daniel Petisme <daniel.petisme@gmail.com>
 */
public class FileGoloSourceHandler extends AbstractGoloSourceHandler {

   public static final DescriptorImpl descriptor = new DescriptorImpl();
   private static Logger LOGGER = Logger.getLogger(FileGoloSourceHandler.class.getName());
   public final String filename;

   @DataBoundConstructor
   public FileGoloSourceHandler(String filename) {
      this.filename = filename;
   }

   @Override
   public FilePath getScriptFile(FilePath workspace, AbstractBuild<?, ?> build, BuildListener listener) throws IOException, InterruptedException {
      FilePath script = null;
      EnvVars env = build.getEnvironment(listener);
      if (StringUtils.isNotBlank(filename)) {
         String expandedFilename = env.expand(filename);
         script = new FilePath(workspace, expandedFilename);
      } else {
         throw new IOException(Messages.FileGoloSource_NoFilenameProvided());
      }

      return script;
   }

   @Override
   public boolean cleanScriptFile(FilePath script, BuildListener listener) throws IOException, InterruptedException {
      return true; //Nothing to clean
   }

   @Extension
   public static class DescriptorImpl extends Descriptor<AbstractGoloSourceHandler> {
      @Override
      public String getDisplayName() {
         return Messages.FileGoloSource_DisplayName();
      }

   }
}
