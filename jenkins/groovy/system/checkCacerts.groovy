// https://github.com/jenkinsci/jenkins-scripts/blob/master/scriptler/checkSSLConnection.groovy

import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import java.security.KeyStore
import java.security.Principal
import java.security.cert.Certificate
import java.security.cert.X509Certificate

try {

    println("## DUMP JVM TRUST MANAGERS ##")
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    tmf.init((KeyStore) null)
    for (TrustManager trustManager : tmf.getTrustManagers()) {
        println(trustManager)

        if (trustManager instanceof X509TrustManager) {
            X509TrustManager x509TrustManager = (X509TrustManager) trustManager
            for (X509Certificate certificate: x509TrustManager.getAcceptedIssuers()) {
                println("\t" + certificate.getSubjectDN())
            }
            println("\tAccepted issuers count : " + x509TrustManager.getAcceptedIssuers().length)
            println("###################")
        } else {
            println("Skip " + trustManager + " - " + trustManager.getClass())
        }
    }
} catch (Exception e) {
    e.printStackTrace()
    println "See stacktrace outputted in system.out for " + e
}
try {
    String url = "https://kubernetes-api.domain.com:6443"
    HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection()
    println(url + "->" + urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage())
    for (Certificate certificate : urlConnection.getServerCertificates()) {
        if (certificate instanceof X509Certificate) {
            X509Certificate x509Certificate = (X509Certificate) certificate
            Principal subjectDN = x509Certificate.getSubjectDN()
            println("\t" + subjectDN.getClass() + " - " + subjectDN)
        } else {
            println(certificate)
        }
    }
} catch (Exception e) {
    println "See stacktrace outputted in system.out for " + e
    e.printStackTrace()
}
