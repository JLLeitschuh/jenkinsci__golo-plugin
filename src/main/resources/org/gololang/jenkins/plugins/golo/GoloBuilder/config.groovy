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


if (descriptor.installations.length != 0) {
   f.entry(title: _("Golo_Version"), field: "installationId") {

      select(class: "setting-input", name: "installationId") {
         f.option(value: "Default", _("Golo_Default_Version"))
         descriptor.installations.each() { installation ->
            f.option(selected: installation.id == instance?.installationId, value: installation.id, installation.name)
         }
      }
   }
   f.entry(field: "sourceHandler") {
      descriptor.GOLO_SOURCE_HANDLERS.each { sourceHandler ->
         f.radioBlock(title: sourceHandler.displayName, name: "sourceHandler", value: sourceHandler, checked: sourceHandler == instance?.sourceHandler?.descriptor) {
            f.invisibleEntry {
               input(type: "hidden", name: "stapler-class", value: sourceHandler.clazz.name)
            }
            st.include(from: sourceHandler, page: sourceHandler.configPage)
         }
      }
   }
}