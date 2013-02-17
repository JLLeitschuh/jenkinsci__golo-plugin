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

import hudson.CopyOnWrite;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * @author Daniel Petisme <daniel.petisme@gmail.com>
 */
public abstract class AbstractGolo extends Builder {

   private static Logger LOGGER = Logger.getLogger(AbstractGolo.class.getName());
   public final AbstractGoloSourceHandler sourceHandler;

   @DataBoundConstructor
   public AbstractGolo(AbstractGoloSourceHandler sourceHandler) {
      this.sourceHandler = sourceHandler;
   }

   protected static abstract class AbstractGoloBuilderDescriptor extends BuildStepDescriptor<Builder> {

      public static List<Descriptor<AbstractGoloSourceHandler>> GOLO_SOURCE_HANDLERS = AbstractGoloSourceHandler.LIST;
      @CopyOnWrite
      private volatile GoloInstallation[] installations = new GoloInstallation[0];
      // Used for grouping radio buttons together
      private AtomicInteger instanceCounter = new AtomicInteger(0);

      public AbstractGoloBuilderDescriptor(Class<? extends Builder> clazz) {
         super(clazz);
         load();
      }

      public boolean isApplicable(Class<? extends AbstractProject> clazz) {
         return (getInstallations() != null && getInstallations().length > 0);
      }

      public GoloInstallation[] getInstallations() {
         return installations;
      }

      public void setInstallations(GoloInstallation[] installations) {
         this.installations = installations;
         save();
      }

      public int nextInstanceID() {
         return instanceCounter.incrementAndGet();
      }

      @Override
      public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
         super.configure(req, json);
         save();
         return true;
      }
   }

}
