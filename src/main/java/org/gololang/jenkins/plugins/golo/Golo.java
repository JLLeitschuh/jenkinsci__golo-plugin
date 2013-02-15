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

import hudson.CopyOnWrite;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import java.util.logging.Logger;

/**
 * @author Daniel Petisme <daniel.petisme@gmail.com>
 */
public class Golo extends AbstractGolo {

   private static Logger LOGGER = Logger.getLogger(Golo.class.getName());

   @Extension
   public static final GoloDescriptor DESCRIPTOR = new GoloDescriptor();


   public Golo(GoloSource scriptSource) {
      super(scriptSource);
   }

   @Override
   public Descriptor<Builder> getDescriptor() {
      return DESCRIPTOR;
   }

   public static final class GoloDescriptor extends AbstractGoloBuilderDescriptor {

      @CopyOnWrite
      private volatile GoloInstallation[] installations = new GoloInstallation[0];

      public GoloDescriptor() {
         super(Golo.class);
         load();
      }

      @Override
      public boolean isApplicable(Class<? extends AbstractProject> aClass) {
         return (getInstallations() != null && getInstallations().length > 0);
      }

      @Override
      public String getDisplayName() {
         return Messages.Golo_DisplayName();
      }


      @Override
      public Builder newInstance(StaplerRequest req, JSONObject formData) throws FormException {
         return req.bindJSON(Golo.class, formData);
      }

      public GoloInstallation[] getInstallations() {
         return installations;
      }

      public void setInstallations(GoloInstallation[] installations) {
         this.installations = installations;
         save();
      }
   }
}
