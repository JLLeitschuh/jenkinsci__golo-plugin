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
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Daniel Petisme <daniel.petisme@gmail.com>
 */
public class FileGoloSource implements GoloSource {

   private static Logger LOGGER = Logger.getLogger(FileGoloSource.class.getName());

   @Extension
   public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

   private String scriptFile;

   @DataBoundConstructor
   public FileGoloSource(String scriptFile) {
      this.scriptFile = scriptFile;
   }

   @Override
   public FilePath getScriptFile(FilePath projectWorkspace) {
      return new FilePath(projectWorkspace, scriptFile);
   }

   @Override
   public FilePath getScriptFile(FilePath projectWorkspace, AbstractBuild<?, ?> build, BuildListener listener) throws IOException, InterruptedException {
      EnvVars env = build.getEnvironment(listener);
      String expandedScriptdFile = env.expand(this.scriptFile);
      return new FilePath(projectWorkspace, expandedScriptdFile);
   }

   public String getScriptFile() {
      return scriptFile;
   }


   public Descriptor<GoloSource> getDescriptor() {
      return DESCRIPTOR;
   }

   public static class DescriptorImpl extends Descriptor<GoloSource> {

      public DescriptorImpl() {
         super(FileGoloSource.class);
      }

      @Override
      public String getDisplayName() {
         return Messages.FileGoloSource_DisplayName();
      }

      @Override
      public FileGoloSource newInstance(StaplerRequest req, JSONObject formData) throws FormException {
         return req.bindJSON(FileGoloSource.class, formData);
      }
   }
}
