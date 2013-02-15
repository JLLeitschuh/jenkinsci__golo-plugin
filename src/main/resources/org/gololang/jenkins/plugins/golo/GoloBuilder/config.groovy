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
package org.gololang.jenkins.plugins.golo.GoloBuilder

// Namespaces
st = namespace("jelly:stapler")
j = namespace("jelly:core")
l = namespace(lib.LayoutTagLib)
f = namespace(lib.FormTagLib)
def descriptor = org.gololang.jenkins.plugins.golo.GoloBuilder.DESCRIPTOR

f.entry() {
   if (descriptor.installations.length != 0) {
      f.entry(title: _("Golo_Version")) {
         select(class: "setting-input", name: "golo.installation") {
            option(value: "(Default)", _("Golo_Default_Version"))
            descriptor.installations.each { installation ->
               f.option(selected: installation.name == instance?.installation?.name, value: installation.name, installation.name)
            }
         }
      }

      def instanceID = descriptor.nextInstanceID()
      descriptor.scriptSources.inject(0) { loop, source ->
         f.radioBlock(help: source.helpFile, title: source.displayName, name: "${instanceID}.scriptSource", value: loop, checked: instance?.scriptSource?.descriptor == source) {
            st.include(page: source.configPage, from: source)
            loop++
         }
      }
   }
}