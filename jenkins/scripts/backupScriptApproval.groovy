import java.lang.reflect.*;
import jenkins.model.Jenkins;
import jenkins.model.*;
import org.jenkinsci.plugins.scriptsecurity.scripts.*;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.*;
import static groovy.json.JsonOutput.*

scriptApproval = ScriptApproval.get()
alreadyApproved = new HashSet<>(Arrays.asList(scriptApproval.getApprovedSignatures()))
println prettyPrint( toJson(alreadyApproved) )
