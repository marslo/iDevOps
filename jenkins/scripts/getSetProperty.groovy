#!/usr/bin/env groovy

// for timezone
println(System.getProperty("org.apache.commons.jelly.tags.fmt.timeZone"));
System.clearProperty("org.apache.commons.jelly.tags.fmt.timeZone");
println("====")
println(System.getProperty("org.apache.commons.jelly.tags.fmt.timeZone"))

System.setProperty("org.apache.commons.jelly.tags.fmt.timeZone", "Asia/Shanghai");
println("====")
println(System.getProperty("org.apache.commons.jelly.tags.fmt.timeZone"))

// for csp
System.clearProperty("hudson.model.DirectoryBrowserSupport.CSP");
System.setProperty("hudson.model.DirectoryBrowserSupport.CSP", "sandbox allow-same-origin allow-scripts; default-src 'self'; script-src * 'unsafe-eval'; img-src *; style-src * 'unsafe-inline'; font-src *");
println(System.getProperty("hudson.model.DirectoryBrowserSupport.CSP"))


// vim: ft=Jenkinsfile ts=2 sts=2 sw=2 et
